<%@ page contentType="text/html; charset=utf-8" %><%@ include file="./init.jsp" %><%

//동의
if(m.isPost() && f.validate()) {

    int newId = _log.add("A", "일일 동의", 0, "기록 동의 및 가려진 정보 표시");
    if(newId == -1) { m.jsAlert("이미 처리되었습니다."); }

    auth.put("PAGREE_DATE", sysToday);
    auth.setAuthInfo();

    //이동
    m.js("parent.parent.location.href = parent.parent.location.href;");
    return;
}

//출력
p.setLayout("poplayer");
p.setBody("main.privacy_agree");
p.setVar("p_title", "일일 동의");
p.display();

%>