<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page language="java" contentType="text/html; charset=utf-8"
         pageEncoding="utf-8" %>
<%
    String path = request.getContextPath();
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>通话记录查询系统</title>
</head>
<body>

<style type="text/css">

    div  {
        position:absolute;
        top:50%;
        left:50%;
        margin:-150px 0 0 -200px;
        width:400px;
        height:300px;
        border:1px solid #008800;
    }

</style>
<div align="center">
    <form action="/queryCallLogList" method="post">
        手机号码：<input type="text", name="telephone"><br>
        <br>年：<input type="text", name="year"><br>
        <br>月：<input type="text", name="month"><br>
        <br>日：<input type="text", name="day"><br>
        
        <br> <h3>${requestScope.information}</h3>
        <input type="submit" value="开始查询通话记录">
    </form>
</div>
</body>
</html>
