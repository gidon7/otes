<%@ page import="java.util.*, java.io.*, dao.*, malgnsoft.db.*, malgnsoft.util.*" %><%

    String tplRoot = Config.getDocRoot() + "/testing/html";
    String docRoot = Config.getDocRoot();

    Malgn m = new Malgn(request, response, out);

    Form f = new Form();
    f.setRequest(request);

    Page p = new Page(tplRoot);
    p.setRequest(request);
    p.setWriter(out);
    p.setPageContext(pageContext);


    String userId = null;
    Auth auth = new Auth(request, response);
    if(auth.isValid()) {
        userId = auth.getString("user_id");
    }

%>