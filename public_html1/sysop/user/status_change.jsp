<%@ page contentType="text/html; charset=utf-8" %><%@ include file="init.jsp" %><%

//CHECKED-2014.06.27

//접근권한
if(!Menu.accessible(19, userId, userKind)) { m.jsError("접근 권한이 없습니다."); return; }
if(!superBlock) { m.jsError("접근 권한이 없습니다."); return; }

//기본키
String idx = m.rs("idx");
if("".equals(idx)) { m.jsError("기본키는 반드시 지정해야 합니다."); return; }

//객체
UserDao user = new UserDao();

int ucnt = user.findCount("id IN (" + idx + ") AND status = -2 AND site_id = " + siteId);
String[] uidx = idx.split(",");

//탈퇴회원제한
if(uidx.length == 1 && ucnt > 0) {
    m.jsAlert("선택한 회원은 탈퇴회원입니다.\\n탈퇴회원은 상태변경 처리 제외됩니다.");
    m.jsReplace("user_list.jsp?" + m.qs("idx"), "parent");
    return;
}else if(ucnt > 0){
    m.jsAlert("선택한 회원 중에 탈퇴회원이 있습니다.\\n탈퇴회원은 상태변경 처리 제외됩니다.");
    m.jsReplace("user_list.jsp?" + m.qs("idx"), "parent");
    return;
}

//폼체크
f.addElement("status", null, "hname:'상태', required:'Y'");

//등록
if(m.isPost() && f.validate()) {

    user.item("status", f.get("status"));
    if(!user.update("id IN (" + idx + ") AND site_id = " + siteId)) { m.jsError("회원상태을 수정하는 중 오류가 발생했습니다."); return; }

    m.jsReplace("user_list.jsp?" + m.qs("idx"), "parent");
    return;
}

//출력
p.setLayout("poplayer");
p.setBody("user.status_change");
p.setVar("p_title", "회원상태 변경");
p.setVar("query", m.qs());
p.setVar("list_query", m.qs("id"));
p.setVar("form_script", f.getScript());

p.display();

%>