package com.webLibraryManagementSystem.controllers;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.stream.Collectors;

import com.webLibraryManagementSystem.domain.Book;
import com.webLibraryManagementSystem.domain.IssueRecord;
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
import com.webLibraryManagementSystem.utilities.IssueStatus;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/IssueBookServlet")
public class IssueBookServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	MemberService memberService = new MemberServiceImpl();
	BookServices bookService = new BookServicesImpl();
	IssueService issueService = new IssueServiceImpl();

	public IssueBookServlet() {
		super();
	}

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		try {
			List<Book> books;

			books = bookService.getBooks().stream().filter(
					b -> b.getStatus() == BookStatus.ACTIVE && b.getAvailability() == BookAvailability.AVAILABLE)
					.collect(Collectors.toList());

			request.setAttribute("booksList", books);

			List<Member> members;

			members = memberService.getMembers();

			request.setAttribute("membersList", members);

		} catch (DatabaseOperationException | InvalidException e) {
			request.setAttribute("errorMessage", e.getMessage());
		}

		RequestDispatcher dispatcher = request.getRequestDispatcher("issueBook.jsp");
		dispatcher.forward(request, response);
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		int memberId = parseId(request.getParameter("memberHiden"));
		int bookId = parseId(request.getParameter("bookHiden"));

		LocalDate date = null;
		try {
			date = LocalDate.parse(request.getParameter("issueDate"));
		} catch (DateTimeParseException e) {
			request.setAttribute("errorMessage", "Please enter date in yyyy-mm-dd format");
			doGet(request, response);
			return;
		}

		IssueRecord newIssue = new IssueRecord(-1, bookId, memberId, IssueStatus.ISSUED, date, null);
		try {
			issueService.addIssue(newIssue);
			request.setAttribute("successMessage", "Book issued successfully!");

			request.setAttribute("memberSelected", null);
			request.setAttribute("bookSelected", null);
			request.setAttribute("dateSelected", "");
			request.setAttribute("memberNameTyped", "");
			request.setAttribute("book", "");

			List<Book> books = bookService.getBooks().stream().filter(
					b -> b.getStatus() == BookStatus.ACTIVE && b.getAvailability() == BookAvailability.AVAILABLE)
					.collect(Collectors.toList());
			request.setAttribute("booksList", books);

			request.setAttribute("membersList", memberService.getMembers());

			request.getRequestDispatcher("issueBook.jsp").forward(request, response);
			return;

		} catch (InvalidException | BookNotFoundException | DatabaseOperationException | InvalidIssueDataException
				| InvalidMemberDataException e) {
			request.setAttribute("errorMessage", e.getMessage());
		}

		doGet(request, response);
	}

	private int parseId(String param) {
		try {
			return Integer.parseInt(param);
		} catch (NumberFormatException e) {
			return 0;
		}
	}
}
