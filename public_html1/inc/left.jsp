<%@ page contentType="text/html; charset=utf-8" %><%@ include file="init.jsp" %><%

//폼입력
String pcode = m.rs("pcode");
boolean useSub = "Y".equals(m.rs("sub"));
int depth = m.ri("depth", 2);
int depthEnd = useSub ? depth + 1 : depth;

//객체
SitemapDao sitemap = new SitemapDao(siteId);

//목록
/*
DataSet list = sitemap.find(
	" site_id = " + siteId + " AND depth = " + depth + " AND parent_cd = '" + pcode + "' "
	+ " AND display_type IN " + (userId > 0 ? "('A', 'I')" : "('A', 'O')")
	+ " AND display_yn = 'Y' AND status = 1 "
	, "*"
	, "sort ASC"
);
*/
sitemap.setData(sitemap.getList());
DataSet list = new DataSet();
DataSet rs = sitemap.getList(pcode, "display_type IN " + (userId > 0 ? "('A', 'I')" : "('A', 'O')") + " AND display_yn = 'Y'");

if("mypage".equals(pcode) && isRemoteHost) {
	rs.last();
	rs.addRow();
	rs.put("site_id", siteId);
	rs.put("code", "mycourselesson");
	rs.put("parent_cd", "mypage");
	rs.put("depth", 2);
	rs.put("link", "/mypage/course_lesson_list.jsp");
	rs.put("menu_nm", "내화상강의");
	rs.put("target", "_self");
	rs.put("sort", 99);
	rs.put("display_yn", "Y");
}

rs.first();
while(rs.next()) {
	if(!rs.b("display_yn") || depth > rs.i("depth") || depthEnd < rs.i("depth")) continue;
	list.addRow(rs.getRow());
	list.put("is_sub", depth < rs.i("depth"));
}

//출력
p.setLayout(null);
p.setBody("layout.left");
p.setLoop("sub_menu", list);
p.setVar("class", m.rs("class", "lnb_list"));
p.display();

%>