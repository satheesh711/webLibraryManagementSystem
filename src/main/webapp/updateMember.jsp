<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"
         import="java.util.List,com.webLibraryManagementSystem.domain.Member" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Update Member</title>
    <link rel="stylesheet" type="text/css" href="css/updatemember.css">

    <style>
        .background-pane {
            width: 654px;
            margin: 50px auto;
            padding: 20px;
            background-color: #f0f0f0;
            border-radius: 10px;
        }
        .title-label {
            font-size: 24px;
            font-weight: bold;
            text-align: center;
            margin-bottom: 10px;
        }
        .error-label {
            font-weight: bold;
            color: red;
            text-align: center;
            display: block;
            margin-bottom: 10px;
        }
        .success-label {
            font-weight: bold;
            color: green;
            text-align: center;
            display: block;
            margin-bottom: 10px;
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
            font-size: 16px;
        }
        .submit-button:hover {
            background-color: #45a049;
        }
    </style>
</head>
<body>
    <div class="background-pane">
        <label class="title-label">UPDATE MEMBER</label>

        <c:if test="${not empty errorMessage}">
            <span class="error-label">${errorMessage}</span>
        </c:if>
        <c:if test="${not empty successMessage}">
            <span class="success-label">${successMessage}</span>
        </c:if>

        <form action="MemberUpdateServlet" method="post" class="form-container">

            <div class="form-row">
                <label class="form-label">Select Member:</label>
                <select name="memberId" class="combo-box" id="memberId" onchange="fillMemberData()" required>
                    <option value="" disabled selected>Select Member</option>
                    <c:forEach var="m" items="${membersList}">
                        <option value="${m.memberId}"
                                data-name="${m.name}"
                                data-email="${m.email}"
                                data-mobile="${m.mobile}"
                                data-gender="${m.gender}"
                                data-address="${m.address}">
                            ${m.name} (${m.email})
                        </option>
                    </c:forEach>
                </select>
            </div>

            <div class="form-row">
                <label class="form-label">Name:</label>
                <input type="text" name="name" class="text-field" id="name" disabled required>
            </div>

            <div class="form-row">
                <label class="form-label">Email:</label>
                <input type="email" name="email" class="text-field" id="email" disabled required>
            </div>

            <div class="form-row">
                <label class="form-label">Mobile:</label>
                <input type="text" name="mobile" class="text-field" id="mobile" disabled required>
            </div>

            <div class="form-row">
                <label class="form-label">Gender:</label>
                <select name="gender" class="combo-box" id="gender" disabled required>
                    <option value="" disabled selected>Select Gender</option>
                    <option value="MALE">Male</option>
                    <option value="FEMALE">Female</option>
                    <option value="OTHER">Other</option>
                </select>
            </div>

            <div class="form-row">
                <label class="form-label">Address:</label>
                <input type="text" name="address" class="text-field" id="address" disabled required>
            </div>

            <button type="submit" class="submit-button">UPDATE</button>
        </form>
    </div>

    <script>
        function fillMemberData() {
            const dropdown = document.getElementById("memberId");
            const selectedOption = dropdown.options[dropdown.selectedIndex];

            if (selectedOption && selectedOption.value !== "") {
                document.getElementById("name").value = selectedOption.dataset.name || "";
                document.getElementById("email").value = selectedOption.dataset.email || "";
                document.getElementById("mobile").value = selectedOption.dataset.mobile || "";
                document.getElementById("gender").value = selectedOption.dataset.gender || "";
                document.getElementById("address").value = selectedOption.dataset.address || "";

                document.getElementById("name").disabled = false;
                document.getElementById("email").disabled = false;
                document.getElementById("mobile").disabled = false;
                document.getElementById("gender").disabled = false;
                document.getElementById("address").disabled = false;
            }
        }
    </script>
</body>
</html>
