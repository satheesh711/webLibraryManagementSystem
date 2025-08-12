package com.webLibraryManagementSystem.controllers;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.stream.Collectors;

import com.webLibraryManagementSystem.domain.Book;
import com.webLibraryManagementSystem.domain.Member;
import com.webLibraryManagementSystem.exceptions.BookNotFoundException;
import com.webLibraryManagementSystem.exceptions.DatabaseOperationException;
import com.webLibraryManagementSystem.exceptions.InvalidException;
import com.webLibraryManagementSystem.exceptions.InvalidIssueDataException;
import com.webLibraryManagementSystem.exceptions.InvalidMemberDataException;
import com.webLibraryManagementSystem.services.BookServices;
import com.webLibraryManagementSystem.services.IssueService;
import com.webLibraryManagementSystem.services.MemberService;
import com.webLibraryManagementSystem.services.impl.BookServicesImpl;
import com.webLibraryManagementSystem.services.impl.IssueServiceImpl;
import com.webLibraryManagementSystem.services.impl.MemberServiceImpl;
import com.webLibraryManagementSystem.utilities.BookAvailability;
import com.webLibraryManagementSystem.utilities.BookStatus;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/ReturnBookServlet")
public class ReturnBookServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	private final MemberService memberService = new MemberServiceImpl();
	private final BookServices bookService = new BookServicesImpl();
	private final IssueService issueService = new IssueServiceImpl();

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		loadLists(request);
		RequestDispatcher dispatcher = request.getRequestDispatcher("returnBook.jsp");
		dispatcher.forward(request, response);
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		String memberIdStr = request.getParameter("memberId");
		String bookIdStr = request.getParameter("bookId");
		String returnDateStr = request.getParameter("returnDate");

		Integer memberId = parseId(memberIdStr);
		Integer bookId = parseId(bookIdStr);
		LocalDate returnDate = parseDate(returnDateStr);

		try {

			issueService.returnBook(bookId, memberId, returnDate);
			Book book = bookService.getBookById(bookId);
			request.setAttribute("successMessage", book.getTitle() + " book returned ");
			request.setAttribute("memberSelected", null);
			request.setAttribute("bookSelected", null);
			request.setAttribute("dateSelected", null);
			request.setAttribute("memberId", null);
			request.setAttribute("bookId", null);

			loadLists(request);

		} catch (BookNotFoundException | DatabaseOperationException | InvalidIssueDataException
				| InvalidMemberDataException e) {
			request.setAttribute("errorMessage", e.getMessage());
			request.setAttribute("dateSelected", returnDateStr);
			loadLists(request);
		}

		RequestDispatcher dispatcher = request.getRequestDispatcher("returnBook.jsp");
		dispatcher.forward(request, response);
	}

	private Integer parseId(String idStr) {
		try {
			return Integer.parseInt(idStr);
		} catch (NumberFormatException e) {
			return 0;
		}
	}

	private LocalDate parseDate(String dateStr) {
		try {
			return LocalDate.parse(dateStr);
		} catch (DateTimeParseException e) {
			return null;
		}
	}

	private void loadLists(HttpServletRequest request) {

		try {
			List<Book> books = bookService.getBooks().stream().filter(
					b -> b.getStatus().equals(BookStatus.ACTIVE) && b.getAvailability().equals(BookAvailability.ISSUED))
					.collect(Collectors.toList());

			request.setAttribute("booksList", books);

		} catch (DatabaseOperationException e) {
			request.setAttribute("errorMessage", e.getMessage());
		}

		try {
			List<Integer> issuedMemberIds = bookService.getActiveIssuedBooks().stream().map(i -> i.getMemberId())
					.collect(Collectors.toList());

			List<Member> members = memberService.getMembers().stream()
					.filter(m -> issuedMemberIds.contains(m.getMemberId())).collect(Collectors.toList());

			request.setAttribute("membersList", members);

		} catch (InvalidException | DatabaseOperationException e) {
			request.setAttribute("errorMessage", e.getMessage());
		}
	}
}
