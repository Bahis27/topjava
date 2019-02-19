<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <title>Users</title>
</head>
<body>
<h3><a href="index.html">Home</a></h3>
<h2>Users</h2>
<table border="1" cellpadding="8" cellspacing="0">
    <thead>
    <tr>
        <th>Name</th>
        <th>EMail</th>
        <th>Calories</th>
        <th>Roles</th>
    </tr>
    </thead>
    <c:forEach items="${users}" var="user">
        <jsp:useBean id="user" scope="page" type="ru.javawebinar.topjava.model.User"/>
        <tr>
            <td>${user.name}</td>
            <td>${user.email}</td>
            <td>${user.caloriesPerDay}</td>
            <td>${user.roles}</td>
        </tr>
    </c:forEach>
</table>
</body>
</html>