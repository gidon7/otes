<%@ page contentType="text/html; charset=utf-8" %><%@ include file="init_api.jsp" %><%@ page import="malgnsoft.json.*" %><%

KtRemoteDao ktRemote = new KtRemoteDao(siteId, isDevServer); ktRemote.setDebug(out);
LanEduDao lanEdu = new LanEduDao(siteId, isDevServer); lanEdu.setDebug(out);

if(m.isPost() && f.validate()) {
    if("manual".equals(f.get("mode"))) {

        Http http = new Http(f.get("url"));
        http.setDebug(out);
        http.setHeader("Content-Type", "application/json");
        if(!"".equals(f.get("token"))) http.setHeader("Authorization", "Bearer " + f.get("token"));
        http.setData(f.get("data"));
        String jstr = http.send("POST");
        out.print(jstr);

    } else {

        String value = f.get("kvalue");
        String mode = f.get("mode");

        String ret = "";
        if("pcreate".equals(mode)) { //플랜 생성
            long now = m.getUnixTime() + 20000000;
            m.p(lanEdu.insertPlan("테스트 방 생성", "H", 229, siteinfo.s("lanedu_id"), "", now + (120 * 31), now + (60 * 60 * 48)));
        } else if("pupdate".equals(mode)) { //플랜 수정

        } else if("pdelete".equals(mode)) { //플랜 삭제

        } else if("plan_list".equals(mode)){
          m.p(lanEdu.getPlanList(siteinfo.s("lanedu_id"), 1, 1640102400, 1640185199));
        } else if("cucreate".equals(mode)) { //수강생 출석 등록

        } else if("cuupdate".equals(mode)) { //수강생 출석 수정

        } else if("cudelete".equals(mode)) { //수강생 출석 삭제

        } else if("plan_list".equals(mode)) { //수강생 토큰 생성
            
        } else if("history".equals(mode)) { //플랜 생성 이력

        } else if("attend".equals(mode)) { //플랜 출결 이력

        } else if("org_insert".equals(mode)) {
            //m.p(lanEdu.insertOrganization(siteId, siteinfo.s("site_nm")));
        } else if("org_list".equals(mode)) {
            m.p(siteinfo.s("lanedu_id"));
            m.p(lanEdu.getOrganizationInfo(siteinfo.s("lanedu_id")));
            //m.p(lanEdu.getOrganizationList());
        } else if("org_del".equals(mode)) {
            m.p(lanEdu.deleteOrganization(siteinfo.s("lanedu_id")));
        } else if("license_insert".equals(mode)) {
            m.p(lanEdu.insertLicense(siteinfo.s("lanedu_id"), "H", 1));
        } else if("license_update".equals(mode)) {
            
        } else if("license_del".equals(mode)) {
            m.p(lanEdu.deleteLicense(siteinfo.s("lanedu_id"), value));
        } else {
            ret = "API 찾을수 없음";
        }
        out.print(ret);
    }
}
%>