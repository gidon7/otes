<%@ page contentType="text/html; charset=utf-8" %><%@ include file="init.jsp" %><%

//기본키
int lid = m.ri("id");
int cid = m.ri("cid");
if(lid == 0 || cid == 0) { m.jsError("기본키는 반드시 지정해야 합니다."); return; }

//객체
CourseLessonDao courseLesson = new CourseLessonDao();
CourseDao course = new CourseDao();
LessonDao lesson = new LessonDao();
UserDao user = new UserDao();
MCal mcal = new MCal();

DataSet courseInfo = course.find("site_id = " + siteId + " AND id = " + cid + " AND status = 1", "study_sdate, study_edate");
if(!courseInfo.next()) { m.jsError("해당 과정정보가 없습니다."); return; }

//정보
DataSet info = courseLesson.query(
    "SELECT a.course_id, a.lesson_id, a.tutor_id, a.start_date, a.end_date, a.start_time, a.end_time, a.twoway_url, a.host_num, "
    + " l.onoff_type, l.lesson_type, l.lesson_nm, l.start_url, l.total_time, l.complete_time, l.content_width, l.content_height, l.manager_id, l.description, l.reg_date, l.status, u.login_id, u.user_nm tutor_nm"
    + " FROM " + courseLesson.table + " a "
    + " INNER JOIN " + course.table + " c ON a.course_id = c.id AND c.site_id = " + siteId + " AND c.status = 1 "
    + " INNER JOIN " + lesson.table + " l ON a.lesson_id = l.id AND l.site_id = " + siteId + " AND l.status != -1 "
    + " LEFT JOIN " + user.table + " u ON a.tutor_id = u.id AND u.site_id = " + siteId + " AND u.status = 1 "
    + " WHERE a.status != -1 AND a.site_id = " + siteId + " AND l.onoff_type = 'T'"
    + " AND a.course_id = " + cid + " AND a.lesson_id = " + lid
);

if(!info.next()) { m.jsError("해당 정보가 없습니다."); return; }

//폼체크
String startDate = !"".equals(info.s("start_date")) ? m.time("yyyy-MM-dd", info.s("start_date")) : m.time("yyyy-MM-dd");
String endDate = !"".equals(info.s("end_date")) ? m.time("yyyy-MM-dd", info.s("end_date")) : startDate;
String startHour = info.s("start_time").length() == 6 ? info.s("start_time").substring(0, 2) : "00";
String startMinute = info.s("start_time").length() == 6 ? info.s("start_time").substring(2, 4) : "00";
String endHour = info.s("end_time").length() == 6 ? info.s("end_time").substring(0, 2) : "23";
String endMinute = info.s("end_time").length() == 6 ? info.s("end_time").substring(2, 4) : "55";

f.addElement("lesson_type", info.s("lesson_type"), "hname:'강의타입'");
f.addElement("room_type", info.s("start_url"), "hname:'강의실타입'");
f.addElement("lesson_nm", info.s("lesson_nm"), "hname:'교과목명', required:'Y'");
f.addElement("total_time", info.i("total_time"), "hname:'학습시간', option:'number'");
f.addElement("complete_time", info.i("complete_time"), "hname:'인정시간', option:'number'");
f.addElement("content_width", info.i("content_width"), "hname:'창넓이', option:'number'");
f.addElement("content_height", info.i("content_height"), "hname:'창높이', option:'number'");
if(!courseManagerBlock) f.addElement("manager_id", info.i("manager_id"), "hname:'담당자'");
f.addElement("status", info.i("status"), "hname:'상태', required:'Y'");
f.addElement("start_date", startDate, "hname:'시작일', required:'Y'");
f.addElement("end_date", endDate, "hname:'마감일', required:'Y'");
f.addElement("start_time_hour", startHour, "hname:'시작시', option:'number'");
f.addElement("start_time_min", startMinute, "hname:'시작분', option:'number'");
f.addElement("end_time_hour", endHour, "hname:'마감시', option:'number'");
f.addElement("end_time_min", endMinute, "hname:'마감분', option:'number'");

//등록
if(m.isPost() && f.validate()) {

    String sdate = m.time("yyyyMMdd", f.get("start_date"));
    String startTime = f.get("start_time_hour") + f.get("start_time_min") + "00";
    String endTime = f.get("end_time_hour") + f.get("end_time_min") + "59";

    if(1 > m.diffDate("I", sdate + startTime, sdate + endTime)) { m.jsAlert("강의 시작시간이 종료시간을 초과했습니다."); return; }

    if(isBiz) {
        Json j = lanEdu.updatePlan(info.s("twoway_url"), f.get("lesson_nm"), info.i("host_num"), m.getUnixTime(sdate + startTime), m.getUnixTime(sdate + endTime));
        if(200 != j.getInt("//code")) {
            m.jsAlert(j.getString("//message"));
            return;
        }
    } else {
        if(!ktRemote.updatePlan(info.s("twoway_url"), f.get("lesson_nm"), info.i("tutor_id"), m.getUnixTime(sdate + startTime), m.getUnixTime(sdate + endTime))) { m.jsAlert("수정하는 중 오류가 발생했습니다[1]."); return; }
    }

    lesson.item("lesson_nm", f.get("lesson_nm"));
    lesson.item("total_time", f.getInt("total_time"));
    lesson.item("complete_time", f.getInt("complete_time"));
    lesson.item("content_width", f.getInt("content_width"));
    lesson.item("content_height", f.getInt("content_height"));
    lesson.item("manager_id", !courseManagerBlock ? f.getInt("manager_id") : userId);
    lesson.item("status", f.getInt("status"));
    if(!lesson.update("id = " + info.i("lesson_id") + " AND site_id = " + siteId)) { m.jsAlert("수정하는 중 오류가 발생했습니다[2]."); return; }

    courseLesson.item("start_date", sdate);
    courseLesson.item("end_date", sdate);
    courseLesson.item("start_time", startTime);
    courseLesson.item("end_time", endTime);

    if(!courseLesson.update("lesson_id = " + lid + " AND course_id = " + cid)) { m.jsAlert("수정하는 중 오류가 발생했습니다[3]."); return; }
    m.js("try { parent.opener.location.href = parent.opener.location.href; } catch(e) { } parent.window.close();");
    return;
}

//포맷팅
info.put("lesson_type_conv", m.getItem(info.s("lesson_type"), lesson.allTypes));
info.put("room_type_conv", isBiz ? m.getItem(info.s("start_url"), lanEdu.roomTypes) : m.getItem(info.s("start_url"), ktRemote.roomTypes));
info.put("reg_date_conv", m.time("yyyy.MM.dd HH:mm:ss", info.s("reg_date")));
info.put("start_time_hour", startHour);
info.put("start_time_min", startMinute);
info.put("end_time_hour", endHour);
info.put("end_time_min", endMinute);

//출력
p.setLayout("pop");
p.setBody("twoway.lesson_insert");
p.setVar("p_title", "화상강의관리");
p.setVar("query", m.qs());
p.setVar("list_query", m.qs("id"));
p.setVar("form_script", f.getScript());

p.setVar("modify", true);
p.setVar("study_sdate", courseInfo.s("study_sdate"));
p.setVar("study_edate", courseInfo.s("study_edate"));
p.setVar(info);

p.setLoop("status_list", m.arr2loop(lesson.statusList));
p.setLoop("hours", mcal.getHours());
p.setLoop("minutes", mcal.getMinutes(5));

p.display();

%>