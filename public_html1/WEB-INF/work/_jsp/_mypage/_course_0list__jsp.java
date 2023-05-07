/*
 * JSP generated by Resin-3.1.15 (built Mon, 13 Oct 2014 06:45:33 PDT)
 */

package _jsp._mypage;
import javax.servlet.*;
import javax.servlet.jsp.*;
import javax.servlet.http.*;
import java.util.*;
import java.io.*;
import dao.*;
import malgnsoft.db.*;
import malgnsoft.util.*;

public class _course_0list__jsp extends com.caucho.jsp.JavaPage
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
      

//request.setCharacterEncoding("utf-8");

String docRoot = Config.getDocRoot();
String jndi = Config.getJndi();

Malgn m = new Malgn(request, response, out);

Form f = new Form("form1");
try { f.setRequest(request); }
catch(Exception ex) { out.print("Overflow file size. - " + ex.getMessage()); return; }

SiteDao Site = new SiteDao(); //Site.clear();
DataSet siteinfo = Site.getSiteInfo(request.getServerName());
SiteConfigDao SiteConfig = new SiteConfigDao(siteinfo.i("id"));
if(1 != siteinfo.i("status") || "".equals(siteinfo.s("doc_root"))) {
	m.jsReplace("about:blank", "top"); 
	return;
}
//Hashtable<String, String> siteconfig = SiteConfig.getSiteConfig(siteinfo.s("id"));

String webUrl = request.getScheme() + "://" + request.getServerName();
int port = request.getServerPort();
if(port != 80) webUrl += ":" + port;
String dataDir = siteinfo.s("doc_root") + "/data";
String tplRoot = siteinfo.s("doc_root") + "/html";
f.dataDir = dataDir;
m.dataDir = dataDir;
//m.dataUrl = "https://cdn.malgnlms.com/cdndata/" + siteinfo.s("ftp_id");
m.dataUrl = Config.getDataUrl() + (!"/data".equals(Config.getDataUrl()) ? siteinfo.s("ftp_id") : "");

boolean isDevServer = -1 < request.getServerName().indexOf("lms.malgn.co.kr");
if(!"".equals(siteinfo.s("logo"))) siteinfo.put("logo_url", m.getUploadUrl(siteinfo.s("logo")));
else siteinfo.put("logo_url", "/common/images/default/malgn_logo.jpg");

//IP\ucc28\ub2e8
String userIp = request.getRemoteAddr();
if(!"".equals(siteinfo.s("allow_ip_user")) && !Site.checkIP(userIp, siteinfo.s("allow_ip_user"))) {
	m.redirect("/main/guide.jsp");
	return;
}

Page p = new Page(tplRoot);
p.setRequest(request);
p.setPageContext(pageContext);
p.setWriter(out);
p.setBaseRoot("/home/lms/public_html/html");

//\uc5b8\uc5b4
String sysLocale = "".equals(siteinfo.s("locale")) ? "default" : siteinfo.s("locale");
Message _message = new Message(sysLocale);
_message.reloadAll();
m.setMessage(_message);
//p.setMessage(_message);

int siteId = siteinfo.i("id");
int userId = 0;
String loginId = "";
String loginMethod = "";
String userName = "";
String userEmail = "";
String userKind = "";
int userDeptId = 0;
String userGroups = "";
int userGroupDisc = 0;
//String aloginYn = "";
String userSessionId = "";
boolean userB2BBlock = false;
String userB2BName = "";
String userB2BFile = "";
String sysToday = m.time("yyyyMMdd");
String sysNow = m.time("yyyyMMddHHmmss");

//SessionDao mSession = new SessionDao("user");
SessionDao mSession = new SessionDao(request, response);
//mSession.setId(session.getId());
mSession.setSiteId(siteId);

Auth auth = new Auth(request, response);
auth.loginURL = "/member/login.jsp";
auth.keyName = "MLMS14" + siteId + "7";
if(0 < siteinfo.i("session_hour")) auth.setValidTime(siteinfo.i("session_hour") * 3600);
if(auth.isValid()) {
	userId = auth.getInt("ID");
	loginId = auth.getString("LOGINID");
	loginMethod = auth.getString("LOGINMETHOD");
	userName = auth.getString("NAME");
	userEmail = auth.getString("EMAIL");
	userKind = auth.getString("KIND");
	userDeptId = auth.getInt("DEPT");
	userGroups = auth.getString("GROUPS");
	userGroupDisc = !"null".equals(auth.getString("GROUPS_DISC")) ? m.parseInt(auth.getString("GROUPS_DISC")) : 0;
	//aloginYn = auth.getString("ALOGIN_YN");
	userSessionId = auth.getString("SESSIONID");
	userB2BName = auth.getString("B2BNAME");
	userB2BFile = auth.getString("B2BFILE");
	if("SYSLOGIN".equals(userSessionId)) siteinfo.put("duplication_yn", "Y");

	mSession.setId(userSessionId);
	mSession.setUserId(userId);

	if(userGroups != null) {
		if(-1 < userGroups.indexOf(",")) for(String userGroupId : m.split(",", userGroups)) p.setVar("SYS_USERGROUP_" + userGroupId, true);
		else p.setVar("SYS_USERGROUP_" + userGroups, true);
	}

	p.setVar("login_block", true);
} else {
	p.setVar("login_block", false);

	if(siteinfo.b("close_yn")) {		
		boolean isNeedLogin = true;
		String[] exceptPages = m.split("|", siteinfo.s("close_except"));
		
		if(-1 < request.getRequestURI().indexOf("/member/login.jsp")
			|| -1 < request.getRequestURI().indexOf("/member/find.jsp")
			|| -1 < request.getRequestURI().indexOf("/member/alogin.jsp")
			|| -1 < request.getRequestURI().indexOf("/member/slogin.jsp")
			|| -1 < request.getRequestURI().indexOf("/member/slogin_input.jsp")
			|| -1 < request.getRequestURI().indexOf("/member/sysop_slogin.jsp")
			|| -1 < request.getRequestURI().indexOf("/main/site_cache.jsp")
			|| -1 < request.getRequestURI().indexOf("/mobile/login.jsp")
			|| -1 < request.getRequestURI().indexOf("/mypage/certificate.jsp")
			|| -1 < request.getRequestURI().indexOf("/mypage/certificate_course.jsp")
			|| -1 < request.getRequestURI().indexOf("/kollus/check_api.jsp")
			|| -1 < request.getRequestURI().indexOf("/common/")
		) {
			isNeedLogin = false;
		} else if(!"".equals(siteinfo.s("close_except"))) {
			for(int i = 0; i < exceptPages.length; i++) {
				if(-1 < request.getRequestURI().indexOf(exceptPages[i])) { isNeedLogin = false; continue; }
			}
		}

		if(isNeedLogin) {
			m.redirect(!m.isMobile() ? auth.loginURL : "/mobile/login.jsp");
			return;
		}
	}
}

userB2BBlock = !"".equals(userB2BName) && null != userB2BName;
p.setVar("SYS_HTTPHOST", request.getServerName());
p.setVar("SYS_LOGINID", loginId);
p.setVar("SYS_USERNAME", userName);
p.setVar("SYS_USEREMAIL", userEmail);
p.setVar("SYS_USERKIND", userKind);
p.setVar("SYS_DEPTID", userDeptId);
p.setVar("SYS_GROUP_DISC", userGroupDisc);
p.setVar("SYS_B2BBLOCK", userB2BBlock);
p.setVar("SYS_B2BNAME", userB2BName);
p.setVar("SYS_B2BFILE", userB2BFile);
p.setVar("SYS_PAGE_URL", request.getRequestURL() + (!"".equals(m.qs()) ? "?" + m.qs() : ""));
p.setVar("SYS_TITLE", siteinfo.s("site_nm"));
p.setVar("webUrl", webUrl);
p.setVar("SITE_INFO", siteinfo);
//p.setVar("SITE_CONFIG", siteconfig);
p.setVar("CURR_DATE", m.time("yyyyMMdd"));
p.setVar("SYS_EK", m.encrypt(loginId + siteinfo.s("sso_key") + m.time("yyyyMMdd"), "SHA-256"));
p.setVar("IS_MOBILE", m.isMobile());
p.setVar("IS_DEV_SERVER", isDevServer);
p.setVar("SYS_LOCALE", sysLocale);
p.setVar("SYS_TODAY", sysToday);
p.setVar("SYS_NOW", sysNow);
p.setVar("SYS_COMMON_CDN", !isDevServer ? "//cdn.malgnlms.com" : "");

MenuDao Menu = new MenuDao(p, sysLocale);

UserSessionDao UserSession = new UserSessionDao();
UserSession.setSiteId(siteId);
if(userId != 0 && !siteinfo.b("duplication_yn") && !UserSession.isValid(userSessionId, userId)) {
	if(request.getRequestURI().indexOf("/member/logout.jsp") == -1 && request.getRequestURI().indexOf("/mobile/logout.jsp") == -1) {
		m.jsAlert(_message.get("alert.common.logout_session"));
		if(request.getRequestURI().indexOf("/mobile/") != -1) m.jsReplace("/mobile/logout.jsp?mode=session");
		else m.jsReplace("/member/logout.jsp?mode=session");
		return;
	}
}


      

//\ub85c\uadf8\uc778
if(0 == userId) { auth.loginForm(); return; }

//\uc815\ubcf4-\ud68c\uc6d0
UserDao user = new UserDao();
DataSet uinfo = user.find("id = " + userId + " AND status = 1");
if(!uinfo.next()) { m.jsError(_message.get("alert.member.nodata")); return; }
uinfo.put("mobile_conv", !"".equals(uinfo.s("mobile")) ? SimpleAES.decrypt(uinfo.s("mobile")) : "");
//uinfo.put("mobile", !"".equals(uinfo.s("mobile")) ? SimpleAES.decrypt(uinfo.s("mobile")) : "");
String ch = !userB2BBlock ? m.rs("ch", "mypage") : "b2b";


      

//\uac1d\uccb4
CourseUserDao courseUser = new CourseUserDao();
CourseDao course = new CourseDao();
LmCategoryDao category = new LmCategoryDao("course");

String type = m.rs("type");
String today = m.time("yyyyMMdd");

//\uc218\uac15\uc911\uc778 \uacfc\uc815
DataSet list1 = courseUser.query(
	" SELECT a.*, c.year, c.step, c.course_nm, c.course_type, c.onoff_type, c.course_file, c.credit, c.lesson_time, c.renew_max_cnt, c.renew_yn, c.mobile_yn, ct.category_nm "
	+ " FROM " + courseUser.table + " a "
	+ " INNER JOIN " + course.table + " c ON a.course_id = c.id "
	+ " LEFT JOIN " + category.table + " ct ON c.category_id = ct.id AND ct.module = 'course' AND ct.status = 1 "
	+ " WHERE a.user_id = " + userId + " AND a.status IN (0, 1, 3) "
	+ (!"".equals(type) ? " AND c.onoff_type " + ("on".equals(type) ? "=" : "!=") + " 'N' " : "")
	+ " AND a.close_yn = 'N' AND a.end_date >= '" + today + "' "
	+ " ORDER BY a.id DESC "
);
while(list1.next()) {
	list1.put("start_date_conv", m.time(_message.get("format.date.dot"), list1.s("start_date")));
	list1.put("end_date_conv", m.time(_message.get("format.date.dot"), list1.s("end_date")));
	list1.put("study_date_conv", list1.s("start_date_conv") + " - " + list1.s("end_date_conv"));
	list1.put("course_nm_conv", m.cutString(list1.s("course_nm"), 40));
	list1.put("progress_ratio", m.nf(list1.d("progress_ratio"), 1).replace(".0", ""));
	list1.put("total_score", m.nf(list1.d("total_score"), 1).replace(".0", ""));
	list1.put("type_conv", m.getValue(list1.s("course_type"), course.typesMsg));
	list1.put("onoff_type_conv", m.getValue(list1.s("onoff_type"), course.onoffTypesMsg));
	list1.put("credit", list1.i("credit"));
	list1.put("mobile_block", list1.b("mobile_yn"));

	list1.put("renew_block", courseUser.setRenewBlock(list1.getRow()));

	if(!"".equals(list1.s("course_file"))) {
		list1.put("course_file_url", m.getUploadUrl(list1.s("course_file")));
	} else {
		list1.put("course_file_url", "/html/images/common/noimage_course.gif");
	}

	String status = "";
	boolean isOpen = false;
	boolean isCancel = false;
	if(list1.i("status") == 0) {
		status = _message.get("list.course_user.etc.waiting_approve");
		if(0 == list1.i("order_id")) isCancel = true;
	} else if(0 > m.diffDate("D", list1.s("start_date"), today)) {
		status = _message.get("list.course_user.etc.waiting_learning");
		//if(0 == list1.i("order_id")) isCancel = true;
		isCancel = true;
	} else {
		if(list1.b("complete_yn")) {
			status = _message.get("list.course_user.etc.complete_success");
		} else {
			status = _message.get("list.course_user.etc.learning");
			//if(0 == list1.i("order_id") && "A".equals(list1.s("course_type"))) isCancel = true;
			if(0 == list1.i("order_id")) isCancel = true;
		}
		isOpen = true;
	}

	list1.put("status_conv", status);
	list1.put("open_block", isOpen);
	list1.put("cancel_block", isCancel);
}

//\uc885\ub8cc\ub41c \uacfc\uc815
boolean isRestudy = false;
DataSet list2 = courseUser.query(
	"SELECT a.*, c.course_nm, c.course_type, c.restudy_yn, c.restudy_day, c.onoff_type, c.course_file, c.credit, c.lesson_time, c.mobile_yn, c.complete_auto_yn, ct.category_nm "
	+ " FROM " + courseUser.table + " a "
	+ " INNER JOIN " + course.table + " c ON a.course_id = c.id "
	+ " LEFT JOIN " + category.table + " ct ON c.category_id = ct.id AND ct.module = 'course' AND ct.status = 1 "
	+ " WHERE a.user_id = " + userId + " AND a.status IN (1, 3) "
	+ (!"".equals(type) ? " AND c.onoff_type " + ("on".equals(type) ? "=" : "!=") + " 'N' " : "")
	+ " AND (a.end_date < '" + today + "' OR a.close_yn = 'Y') "
	+ " ORDER BY a.end_date DESC, a.id DESC "
);
while(list2.next()) {
	list2.put("start_date_conv", m.time(_message.get("format.date.dot"), list2.s("start_date")));
	list2.put("end_date_conv", m.time(_message.get("format.date.dot"), list2.s("end_date")));
	list2.put("study_date_conv", list2.s("start_date_conv") + " - " + list2.s("end_date_conv"));
	list2.put("course_nm_conv", m.cutString(list2.s("course_nm"), 40));
	list2.put("progress_ratio", m.nf(list2.d("progress_ratio"), 1).replace(".0", ""));
	list2.put("total_score", m.nf(list2.d("total_score"), 1).replace(".0", ""));
	list2.put("type_conv", m.getValue(list2.s("course_type"), course.typesMsg));
	list2.put("onoff_type_conv", m.getValue(list2.s("onoff_type"), course.onoffTypesMsg));
	//list2.put("status_conv", list2.b("complete_yn") ? "\uc218\ub8cc" : "\ubbf8\uc218\ub8cc");
	list2.put("status_conv",
		!"".equals(list2.s("complete_date")) ? ( "Y".equals(list2.s("complete_yn")) ? _message.get("list.course_user.etc.complete_success") : _message.get("list.course_user.etc.complete_fail") )
		: ( !"Y".equals(list2.s("complete_auto_yn"))
			? _message.get("list.course_user.etc.inprogress")
			: _message.get("list.course_user.etc.complete_fail")
		)
	);

	list2.put("mobile_block", list2.b("mobile_yn"));

	if(!"".equals(list2.s("course_file"))) {
		list2.put("course_file_url", m.getUploadUrl(list2.s("course_file")));
	} else {
		list2.put("course_file_url", "/html/images/common/noimage_course.gif");
	}

	list2.put("restudy_block", false);
	if(list2.b("restudy_yn")) {
		String edate = m.addDate("D", list2.i("restudy_day"), list2.s("end_date"), "yyyyMMdd");
		if(list2.b("restudy_yn") && 0 <= m.diffDate("D", today, edate)) {
			list2.put("restudy_block", true);
			isRestudy = true;
		}
	}
}

//\ucd9c\ub825
p.setLayout(ch);
p.setBody("mypage.course_list");
p.setVar("p_title", "\uc218\uac15\ud604\ud669");

p.setLoop("list1", list1);
p.setLoop("list2", list2);

p.setVar("user", uinfo);
p.setVar("restudy_block", isRestudy);

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
    depend = new com.caucho.vfs.Depend(appDir.lookup("mypage/course_list.jsp"), 1721652360183827375L, false);
    com.caucho.jsp.JavaPage.addDepend(_caucho_depends, depend);
    depend = new com.caucho.vfs.Depend(appDir.lookup("mypage/init.jsp"), 6109977202324295072L, false);
    com.caucho.jsp.JavaPage.addDepend(_caucho_depends, depend);
    depend = new com.caucho.vfs.Depend(appDir.lookup("init.jsp"), -919485065315744250L, false);
    com.caucho.jsp.JavaPage.addDepend(_caucho_depends, depend);
  }
}