<%@ page contentType="text/html; charset=utf-8" %><%@ include file="init.jsp" %><%

//객체
TestDao test = new TestDao();

//폼체크
f.addElement("user_nm", null, "required: 'Y'");
f.addElement("email", null, "option: 'email'");

//    DataSet users = test.query("SELECT USER_NM, EMAIL from TB_TEST", 5); //쿼리문 뒤에 숫자를 넣어주면 그 숫자만큼 정보를 가져온다.

//    DataSet users = test.query("SELECT USER_NM, EMAIL FROM TB_TEST WHERE EMAIL = 'hong@gmail.com'");


//DB에 등록
if(m.isPost() && f.validate()) {
    test.item("user_nm", f.get("user_nm"));
    test.item("email", f.get("email"));
    if(!test.insert()) { //true false 리턴
        m.jsError("등록 오류가 발생했습니다.");
    } else{
        m.jsAlert("성공적으로 등록되었습니다.");
    }
    return;
}


//데이터셋
DataSet users = new DataSet();

users.addRow();
users.put("user_nm", "Daniel");
users.put("email", "daniel@gmail.com");

// 여러 개가 검색되어도 id가 낮은거 하나만 가져온다.
DataSet info = test.find("user_nm = 'GD'"); // 단일 레코드
//DataSet info = test.query("SELECT * FROM TB_TEST");// 복수 레코드

if(!info.next()) { // next() 를 통해 포인터를 이동시켜 첫번째 데이터가 존재하는지 확인한다.
    m.jsError("회원정보를 찾을 수 없습니다."); // alert 한 후 history.go(-1) 이전페이지로
    return; // 리턴을 하지 않으면 아래 내용이 실행될 수 있다.
}

int inputNum = test.execute("INSERT INTO TB_TEST (user_nm, email) VALUES ('hong', 'honghong@gmail.com')");// 영향 받을 레코드 개수를 리턴

//DB 수정
test.item("user_nm","GD");
//test.update("user_nm = 'hong'");
int updateNum = test.execute("UPDATE TB_TEST SET email = 'igd@gmail.com' WHERE id = '50' "); // 40번째나 둘중 하나 사용

//DB 삭제
//    test.delete("user_nm = 'hong'");
//    user.execute("DELETE FROM TB_TEST WHERE user_nm = '홍길동'"); // 44번째나 둘중 하나 사용


//ListManager lm = new ListManager();
//
//lm.setRequest(request);
//lm.setListNum(10);
//lm.setTable(test.table);
//lm.setFields("*");
//    lm.addWhere("status !=-1");// where절
//    lm.addSearch("name, email, phone", "Hong", "LIKE");
//    	SQL : (name LIKE '%Hong%' OR email LIKE '%Hong%' OR phone LIKE


//DataSet list = lm.getDataSet();
//while(list.next()) { // first() last() prev() 커서 이동
//    list.put("name_conv", list.s("user_nm") + "씨");
//}

//Excel
//ExcelReader ex = new ExcelReader("");
//DataSet exList = ex.getDataSet();


//XML
SimpleParser sp = new SimpleParser(docRoot + "/testing/test.xml"); // 속성값을 가져오려면 다른 XML parser 이용해야
DataSet spList = sp.getDataSet("//note"); // 노드 경로
m.p("XML: " + spList);





p.setLayout("main");
p.setBody("main.index");
p.setVar("form_script", f.getScript());
p.setVar("title", "Admin Page");
p.setVar("is_admin", true); // is_admin(조건 변수명)이 true라면 html에서 문장이 나온다.
p.setLoop("users", users); //users라는 DataSet객체가 회원목록이므로 setloop메소드를 이용해서 데이터를 넘겨준다. html에서 loop지시자를 통해 회원목록을 출력하는 html을 생성
p.setVar(info);
p.setLoop("info", info);
p.setVar("input_num", inputNum);
p.setVar("update_num", updateNum);
p.display();


%>
