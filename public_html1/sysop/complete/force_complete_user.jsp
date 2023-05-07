<%@ page contentType="text/html; charset=utf-8" %><%@ include file="init.jsp" %><%

//접근권한
if(!Menu.accessible(76, userId, userKind)) { m.jsError("접근 권한이 없습니다."); return; }

//기본키
int cid = m.ri("cid");
if(cid == 0) { m.jsError("기본키는 반드시 지정해야 합니다."); return; }

//객체
LmCategoryDao category = new LmCategoryDao("course");
CourseDao course = new CourseDao();
CourseUserDao courseUser = new CourseUserDao();
ActionLogDao actionLog = new ActionLogDao();

//카테고리
DataSet categories = category.getList(siteId);

//정보-과정
DataSet cinfo = course.find(
        "id = " + cid + " AND status != -1 AND site_id = " + siteId + ""
                + ("C".equals(userKind) ? " AND id IN (" + manageCourses + ") " : "")
);
if(!cinfo.next()) { m.jsError("해당 정보가 없습니다."); return; }

//폼체크
f.addElement("action_reason", null, "hname:'액션사유', required:'Y'");

//처리
if(m.isPost() && f.validate() &&"force_complete".equals(m.rs("mode"))) {

	if("".equals(m.rs("cuid"))) { m.jsAlert("기본키는 반드시 지정해야 합니다."); return; }

	DataSet cuinfo = courseUser.find("status IN (1,3) AND close_yn = 'N' AND id = " + m.rs("cuid") + " AND site_id = " + siteId + "");
	while(cuinfo.next()) {

		DataSet binfo = new DataSet();
		binfo.addRow();
		binfo.put("site_id",cuinfo.s("site_id"));
		binfo.put("id",cuinfo.s("id"));
		binfo.put("user_id",cuinfo.s("user_id"));
		binfo.put("course_id",cuinfo.s("course_id"));
		binfo.put("complete_yn",cuinfo.s("complete_yn"));
		binfo.put("complete_no",cuinfo.s("complete_no"));
		binfo.put("complete_date",cuinfo.s("complete_date"));
		binfo.put("fail_reason",cuinfo.s("fail_reason"));
		binfo.put("mod_date",cuinfo.s("mod_date"));

		String compNo = m.time("yyyy", cuinfo.s("end_date")) + "-" + cuinfo.i("id");
		if ("Y".equals(cinfo.s("complete_no_yn"))) {
			if ("R".equals(cinfo.s("postfix_type"))) {
				compNo = courseUser.getCompNo(courseUser.getUserRank(cuinfo.i("id"), cinfo.i("course_id"), cinfo.s("postfix_ord")), cinfo.s("complete_prefix"), cinfo.i("postfix_cnt"));
			} else {
				compNo = cinfo.s("complete_prefix") + "-" + m.strrpad("" + cuinfo.i("id"), cinfo.i("postfix_cnt"), "0");
			}
		}

		courseUser.item("complete_yn", "Y");
		courseUser.item("complete_no", compNo);
		courseUser.item("complete_date", cuinfo.s("end_date") + "235959");
		courseUser.item("fail_reason", "");
		courseUser.item("mod_date", m.time("yyyyMMdd") + "235959");
		if (!courseUser.update("status IN (1,3) AND course_id = " + cid + " AND close_yn = 'N' AND id = " + cuinfo.i("id") + " AND site_id = " + siteId)) {
			m.jsAlert("종료를 취소하는 중 오류가 발생했습니다.");
			return;
		}

		DataSet ainfo = courseUser.find("status IN (1,3) AND close_yn = 'N' AND id = " + cuinfo.i("id") + " AND site_id = " + siteId + "" , "site_id, id, user_id, course_id, complete_yn, complete_no, complete_date, fail_reason, mod_date");

		//액션로그
		actionLog.item("site_id", siteId);
		actionLog.item("user_id", userId);
		actionLog.item("module", "course_user");
		actionLog.item("module_id", cuinfo.i("id"));
		actionLog.item("action_type", "F");
		actionLog.item("action_desc", "강제수료처리");
		actionLog.item("before_info", binfo.serialize());
		actionLog.item("after_info", ainfo.serialize());
		actionLog.item("action_reason", f.get("action_reason"));
		actionLog.item("important_yn", "Y");
		actionLog.item("reg_date", m.time("yyyyMMddHHmmss"));
		actionLog.item("status", 1);
		if(!actionLog.insert()) {
			m.jsError("삭제하는 중 오류가 발생했습니다. [E2]");
			return;
		}

	}

	m.jsAlert("강제수료처리 되었습니다.");
	m.jsReplace("complete_user.jsp?cid=" + cid, "parent");
	return;
}

//출력
p.setLayout("poplayer");
p.setBody("complete.force_complete_user");
p.setVar("p_title", "강제수료처리 사유");
p.setVar("query", m.qs());
p.setVar("list_query", m.qs("id"));
p.setVar("form_script", f.getScript());

p.display();

%>