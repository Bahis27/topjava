<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
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
        <th colspan="2">Action</th>
    </tr>
    <jsp:useBean id="mealtolist" scope="request" type="java.util.List"/>
    <jsp:useBean id="formatter" scope="request" type="java.time.format.DateTimeFormatter"/>

    <c:forEach var="mealto" items="${mealtolist}">
        <c:choose>
            <c:when test="${mealto.excess}">
                <tr style="color: crimson">
            </c:when>
            <c:otherwise>
                <tr style="color: green">
            </c:otherwise>
        </c:choose>
        <td>${mealto.dateTime.format(formatter)}</td>
        <td>${mealto.description}</td>
        <td>${mealto.calories}</td>
        <td>
            <a href="${pageContext.request.contextPath}/meals?action=edit&mealtoid=${mealto.id}">edit</a>
        </td>
        <td>
            <a href="${pageContext.request.contextPath}/meals?action=delete&mealtoid=${mealto.id}">delete</a>
        </td>
        </tr>
    </c:forEach>
</table>

<h3>Add new entry here:</h3>
<table border="2">
    <tr>
        <td>
            <form method="post" action="${pageContext.request.contextPath}/meals">
                Date: <label><input type="datetime-local" name="dateTime"></label><br>
                Description: <label><input type="text" name="description"></label><br>
                Calories: <label><input type="number" name="calories"></label><br>
                <input type="submit" value="Add">
            </form>
        </td>
    </tr>
</table>

</body>
</html>