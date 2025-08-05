<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Issue Book</title>
    <link rel="stylesheet" type="text/css" href="css/issue.css">
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
        .form-title {
            font-size: 20px;
            font-weight: bold;
            font-style: italic;
            margin-bottom: 15px;
        }
        .error-label {
            display: block;
            color: red;
            font-weight: bold;
            margin-bottom: 10px;
        }
        .success-label {
            display: block;
            color: green;
            font-weight: bold;
            margin-bottom: 10px;
        }
        .input-label {
            display: block;
            font-weight: bold;
            margin-bottom: 5px;
            text-align: left;
        }
        .input-field {
            width: 100%;
            padding: 6px;
            margin-bottom: 15px;
            border-radius: 5px;
            border: 1px solid #ccc;
        }
        .primary-button {
            background-color: green;
            color: white;
            padding: 8px 20px;
            border: none;
            border-radius: 5px;
            cursor: pointer;
        }
        .primary-button:hover {
            background-color: darkgreen;
        }
    </style>
</head>
<body>

<div class="main-pane">

    <div class="form-title">Issue Book</div>

    <label class="error-label">
        <%= request.getAttribute("errorMessage") != null ? request.getAttribute("errorMessage") : "" %>
    </label>

	<label class="success-label">
        <%= request.getAttribute("successMessage") != null ? request.getAttribute("successMessage") : "" %>
    </label>

    <form action="IssueBookServlet" method="post">
        <label class="input-label">Select Member</label>
        <select name="memberId" class="input-field" required>
            <option value="" disabled selected>Select Member</option>
            <%
                java.util.List<com.webLibraryManagementSystem.domain.Member> membersList = 
                    (java.util.List<com.webLibraryManagementSystem.domain.Member>) request.getAttribute("membersList");
                if(membersList != null){
                    for(com.webLibraryManagementSystem.domain.Member member : membersList){
            %>
                <option value="<%= member.getMemberId() %>"><%= member.getName() %></option>
            <%
                    }
                }
            %>
        </select>

        <label class="input-label">Select Book</label>
        <select name="bookId" class="input-field" required>
            <option value="" disabled selected>Select Book</option>
            <%
                java.util.List<com.webLibraryManagementSystem.domain.Book> booksList = 
                    (java.util.List<com.webLibraryManagementSystem.domain.Book>) request.getAttribute("booksList");
                if(booksList != null){
                    for(com.webLibraryManagementSystem.domain.Book book : booksList){
            %>
                <option value="<%= book.getBookId() %>"><%= book.getTitle() %></option>
            <%
                    }
                }
            %>
        </select>

        <label class="input-label">Issue Date</label>
        <input type="date" name="issueDate" class="input-field"
               value="" />

        <button type="submit" class="primary-button">ISSUE</button>
    </form>

</div>

</body>
</html>
