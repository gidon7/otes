<%@ page contentType="text/html; charset=utf-8" %><%@ include file="init.jsp" %><%

//접근권한
if(!Menu.accessible(139, userId, userKind)) { m.jsError("접근 권한이 없습니다."); return; }

if(!isBiz) { m.jsAlert("kt랜선에듀biz 서비스를 신청하셔야 이용할 수 있습니다."); return; }

//객체
CourseLessonDao courseLesson = new CourseLessonDao();
LessonDao lesson = new LessonDao();
UserDao user = new UserDao(isBlindUser);
CourseDao course = new CourseDao();


//폼체크
f.addElement("s_listnum", null, null);
f.addElement("start_date", m.time("yyyy-MM-dd"), null);

//목록
ListManager lm = new ListManager();
//lm.d(out);
lm.setRequest(request);
lm.setListNum("excel".equals(m.rs("mode")) ? 20000 : f.getInt("s_listnum", 20));
lm.setTable(
    courseLesson.table + " a "
    + "INNER JOIN " + lesson.table + " l ON a.lesson_id = l.id AND l.site_id = " + siteId + " AND l.status = 1 "
    + "INNER JOIN " + user.table + " u ON a.tutor_id = u.id AND u.status = 1 AND u.site_id = " + siteId + " "
    + "INNER JOIN " + course.table + " c on a.course_id = c.id AND c.site_id = " + siteId + " "
);
lm.setFields("a.chapter, a.start_date, a.start_time, a.end_date, a.end_time, a.host_num, a.course_id, a.lesson_id, l.start_url, l.lesson_nm, u.id user_id, u.user_nm, u.login_id, c.course_nm");
lm.addWhere("a.status != -1");
lm.addWhere("a.site_id = " + siteId + "");
lm.addWhere("l.lesson_type='15'");
lm.addSearch("a.start_date", m.time("yyyyMMdd", f.get("start_date", sysNow)));
lm.setOrderBy("a.start_date asc, a.start_time asc, a.end_date asc, a.end_time asc");

//포멧팅
DataSet list = lm.getDataSet();
while(list.next()) {
    list.put("start_date_conv", m.time("yyyy.MM.dd HH:mm", list.s("start_date") + list.s("start_time")));
    list.put("end_date_conv", m.time("yyyy.MM.dd HH:mm", list.s("end_date") + list.s("end_time")));
    list.put("twoway_type_conv", m.getItem(list.s("start_url"), lanEdu.roomTypes));
    list.put("lesson_nm_conv", m.cutString(list.s("lesson_nm"), 100));

	user.maskInfo(list);
}

//기록-개인정보조회
if("".equals(m.rs("mode")) && list.size() > 0 && !isBlindUser) _log.add("L", Menu.menuNm, list.size(), "이러닝 운영", list);

//엑셀
if("excel".equals(m.rs("mode"))) {
    if(list.size() > 0 && !isBlindUser) _log.add("E", Menu.menuNm, list.size(), "이러닝 운영", list);

    ExcelWriter ex = new ExcelWriter(response, "화상강의관리(" + m.time("yyyy-MM-dd") + ").xls");
    ex.setData(list, new String[] { "__ord=>No", "course_nm=>과정명", "lesson_nm=>강의명", "chapter=>차시", "twoway_type_conv=>강의실유형", "start_date_conv=>시작시간", "end_date_conv=>종료시간", "login_id=>강사", "host_num=>호스트" }, "화상강의관리(" + m.time("yyyy-MM-dd") + ")");
    ex.write();
    return;
}

//출력
p.setLayout("pop");
p.setBody("twoway.lesson_list");
p.setVar("list_query", m.qs("id"));
p.setVar("query", m.qs());
p.setVar("form_script", f.getScript());

p.setVar("pinfo", pinfo);
p.setLoop("list", list);
p.setVar("list_total", lm.getTotalString());
p.setVar("pagebar", lm.getPaging());

p.display();

%>