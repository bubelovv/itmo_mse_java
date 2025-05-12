<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="jakarta.tags.core" prefix="c" %>
<%@ taglib uri="jakarta.tags.fmt" prefix="fmt" %>
<!DOCTYPE html>
<html>

<head>
    <meta charset="UTF-8">
    <title>${empty car ? 'Add New Car' : 'Edit Car'}</title>
    <style>
        body {
            font-family: Arial, sans-serif;
            margin: 0;
            padding: 20px;
            background-color: #f5f5f5;
        }

        .container {
            max-width: 600px;
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

        .home-link {
            display: block;
            margin-bottom: 1rem;
        }

        .form-group {
            margin-bottom: 1.2rem;
        }

        label {
            display: block;
            margin-bottom: 0.5rem;
            font-weight: bold;
            color: #555;
        }

        input[type="text"],
        input[type="number"],
        input[type="date"] {
            width: 100%;
            padding: 10px;
            border: 1px solid #ddd;
            border-radius: 4px;
            font-size: 1rem;
            box-sizing: border-box;
        }

        .button-container {
            display: flex;
            gap: 1rem;
            margin-top: 1.5rem;
        }

        .submit-button {
            padding: 0.8rem 1.5rem;
            background-color: #4285F4;
            color: white;
            border: none;
            border-radius: 4px;
            cursor: pointer;
            font-size: 1rem;
            transition: background-color 0.3s;
        }

        .cancel-button {
            padding: 0.8rem 1.5rem;
            background-color: #757575;
            color: white;
            border: none;
            border-radius: 4px;
            cursor: pointer;
            text-decoration: none;
            font-size: 1rem;
            transition: background-color 0.3s;
        }

        .submit-button:hover {
            background-color: #3367D6;
        }

        .cancel-button:hover {
            background-color: #616161;
        }
    </style>
</head>

<body>
<div class="container">
    <a href="<c:url value='/cars'/>" class="home-link">← Back to Cars</a>
    <h1>${empty car ? 'Add New Car' : 'Edit Car'}</h1>

    <jsp:useBean id="carForm" class="ru.itmo.Car" scope="request"/>

    <form method="post" action="<c:url value='${empty car ? "/cars/new" : "/cars/edit"}'/>">
        <c:if test="${not empty car}">
            <input type="hidden" name="id" value="${car.id}">
        </c:if>

        <div class="form-group">
            <label for="make">Make:</label>
            <input type="text" id="make" name="make" value="${not empty car ? car.make : ''}" required>
        </div>

        <div class="form-group">
            <label for="model">Model:</label>
            <input type="text" id="model" name="model" value="${not empty car ? car.model : ''}" required>
        </div>

        <div class="form-group">
            <label for="color">Color:</label>
            <input type="text" id="color" name="color" value="${not empty car ? car.color : ''}">
        </div>

        <div class="form-group">
            <label for="manufactureDate">Manufacture Date:</label>
            <input type="date" id="manufactureDate" name="manufactureDate"
                   value="<fmt:formatDate value='${not empty car ? car.manufactureDate : null}' pattern='yyyy-MM-dd'/>">
        </div>

        <div class="form-group">
            <label for="price">Price:</label>
            <input type="number" id="price" name="price" value="${not empty car ? car.price : ''}"
                   step="0.01">
        </div>

        <div class="button-container">
            <button type="submit" class="submit-button">Save</button>
            <a href="<c:url value='/cars'/>" class="cancel-button">Cancel</a>
        </div>
    </form>
</div>
</body>

</html>
