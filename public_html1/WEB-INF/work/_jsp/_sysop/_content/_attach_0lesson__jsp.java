/*
 * JSP generated by Resin-3.1.15 (built Mon, 13 Oct 2014 06:45:33 PDT)
 */

package _jsp._sysop._content;
import javax.servlet.*;
import javax.servlet.jsp.*;
import javax.servlet.http.*;
import java.util.*;
import java.io.*;
import dao.*;
import malgnsoft.db.*;
import malgnsoft.util.*;

public class _attach_0lesson__jsp extends com.caucho.jsp.JavaPage
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
      

String docRoot = Config.getDocRoot();
String jndi = Config.getJndi();
String tplRoot = Config.getDocRoot() + "/sysop/html";

Malgn m = new Malgn(request, response, out);

Form f = new Form("form1");
try { f.setRequest(request); } catch (Exception ex) { out.print("\uc81c\ud55c \uc6a9\ub7c9 \ucd08\uacfc - " + ex.getMessage()); return; }

Page p = new Page(tplRoot);
p.setRequest(request);
p.setPageContext(pageContext);
p.setWriter(out);
p.setBaseRoot("/home/lms/public_html/html");

SiteDao Site = new SiteDao();
DataSet siteinfo = Site.getSiteInfo(request.getServerName(), "sysop");
SiteConfigDao SiteConfig = new SiteConfigDao(siteinfo.i("id"));
if(1 != siteinfo.i("sysop_status") || "".equals(siteinfo.s("doc_root"))) { m.jsReplace("/main/guide.jsp", "top"); return; }
//Hashtable<String, String> siteconfig = SiteConfig.getSiteConfig(siteinfo.s("id"));

String siteDomain = request.getScheme() + "://" + request.getServerName();
int port = request.getServerPort();
if(port != 80) siteDomain += ":" + port;
String webUrl = siteDomain + "/sysop";

String dataDir = siteinfo.s("doc_root") + "/data";
f.dataDir = dataDir;
m.dataDir = dataDir;
m.dataUrl = Config.getDataUrl() + (!"/data".equals(Config.getDataUrl()) ? siteinfo.s("ftp_id") : "");

boolean isDevServer = -1 < request.getServerName().indexOf("lms.malgn.co.kr");
siteinfo.put("logo_url", (!"/data".equals(Config.getDataUrl()) ? "" : siteDomain) + m.getUploadUrl(siteinfo.s("logo")));

//IP\ucc28\ub2e8
String userIp = request.getRemoteAddr();
boolean isMalgnOffice = "115.91.52.203".equals(userIp) || "115.91.52.204".equals(userIp) || "125.128.232.145".equals(userIp) || "59.5.222.106".equals(userIp);
if(!"".equals(siteinfo.s("allow_ip_sysop")) && !isMalgnOffice && !Site.checkIP(userIp, siteinfo.s("allow_ip_sysop"))) {
	m.redirect("/");
	return;
}

//\uc5b8\uc5b4
String sysLocale = "".equals(siteinfo.s("locale")) ? "default" : siteinfo.s("locale");
//String sysLocale = "default";
Message _message = new Message(sysLocale);
_message.reloadAll();
m.setMessage(_message);
//p.setMessage(_message);

//\uae30\ubcf8 \ud68c\uc6d0\uc815\ubcf4
int siteId = siteinfo.i("id");
int userId = 0;
String loginId = "";
String userName = "";
String userKind = "";
int userDeptId = 0;
String userGroups = "";
String manageCourses = "";
String userSessionId = "";
boolean isUserMaster = false;
String winTitle = "[\uad00\ub9ac\uc790] " + siteinfo.s("site_nm");
String sysToday = m.time("yyyyMMdd");
String sysNow = m.time("yyyyMMddHHmmss");

//SessionDao mSession = new SessionDao("sysop");
SessionDao mSession = new SessionDao(request, response);
//mSession.setId(session.getId());
mSession.setSiteId(siteId);

//\ub85c\uadf8\uc778 \uc5ec\ubd80\ub97c \uccb4\ud06c
Auth auth = new Auth(request, response);
auth.loginURL = "/sysop/main/login.jsp";
auth.keyName = "MLMSKEY2014" + siteId + "7";
if(0 < siteinfo.i("sysop_session_hour")) auth.setValidTime(siteinfo.i("sysop_session_hour") * 60);
if(auth.isValid()) {
	userId = m.parseInt(auth.getString("ID"));
	loginId = auth.getString("LOGINID");
	userName = auth.getString("NAME");
	userKind = auth.getString("KIND");
	userDeptId = auth.getInt("DEPT");
	userGroups = auth.getString("GROUPS");
	manageCourses = auth.getString("MANAGE_COURSES");
	userSessionId = userSessionId = auth.getString("SESSIONID");
	isUserMaster = "Y".equals(auth.getString("IS_USER_MASTER"));
	if("SYSLOGIN".equals(userSessionId)) siteinfo.put("dup_sysop_yn", "Y");

	mSession.setId(userSessionId);
	mSession.setUserId(userId);

} else {
	if(request.getRequestURI().indexOf("/main/login.jsp") == -1
		&& request.getRequestURI().indexOf("/vod/upload.jsp") == -1
		&& request.getRequestURI().indexOf("/main/slogin.jsp") == -1
		&& request.getRequestURI().indexOf("/site/site_template.jsp") == -1
		&& request.getRequestURI().indexOf("/site/site_maildir.jsp") == -1
		&& (request.getRequestURI().indexOf("/user/sleep_insert.jsp") == -1 || !"log".equals(m.rs("after")))
	) {
		m.jsReplace(auth.loginURL, "top");
		return;
	}
}

MenuDao Menu = new MenuDao(p, siteId, "default");
SiteMenuDao SiteMenu = new SiteMenuDao();

boolean superBlock = "S".equals(userKind);
boolean adminBlock = "S".equals(userKind) || "A".equals(userKind);
boolean courseManagerBlock = "C".equals(userKind);
boolean deptManagerBlock = "D".equals(userKind);

//boolean isAuthCrm = superBlock || (-1 < siteinfo.s("auth_crm").indexOf("|" + userKind + "|"));
boolean isAuthCrm = superBlock || Menu.accessible(-999, userId, userKind, false);

//\ub85c\uadf8\uc544\uc6c3-\uacfc\uc815\uc6b4\uc601\uc790
if(courseManagerBlock && "".equals(manageCourses) && request.getRequestURI().indexOf("/main/logout.jsp") == -1) {
	m.jsAlert("\ub2f4\ub2f9\ud55c \uacfc\uc815\uc774 \uc5c6\uc2b5\ub2c8\ub2e4.\\n \uad00\ub9ac\uc790\uc5d0\uac8c \ubb38\uc758\ud558\uc138\uc694.");
	m.jsReplace("/sysop/main/logout.jsp", "top");
	return;
}

//\ub9e4\ub274\uc5bc
ManualDao Manual = new ManualDao();
int ManualId = Menu.getOneInt("SELECT manual_id FROM " + Menu.table + " WHERE link = '" + m.replace(request.getRequestURI(), "/sysop", "..") + "'");
if(0 < ManualId) {
	int ManualStatus = Manual.getOneInt("SELECT status FROM " + Manual.table + " WHERE id = " + ManualId);
	if(0 < ManualStatus) p.setVar("SYS_MENU_MANUAL_ID", ManualId);
}

p.setVar("WEB_URL", webUrl);
p.setVar("FRONT_URL", siteDomain);
p.setVar("SYS_TITLE", winTitle);
p.setVar("SYS_USERKIND", userKind);
p.setVar("SITE_INFO", siteinfo);
//p.setVar("SITE_CONFIG", siteconfig);
p.setVar("IS_AUTH_CRM", isAuthCrm);
p.setVar("IS_DEV_SERVER", isDevServer);
p.setVar("SYS_LOCALE", sysLocale);
p.setVar("SYS_TODAY", sysToday);
p.setVar("SYS_NOW", sysNow);
p.setVar("SYS_COMMON_CDN", !isDevServer ? "//cdn.malgnlms.com" : "");

p.setVar("user_master_block", isUserMaster || isMalgnOffice);
p.setVar("malgn_office_block", isMalgnOffice);
p.setVar("super_block", superBlock);
p.setVar("admin_block", adminBlock);
p.setVar("course_manager_block", courseManagerBlock);
p.setVar("dept_manager_block", deptManagerBlock);

UserSessionDao UserSession = new UserSessionDao();
UserSession.setSiteId(siteId);
UserSession.setType("sysop");
if(userId != 0 && "N".equals(siteinfo.s("dup_sysop_yn")) && ("".equals(userSessionId) || userSessionId == null || !UserSession.isValid(userSessionId, userId))) {
	if(request.getRequestURI().indexOf("/sysop/main/logout.jsp") == -1) {
		m.jsAlert("\uc138\uc158\uc774 \ub9cc\ub8cc\ub418\uc5c8\uac70\ub098 \uc911\ubcf5 \ub85c\uadf8\uc778\uc774 \ub418\uc5b4 \uc790\ub3d9\uc73c\ub85c \ub85c\uadf8\uc544\uc6c3 \ub429\ub2c8\ub2e4.");
		m.jsReplace("/sysop/main/logout.jsp?mode=session", "top");
		return;
	}
}


      

//\ud3fc\uc785\ub825
int lid = m.ri("lid");
String allow = m.rs("allow", "image,media,file");

//\uac1d\uccb4
FileDao file = new FileDao();
LessonDao lesson = new LessonDao();

//\uc0ad\uc81c
if("del".equals(m.rs("mode")) && m.ri("id") > 0) {

	DataSet info = file.get(m.ri("id"));
	if(!info.next()) { m.jsAlert("\ud574\ub2f9 \uc815\ubcf4\uac00 \uc5c6\uc2b5\ub2c8\ub2e4."); return; }

	if(file.delete(info.i("id"))) {
		if(!"".equals(info.s("filename"))) m.delFileRoot(m.getUploadPath(info.s("filename")));
	}
	m.jsReplace("attach_lesson.jsp?" + m.qs("id, mode"));
	return;
}

//\uc218\uc815
if("mod".equals(m.rs("mode")) && m.ri("id") > 0) {

	DataSet info = file.get(m.ri("id"));
	if(!info.next()) { m.jsAlert("\ud574\ub2f9 \uc815\ubcf4\uac00 \uc5c6\uc2b5\ub2c8\ub2e4."); return; }

	file.item("file_nm", f.get("file_nm"));

	if(!file.update("id = " + m.ri("id") + " AND site_id = " + siteId)) { m.jsAlert("\uc218\uc815\ud558\ub294 \uc911 \uc624\ub958\uac00 \ubc1c\uc0dd\ud588\uc2b5\ub2c8\ub2e4."); }
	else { m.jsAlert("\uc218\uc815\ud558\uc600\uc2b5\ub2c8\ub2e4."); }

	return;
}

/*
//\ub300\ud45c\uc774\ubbf8\uc9c0\ubcc0\uacbd
if("mod".equals(m.rs("mode")) && m.ri("id") > 0) {
	DataSet info = file.get(m.ri("id"));
	if(!info.next()) {
		m.jsError("\ud574\ub2f9 \uc815\ubcf4\uac00 \uc5c6\uc2b5\ub2c8\ub2e4.");
		return;
	}
	if(-1 != file.execute("UPDATE " + file.table + " SET main_yn = 'N' WHERE module = 'lesson' AND module_id = '" + lid + "'")) {
		file.item("main_yn", "Y");
		if(!file.update()) { }
	}
	m.jsReplace("attach.jsp?" + m.qs("id, mode"));
	return;
}

//\uac8c\uc2dc\ubb3c\uc5d0 \ud30c\uc77c\uc885\ub958 \uc5c5\ub370\uc774\ud2b8
if(module.equals("post") && moduleId > 0) post.updateFileCount(moduleId);

//\ub300\ud45c\uc774\ubbf8\uc9c0\uc874\uc7ac\uac80\uc0ac
boolean exists = file.findCount("module = '" + module + "' AND module_id = '" + moduleId + "' AND main_yn = 'Y'") == 1;
*/

//\ud3fc\uccb4\ud06c
f.addElement("file_nm", null, "hname:'\ub178\ucd9c\ud30c\uc77c\uba85'");

//\ubaa9\ub85d
DataSet list = file.query(
	"SELECT a.* FROM " + file.table + " a"
	+ " WHERE a.module = 'lesson' AND a.module_id = '" + lid + "'"
	+ " ORDER BY a.id ASC"
);

//\ud3ec\uba67\ud305
int no = 0;
while(list.next()) {
	boolean isImage = list.s("filename").toLowerCase().matches("^(.+)\\.(jpg|jpeg|gif|png)$");
	/*
	if(!exists && isImage) { //\ub300\ud45c\uc774\ubbf8\uc9c0\uac00 \uc5c6\ub294\uacbd\uc6b0 \uccab \ubc88\uc9f8 \uc774\ubbf8\uc9c0\ub97c \uc790\ub3d9\uc9c0\uc815
		file.execute("UPDATE " + file.table + " SET main_yn = 'Y' WHERE id = " + list.i("id"));
		list.put("main_yn", "Y");
		exists = true;
	}
	*/
	list.put("__idx", ++no);
	list.put("reg_date_conv", m.time("yyyy.MM.dd HH:mm", list.s("reg_date")));
	list.put("checked", "Y".equals(list.s("main_yn")) ? "checked" : "");
	list.put("ext", file.getFileIcon(list.s("filename")));
	list.put("ek", m.encrypt(list.s("id")));
	list.put("filename_conv", m.urlencode(Base64Coder.encode(list.s("filename"))));
	list.put("fileurl", m.getUploadUrl(list.s("filename")));
	//if("manual".equals(module)) list.put("fileurl", "http://" + Site.getCenterWebUrl() + m.getUploadUrl(list.s("filename")));
	list.put("is_img", isImage);
}

//\ud30c\uc77c\ud0c0\uc785
Hashtable<String, String> types = new Hashtable<String, String>();
types.put("image", "false"); types.put("media", "false"); types.put("file", "false");
String[] allows = !"".equals(allow) ? allow.split("\\,") : null;
if(null != allows) {
	for(int i=0; i<allows.length; i++) if(types.containsKey(allows[i])) types.put(allows[i], "true");
}

//\ucd9c\ub825
p.setLayout("blank");
p.setBody("content.attach_lesson");
p.setVar("form_script",  f.getScript());
p.setVar("query", m.qs());
p.setVar("list_query", m.qs("id"));
p.setVar("del_query", m.qs("id, mode"));
p.setVar("mod_query", m.qs("id, mode"));

p.setLoop("list", list);
p.setVar("use_img", (String)types.get("image"));
p.setVar("use_movie", (String)types.get("media"));
p.setVar("use_file", (String)types.get("file"));
p.setVar("use_desc", 1 != m.ri("nodesc"));
p.display();


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
    depend = new com.caucho.vfs.Depend(appDir.lookup("sysop/content/attach_lesson.jsp"), 6716407992072026537L, false);
    com.caucho.jsp.JavaPage.addDepend(_caucho_depends, depend);
    depend = new com.caucho.vfs.Depend(appDir.lookup("sysop/init.jsp"), -7592216907510913785L, false);
    com.caucho.jsp.JavaPage.addDepend(_caucho_depends, depend);
  }
}