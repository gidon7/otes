/*
 * JSP generated by Resin-3.1.15 (built Mon, 13 Oct 2014 06:45:33 PDT)
 */

package _jsp._testing;
import javax.servlet.*;
import javax.servlet.jsp.*;
import javax.servlet.http.*;
import java.util.*;
import java.io.*;
import dao.*;
import malgnsoft.db.*;
import malgnsoft.util.*;

public class _index__jsp extends com.caucho.jsp.JavaPage
{
  private static final java.util.HashMap<String,java.lang.reflect.Method> _jsp_functionMap = new java.util.HashMap<String,java.lang.reflect.Method>();
  private boolean _caucho_isDead;
  
  public void
  _jspService(javax.servlet.http.HttpServletRequest request,
              javax.servlet.http.HttpServletResponse response)
    throws java.io.IOException, javax.servlet.ServletException
  {
    javax.servlet.http.HttpSession session = request.getSession(true);
    com.caucho.server.webapp.WebApp _jsp_application = _caucho_getApplication();
    javax.servlet.ServletContext application = _jsp_application;
    com.caucho.jsp.PageContextImpl pageContext = _jsp_application.getJspApplicationContext().allocatePageContext(this, _jsp_application, request, response, null, session, 8192, true, false);
    javax.servlet.jsp.PageContext _jsp_parentContext = pageContext;
    javax.servlet.jsp.JspWriter out = pageContext.getOut();
    final javax.el.ELContext _jsp_env = pageContext.getELContext();
    javax.servlet.ServletConfig config = getServletConfig();
    javax.servlet.Servlet page = this;
    response.setContentType("text/html; charset=utf-8");
    request.setCharacterEncoding("UTF-8");
    try {
      

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


      

//\uac1d\uccb4
TestDao test = new TestDao();

//\ud3fc\uccb4\ud06c
f.addElement("user_nm", null, "required: 'Y'");
f.addElement("email", null, "option: 'email'");

//    DataSet users = test.query("SELECT USER_NM, EMAIL from TB_TEST", 5); //\ucffc\ub9ac\ubb38 \ub4a4\uc5d0 \uc22b\uc790\ub97c \ub123\uc5b4\uc8fc\uba74 \uadf8 \uc22b\uc790\ub9cc\ud07c \uc815\ubcf4\ub97c \uac00\uc838\uc628\ub2e4.

//    DataSet users = test.query("SELECT USER_NM, EMAIL FROM TB_TEST WHERE EMAIL = 'hong@gmail.com'");


//DB\uc5d0 \ub4f1\ub85d
if(m.isPost() && f.validate()) {
    test.item("user_nm", f.get("user_nm"));
    test.item("email", f.get("email"));
    if(!test.insert()) { //true false \ub9ac\ud134
        m.jsError("\ub4f1\ub85d \uc624\ub958\uac00 \ubc1c\uc0dd\ud588\uc2b5\ub2c8\ub2e4.");
    } else{
        m.jsAlert("\uc131\uacf5\uc801\uc73c\ub85c \ub4f1\ub85d\ub418\uc5c8\uc2b5\ub2c8\ub2e4.");
    }
    return;
}


//\ub370\uc774\ud130\uc14b
DataSet users = new DataSet();

users.addRow();
users.put("user_nm", "Daniel");
users.put("email", "daniel@gmail.com");

// \uc5ec\ub7ec \uac1c\uac00 \uac80\uc0c9\ub418\uc5b4\ub3c4 id\uac00 \ub0ae\uc740\uac70 \ud558\ub098\ub9cc \uac00\uc838\uc628\ub2e4.
DataSet info = test.find("user_nm = 'GD'"); // \ub2e8\uc77c \ub808\ucf54\ub4dc
//DataSet info = test.query("SELECT * FROM TB_TEST");// \ubcf5\uc218 \ub808\ucf54\ub4dc

if(!info.next()) { // next() \ub97c \ud1b5\ud574 \ud3ec\uc778\ud130\ub97c \uc774\ub3d9\uc2dc\ucf1c \uccab\ubc88\uc9f8 \ub370\uc774\ud130\uac00 \uc874\uc7ac\ud558\ub294\uc9c0 \ud655\uc778\ud55c\ub2e4.
    m.jsError("\ud68c\uc6d0\uc815\ubcf4\ub97c \ucc3e\uc744 \uc218 \uc5c6\uc2b5\ub2c8\ub2e4."); // alert \ud55c \ud6c4 history.go(-1) \uc774\uc804\ud398\uc774\uc9c0\ub85c
    return; // \ub9ac\ud134\uc744 \ud558\uc9c0 \uc54a\uc73c\uba74 \uc544\ub798 \ub0b4\uc6a9\uc774 \uc2e4\ud589\ub420 \uc218 \uc788\ub2e4.
}

int inputNum = test.execute("INSERT INTO TB_TEST (user_nm, email) VALUES ('hong', 'honghong@gmail.com')");// \uc601\ud5a5 \ubc1b\uc744 \ub808\ucf54\ub4dc \uac1c\uc218\ub97c \ub9ac\ud134

//DB \uc218\uc815
test.item("user_nm","GD");
//test.update("user_nm = 'hong'");
int updateNum = test.execute("UPDATE TB_TEST SET email = 'igd@gmail.com' WHERE id = '50' "); // 40\ubc88\uc9f8\ub098 \ub458\uc911 \ud558\ub098 \uc0ac\uc6a9

//DB \uc0ad\uc81c
//    test.delete("user_nm = 'hong'");
//    user.execute("DELETE FROM TB_TEST WHERE user_nm = '\ud64d\uae38\ub3d9'"); // 44\ubc88\uc9f8\ub098 \ub458\uc911 \ud558\ub098 \uc0ac\uc6a9


//ListManager lm = new ListManager();
//
//lm.setRequest(request);
//lm.setListNum(10);
//lm.setTable(test.table);
//lm.setFields("*");
//    lm.addWhere("status !=-1");// where\uc808
//    lm.addSearch("name, email, phone", "Hong", "LIKE");
//    	SQL : (name LIKE '%Hong%' OR email LIKE '%Hong%' OR phone LIKE


//DataSet list = lm.getDataSet();
//while(list.next()) { // first() last() prev() \ucee4\uc11c \uc774\ub3d9
//    list.put("name_conv", list.s("user_nm") + "\uc528");
//}

//Excel
//ExcelReader ex = new ExcelReader("");
//DataSet exList = ex.getDataSet();


//XML
SimpleParser sp = new SimpleParser(docRoot + "/testing/test.xml"); // \uc18d\uc131\uac12\uc744 \uac00\uc838\uc624\ub824\uba74 \ub2e4\ub978 XML parser \uc774\uc6a9\ud574\uc57c
DataSet spList = sp.getDataSet("//note"); // \ub178\ub4dc \uacbd\ub85c
m.p("XML: " + spList);





p.setLayout("main");
p.setBody("main.index");
p.setVar("form_script", f.getScript());
p.setVar("title", "Admin Page");
p.setVar("is_admin", true); // is_admin(\uc870\uac74 \ubcc0\uc218\uba85)\uc774 true\ub77c\uba74 html\uc5d0\uc11c \ubb38\uc7a5\uc774 \ub098\uc628\ub2e4.
p.setLoop("users", users); //users\ub77c\ub294 DataSet\uac1d\uccb4\uac00 \ud68c\uc6d0\ubaa9\ub85d\uc774\ubbc0\ub85c setloop\uba54\uc18c\ub4dc\ub97c \uc774\uc6a9\ud574\uc11c \ub370\uc774\ud130\ub97c \ub118\uaca8\uc900\ub2e4. html\uc5d0\uc11c loop\uc9c0\uc2dc\uc790\ub97c \ud1b5\ud574 \ud68c\uc6d0\ubaa9\ub85d\uc744 \ucd9c\ub825\ud558\ub294 html\uc744 \uc0dd\uc131
p.setVar(info);
p.setLoop("info", info);
p.setVar("input_num", inputNum);
p.setVar("update_num", updateNum);
p.display();



      out.write(_jsp_string0, 0, _jsp_string0.length);
    } catch (java.lang.Throwable _jsp_e) {
      pageContext.handlePageException(_jsp_e);
    } finally {
      _jsp_application.getJspApplicationContext().freePageContext(pageContext);
    }
  }

  private java.util.ArrayList _caucho_depends = new java.util.ArrayList();

  public java.util.ArrayList _caucho_getDependList()
  {
    return _caucho_depends;
  }

  public void _caucho_addDepend(com.caucho.vfs.PersistentDependency depend)
  {
    super._caucho_addDepend(depend);
    com.caucho.jsp.JavaPage.addDepend(_caucho_depends, depend);
  }

  public boolean _caucho_isModified()
  {
    if (_caucho_isDead)
      return true;
    if (com.caucho.server.util.CauchoSystem.getVersionId() != 6749855747778707107L)
      return true;
    for (int i = _caucho_depends.size() - 1; i >= 0; i--) {
      com.caucho.vfs.Dependency depend;
      depend = (com.caucho.vfs.Dependency) _caucho_depends.get(i);
      if (depend.isModified())
        return true;
    }
    return false;
  }

  public long _caucho_lastModified()
  {
    return 0;
  }

  public java.util.HashMap<String,java.lang.reflect.Method> _caucho_getFunctionMap()
  {
    return _jsp_functionMap;
  }

  public void init(ServletConfig config)
    throws ServletException
  {
    com.caucho.server.webapp.WebApp webApp
      = (com.caucho.server.webapp.WebApp) config.getServletContext();
    super.init(config);
    com.caucho.jsp.TaglibManager manager = webApp.getJspApplicationContext().getTaglibManager();
    com.caucho.jsp.PageContextImpl pageContext = new com.caucho.jsp.PageContextImpl(webApp, this);
  }

  public void destroy()
  {
      _caucho_isDead = true;
      super.destroy();
  }

  public void init(com.caucho.vfs.Path appDir)
    throws javax.servlet.ServletException
  {
    com.caucho.vfs.Path resinHome = com.caucho.server.util.CauchoSystem.getResinHome();
    com.caucho.vfs.MergePath mergePath = new com.caucho.vfs.MergePath();
    mergePath.addMergePath(appDir);
    mergePath.addMergePath(resinHome);
    com.caucho.loader.DynamicClassLoader loader;
    loader = (com.caucho.loader.DynamicClassLoader) getClass().getClassLoader();
    String resourcePath = loader.getResourcePathSpecificFirst();
    mergePath.addClassPath(resourcePath);
    com.caucho.vfs.Depend depend;
    depend = new com.caucho.vfs.Depend(appDir.lookup("testing/index.jsp"), -1913027554229175698L, false);
    com.caucho.jsp.JavaPage.addDepend(_caucho_depends, depend);
    depend = new com.caucho.vfs.Depend(appDir.lookup("testing/init.jsp"), -605037953067765338L, false);
    com.caucho.jsp.JavaPage.addDepend(_caucho_depends, depend);
  }

  private final static char []_jsp_string0;
  static {
    _jsp_string0 = "\r\n".toCharArray();
  }
}