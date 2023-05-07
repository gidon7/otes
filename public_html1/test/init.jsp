<%@ page import="java.util.*, java.io.*, dao.*, malgnsoft.db.*, malgnsoft.util.*" %><%

    String tplRoot = Config.getDocRoot() + "/test/html";

    Malgn m = new Malgn(request, response, out);

    Form f = new Form();
    f.setRequest(request);

    Page p = new Page(tplRoot);
    p.setRequest(request);
    p.setWriter(out);
    p.setPageContext(pageContext);

%>