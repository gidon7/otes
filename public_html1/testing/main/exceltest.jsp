<%@ page contentType="text/html; charset=utf-8" %><%@ include file="init.jsp" %><%

    TestDao test = new TestDao();
    DataSet users = test.find();
    //엑셀파일에 데이터 저장하기

//    ex.put(1, 1, "test1");
//    ex.put(1, 2, "test2");
//    m.p(users);
    String[] cols = {"id=>아이디", "email=>이메일", "user_nm=>이름"};
    ExcelWriter ex = new ExcelWriter("/home/igd/public_html/data/test/test2.xls");
    ex.setData(users, cols);
    ex.write();

%>
