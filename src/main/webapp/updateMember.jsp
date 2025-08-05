<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"
         import="java.util.List,com.webLibraryManagementSystem.domain.Member" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Update Member</title>
    <link rel="stylesheet" type="text/css" href="css/updatemember.css">

    <style>
        .background-pane {
            width: 654px;
            height: 460px;
            margin: 50px auto;
            padding: 20px;
            background-color: #f0f0f0;
            border-radius: 10px;
            position: relative;
        }
        .title-label {
            font-size: 24px;
            font-weight: bold;
            display: block;
            text-align: center;
            margin-bottom: 10px;
        }
        .error-label {
            font-family: 'Times New Roman', serif;
            font-weight: bold;
            color: red;
            font-size: 18px;
            text-align: center;
            display: block;
            margin-bottom: 20px;
        }
        .form-container {
            width: 100%;
            margin-top: 10px;
        }
        .form-row {
            display: flex;
            align-items: center;
            margin-bottom: 15px;
        }
        .form-label {
            width: 150px;
            font-weight: bold;
        }
        .text-field, .combo-box {
            width: 200px;
            padding: 5px;
            margin-left: 10px;
        }
        .submit-button {
            display: block;
            margin: 30px auto 0 auto;
            padding: 8px 20px;
            background-color: #4CAF50;
            color: white;
            border: none;
            border-radius: 5px;
            cursor: pointer;
            font-style: italic;
            font-size: 16px;
        }
        .submit-button:hover {
            background-color: #45a049;
        }
    </style>
</head>
<body>
    <div class="background-pane">
        <!-- Title -->
        <label class="title-label">UPDATE MEMBER</label>

        <!-- Error Label -->
        <label class="error-label">
            <%= request.getAttribute("errorMessage") != null ? request.getAttribute("errorMessage") : "" %>
        </label>

        <!-- Form Start -->
        <form action="UpdateMemberServlet" method="post" class="form-container">

            <div class="form-row">
                <label class="form-label">Select Member:</label>
                <select name="memberId" class="combo-box" required>
                    <option value="" disabled selected>Select Member</option>
                    <%
                        @SuppressWarnings("unchecked")
                        List<com.webLibraryManagementSystem.domain.Member> membersList = 
                            (List<com.webLibraryManagementSystem.domain.Member>) request.getAttribute("membersList");
                        if(membersList != null){
                            for(com.webLibraryManagementSystem.domain.Member member : membersList){
                    %>
                        <option value="<%= member.getMemberId() %>"><%= member.getName() %></option>
                    <%
                            }
                        }
                    %>
                </select>
            </div>

            <div class="form-row">
                <label class="form-label">Name:</label>
                <input type="text" name="name" class="text-field" id="name" disabled>
            </div>

            <div class="form-row">
                <label class="form-label">Email:</label>
                <input type="email" name="email" class="text-field" id="email" disabled>
            </div>

            <div class="form-row">
                <label class="form-label">Mobile:</label>
                <input type="text" name="mobile" class="text-field" id="mobile" disabled>
            </div>

            <div class="form-row">
                <label class="form-label">Gender:</label>
                <select name="gender" class="combo-box" id="gender" disabled>
                    <option value="" disabled selected>Select Gender</option>
                    <option value="Male">Male</option>
                    <option value="Female">Female</option>
                    <option value="Other">Other</option>
                </select>
            </div>

            <div class="form-row">
                <label class="form-label">Address:</label>
                <input type="text" name="address" class="text-field" id="address" disabled>
            </div>

            <!-- Submit Button -->
            <button type="submit" class="submit-button">UPDATE</button>
        </form>
    </div>
</body>
</html>
