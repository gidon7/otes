<%@ include file="../init.jsp" %><%
    String ch = m.rs("ch", "sysop");

    //정보-사이트설정
    DataSet siteconfig = SiteConfig.getArr(new String[] {"kt_remote", "lanedu_type"});
    String type = siteconfig.s("lanedu_type");
    if(!"L".equals(type) && !"B".equals(type)) { m.jsAlert("kt랜선에듀 서비스를 신청하셔야 이용할 수 있습니다."); return; }
    boolean isBiz = "B".equals(type);

    KtRemoteDao ktRemote = new KtRemoteDao(siteId, isDevServer);
    LanEduDao lanEdu = new LanEduDao(siteId, isDevServer); lanEdu.setDebug(out);
    LanEduProductDao lanEduProduct = new LanEduProductDao(siteId);

    DataSet pinfo = lanEduProduct.find("site_id = " + siteId + " AND status = 1");
    if(isBiz) {
        if("".equals(siteinfo.s("lanedu_id"))) { m.jsError("조직 정보가 없습니다."); return; }
        if(!pinfo.next()) { m.jsError("제품키 정보가 없습니다."); return; }
        p.setVar("biz_block", true);
    }
    p.setVar("expire_date_conv", m.time("yyyy년 MM월 dd일 HH:mm:ss", pinfo.s("expire_date")));
    p.setVar("max_host", pinfo.s("host"));
%>