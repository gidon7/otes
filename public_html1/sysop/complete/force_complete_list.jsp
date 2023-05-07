<%@ page contentType="text/html; charset=utf-8" %><%@ include file="init.jsp" %><%

//기본키
String mode = m.rs("mode");
boolean managementBlock = "management".equals(mode);

//접근권한
if(!Menu.accessible(141, userId, userKind)) { m.jsError("접근 권한이 없습니다."); return; }

//객체
ActionLogDao actionLog = new ActionLogDao();
UserDao user = new UserDao(isBlindUser);
CourseDao course = new CourseDao();
CourseUserDao courseUser = new CourseUserDao();

//기본키
int cid = m.ri("cid");
if(managementBlock && cid == 0) { m.jsError("기본키는 반드시 지정해야 합니다."); return; }

//폼체크
f.addElement("s_field", null, null);
f.addElement("s_keyword", null, null);

//목록
ListManager lm = new ListManager();
//lm.d(out);
lm.setRequest(request);
lm.setListNum("excel".equals(m.rs("mode")) ? 20000 : 20);
lm.setTable(
        actionLog.table + " a "
                + " LEFT JOIN " + courseUser.table + " cu ON a.module_id = cu.id AND cu.site_id = " + siteId + ""
                + " LEFT JOIN " + course.table + " c ON cu.course_id = c.id AND c.site_id = " + siteId + ""
                + " LEFT JOIN " + user.table + " m ON a.user_id = m.id AND m.site_id = " + siteId + ""
                + " LEFT JOIN " + user.table + " u ON cu.user_id = u.id AND u.site_id = " + siteId + ""
);
lm.setFields("a.*, m.login_id manager_login_id, m.user_nm manager_nm, m.status ustatus, cu.id cuid, cu.course_id, u.user_nm, u.login_id, u.id uid, c.course_nm, c.onoff_type");
if(managementBlock) lm.addWhere("cu.course_id = " + cid);
lm.addWhere("cu.status != -1");
lm.addWhere("a.action_type = 'F'");
lm.addWhere("a.module = 'course_user'");
lm.addWhere("a.status != -1");
lm.addWhere("a.site_id = " + siteId);
if(!"".equals(f.get("s_field"))) lm.addSearch(f.get("s_field"), f.get("s_keyword"), "LIKE");
else if("".equals(f.get("s_field")) && !"".equals(f.get("s_keyword"))) {
    if(managementBlock) { lm.addSearch("a.action_reason,u.user_nm,u.login_id,m.login_id,m.user_nm", f.get("s_keyword"), "LIKE"); }
    else { lm.addSearch("a.action_reason,u.user_nm,u.login_id,m.login_id,m.user_nm,c.course_nm", f.get("s_keyword"), "LIKE"); }
}
lm.setOrderBy(!"".equals(m.rs("ord")) ? m.rs("ord") : "a.id DESC");

//포멧팅
DataSet list = lm.getDataSet();
while(list.next()) {
    list.put("onoff_type_conv", m.getItem(list.s("onoff_type"), course.onoffTypes));
    list.put("reg_date_conv", m.time("yyyy.MM.dd HH:mm", list.s("reg_date")));
    user.maskInfo(list);
}

//기록-개인정보조회
if(list.size() > 0 && !isBlindUser) _log.add("L", Menu.menuNm, list.size(), "이러닝 운영");



//출력
p.setLayout(ch);
p.setBody("complete.force_complete_list");
p.setVar("p_title", managementBlock? "강제수료처리" : "수동처리관리");
p.setVar("query", m.qs());
p.setVar("list_query", m.qs("id"));
p.setVar("form_script", f.getScript());

p.setLoop("list", list);
p.setVar("management_block", managementBlock);
p.setVar("list_total", lm.getTotalString());
p.setVar("pagebar", lm.getPaging());

p.display();

%>