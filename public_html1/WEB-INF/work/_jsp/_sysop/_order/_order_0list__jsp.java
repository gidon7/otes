/*
 * JSP generated by Resin-3.1.15 (built Mon, 13 Oct 2014 06:45:33 PDT)
 */

package _jsp._sysop._order;
import javax.servlet.*;
import javax.servlet.jsp.*;
import javax.servlet.http.*;
import java.util.*;
import java.io.*;
import dao.*;
import malgnsoft.db.*;
import malgnsoft.util.*;

public class _order_0list__jsp extends com.caucho.jsp.JavaPage
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


      

String ch = m.rs("ch", "sysop");

p.setVar("pop_block", "pop".equals(ch) || "poplayer".equals(ch));

      

//\uc811\uadfc\uad8c\ud55c
if(!Menu.accessible(60, userId, userKind)) { m.jsError("\uc811\uadfc \uad8c\ud55c\uc774 \uc5c6\uc2b5\ub2c8\ub2e4."); return; }

//\uac1d\uccb4
OrderDao order = new OrderDao();
OrderItemDao orderItem = new OrderItemDao();
PaymentDao payment = new PaymentDao();
UserDao user = new UserDao(isBlindUser);
ActionLogDao actionLog = new ActionLogDao();//\uc218\uc815\uc911

//\uc785\uae08\ud655\uc778
if("deposit".equals(m.rs("mode"))) {
	//\uae30\ubcf8\ud0a4
	String oid = m.rs("oid");
	if("".equals(oid)) { m.jsAlert("\uae30\ubcf8\ud0a4\ub294 \ubc18\ub4dc\uc2dc \uc9c0\uc815\ud574\uc57c \ud569\ub2c8\ub2e4."); return; }

	if(!order.confirmDeposit(oid, siteinfo)) { m.jsAlert("\uc218\uc815\ud558\ub294 \uc911 \uc624\ub958\uac00 \ubc1c\uc0dd\ud588\uc2b5\ub2c8\ub2e4."); return; }

	//\uc774\ub3d9
	m.jsAlert("\uc785\uae08\ud655\uc778\uc774 \uc644\ub8cc\ub410\uc2b5\ub2c8\ub2e4.");
	m.jsReplace("order_list.jsp?" + m.qs("mode,oid"), "parent");
	return;
}

//\uc218\uc815\uc911
//\uc0ad\uc81c
if("del".equals(m.rs("mode"))) {

	int id = m.ri("id");
	if(id == 0) { m.jsAlert("\uae30\ubcf8\ud0a4\ub294 \ubc18\ub4dc\uc2dc \uc9c0\uc815\ud574\uc57c \ud569\ub2c8\ub2e4."); return; }

	//\uc815\ubcf4-before
	DataSet binfo = order.query(
			" SELECT a.*, oi.status oi_status "
			+ " FROM " + order.table + " a "
			+ " INNER JOIN " + orderItem.table + " oi ON oi.order_id = a.id AND oi.site_id = " + siteId + ""
			+ " WHERE a.id = ? AND a.site_id = " + siteId
			, new Object[] {id}
	);
	if(!binfo.next()) { m.jsError("\ud574\ub2f9 \uc815\ubcf4\uac00 \uc5c6\uc2b5\ub2c8\ub2e4."); return; }


	order.item("status", -1);
	orderItem.item("status", -1);
	if(!order.update("id = " + id + " AND site_id = " + siteId + "") || !orderItem.update("order_id = " + id + " AND site_id = " + siteId + "")) {
		m.jsAlert("\uc0ad\uc81c\ud558\ub294 \uc911 \uc624\ub958\uac00 \ubc1c\uc0dd\ud588\uc2b5\ub2c8\ub2e4."); return;
	}

	//\uc815\ubcf4-after
	DataSet ainfo = order.query(
			" SELECT a.*, oi.status oi_status "
			+ " FROM " + order.table + " a "
			+ " INNER JOIN " + orderItem.table + " oi ON oi.order_id = a.id AND oi.site_id = " + siteId + ""
			+ " WHERE a.id = ? AND a.site_id = " + siteId
			, new Object[] {id}
	);
	if(!ainfo.next()) { m.jsError("\ud574\ub2f9 \uc815\ubcf4\uac00 \uc5c6\uc2b5\ub2c8\ub2e4."); return; }

	//\uc561\uc158\ub85c\uadf8
	actionLog.item("site_id", siteId);
	actionLog.item("user_id", userId);
	actionLog.item("module", "tradepay");
	actionLog.item("module_id", binfo.i("user_id"));
	actionLog.item("action_type", "D");
	actionLog.item("action_desc", "\uc8fc\ubb38\ub0b4\uc5ed\uc0ad\uc81c");
	actionLog.item("before_info", binfo.serialize());
	actionLog.item("after_info", ainfo.serialize());
	actionLog.item("important_yn", "Y");
	actionLog.item("reg_date", m.time("yyyyMMddHHmmss"));
	actionLog.item("status", 1);
	if(!actionLog.insert()) {
		m.jsError("\uc0ad\uc81c\ud558\ub294 \uc911 \uc624\ub958\uac00 \ubc1c\uc0dd\ud588\uc2b5\ub2c8\ub2e4. [E2]");
		return;
	}

	m.jsAlert("\uc0ad\uc81c\ub418\uc5c8\uc2b5\ub2c8\ub2e4.");
	m.jsReplace("order_list.jsp?" + m.qs("mode,id"), "parent");
	return;
}

//\ud3fc\uccb4\ud06c
f.addElement("s_order_sdate", null, null);
f.addElement("s_order_edate", null, null);
f.addElement("s_pay_sdate", null, null);
f.addElement("s_pay_edate", null, null);
f.addElement("s_method", null, null);
f.addElement("s_status", null, null);
f.addElement("s_field", null, null);
f.addElement("s_keyword", null, null);

//\ubaa9\ub85d
ListManager lm = new ListManager();
//lm.d(out);
lm.setRequest(request);
lm.setListNum("excel".equals(f.get("mode")) ? 20000 : 20);
lm.setTable(
	order.table + " a "
	+ " INNER JOIN " + user.table + " u ON a.user_id = u.id AND u.site_id = " + siteId + " "
);
lm.setFields(
	"a.*, u.login_id, u.user_nm, u.email_yn, u.sms_yn, u.status user_status"
	+ ", (SELECT SUM(quantity) FROM " + orderItem.table + " WHERE order_id = a.id AND status != -1) item_quantity "
	+ ", (SELECT COUNT(*) FROM " + orderItem.table + " WHERE order_id = a.id AND status != -1) item_count "
);
lm.addWhere("a.status != -1");
lm.addWhere("a.site_id = " + siteId + "");
lm.addSearch("a.order_date", m.time("yyyyMMdd", f.get("s_order_sdate")), ">=");
lm.addSearch("a.order_date", m.time("yyyyMMdd", f.get("s_order_edate")), "<=");
lm.addSearch("a.pay_date", m.time("yyyyMMdd000000", f.get("s_pay_sdate")), ">=");
lm.addSearch("a.pay_date", m.time("yyyyMMdd235959", f.get("s_pay_edate")), "<=");
lm.addSearch("a.status", f.get("s_status"));
lm.addSearch("a.paymethod", f.get("s_method"));
if(f.getInt("s_status") != -99) lm.addWhere("a.status != -99");
if(!"".equals(f.get("s_field"))) lm.addSearch(f.get("s_field"), f.get("s_keyword"), "LIKE");
else if("".equals(f.get("s_field")) && !"".equals(f.get("s_keyword"))) {
	lm.addSearch("a.id, a.order_nm, a.ord_nm, u.login_id", f.get("s_keyword"), "LIKE");
}
lm.setOrderBy(!"".equals(f.get("ord")) ? f.get("ord") : "a.id DESC");

//\ud3ec\uba67\ud305
DataSet list = lm.getDataSet();
while(list.next()) {
	list.put("user_out_block", -1 == list.i("user_status"));
	list.put("login_id_conv", !list.b("user_out_block") ? list.s("login_id") : "[\ud0c8\ud1f4]");
	list.put("email_yn_conv", m.getItem(list.s("email_yn"), user.receiveYn));
	list.put("sms_yn_conv", m.getItem(list.s("sms_yn"), user.receiveYn));

	list.put("method_conv", m.getItem(list.s("paymethod"), order.methods));
	list.put("order_date_conv", m.time("yyyy.MM.dd", list.s("order_date")));
	list.put("pay_price_conv", m.nf(list.i("pay_price")));
	list.put("status_conv", m.getItem(list.s("status"), order.statusList));
	list.put("payment_block", !"99".equals(list.s("paymethod")));

	list.put("pay_date_conv", !"".equals(list.s("pay_date")) ? m.time("yyyy.MM.dd", list.s("pay_date")) : "-");
	list.put("refund_date_conv", m.time("yyyy.MM.dd", list.s("refund_date")));
	list.put("reg_date_conv", m.time("yyyy.MM.dd", list.s("reg_date")));

	list.put("deposit_block", "90".equals(list.s("paymethod")) && 2 == list.i("status"));
	list.put("important_block", list.b("deposit_block"));
	list.put("ROW_CLASS", list.b("important_block") ? "important" : list.s("ROW_CLASS"));
	user.maskInfo(list);
}

//\uae30\ub85d-\uac1c\uc778\uc815\ubcf4\uc870\ud68c
if("".equals(m.rs("mode")) && list.size() > 0 && !isBlindUser) _log.add("L", Menu.menuNm, list.size(), "\uc774\ub7ec\ub2dd \uc6b4\uc601", list);

//\uc5d1\uc140
if("excel".equals(m.rs("mode"))) {
	if(list.size() > 0 && !isBlindUser) _log.add("E", Menu.menuNm, list.size(), "\uc774\ub7ec\ub2dd \uc6b4\uc601", list);

	ExcelWriter ex = new ExcelWriter(response, "\uc8fc\ubb38\uad00\ub9ac(" + m.time("yyyy-MM-dd") + ").xls");
	ex.setData(list, new String[] { "__ord=>No", "id=>\uc8fc\ubb38\uc544\uc774\ub514", "order_date_conv=>\uc8fc\ubb38\uc77c", "user_nm=>\ud68c\uc6d0\uba85", "login_id_conv=>\ud68c\uc6d0\uc544\uc774\ub514", "order_nm=>\uc8fc\ubb38\uba85", "item_count=>\uc8fc\ubb38\ud56d\ubaa9\uc218", "item_quantity=>\uc804\uccb4\uc8fc\ubb38\uc218\ub7c9", "price=>\uc8fc\ubb38\ucd1d\uc561", "coupon_price=>\ucfe0\ud3f0\ud560\uc778\uae08\uc561", "disc_group_price=>\ud68c\uc6d0\ud560\uc778\uae08\uc561", "pay_price=>\uc2e4\uacb0\uc81c\uae08\uc561", "method_conv=>\uacb0\uc81c\ubc29\ubc95", "pay_date_conv=>\uc2e4\uacb0\uc81c\uc77c", "refund_price=>\ud658\ubd88\uae08\uc561(\ucd1d\uacc4)", "refund_date_conv=>\ud658\ubd88\uc77c(\ucd5c\uc885)", "refund_note=>\ud658\ubd88\ube44\uace0(\ucd5c\uc885)", "ord_nm=>\uc8fc\ubb38\uc790\uba85", "ord_reci=>\uc218\ub839\uc790\uba85", "ord_zipcode=>\ubc30\uc1a1\uc9c0\uc6b0\ud3b8\ubc88\ud638", "ord_address=>\uad6c\ubc30\uc1a1\uc9c0\uc8fc\uc18c", "ord_new_address=>\ub3c4\ub85c\uc8fc\uc18c", "ord_addr_dtl=>\uc0c1\uc138\uc8fc\uc18c", "ord_email=>\uc8fc\ubb38\uc790\uc774\uba54\uc77c", "ord_phone=>\uc8fc\ubb38\uc790\uc5f0\ub77d\ucc98", "ord_mobile=>\uc8fc\ubb38\uc790\ud734\ub300\ud3f0", "ord_memo=>\uc8fc\ubb38\uc790\uc694\uccad\uc0ac\ud56d", "email_yn_conv=>\uc774\uba54\uc77c\uc218\uc2e0\ub3d9\uc758\uc5ec\ubd80", "sms_yn_conv=>SMS\uc218\uc2e0\ub3d9\uc758\uc5ec\ubd80", "reg_date_conv=>\ub4f1\ub85d\uc77c", "status_conv=>\uc0c1\ud0dc" }, "\uc8fc\ubb38\uad00\ub9ac(" + m.time("yyyy-MM-dd") + ")");
	ex.write();
	return;
}

//\ucd9c\ub825
p.setBody("order.order_list");
p.setVar("query", m.qs());
p.setVar("list_query", m.qs("id"));
p.setVar("form_script", f.getScript());

p.setLoop("list", list);
p.setVar("list_total", lm.getTotalString());
p.setVar("pagebar", lm.getPaging());
p.setVar("isMalgn", isMalgnOffice && "malgn".equals(loginId)); //\uc218\uc815\uc911

//p.setLoop("methods", m.arr2loop(order.methods));
p.setLoop("methods", payment.getMethods(siteinfo));
p.setLoop("status_list", m.arr2loop(order.statusList));
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
    depend = new com.caucho.vfs.Depend(appDir.lookup("sysop/order/order_list.jsp"), 3230219344721502256L, false);
    com.caucho.jsp.JavaPage.addDepend(_caucho_depends, depend);
    depend = new com.caucho.vfs.Depend(appDir.lookup("sysop/order/init.jsp"), 8792585346402961660L, false);
    com.caucho.jsp.JavaPage.addDepend(_caucho_depends, depend);
    depend = new com.caucho.vfs.Depend(appDir.lookup("sysop/init.jsp"), 7857552796152506446L, false);
    com.caucho.jsp.JavaPage.addDepend(_caucho_depends, depend);
  }
}