<%@ include file="/init.jsp" %><%
    boolean isMalgnOffice = "106.244.224.183".equals(userIp) || "125.129.123.211".equals(userIp) || "59.5.222.106".equals(userIp);

    if(!isMalgnOffice) {
        m.jsError("unauthorized access.");
        m.log("ktle_api", "외부접근 -> " + userIp);
        return;
    }
%>