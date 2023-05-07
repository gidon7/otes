<%@ page contentType="text/html; charset=utf-8" %><%@ include file="init.jsp" %><%

//기본키
String mode = m.rs("mode");
boolean managementBlock = "management".equals(mode);

//접근권한
if(!Menu.accessible(141, userId, userKind)) { m.jsError("접근 권한이 없습니다."); return; }

//기본키
int aid = m.ri("aid");
if(aid == 0) { m.jsError("기본키는 반드시 지정해야 합니다."); return; }

//객체
ActionLogDao actionLog = new ActionLogDao();

DataSet ainfo = actionLog.find("id = " + aid + " AND site_id = " + siteId + "");
if(!ainfo.next()) { m.jsError("해당 정보가 없습니다."); return; }

//폼체크
f.addElement("action_reason", ainfo.s("action_reason"), "hname:'액션사유', required:'Y'");

//처리
if(m.isPost() && f.validate()) {

    actionLog.item("action_reason", f.get("action_reason"));
    if(!actionLog.update("id = " + aid + " AND site_id = " + siteId + "")) { m.jsAlert("수정하는 중 오류가 발생했습니다."); return; }

    m.jsAlert("수정하였습니다.");
    m.jsReplace("force_complete_list.jsp?" +  m.qs(), "parent");
    return;
}

//출력
p.setLayout("poplayer");
p.setBody("complete.force_complete_reason_modify");
p.setVar("p_title", "강제수료처리 사유");
p.setVar("query", m.qs());
p.setVar("list_query", m.qs("id"));
p.setVar("form_script", f.getScript());

p.display();

%>