<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"
         import="java.util.List,com.webLibraryManagementSystem.domain.Member" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>View All Members</title>
    <link rel="stylesheet" type="text/css" href="css/viewallmembers.css">

    <style>
        body {
            font-family: Arial, sans-serif;
            background-color: #f8f8f8;
        }
        .container {
            width: 90%;
            margin: 30px auto;
            background-color: #fff;
            padding: 20px;
            border-radius: 8px;
            box-shadow: 0 0 10px rgba(0,0,0,0.1);
        }
        h2 {
            text-align: center;
            font-style: italic;
            font-weight: bold;
            margin-bottom: 10px;
        }
        .error-label {
            text-align: center;
            color: red;
            font-style: italic;
            font-weight: bold;
            margin-bottom: 20px;
        }
        table {
            width: 100%;
            border-collapse: collapse;
            margin-top: 15px;
        }
        table, th, td {
            border: 1px solid #ccc;
        }
        th {
            background-color: #4CAF50;
            color: white;
            text-align: center;
            padding: 10px;
        }
        td {
            text-align: center;
            padding: 8px;
        }
        .action-btn {
            padding: 4px 10px;
            margin: 2px;
            border: none;
            border-radius: 3px;
            cursor: pointer;
            color: white;
        }
        .edit-btn { background-color: #2196F3; }
        .delete-btn { background-color: #f44336; }
    </style>
</head>
<body>
<div class="container">

    <h2>View All Members</h2>

    <div class="error-label">
        <%= request.getAttribute("errorMessage") != null ? request.getAttribute("errorMessage") : "" %>
    </div>

    <table>
        <thead>
            <tr>
                <th>ID</th>
                <th>Name</th>
                <th>Email</th>
                <th>Mobile</th>
                <th>Gender</th>
                <th>Address</th>
                <th>Actions</th>
            </tr>
        </thead>
        <tbody>
        <%
            @SuppressWarnings("unchecked")
            List<com.webLibraryManagementSystem.domain.Member> membersList =
                    (List<com.webLibraryManagementSystem.domain.Member>) request.getAttribute("membersList");

            if (membersList != null && !membersList.isEmpty()) {
                for (com.webLibraryManagementSystem.domain.Member member : membersList) {
        %>
            <tr>
                <td><%= member.getMemberId() %></td>
                <td><%= member.getName() %></td>
                <td><%= member.getEmail() %></td>
                <td><%= member.getMobile() %></td>
                <td><%= member.getGender() %></td>
                <td><%= member.getAddress() %></td>
                <td>
                    <form action="UpdateMemberServlet" method="get" style="display:inline;">
                        <input type="hidden" name="memberId" value="<%= member.getMemberId() %>">
                        <button type="submit" class="action-btn edit-btn">Edit</button>
                    </form>
                    <form action="DeleteMemberServlet" method="post" style="display:inline;">
                        <input type="hidden" name="memberId" value="<%= member.getMemberId() %>">
                        <button type="submit" class="action-btn delete-btn">Delete</button>
                    </form>
                </td>
            </tr>
        <%
                }
            } else {
        %>
            <tr>
                <td colspan="7">No members found.</td>
            </tr>
        <%
            }
        %>
        </tbody>
    </table>
</div>
</body>
</html>
