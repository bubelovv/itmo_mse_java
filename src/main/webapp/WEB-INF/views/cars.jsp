<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="jakarta.tags.core" prefix="c" %>
<%@ taglib uri="jakarta.tags.fmt" prefix="fmt" %>
<!DOCTYPE html>
<html>

<head>
    <meta charset="UTF-8">
    <title>Car List</title>
    <style>
        body {
            font-family: Arial, sans-serif;
            margin: 0;
            padding: 20px;
            background-color: #f5f5f5;
        }

        .container {
            max-width: 1000px;
            margin: 0 auto;
            background-color: white;
            border-radius: 8px;
            box-shadow: 0 4px 6px rgba(0, 0, 0, 0.1);
            padding: 2rem;
        }

        h1 {
            color: #333;
            margin-bottom: 1.5rem;
        }

        .button {
            padding: 0.8rem 1.5rem;
            background-color: #4285F4;
            color: white;
            border: none;
            border-radius: 4px;
            cursor: pointer;
            text-decoration: none;
            font-size: 1rem;
            transition: background-color 0.3s;
            display: inline-block;
            margin-bottom: 1.5rem;
        }

        .button:hover {
            background-color: #3367D6;
        }

        .home-link {
            display: block;
            margin-bottom: 1rem;
        }

        table {
            width: 100%;
            border-collapse: collapse;
            margin-top: 1rem;
        }

        th, td {
            padding: 12px;
            text-align: left;
            border-bottom: 1px solid #ddd;
        }

        th {
            background-color: #f2f2f2;
            color: #333;
        }

        tr:hover {
            background-color: #f9f9f9;
        }

        .action-buttons {
            display: flex;
            gap: 0.5rem;
        }

        .edit-button {
            padding: 6px 12px;
            background-color: #4285F4;
            color: white;
            border: none;
            border-radius: 4px;
            cursor: pointer;
            text-decoration: none;
            font-size: 0.9rem;
        }

        .delete-button {
            padding: 6px 12px;
            background-color: #DB4437;
            color: white;
            border: none;
            border-radius: 4px;
            cursor: pointer;
            font-size: 0.9rem;
        }

        .edit-button:hover {
            background-color: #3367D6;
        }

        .delete-button:hover {
            background-color: #C53929;
        }

        .no-cars {
            padding: 20px;
            text-align: center;
            background-color: #f9f9f9;
            border-radius: 4px;
        }
    </style>
</head>

<body>
<div class="container">
    <a href="<c:url value='/'/>" class="home-link">← Back to Home</a>
    <h1>Car List</h1>

    <a href="<c:url value='/cars/new'/>" class="button">Add New Car</a>

    <c:if test="${empty cars}">
        <div class="no-cars">No cars found.</div>
    </c:if>

    <c:if test="${not empty cars}">
        <table>
            <thead>
            <tr>
                <th>ID</th>
                <th>Make</th>
                <th>Model</th>
                <th>Color</th>
                <th>Manufacture Date</th>
                <th>Price</th>
                <th>Actions</th>
            </tr>
            </thead>
            <tbody>
            <c:forEach var="car" items="${cars}">
                <tr>
                    <td>
                        <c:out value="${car.id}"/>
                    </td>
                    <td>
                        <c:out value="${car.make}"/>
                    </td>
                    <td>
                        <c:out value="${car.model}"/>
                    </td>
                    <td>
                        <c:out value="${car.color}"/>
                    </td>
                    <td>
                        <fmt:formatDate value="${car.manufactureDate}" pattern="yyyy-MM-dd"/>
                    </td>
                    <td>
                        <c:if test="${not empty car.price}">
                            <fmt:formatNumber value="${car.price}" type="currency"
                                              currencySymbol="$"/>
                        </c:if>
                    </td>
                    <td class="action-buttons">
                        <a href="<c:url value='/cars/edit?id=${car.id}'/>" class="edit-button">Edit</a>

                        <form style="display: inline" method="post" action="<c:url value='/cars/delete'/>">
                            <input type="hidden" name="id" value="${car.id}">
                            <button type="submit" class="delete-button"
                                    onclick="return confirm('Are you sure you want to delete this car?')">Delete
                            </button>
                        </form>
                    </td>
                </tr>
            </c:forEach>
            </tbody>
        </table>
    </c:if>
</div>
</body>

</html>
