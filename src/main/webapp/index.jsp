<!DOCTYPE html>
<html>
<style>
    body {
        font-family: Arial, sans-serif;
        margin: 0;
        padding: 0;
        display: flex;
        justify-content: center;
        align-items: center;
        height: 100vh;
        background-color: #f5f5f5;
    }

    .container {
        text-align: center;
        padding: 2rem;
        background-color: white;
        border-radius: 8px;
        box-shadow: 0 4px 6px rgba(0, 0, 0, 0.1);
    }

    h1 {
        color: #333;
        margin-bottom: 1.5rem;
    }

    .button-container {
        display: flex;
        justify-content: center;
        gap: 1rem;
        margin-top: 1.5rem;
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
    }

    .button:hover {
        background-color: #3367D6;
    }
</style>
<body>
<div class="container">
    <h1>Welcome to Car Management System</h1>
    <p>Choose an option to continue:</p>

    <div class="button-container">
        <a href="<%=request.getContextPath()%>/cars" class="button">View All Cars</a>
        <a href="<%=request.getContextPath()%>/cars/new" class="button">Add New Car</a>
    </div>
</div>
</body>
</html>