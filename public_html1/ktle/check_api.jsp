<%@ page contentType="text/html; charset=utf-8" %><%@ include file="init.jsp" %><%

String action = m.rs("action");
String planId = m.rs("planId");
String planUserId = m.rs("userId");

if("".equals(action) || "".equals(planId)) return;

CourseLessonDao courseLesson = new CourseLessonDao();
CourseProgressDao courseProgress = new CourseProgressDao(); courseProgress.setInsertIgnore(true);
CourseUserDao courseUser = new CourseUserDao();
CourseUserLogDao courseUserLog = new CourseUserLogDao();

String now = m.time();

m.log("kt_lan -> action : " + action + " / planId : " + planId + " / planUserId : " + planUserId);

if("join".equals(action)) {
    if("".equals(planUserId) || -1 < planUserId.indexOf("HOST")) return;

    String[] planUser = m.split("-", planUserId);
    if(planUser.length != 3) { m.log("kt_lan", action + " / 데이터 불일치 / planId : " + planId +  " / planUserId : " + planUserId); return; }

    int psid = m.parseInt(planUser[1]);
    int cuid = m.parseInt(planUser[2]);

    DataSet info = courseUser.query(
        " SELECT a.id, a.user_id, a.course_id, cl.chapter, cl.lesson_id "
        + " FROM " + courseUser.table + " a "
        + " INNER JOIN " + courseLesson.table + " cl ON a.course_id = cl.course_id AND cl.site_id = " + psid
        + " WHERE a.site_id = " + psid + " AND a.id = " + cuid + " AND cl.twoway_url = '" + planId + "'"
    );

    if(!info.next()) { m.log("kt_lan", action + " / 수강생 정보없음 / planId : " + planId +  " / planUserId : " + planUserId); return; }

    courseUserLog.item("course_user_id", cuid);
    courseUserLog.item("user_id", info.i("user_id"));
    courseUserLog.item("course_id", info.i("course_id"));
    courseUserLog.item("chapter", info.i("chapter"));
    courseUserLog.item("lesson_id", info.i("lesson_id"));
    courseUserLog.item("status", 8);
    courseUserLog.item("reg_date", now);
    courseUserLog.item("site_id", psid);
    if(!courseUserLog.insert()) { m.log("kt_lan", action + " / DB 저장에러 / planId : " + planId +  " / planUserId : " + planUserId); }

    DataSet cuInfo = courseProgress.find("course_user_id = " + cuid + " AND lesson_id = " + info.i("lesson_id") + " AND site_id = " + psid);
    if(!cuInfo.next()) { m.log("kt_lan",  action + " / 진도율 정보 없음 / planId : " + planId + " / planUserId : " + planUserId); return; }

    courseProgress.item("paragraph", now);
    if(!courseProgress.update("course_user_id = " + cuid + " AND lesson_id = " + info.i("lesson_id") + " AND site_id = " + psid)) { m.log("kt_lan", action + " 입장시간 갱신실패 / planId : " + planId +  " / planUserId : " + planUserId + " / reg_date : " + now); };

    m.log("kt_lan", action + " 입장시간 / planId : " + planId +  " / planUserId : " + planUserId + " / reg_date : " + now);
    return;

} else if("leave".equals(action)) {
    if("".equals(planUserId) || -1 < planUserId.indexOf("HOST")) return;

    String[] planUser = m.split("-", planUserId);
    if(planUser.length != 3) { m.log("kt_lan", action + " / 데이터 불일치 / planId : " + planId +  " / planUserId : " + planUserId); return; }

    int psid = m.parseInt(planUser[1]);
    int cuid = m.parseInt(planUser[2]);

    DataSet info = courseUserLog.query(
        " SELECT a.* "
        + " FROM " + courseUserLog.table + " a "
        + " WHERE a.status = 8 AND a.site_id = " + psid + " AND a.course_user_id = " + cuid
        + " ORDER BY a.reg_date DESC LIMIT 1"
    );

    if(!info.next()) { m.log("kt_lan", action + " / 수강생 정보없음 / planId : " + planId +  " / planUserId : " + planUserId); return; }

    courseUserLog.item("course_user_id", cuid);
    courseUserLog.item("user_id", info.i("user_id"));
    courseUserLog.item("course_id", info.i("course_id"));
    courseUserLog.item("chapter", info.i("chapter"));
    courseUserLog.item("lesson_id", info.i("lesson_id"));
    courseUserLog.item("status", 9);
    courseUserLog.item("reg_date", now);
    courseUserLog.item("site_id", psid);

    if(!courseUserLog.insert()) { m.log("kt_lan", action + " DB 저장에러 / planId : " + planId +  " / planUserId : " + planUserId); return; }

    m.log("kt_lan", action + " / now : " + now +  " / info.reg_date : " + info.s("reg_date"));

    //기존 start 정보를 가져올 것
    DataSet cuInfo = courseProgress.find("course_user_id = " + cuid + " AND lesson_id = " + info.i("lesson_id") + " AND site_id = " + psid);
    if(!cuInfo.next()) { m.log("kt_lan",  action + " / 진도율 정보 없음 / planId : " + planId + " / planUserId : " + planUserId); return; }

    courseProgress.item("paragraph", cuInfo.s("paragraph") + "-" + now);
    if(!courseProgress.update("course_user_id = " + cuid + " AND lesson_id = " + info.i("lesson_id") + " AND site_id = " + psid)) { m.log("kt_lan", action + " 퇴장시간 갱신실패 / planId : " + planId +  " / planUserId : " + planUserId + " / reg_date : " + now); };

    DataSet clInfo = courseLesson.find("twoway_url = '" + planId + "' AND status = 1", "*");
    if(!clInfo.next()) { m.log("kt_lan", action + " / 해당 차시 없음, planId -> " + planId); return; }
    String sDateTime = clInfo.s("start_date") + clInfo.s("start_time");
    String eDateTime = clInfo.s("end_date") + clInfo.s("end_time");
    String start = m.diffDate("S", sDateTime, cuInfo.s("paragraph")) < 0 ? sDateTime : cuInfo.s("paragraph"); //더 큰 숫자 == 늦은 숫자로 입장 시간을 계산
    String end = m.diffDate("S", eDateTime, now) > 0 ? eDateTime : now; //더 작은 숫자 == 더 빠른 숫자로 퇴장 시간을 계산

    m.log("kt_lan", action + "/sDateTime : " + sDateTime + "/eDateTime : " + eDateTime + "/start : " + start + "/end : " + end);

    int gap = m.diffDate("S", start, end);
    m.log("kt_lan", action + " / planId : " + planId +  " / planUserId : " + planUserId + " / gap : " + gap);

    if(gap > 0) { //강의 시간전에 입장한 수강생이 설정한 시간전 퇴장시엔 진도율을 반영하지 않음
        courseProgress.setStudyTime(gap);
        int ret = courseProgress.updateProgress(cuid, info.i("lesson_id"), info.i("chapter"));
        m.log("kt_lan", action + " / planId : " + planId + " / planUserId : " + planUserId + " / courseProgress.updateProgress : " + ret);

        //수강생 정보 업데이트
        if (ret == 2) {
            courseUser.setProgressRatio(cuid);
            courseUser.updateScore(cuid, "progress"); //점수일괄업데이트
            courseUser.closeUser(cuid, userId);
        }
    }
    return;

} else if("start".equals(action)) {
    DataSet clInfo = courseLesson.find("twoway_url = '" + planId + "' AND status = 1", "*");
    if(!clInfo.next()) { m.log("kt_lan", action + " / 해당 차시 없음, planId -> " + planId); return; } //강의실을 여러번 열고 닫을 수 있으며 '처음 연 시간'을 기준으로 계산 할 것.
    if("".equals(clInfo.s("oper_sdate"))) {
        courseLesson.clear();
        courseLesson.item("oper_sdate", now);
        if(!courseLesson.update("twoway_url = '" + planId + "' AND status = 1")) { m.log("kt_lan", "강의 시작 시간 업데이트 실패 planId = " + planId); }
    }

    m.log("kt_lan", action + " / planId = " + planId);
    return;
} else if("end".equals(action)) {
    DataSet clInfo = courseLesson.find("twoway_url = '" + planId + "' AND status = 1", "*");
    if(!clInfo.next()) { m.log("kt_lan", action + " / 해당 차시 없음, planId -> " + planId); return; } //강의실을 여러번 열고 닫을수 있어 '마지막으로 강의실을 닫은 시간'으로 업데이트 할 것.
    courseLesson.clear();
    courseLesson.item("oper_edate", now);
    if(!courseLesson.update("twoway_url = '" + planId + "' AND status = 1")) { m.log("kt_lan", "강의 종료 시간 업데이트 실패 planId = " + planId); }

    //강사의 강제퇴장 처리로 인해 leave 처리가 안된 수강생 강제 처리
    DataSet cpList = courseProgress.find("course_id = " + clInfo.i("course_id") + " AND lesson_id = " + clInfo.i("lesson_id") + " AND site_id = " + clInfo.i("site_id") + " AND paragraph NOT LIKE '%-%'");
    while(cpList.next()) {
        m.log("kt_lan", action + " / 강제 퇴장 수강생 진도율 처리 / cid : " + cpList.i("course_id") + " / lesson_id : " + cpList.i("lesson_id") + " / cuid : " + cpList.i("course_user_id"));
        courseProgress.item("paragraph", cpList.s("paragraph") + "-" + now);
        if(!courseProgress.update("course_user_id = " + cpList.i("course_user_id") + " AND lesson_id = " + cpList.i("lesson_id") + " AND site_id = " + cpList.i("site_id"))) { m.log("kt_lan", action + " 퇴장시간 갱신실패 / planId : " + planId); };

        String sDateTime = clInfo.s("start_date") + clInfo.s("start_time");
        String eDateTime = clInfo.s("end_date") + clInfo.s("end_time");
        String start = m.diffDate("S", sDateTime, cpList.s("paragraph")) < 0 ? sDateTime : cpList.s("paragraph"); //더 큰 숫자 == 늦은 숫자로 입장 시간을 계산
        String end = m.diffDate("S", eDateTime, now) > 0 ? eDateTime : now; //더 작은 숫자 == 더 빠른 숫자로 퇴장 시간을 계산
        m.log("kt_lan", action + "/sDateTime : " + sDateTime + "/eDateTime : " + eDateTime + "/start : " + start + "/end : " + end);

        int gap = m.diffDate("S", start, end);
        m.log("kt_lan", action + " / 강제퇴장 시 학습시간 / cid : " + cpList.i("course_id") + " / lesson_id" + cpList.i("lesson_id") + " / cuid : " + cpList.i("course_user_id") + " / gap : " + gap);

        if(gap > 0) {
            courseProgress.setStudyTime(gap);
            int ret = courseProgress.updateProgress(cpList.i("course_user_id"), cpList.i("lesson_id"), cpList.i("chapter"));
            m.log("kt_lan", action + " / planId : " + planId + " / cuid : " + cpList.i("course_user_id") + " / cid : " + cpList.i("course_id") + " / lid : " + cpList.i("lesson_id") + " / courseProgress.updateProgress : " + ret);

            //수강생 정보 업데이트
            if (ret == 2) {
                courseUser.setProgressRatio(cpList.i("course_user_id"));
                courseUser.updateScore(cpList.i("course_user_id"), "progress"); //점수일괄업데이트
                courseUser.closeUser(cpList.i("course_user_id"), cpList.i("user_id"));
            }
        }
    }

    m.log("kt_lan", action + " / planId = " + planId);
    return;
} else {
    m.log("kt_lan", action + " / planId = " + planId);
}

%>
