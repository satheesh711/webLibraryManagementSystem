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
		String memberFilter = request.getParameter("memberNameTyped");
		String bookFilter = request.getParameter("bookTyped");
		loadLists(request, memberFilter, bookFilter);
		RequestDispatcher dispatcher = request.getRequestDispatcher("returnBook.jsp");
		dispatcher.forward(request, response);
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		String memberIdStr = request.getParameter("memberId");
		String bookIdStr = request.getParameter("bookId");
		String returnDateStr = request.getParameter("returnDate");

		String memberFilter = request.getParameter("memberNameTyped");
		String bookFilter = request.getParameter("bookTyped");

		Integer memberId = parseId(memberIdStr);
		Integer bookId = parseId(bookIdStr);
		LocalDate returnDate = parseDate(returnDateStr);

		if (memberId == null || bookId == null || returnDate == null) {
			request.setAttribute("errorMessage", "Select valid Book, Member, and Date");
			request.setAttribute("dateSelected", returnDateStr);
			loadLists(request, memberFilter, bookFilter);
			RequestDispatcher dispatcher = request.getRequestDispatcher("returnBook.jsp");
			dispatcher.forward(request, response);
			return;
		}

		try {
			Book book = bookService.getBookById(bookId);
			Member member = memberService.getMemberId(memberId);

			issueService.returnBook(book, memberId, returnDate);

			request.setAttribute("successMessage", book.getTitle() + " book returned by " + member.getName());
			request.setAttribute("memberSelected", null);
			request.setAttribute("bookSelected", null);
			request.setAttribute("dateSelected", null);
			request.setAttribute("memberId", null);
			request.setAttribute("bookId", null);

			loadLists(request, null, null);

		} catch (BookNotFoundException | InvalidException | DatabaseOperationException e) {
			request.setAttribute("errorMessage", e.getMessage());
			request.setAttribute("dateSelected", returnDateStr);
			loadLists(request, memberFilter, bookFilter);
		}

		RequestDispatcher dispatcher = request.getRequestDispatcher("returnBook.jsp");
		dispatcher.forward(request, response);
	}

	private Integer parseId(String idStr) {
		try {
			return Integer.parseInt(idStr);
		} catch (NumberFormatException e) {
			return null;
		}
	}

	private LocalDate parseDate(String dateStr) {
		try {
			return LocalDate.parse(dateStr);
		} catch (DateTimeParseException e) {
			return null;
		}
	}

	private void loadLists(HttpServletRequest request, String memberFilter, String bookFilter) {
		String bookSearch = (bookFilter != null) ? bookFilter.trim().toLowerCase() : "";
		String memberSearch = (memberFilter != null) ? memberFilter.trim().toLowerCase() : "";

		try {
			List<Book> books = bookService.getBooks().stream().filter(
					b -> b.getStatus().equals(BookStatus.ACTIVE) && b.getAvailability().equals(BookAvailability.ISSUED))
					.filter(b -> bookSearch.isEmpty() || b.getTitle().toLowerCase().contains(bookSearch)
							|| b.getAuthor().toLowerCase().contains(bookSearch))
					.collect(Collectors.toList());
			request.setAttribute("booksList", books);
		} catch (DatabaseOperationException e) {
			request.setAttribute("errorMessage", e.getMessage());
		}

		try {
			List<Integer> issuedMemberIds = bookService.getActiveIssuedBooks().stream().map(i -> i.getMemberId())
					.collect(Collectors.toList());

			List<Member> members = memberService.getMembers().stream()
					.filter(m -> issuedMemberIds.contains(m.getMemberId()))
					.filter(m -> memberSearch.isEmpty() || m.getName().toLowerCase().contains(memberSearch)
							|| String.valueOf(m.getMobile()).contains(memberSearch))
					.collect(Collectors.toList());

			request.setAttribute("membersList", members);
		} catch (InvalidException | DatabaseOperationException e) {
			request.setAttribute("errorMessage", e.getMessage());
		}
	}
}
