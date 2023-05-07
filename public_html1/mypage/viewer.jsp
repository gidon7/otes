<%@ page contentType="text/html; charset=utf-8" %><%@ include file="init.jsp" %><%

if("play".equals(m.rs("mode")) && m.isMobile()) {
    m.jsErrClose("새로고침을 하시면 강의실이 닫힙니다.");
    return;
}

//기본키
int lid = m.ri("lid");
int cid = m.ri("cid");
if(lid == 0) { m.jsErrClose(_message.get("alert.common.required_key")); return; }

//객체
CourseLessonDao courseLesson = new CourseLessonDao();
LessonDao lesson = new LessonDao();
KtRemoteDao ktRemote = new KtRemoteDao(siteId, isDevServer);
LanEduDao lanEdu = new LanEduDao(siteId, isDevServer);
//목록
DataSet info = courseLesson.query(
    "SELECT a.*, l.lesson_type, l.status lesson_status"
    + " FROM " + courseLesson.table + " a "
    + " INNER JOIN " + lesson.table + " l ON a.lesson_id = l.id AND l.site_id = " + siteId
    + " WHERE a.status = 1 AND a.course_id = " + cid + " AND a.lesson_id = " + lid + " AND a.site_id = " + siteId
);
if(!info.next()) { m.jsErrClose(_message.get("alert.course_lesson.nodata")); return; }

//제한
if(userId != info.i("tutor_id")) { m.jsErrClose("올바른 접근이 아닙니다."); return; }
if(1 != info.i("lesson_status")) { m.jsErrClose(_message.get("alert.lesson.stopped")); return; }

//포맷팅
info.put("start_url", "#");
info.put("start_url_conv", "#");

if("15".equals(info.s("lesson_type"))) {
    DataSet siteconfig = SiteConfig.getArr(new String[] {"kt_remote", "lanedu_type"});
    String startUrl = "";
    if("B".equals(siteconfig.s("lanedu_type"))) {
        Json j = lanEdu.getPlanHostUrl(info.s("twoway_url"), info.i("host_num"));
        if(200 == j.getInt("//code")) { startUrl = j.getString("//result/linkUrl"); }
        else { m.jsAlert(j.getString("//message")); }
    }
    else if("L".equals(siteconfig.s("lanedu_type"))) { startUrl = ktRemote.getPlanUrl(info.s("twoway_url"), userId, loginId, "HOST", ""); }

    info.put("start_url", startUrl);
    info.put("start_url_conv", startUrl);
}

//출력
p.setLayout(null);
p.setBody("classroom.viewer_" + m.getItem(info.s("lesson_type"), lesson.htmlTypes));
p.setVar(info);

p.display();

%>