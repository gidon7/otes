<%@ page contentType="text/html; charset=utf-8" %><%@ include file="/init.jsp" %><%

    TestDao test = new TestDao();
    DataSet info = test.find("user_nm = 'GD'");
    if(info.next()) {
        auth.put("user_nm", info.s("id"));
        auth.setAuthInfo();
    }

    m.p(auth.getString("user_nm"));

%>
