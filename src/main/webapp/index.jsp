<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Library Dashboard</title>
<link rel="stylesheet" href="css/dashboard.css">
</head>
<body>

	<div class="sidebar">
		<h2>📚 Library Dashboard</h2>

		<div class="menu-item">
			<div class="menu-title" onclick="toggleSubMenu('bookMenu', this)">
				<span class="arrow">▶</span> 📖 Book
			</div>
			<div class="submenu" id="bookMenu">
				<a href="addBook.jsp" target="contentFrame">➕ Add Book</a> <a
					href="updateBook.jsp" target="contentFrame">✏️ Update Book</a> <a
					href="updateAvailability.jsp" target="contentFrame">📦 Update
					Availability</a> 
				<a href="viewBooks.jsp" target="contentFrame">📘
					View All Books</a>
			</div>
		</div>

		<div class="menu-item">
			<div class="menu-title" onclick="toggleSubMenu('memberMenu', this)">
				<span class="arrow">▶</span> 👤 Member
			</div>
			<div class="submenu" id="memberMenu">
				<a href="registerMember.jsp" target="contentFrame">📝 Register
					Member</a> <a href="updateMember.jsp" target="contentFrame">✏️
					Update Member</a> <a href="viewMembers.jsp" target="contentFrame">👥
					View All Members</a>
			</div>
		</div>

		<div class="menu-item">
			<div class="menu-title"
				onclick="toggleSubMenu('issueReturnMenu', this)">
				<span class="arrow">▶</span> 🔁 Issue & Return
			</div>
			<div class="submenu" id="issueReturnMenu">
				<a href="issueBook.jsp" target="contentFrame">📤 Issue Book</a> <a
					href="returnBook.jsp" target="contentFrame">📥 Return Book</a>
			</div>
		</div>

		<div class="menu-item">
			<div class="menu-title" onclick="toggleSubMenu('reportsMenu', this)">
				<span class="arrow">▶</span> 📊 Reports
			</div>
			<div class="submenu" id="reportsMenu">
				<a href="booksByCategory.jsp" target="contentFrame">📚 Books by
					Category</a> <a href="activeMembers.jsp" target="contentFrame">✅
					Active Members</a> <a href="overdueBooks.jsp" target="contentFrame">⏰
					Overdue Books</a>
			</div>
		</div>
	</div>

	<div class="content">
		<iframe name="contentFrame"></iframe>
	</div>

	<script src="scripts/dashboard.js">
		
	</script>

</body>
</html>
