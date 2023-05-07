<%@ page contentType="text/javascript; charset=utf-8" %><%@ include file="/init.jsp" %>
var xid = '<%= m.rs("xid") %>';
var uid = <%= userId %>;
if(xid === '' && uid > 0) location.href = 'https://www.itpe.co.kr/api/sso/itpe_sso.jsp';
//else if(auth != null && uid == 0) location.href = '/member/logout.jsp';