<%@ page contentType="text/html; charset=utf-8" %><%@ include file="init.jsp" %><%

    //객체
TestDao test = new TestDao();

test.setDebug(out);

DataSet users = test.find("user_nm = 'hong'","user_nm", 5);
DataSet info = test.query("SELECT * FROM TB_TEST");
if(!info.next()) { // next() 를 통해 포인터를 이동시켜 첫번째 데이터가 존재하는지 확인한다.
    m.jsError("테스트정보를 찾을 수 없습니다."); // alert 한 후 history.go(-1) 이전페이지로
    return; // 리턴을 하지 않으면 아래 내용이 실행될 수 있다.
}
//m.p(info);

//test.item("user_nm", "money");
//test.item("email", "igd@gmail.com");
//test.insert();
int inputNum = test.execute("INSERT INTO TB_TEST (user_nm, email) VALUES ('GD', 'honghong@gmail.com')");// 영향 받을 레코드 개수를 리턴


//DB 수정
//test.item("email", "test@gmail.com");
//test.update("user_nm = 'GD'");
int updateNum = test.execute("UPDATE TB_TEST SET email = 'igd@gmail.com' WHERE id = '34' "); // 24번째나 둘중 하나 사용

//DB 삭제
//    test.delete("user_nm = 'hong'");
//    user.execute("DELETE FROM TB_TEST WHERE user_nm = '홍길동'"); // 28번째나 둘중 하나 사용


p.setLayout("main");
p.setBody("main.index");
p.setVar(info);
p.setLoop("users", users);//users라는 DataSet객체가 목록이므로 setloop메소드를 이용해서 데이터를 넘겨준다. html에서 loop지시자를 통해 목록을 출력하는 html을 생성
p.setVar("input_num", inputNum);
p.setVar("update_num", updateNum);
p.display();


%>
