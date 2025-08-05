<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Return Book</title>
    <link rel="stylesheet" type="text/css" href="css/return.css">
    <style>
        body {
            font-family: Arial, sans-serif;
            background-color: #f2f2f2;
        }
        .main-pane {
            width: 600px;
            margin: 50px auto;
            background-color: #fff;
            border-radius: 10px;
            box-shadow: 0 0 15px rgba(0,0,0,0.2);
            padding: 30px;
            text-align: center;
        }
        .title-label {
            font-size: 24px;
            font-weight: bold;
            margin-bottom: 15px;
        }
        .error-label {
            display: block;
            color: red;
            font-weight: bold;
            margin-bottom: 15px;
        }
        .form-label {
            display: block;
            font-weight: bold;
            text-align: left;
            margin-bottom: 5px;
        }
        .form-input {
            width: 100%;
            padding: 6px;
            border: 1px solid #ccc;
            border-radius: 5px;
            margin-bottom: 15px;
        }
        .submit-button {
            background-color: green;
            color: white;
            padding: 8px 20px;
            border: none;
            border-radius: 5px;
            cursor: pointer;
        }
        .submit-button:hover {
            background-color: darkgreen;
        }
    </style>
</head>
<body>

<div class="main-pane">

    <div class="title-label">Return Book</div>

    <label class="error-label">
        <%= request.getAttribute("errorMessage") != null ? request.getAttribute("errorMessage") : "" %>
    </label>

    <form action="ReturnBookServlet" method="post">

        <label class="form-label">Book ID</label>
        <select name="bookId" class="form-input" required>
            <option value="" disabled selected>Select Book</option>
            <%
                java.util.List<com.webLibraryManagementSystem.domain.Book> booksList =
                    (java.util.List<com.webLibraryManagementSystem.domain.Book>) request.getAttribute("booksList");
                if (booksList != null) {
                    for (com.webLibraryManagementSystem.domain.Book book : booksList) {
            %>
                <option value="<%= book.getBookId() %>"><%= book.getBookId() %> - <%= book.getTitle() %></option>
            <%
                    }
                }
            %>
        </select>

        <label class="form-label">Member ID</label>
        <select name="memberId" class="form-input" required>
            <option value="" disabled selected>Select Member</option>
            <%
                java.util.List<com.webLibraryManagementSystem.domain.Member> membersList =
                    (java.util.List<com.webLibraryManagementSystem.domain.Member>) request.getAttribute("membersList");
                if (membersList != null) {
                    for (com.webLibraryManagementSystem.domain.Member member : membersList) {
            %>
                <option value="<%= member.getMemberId() %>"><%= member.getMemberId() %> - <%= member.getName() %></option>
            <%
                    }
                }
            %>
        </select>

        <label class="form-label">Return Date</label>
        <input type="date" name="returnDate" class="form-input" required value="<%= java.time.LocalDate.now() %>"/>

        <button type="submit" class="submit-button">Return</button>
    </form>

</div>

</body>
</html>
