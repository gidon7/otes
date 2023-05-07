/*
 * JSP generated by Resin-3.1.15 (built Mon, 13 Oct 2014 06:45:33 PDT)
 */

package _jsp;
import javax.servlet.*;
import javax.servlet.jsp.*;
import javax.servlet.http.*;
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
      

String agent = request.getHeader("user-agent");
boolean isMobile = false;
if(null != agent) {
	String[] mobileKeyWords = {
		"iPhone", "iPod", "iPad"
		, "BlackBerry", "Android", "Windows CE"
		, "LG", "MOT", "SAMSUNG", "SonyEricsson"
	};
	for(int i=0; i<mobileKeyWords.length; i++) {
		if(agent.indexOf(mobileKeyWords[i]) != -1) {
			isMobile = true;
			break;
		}
	}
}

Malgn m = new Malgn(request, response, out);

//\ucc98\ub9ac-\uacf5\uc0ac\uc911
SiteDao Site = new SiteDao(); //Site.clear();
DataSet siteinfo = Site.getSiteInfo(request.getServerName());
SiteConfigDao SiteConfig = new SiteConfigDao(siteinfo.i("id"));
if(1 != siteinfo.i("status") || "".equals(siteinfo.s("doc_root"))) {
	m.jsReplace("about:blank", "top"); 
	return;
}

//\ucc98\ub9ac-\uc2a4\ud0a85\uc774\uc0c1\ubaa8\ubc14\uc77c\ud574\uc81c
if(5 <= siteinfo.i("skin_cd")) isMobile = false;

//\ucc98\ub9ac-B2B\ub3c4\uba54\uc778
UserDeptDao userDept = new UserDeptDao();
String B2Bcode = m.rs("b2b");
if(!"".equals(B2Bcode)) {
	DataSet b2binfo = userDept.getB2BInfo(B2Bcode, siteinfo.i("id"));
	if(b2binfo.next()) {
		if(!isMobile) response.sendRedirect("/member/login.jsp?udid=" + b2binfo.i("id"));
		else response.sendRedirect("/mobile/login.jsp?udid=" + b2binfo.i("id"));
		return;
	}
}

if("Y".equals(SiteConfig.s("prepare_yn"))) {

      out.write(_jsp_string0, 0, _jsp_string0.length);
      
	return;
}

//\uc774\ub3d9
String qs = m.qs("");
if(!"".equals(qs)) qs = "?" + qs;

if(!isMobile) {
	response.setStatus(HttpServletResponse.SC_MOVED_PERMANENTLY);
	response.setHeader("Location", request.getContextPath() + "/main/index.jsp" + qs);
	//response.sendRedirect("main/index.jsp" + qs);
} else {
	response.setStatus(HttpServletResponse.SC_MOVED_PERMANENTLY);
	response.setHeader("Location", request.getContextPath() + "/mobile/index.jsp" + qs);
	//response.sendRedirect("mobile/index.jsp" + qs);

}


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
    depend = new com.caucho.vfs.Depend(appDir.lookup("index.jsp"), 565478994693751456L, false);
    com.caucho.jsp.JavaPage.addDepend(_caucho_depends, depend);
  }

  private final static char []_jsp_string0;
  static {
    _jsp_string0 = "\n<!doctype html>\n<html lang=\"ko\">\n<head>\n	<meta charset=\"utf-8\">\n	<title>\uc900\ube44\uc911\uc785\ub2c8\ub2e4.</title>\n	<style>\n	* { margin:0; padding:0; }\n	html, body { width:100%; height:100%; overflow:hidden; }\n	#container { width:100%; height:100%; text-align:center; background:url('/common/images/under_construction.jpg') 50% 50% no-repeat; text-indent:-9999px; }\n	\n	@media screen and (max-width:998px) {\n		#container { background-size:contain; }\n	}\n	@media screen and (max-height:469px) {\n		#container { background-size:contain; }\n	}\n	</style>\n</head>\n<body>\n<div id=\"container\">\n	<h1>\ud648\ud398\uc774\uc9c0 <strong>\uc624\ud508 \uc900\ube44\uc911</strong>\uc785\ub2c8\ub2e4.</h1>\n	<h2>\ud648\ud398\uc774\uc9c0\ub97c \ucc3e\uc544\uc8fc\uc2dc\ub294 \ubaa8\ub4e0 \ubd84\ub4e4\uaed8 \ub354 \ub098\uc740 \uc11c\ube44\uc2a4\ub97c \uc81c\uacf5\ud558\uae30 \uc704\ud574 <strong>\ud648\ud398\uc774\uc9c0\uac00 \uc0c8 \ub2e8\uc7a5 \uc911\uc5d0 \uc788\uc2b5\ub2c8\ub2e4.</strong> \uc88b\uc740 \ubaa8\uc2b5\uc73c\ub85c \uace7 \ucc3e\uc544\ubd59\uaca0\uc2b5\ub2c8\ub2e4. \uac10\uc0ac\ud569\ub2c8\ub2e4.</h2>\n</div>\n</body>\n</html>\n".toCharArray();
  }
}