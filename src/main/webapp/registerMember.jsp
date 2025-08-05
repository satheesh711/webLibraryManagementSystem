<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Member Registration</title>
<link rel="stylesheet" type="text/css"
	href="css/member_Registration.css">
<style>
body {
	font-family: Arial, sans-serif;
	background-color: #f2f2f2;
}

.main-pane {
	width: 500px;
	margin: 50px auto;
	background-color: #fff;
	border-radius: 10px;
	box-shadow: 0 0 15px rgba(0, 0, 0, 0.2);
	padding: 30px;
}

.title-label {
	font-size: 22px;
	font-weight: bold;
	text-align: center;
	margin-bottom: 20px;
}

.error-label {
	display: block;
	text-align: center;
	margin-bottom: 15px;
	font-weight: bold;
	color: red;
}

.success-label {
	display: block;
	text-align: center;
	margin-bottom: 15px;
	font-weight: bold;
	color: green;
}

.form-row {
	display: flex;
	align-items: center;
	margin-bottom: 15px;
}

.form-row label {
	flex: 1;
	font-weight: bold;
}

.form-row input, .form-row select {
	flex: 2;
	padding: 6px;
	border: 1px solid #ccc;
	border-radius: 4px;
}

.button-row {
	text-align: center;
	margin-top: 20px;
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

.nav-buttons {
	display: flex;
	justify-content: space-between;
	margin-bottom: 15px;
}

.nav-button {
	background-color: #007bff;
	color: white;
	border: none;
	border-radius: 5px;
	padding: 5px 10px;
	cursor: pointer;
}

.nav-button:hover {
	background-color: #0056b3;
}
</style>
</head>
<body>

	<div class="main-pane">

		
		<div class="title-label">Member Registration</div>

		<c:if test="${not empty errorMessage}">
			<span class="error-label">${errorMessage}</span>
		</c:if>
		<c:if test="${not empty successMessage}">
			<span class="success-label">${successMessage}</span>
		</c:if>

		<form action="RegisterMemberServlet" method="post">

			<div class="form-row">
				<label for="name">Name:</label> <input type="text" name="name"
					id="name" required />
			</div>

			<div class="form-row">
				<label for="email">Email:</label> <input type="email" name="email"
					id="email" required />
			</div>

			<div class="form-row">
				<label for="mobile">Mobile:</label> <input type="text" name="mobile"
					id="mobile" required />
			</div>

			<div class="form-row">
				<label for="gender">Gender:</label> <select name="gender"
					id="gender" required>
					<option value="" disabled selected>Select Gender</option>
					<option value="MALE">Male</option>
					<option value="FEMALE">Female</option>
					<option value="OTHER">Other</option>
				</select>
			</div>

			<div class="form-row">
				<label for="address">Address:</label> <input type="text"
					name="address" id="address" required />
			</div>

			<div class="button-row">
				<button type="submit" class="submit-button">Submit</button>
			</div>
		</form>

	</div>

</body>
</html>
