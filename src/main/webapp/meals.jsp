<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>
<head>
    <title>Meals</title>
    <style>
        table {
            font-family: arial, sans-serif;
            border-collapse: collapse;
        }

        td {
            border: 1px solid #B9EEFD;
            text-align: left;
            padding: 8px;
        }

        th {
            border: 1px solid #B9EEFD;
            background-color: #1068EF;
            text-align: left;
            padding: 8px;
        }
    </style>
</head>
<body>
<h3><a href="index.html">Home</a></h3>
<h2>Meals</h2>
<br>
<table>
    <tr>
        <th>Date</th>
        <th>Description</th>
        <th>Calories</th>
    </tr>
    <jsp:useBean id="mealtolist" scope="request" type="java.util.List"/>
    <jsp:useBean id="formatter" scope="request" type="java.time.format.DateTimeFormatter"/>

    <c:forEach var="mealto" items="${mealtolist}">
        <c:if test="${mealto.isExcess()}">
            <tr style="background-color: crimson">
        </c:if>
        <c:if test="${!mealto.isExcess()}">
            <tr style="background-color: green">
        </c:if>
        <td>${mealto.getDateTime().format(formatter)}</td>
        <td>${mealto.getDescription()}</td>
        <td>${mealto.getCalories()}</td>
        </tr>
    </c:forEach>
</table>
</body>
</html>