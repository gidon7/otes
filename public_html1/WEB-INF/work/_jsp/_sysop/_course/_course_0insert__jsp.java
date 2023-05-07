/*
 * JSP generated by Resin-3.1.15 (built Mon, 13 Oct 2014 06:45:33 PDT)
 */

package _jsp._sysop._course;
import javax.servlet.*;
import javax.servlet.jsp.*;
import javax.servlet.http.*;
import java.util.*;
import java.io.*;
import dao.*;
import malgnsoft.db.*;
import malgnsoft.util.*;

public class _course_0insert__jsp extends com.caucho.jsp.JavaPage
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
if(1 != siteinfo.i("sysop_status") || "".equals(siteinfo.s("doc_root"))
|| (14 == siteinfo.s("close_date").length()
&& 0 < m.diffDate("I", siteinfo.s("close_date"), m.time("yyyyMMddHHmmss")))
) {
	m.jsReplace("/main/guide.jsp", "top"); return; }
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
boolean isMalgnOffice = "106.244.224.183".equals(userIp) || "125.129.123.211".equals(userIp) || "59.5.222.106".equals(userIp) || "3.35.211.181".equals(userIp);
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
String pagreeDate = "";
boolean isUserMaster = false;
String winTitle = "[\uad00\ub9ac\uc790] " + siteinfo.s("site_nm");
String sysToday = m.time("yyyyMMdd");
String sysNow = m.time("yyyyMMddHHmmss");
String surl = request.getRequestURL() + (!"".equals(m.qs()) ? "?" + m.qs() : "");
final int sysExcelCnt = 20000;

SessionDao mSession = new SessionDao(request, response);

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
	pagreeDate = auth.getString("PAGREE_DATE");
	isUserMaster = "Y".equals(auth.getString("IS_USER_MASTER"));

	//2\ucc28\uc778\uc99d\uccb4\ud06c
	/*if("Y".equals(siteinfo.s("auth2_yn"))
		&&!"Y".equals(auth.getString("AUTH2_YN"))
		&& !"".equals(siteinfo.s("auth2_type"))
		&& !"malgn".equals(loginId)
		&& request.getRequestURI().indexOf("/main/auth2.jsp") == -1
		&& request.getRequestURI().indexOf("/main/otpkey_register.jsp") == -1
		&& request.getRequestURI().indexOf("/sysop/main/logout.jsp") == -1
	) {
		m.jsReplace("/sysop/main/auth2.jsp?returl=" + m.rs("returl", "/sysop/index.jsp"), "top");
		return;
	}*/

	mSession.put("id", userSessionId);
	mSession.save();

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
p.setVar("super_malgn_block", isMalgnOffice && siteinfo.i("super_id") == userId);
p.setVar("malgn_office_block", isMalgnOffice);
p.setVar("super_block", superBlock);
p.setVar("admin_block", adminBlock);
p.setVar("course_manager_block", courseManagerBlock);
p.setVar("dept_manager_block", deptManagerBlock);
p.setVar("SYS_URL", surl);

boolean isBlindUser = !sysToday.equals(pagreeDate);
p.setVar("SYS_BLINDUSER", isBlindUser);

UserSessionDao UserSession = new UserSessionDao();
UserSession.setSiteId(siteId);
UserSession.setType("sysop");
if(userId != 0 && !"SYSLOGIN".equals(userSessionId) && "N".equals(siteinfo.s("dup_sysop_yn")) && ("".equals(userSessionId) || userSessionId == null || !UserSession.isValid(userSessionId, userId))) {
	if(request.getRequestURI().indexOf("/sysop/main/logout.jsp") == -1) {
		m.jsAlert("\uc138\uc158\uc774 \ub9cc\ub8cc\ub418\uc5c8\uac70\ub098 \uc911\ubcf5 \ub85c\uadf8\uc778\uc774 \ub418\uc5b4 \uc790\ub3d9\uc73c\ub85c \ub85c\uadf8\uc544\uc6c3 \ub429\ub2c8\ub2e4.");
		m.jsReplace("/sysop/main/logout.jsp?mode=session", "top");
		return;
	}
}

InfoLogDao _log = new InfoLogDao(siteId); _log.setItems(userId, "B", surl, userIp);
InfoUserDao _logUser = new InfoUserDao(siteId);


      

String ch = "sysop";

p.setVar("auth_course_block", Menu.accessible(33, userId, userKind, false));
p.setVar("auth_management_block", Menu.accessible(75, userId, userKind, false));
p.setVar("auth_complete_block", Menu.accessible(76, userId, userKind, false));
p.setVar("auth_auto_block", Menu.accessible(42, userId, userKind, false));


      

//\uc811\uadfc\uad8c\ud55c
if(!Menu.accessible(33, userId, userKind)) { m.jsError("\uc811\uadfc \uad8c\ud55c\uc774 \uc5c6\uc2b5\ub2c8\ub2e4."); return; }
if(!adminBlock) { m.jsError("\uc811\uadfc \uad8c\ud55c\uc774 \uc5c6\uc2b5\ub2c8\ub2e4."); return; }

//\uac1d\uccb4
CourseDao course = new CourseDao();
LmCategoryDao category = new LmCategoryDao("course");
CourseModuleDao courseModule = new CourseModuleDao();

CourseBookDao courseBook = new CourseBookDao();
CourseTutorDao courseTutor = new CourseTutorDao();
CourseTargetDao courseTarget = new CourseTargetDao();

BookDao book = new BookDao();
UserDao user = new UserDao();
TutorDao tutor = new TutorDao();
GroupDao group = new GroupDao();

MCal mcal = new MCal(); mcal.yearRange = 10;

//\ud3fc\uccb4\ud06c
f.addElement("onoff_type", "N", "hname:'\uacfc\uc815 \uad6c\ubd84', required:'Y'");
f.addElement("category_id", null, "hname:'\uce74\ud14c\uace0\ub9ac', required:'Y'");

f.addElement("course_nm", null, "hname:'\uacfc\uc815\uba85', required:'Y'");
f.addElement("course_type", "A", "hname:'\uacfc\uc815\uad6c\ubd84', required:'Y'");
f.addElement("year", null, "hname:'\ub144\ub3c4', required:'Y'");
f.addElement("step", 1, "hname:'\uae30\uc218', required:'Y', option:'number'");
f.addElement("request_sdate", null, "hname:'\uc218\uac15\uc2e0\uccad\uc2dc\uc791\uc77c'");
f.addElement("request_edate", null, "hname:'\uc218\uac15\uc2e0\uccad\uc885\ub8cc\uc77c'");
f.addElement("study_sdate", null, "hname:'\ud559\uc2b5\uc2dc\uc791\uc77c'");
f.addElement("study_edate", null, "hname:'\ud559\uc2b5\uc885\ub8cc\uc77c'");
f.addElement("lesson_day", 30, "hname:'\ud559\uc2b5\uc77c\uc218', option:'number', min:'1'");
f.addElement("course_file", null, "hname:'\uba54\uc778\uc774\ubbf8\uc9c0', allow:'jpg|jpeg|gif|png'");
f.addElement("list_price", 0, "hname:'\uc815\uac00', option:'number', required:'Y'");
f.addElement("price", 0, "hname:'\uc218\uac15\ub8cc', option:'number', required:'Y'");
f.addElement("taxfree_yn", "N", "hname:'\ubd80\uac00\uc138\uba74\uc138\uc5ec\ubd80'");
f.addElement("disc_group_yn", "Y", "hname:'\uadf8\ub8f9\ud560\uc778\uc801\uc6a9\uc5ec\ubd80'");
f.addElement("memo_yn", "N", "hname:'\uc8fc\ubb38\uba54\ubaa8 \uc0ac\uc6a9\uc5ec\ubd80'");
f.addElement("renew_yn", "N", "hname:'\uc218\uac15\uae30\uac04 \uc5f0\uc7a5\uacb0\uc81c \uc0ac\uc6a9\uc5ec\ubd80'");
f.addElement("renew_max_cnt", 0, "hname:'\ucd5c\ub300 \uc5f0\uc7a5\ud69f\uc218', option:'number', min:'0', max:'999'");
f.addElement("renew_price", 0, "hname:'\uc5f0\uc7a5\ube44\uc6a9', option:'number', min:'0'");
f.addElement("credit", 0, "hname:'\ud559\uc810', required:'Y'");
f.addElement("lesson_time", 1, "hname:'\uc2dc\uc218', min:'0.01', required:'Y'");
f.addElement("mobile_yn", "Y", "hname:'\ubaa8\ubc14\uc77c \uc9c0\uc6d0\uc5ec\ubd80'");
//f.addElement("evaluation_yn", "N", "hname:'\uc218\ub8cc\uae30\uc900 \ub178\ucd9c\uc5ec\ubd80'");
//f.addElement("top_yn", "N", "hname:'\uc0c1\uc2dc \uc0c1\uc704\uace0\uc815'");
f.addElement("recomm_yn", null, "hname:'\ucd94\ucc9c\uacfc\uc815'");

f.addElement("subtitle", null, "hname:'\uacfc\uc815\ubaa9\ub85d \uc18c\uac1c\ubb38\uad6c'");
f.addElement("content1", null, "hname:'\ud14d\uc2a4\ud2b81', allowiframe:'Y'");
f.addElement("content2", null, "hname:'\ud14d\uc2a4\ud2b82', allowiframe:'Y'");
f.addElement("content1_title", "\uacfc\uc815\uc18c\uac1c", "hname:'\ud14d\uc2a4\ud2b81 \ud0c0\uc774\ud2c0'");
f.addElement("content2_title", "\ud559\uc2b5\ubaa9\ud45c", "hname:'\ud14d\uc2a4\ud2b82 \ud0c0\uc774\ud2c0'");

f.addElement("etc1", null, "hname:'\uae30\ud0c01'");
f.addElement("etc2", null, "hname:'\uae30\ud0c02'");
//f.addElement("status", 1, "hname:'\uc0c1\ud0dc', required:'Y'");

//\ub4f1\ub85d
if(m.isPost() && f.validate()) {

	//\uc81c\ud55c
	/*
	if(0 < course.findCount(
			"site_id = " + siteId + " AND subject_id = " + f.getInt("subject_id") + " "
			+ " AND year = " + f.getInt("year") + " AND step = " + f.getInt("step") + ""
			+ " AND status != -1 "
	)) {
		m.jsAlert("\uacfc\uc815\uba85/\ub144\ub3c4/\uae30\uc218\ub97c \uc0ac\uc6a9 \uc911\uc785\ub2c8\ub2e4. \ub2e4\uc2dc \uc785\ub825\ud558\uc138\uc694.");
		return;
	}
	*/

	//\uc81c\ud55c-\uc774\ubbf8\uc9c0URI\ubc0f\uc6a9\ub7c9
	String subtitle = f.get("subtitle");
	String content1 = f.get("content1");
	String content2 = f.get("content2");
	int bytest = subtitle.replace("\r\n", "\n").getBytes("UTF-8").length;
	int bytes1 = content1.replace("\r\n", "\n").getBytes("UTF-8").length;
	int bytes2 = content2.replace("\r\n", "\n").getBytes("UTF-8").length;
	if(-1 < content1.indexOf("<img") && -1 < content1.indexOf("data:image/") && -1 < content1.indexOf("base64")) {
		m.jsAlert("\uc774\ubbf8\uc9c0\ub294 \ucca8\ubd80\ud30c\uc77c \uae30\ub2a5\uc73c\ub85c \uc5c5\ub85c\ub4dc \ud574 \uc8fc\uc138\uc694.");
		return;
	}
	if(500 < bytest) { m.jsAlert("\uacfc\uc815\ubaa9\ub85d \uc18c\uac1c\ubb38\uad6c \ub0b4\uc6a9\uc740 500\ubc14\uc774\ud2b8\ub97c \ucd08\uacfc\ud574 \uc791\uc131\ud558\uc2e4 \uc218 \uc5c6\uc2b5\ub2c8\ub2e4.\\n(\ud604\uc7ac " + bytest + "\ubc14\uc774\ud2b8)"); return; }
	if(60000 < bytes1) { m.jsAlert(f.get("content1_title") + " \ub0b4\uc6a9\uc740 60000\ubc14\uc774\ud2b8\ub97c \ucd08\uacfc\ud574 \uc791\uc131\ud558\uc2e4 \uc218 \uc5c6\uc2b5\ub2c8\ub2e4.\\n(\ud604\uc7ac " + bytes1 + "\ubc14\uc774\ud2b8)"); return; }
	if(60000 < bytes2) { m.jsAlert(f.get("content2_title") + " \ub0b4\uc6a9\uc740 60000\ubc14\uc774\ud2b8\ub97c \ucd08\uacfc\ud574 \uc791\uc131\ud558\uc2e4 \uc218 \uc5c6\uc2b5\ub2c8\ub2e4.\\n(\ud604\uc7ac " + bytes2 + "\ubc14\uc774\ud2b8)"); return; }

	//\uacfc\uc815
	int newId = course.getSequence();
	course.item("id", newId);
	course.item("site_id", siteId);
	course.item("course_nm", f.get("course_nm"));
	course.item("category_id", f.getInt("category_id"));
	course.item("year", f.get("year", m.time("yyyy")));
	course.item("step", f.get("step", "1"));

	course.item("onoff_type", f.get("onoff_type"));
	course.item("course_type", f.get("course_type"));

	if("R".equals(f.get("course_type"))) { //\uc815\uaddc
		course.item("request_sdate", m.time("yyyyMMdd", f.get("request_sdate")));
		course.item("request_edate", m.time("yyyyMMdd", f.get("request_edate")));
		course.item("study_sdate", m.time("yyyyMMdd", f.get("study_sdate")));
		course.item("study_edate", m.time("yyyyMMdd", f.get("study_edate")));
		course.item("renew_price", 0);
		course.item("renew_max_cnt", 0);
		course.item("renew_yn", "N");
	} else if("A".equals(f.get("course_type"))) { //\uc0c1\uc2dc
		course.item("request_sdate", "");
		course.item("request_edate", "");
		course.item("study_sdate", "");
		course.item("study_edate", "");
		course.item("renew_price", f.getInt("renew_price"));
		course.item("renew_max_cnt", f.getInt("renew_max_cnt"));
		course.item("renew_yn", f.get("renew_yn", "N"));
	}
	
	if("P".equals(f.get("onoff_type"))) {
		course.item("study_sdate", "");
		course.item("study_edate", "");
		course.item("renew_price", 0);
		course.item("renew_max_cnt", 0);
		course.item("renew_yn", "N");
	}

	course.item("lesson_day", f.getInt("lesson_day"));
	course.item("lesson_time", f.getDouble("lesson_time"));

	boolean isUpload = false;
	if(null != f.getFileName("course_file")) {
		File f1 = f.saveFile("course_file");
		if(f1 != null) {
			course.item("course_file", f.getFileName("course_file"));
			isUpload = true;
		}
	}

	course.item("list_price", f.getInt("list_price"));
	course.item("price", f.getInt("price"));
	course.item("taxfree_yn", f.get("taxfree_yn", "N"));
	course.item("disc_group_yn", f.get("disc_group_yn", "Y"));
	course.item("memo_yn", f.get("memo_yn", "N"));

	course.item("credit", f.getInt("credit"));
	course.item("mobile_yn", f.get("mobile_yn", "N"));
	course.item("evaluation_yn", "N");
	//course.item("top_yn", f.get("top_yn", "N"));

	course.item("recomm_yn", f.get("recomm_yn", "N"));
	course.item("auto_approve_yn", "Y");
	course.item("sms_yn", "N");
	course.item("target_yn", "N");
	course.item("complete_auto_yn", "N");
	course.item("restudy_yn", "N");
	course.item("restudy_day", 0);

	course.item("limit_lesson_yn", "N");
	course.item("limit_lesson", 0);
	course.item("limit_people_yn", "N");
	course.item("limit_people", 0);
	course.item("limit_ratio_yn", "N");
	course.item("limit_ratio", 0);
	course.item("limit_seek_yn", "N");

	course.item("period_yn", !"N".equals(f.get("onoff_type")) ? "Y" : "N");
	course.item("lesson_order_yn", "N");

	course.item("assign_progress", 100);
	course.item("assign_exam", 0);
	course.item("assign_homework", 0);
	course.item("assign_forum", 0);
	course.item("assign_etc", 0);
	course.item("assign_survey_yn", "N");
	course.item("limit_progress", 60);
	course.item("limit_exam", 0);
	course.item("limit_homework", 0);
	course.item("limit_forum", 0);
	course.item("limit_etc", 0);
	course.item("limit_total_score", 60);
	course.item("class_member", 40); //\uace0\uc815

    course.item("push_survey_yn", "N");

	course.item("sample_lesson_id", 0);
	course.item("before_course_id", 0);

	course.item("subtitle", subtitle);
	course.item("content1_title", f.get("content1_title"));
	course.item("content1", content1);
	course.item("content2_title", f.get("content2_title"));
	course.item("content2", content2);

	course.item("exam_yn", "Y");
	course.item("homework_yn", "Y");
	course.item("forum_yn", "Y");
	course.item("survey_yn", "Y");
	course.item("review_yn", "Y");
	course.item("cert_course_yn", "Y");
	course.item("cert_complete_yn", "Y");

	course.item("etc1", f.get("etc1"));
	course.item("etc2", f.get("etc2"));

	course.item("display_yn", "N");
	course.item("sale_yn", "N");
	course.item("reg_date", m.time("yyyyMMddHHmmss"));
	course.item("status", 1);

	if(!course.insert()) { m.jsAlert("\ub4f1\ub85d\ud558\ub294 \uc911 \uc624\ub958\uac00 \ubc1c\uc0dd\ud588\uc2b5\ub2c8\ub2e4."); return; }


	//\uacfc\uc815\uac8c\uc2dc\ud310
	ClBoardDao board = new ClBoardDao(siteId);
	if(!board.insertBoard(newId)) { }

	//\ud30c\uc77c\ub9ac\uc0ac\uc774\uc9d5
	if(isUpload) {
		try {
			String imgPath = m.getUploadPath(f.getFileName("course_file"));
			String cmd = "convert -resize 1000x " + imgPath + " " + imgPath;
			Runtime.getRuntime().exec(cmd);
		}
		catch(RuntimeException re) { m.errorLog("RuntimeException : " + re.getMessage(), re); }
		catch(Exception e) { m.errorLog("Exception : " + e.getMessage(), e); }
	}

	m.jsAlert(
		"\uc131\uacf5\uc801\uc73c\ub85c \ub4f1\ub85d\ud588\uc2b5\ub2c8\ub2e4. "
		+ "\\n\ud604\uc7ac [\ub178\ucd9c \uc228\uae40 \ubc0f \ud310\ub9e4 \uc911\uc9c0] \uc0c1\ud0dc\uc785\ub2c8\ub2e4. "
		+ "\\n\uc6b4\uc601\uc815\ubcf4/\uac15\uc758\ubaa9\ucc28/\ud3c9\uac00\uc815\ubcf4\ub97c \ubc18\ub4dc\uc2dc \ud655\uc778\ud558\uc2dc\uace0 \uc815\uc0c1\uc0c1\ud0dc\ub85c \ubcc0\uacbd\ud558\uc2dc\uae38 \ubc14\ub78d\ub2c8\ub2e4."
	);

	m.jsReplace("course_modify.jsp?id=" + newId, "parent");
	return;
}

//\ucd9c\ub825
p.setBody("course.course_insert");
p.setVar("query", m.qs());
p.setVar("list_query", m.qs("id"));
p.setVar("form_script", f.getScript());

p.setLoop("status_list", m.arr2loop(course.statusList));
p.setLoop("taxfree_yn", m.arr2loop(course.taxfreeYn));

p.setLoop("years", mcal.getYears());
p.setLoop("types", m.arr2loop(course.types));
p.setLoop("onoff_types", m.arr2loop(course.onoffTypes));
p.setVar("year", m.time("yyyy"));

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
    depend = new com.caucho.vfs.Depend(appDir.lookup("sysop/course/course_insert.jsp"), 6949859554833268649L, false);
    com.caucho.jsp.JavaPage.addDepend(_caucho_depends, depend);
    depend = new com.caucho.vfs.Depend(appDir.lookup("sysop/course/init.jsp"), 4867222188672443604L, false);
    com.caucho.jsp.JavaPage.addDepend(_caucho_depends, depend);
    depend = new com.caucho.vfs.Depend(appDir.lookup("sysop/init.jsp"), 1160630992102546501L, false);
    com.caucho.jsp.JavaPage.addDepend(_caucho_depends, depend);
  }
}