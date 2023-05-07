<%@ page contentType="text/html; charset=utf-8" %><%@ include file="init_api.jsp" %><%@ page import="malgnsoft.json.*" %><%
LanEduDao lanEdu = new LanEduDao(siteId, isDevServer);

lanEdu.setDebug(out);
m.p(lanEdu.getAuthToken());
%>