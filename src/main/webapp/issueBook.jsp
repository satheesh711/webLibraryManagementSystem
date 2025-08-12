<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="jakarta.tags.core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Issue Book</title>
    <link rel="stylesheet" type="text/css" href="css/addBook.css">
    <script>
        function setHiddenId(inputId, listId, hiddenId) {
            const input = document.getElementById(inputId);
            const list = document.getElementById(listId);
            const hidden = document.getElementById(hiddenId);

            const selectedOption = Array.from(list.options)
                .find(option => option.value === input.value);

            hidden.value = selectedOption ? selectedOption.dataset.id : "";
        }
    </script>
</head>
<body>
<div class="form-container">

    <h1>Issue Book</h1>

    <label class="error-label">
        <c:out value="${errorMessage != null ? errorMessage : ''}" />
    </label>
    <label class="success-label">
        <c:out value="${successMessage != null ? successMessage : ''}" />
    </label>

    <form action="IssueBookServlet" method="post">
        <label class="input-label">Select Member</label>
        <c:choose>
            <c:when test="${memberSelected != null}">
                <input list="members" id="memberInput" name="memberNameTyped"
                       placeholder="Search member by name"
                       onchange="setHiddenId('memberInput','members','memberHiden')" autocomplete="off"
                       value="<c:out value='${memberSelected.name}'/> (<c:out value='${memberSelected.mobile}'/>)"
                       required />
            </c:when>
            <c:otherwise>
                <input list="members" id="memberInput" name="memberNameTyped"
                       placeholder="Search member by name"
                       onchange="setHiddenId('memberInput','members','memberHiden')"
                       value="" autocomplete="off"
                       required />
            </c:otherwise>
        </c:choose>

        <datalist id="members">
            <c:forEach var="memberObj" items="${membersList}">
                <option value="${memberObj.name} (${memberObj.mobile})" data-id="${memberObj.memberId}"></option>
            </c:forEach>
        </datalist>
        <input type="hidden" id="memberHiden" name="memberHiden"
               value="${memberSelected != null ? memberSelected.memberId : ''}" />


        <label class="input-label">Select Book</label>
        <c:choose>
            <c:when test="${bookSelected != null}">
                <input list="books" id="bookInput" name="book"
                       placeholder="Search by title or author"
                       onchange="setHiddenId('bookInput','books','bookHiden')" autocomplete="off"
                       value="<c:out value='${bookSelected.title}'/> - <c:out value='${bookSelected.author}'/>"
                       required />
            </c:when>
            <c:otherwise>
                <input list="books" id="bookInput" name="book"
                       placeholder="Search by title or author"
                       onchange="setHiddenId('bookInput','books','bookHiden')"
                       value="" autocomplete="off"
                       required />
            </c:otherwise>
        </c:choose>

        <datalist id="books">
            <c:forEach var="bookObj" items="${booksList}">
                <option value="${bookObj.title} - ${bookObj.author}" data-id="${bookObj.bookId}"></option>
            </c:forEach>
        </datalist>
        <input type="hidden" id="bookHiden" name="bookHiden"
               value="${bookSelected != null ? bookSelected.bookId : ''}" />


        <label>Issue Date</label>
        <input type="date" name="issueDate" required value="${dateSelected}" />

        <button type="submit" class="primary-button">ISSUE</button>
    </form>
</div>
</body>
</html>
