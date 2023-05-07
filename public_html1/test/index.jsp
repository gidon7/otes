<%@ page contentType="text/html; charset=utf-8" %><%@ include file="init.jsp"%><%

    //객체
    TestDao dao = new TestDao();

    //폼체크
    f.addElement("user_nm", null, "required: 'Y'");
    f.addElement("email", null, "option: 'email'");

    //DB에 등록
    if(m.isPost() && f.validate()) {
        dao.item("user_nm", f.get("user_nm"));
        dao.item("email", f.get("email"));
        if(!dao.insert()) { //true false 리턴
            m.jsError("입력오류");
            return;
        }
    }

    //데이터셋
    DataSet dataset = new DataSet();
    dataset.addRow();
    dataset.put("user_nm", "yourname");
    dataset.put("email", "youremail");

//    Hashtable ht = new Hashtable();
//    ht.put("user_nm", "myname");
//    ht.put("email", "myemail");
//    dataset.addRow(ht);
//
//    ht = dataset.getRow();
//
//    // 여러 개가 검색되어도 id가 작은 것 하나만 가져온다.
//    DataSet info = dao.find("name = 'kong'"); // where절
//    if(!info.next()) { // next()를 통해 포인터를 이동시켜 첫번째 데이터가 존재하는지 확인
//        m.jsError("정보를 찾을 수 없습니다."); // alert 후 history.go(-1) 이전페이지로
//        return; // 리턴을 하지 않으면 아래 내용이 실행될 수 있음
//    }


    int inputNum = dao.execute("INSERT INTO TB_TEST (user_nm, email) VALUES ('hong', 'honghong@gmail.com')");
    // 영향 받을 레코드 개수를 리턴

    //DB 수정
    dao.item("user_nm", "kingkong");
    dao.update("user_nm = 'king'");
    int updateNum = dao.execute("UPDATE TB_TEST SET user_nm = 'kong' WHERE user_nm = 'kingkong'");

    //DB 삭제 dao.delete("user_nm = 'hong'");

    // .setDebug(out); 화면에 디버깅 메시지 출력 없으면 로그 파일에

    //목록

    ListManager lm = new ListManager();

    lm.setRequest(request);
    lm.setListNum(20);
    lm.setTable(dao.table); // join
    lm.setFields("*");
    lm.setOrderBy("id desc");
    lm.addWhere("status != -1");
    lm.addSearch("firstname, lastname", "ho", "%LIKE"); // 값이 있는 경우에만 추가
    // firstname LIKE '%ho' OR lastname LIKE '%ho'

    //포맷팅
    DataSet list = lm.getDataSet();

    while(list.next()) { // first() last() prev() 커서 이동
        list.put("name_conv", list.s("user_nm") + "씨");
    }

    //Excel
    ExcelReader ex = new ExcelReader("");
    DataSet exList = ex.getDataSet();
    //XML
    SimpleParser sp = new SimpleParser("test.xml"); // 속성값을 가져오려면 다른 XML parser 이용해야
    DataSet spList = sp.getDataSet("//note"); // 노드 경로
    m.p("XML: " + spList);

    //JSON
    String json = list.serialize();
    DataSet list2 = new DataSet();
    list2.unserialize(json);


    //출력

    p.setLayout("main");
    p.setBody("main.index");
    p.setVar("form_script", f.getScript());

    p.setLoop("d", dataset);
    p.setVar("splist", spList);
//    p.setVar(info); // p.setVar("name", "value"); 가 이김
    p.setVar("is_empty", true);
    p.setVar("condition2nd", true);
    p.setVar("input_num", inputNum);
    p.setVar("update_num", updateNum);
    p.display();

%>