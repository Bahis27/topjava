<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <title>Edit</title>
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

<jsp:useBean id="meal" scope="request" type="ru.javawebinar.topjava.model.Meal"/>
<jsp:useBean id="formatter" scope="request" type="java.time.format.DateTimeFormatter"/>

<table border="2">
    <tr>
        <td>
            <form method="post" action="${pageContext.request.contextPath}/meals">
                <input type="hidden" name="id" value="${meal.id}">
                Date: <label><input type="datetime-local" name="dateTime" value="${meal.dateTime.format(formatter)}"></label><br>
                Description: <label><input type="text" name="description" value="${meal.description}"></label><br>
                Calories: <label><input type="number" name="calories" value="${meal.calories}"></label><br>
                <input type="submit" value="Update">
            </form>
        </td>
    </tr>
</table>

</body>
</html>
