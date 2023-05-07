<%@ page contentType="text/html; charset=utf-8" %><%@ include file="init.jsp" %><%

//접근권한
if(!isMalgnOffice || siteinfo.i("super_id") != userId) { m.jsError("접근 권한이 없습니다."); return; }
//객체
ActionLogDao actionLog = new ActionLogDao();
UserDao user = new UserDao();

//폼체크
f.addElement("s_type", null, null);
f.addElement("s_module", null, null);
f.addElement("s_field", null, null);
f.addElement("s_keyword", null, null);

//목록
ListManager lm = new ListManager();
//lm.d(out);
lm.setRequest(request);
lm.setListNum("excel".equals(m.rs("mode")) ? 20000 : 20);
lm.setTable(
        actionLog.table + " a "
        + " LEFT JOIN " + user.table + " m ON a.user_id = m.id AND m.site_id = a.site_id "
);
lm.setFields("a.*, m.login_id, m.user_nm, m.status ustatus");
lm.addWhere("a.status != -1");
lm.addWhere("a.site_id = " + siteId);
lm.addSearch("a.action_type", f.get("s_type"));
lm.addSearch("a.module", f.get("s_module"));
if(!"".equals(f.get("s_field"))) lm.addSearch(f.get("s_field"), f.get("s_keyword"), "LIKE");
else if("".equals(f.get("s_field")) && !"".equals(f.get("s_keyword"))) {
    lm.addSearch("a.before_info,a.after_info", f.get("s_keyword"), "LIKE");
}
lm.setOrderBy(!"".equals(m.rs("ord")) ? m.rs("ord") : "a.id DESC");

//포멧팅
DataSet list = lm.getDataSet();
while(list.next()) {
    list.put("action_type_conv", m.getItem(list.s("action_type"), actionLog.types));
    list.put("module_conv", m.getItem(list.s("module"), actionLog.modules));
    list.put("reg_date_conv", m.time("yyyy.MM.dd HH:mm", list.s("reg_date")));
    list.put("before_info", m.cutString(list.s("before_info"), 55));
    list.put("after_info", m.cutString(list.s("after_info"), 55));
}


//출력
p.setLayout(ch);
p.setBody("alog.log_list");
p.setVar("p_title", "작업로그관리");
p.setVar("query", m.qs());
p.setVar("list_query", m.qs("id"));
p.setVar("form_script", f.getScript());

p.setLoop("list", list);
p.setVar("list_total", lm.getTotalString());
p.setVar("pagebar", lm.getPaging());

p.setLoop("types", m.arr2loop(actionLog.types));
p.setLoop("modules", m.arr2loop(actionLog.modules));
p.setLoop("status_list", m.arr2loop(actionLog.statusList));
p.display();

%>