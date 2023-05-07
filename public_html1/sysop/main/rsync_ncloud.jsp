<%@ page contentType="text/html; charset=utf-8" %><%@ include file="../init.jsp" %><%

    if(!"malgn".equals(loginId) || !"nlms.malgn.co.kr".equals(request.getServerName())) m.jsReplace("/common/error_404.html");

    String ret = "";
    String path = m.rs("path");

    ret = m.exec("/home/lms/rsync.sh " + path);
    m.p(m.nl2br(ret));
%>