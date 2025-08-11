package com.webLibraryManagementSystem.controllers;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.webLibraryManagementSystem.domain.Book;
import com.webLibraryManagementSystem.exceptions.DatabaseOperationException;
import com.webLibraryManagementSystem.exceptions.DuplicateBookException;
import com.webLibraryManagementSystem.exceptions.InvalidBookDataException;
import com.webLibraryManagementSystem.services.impl.BookServicesImpl;
import com.webLibraryManagementSystem.utilities.BookCategory;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/addBook")
public class AddBookServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	BookServicesImpl bookService = new BookServicesImpl();

	public AddBookServlet() {
		super();
	}

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		List<BookCategory> categories = new ArrayList<>(Arrays.asList(BookCategory.values()));

		request.setAttribute("categories", categories);

		RequestDispatcher dispatcher = request.getRequestDispatcher("addBook.jsp");
		dispatcher.forward(request, response);
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		String bookTitle = request.getParameter("title");
		String bookAuthor = request.getParameter("author");
		BookCategory bookcategory = BookCategory.valueOf(request.getParameter("category"));

		Book newBook = new Book(bookTitle, bookAuthor, bookcategory);

		try {
			bookService.addBook(newBook);

			request.setAttribute("message", bookTitle + " Book added Successfully");
			request.setAttribute("formReset", true);

			request.setAttribute("title", "");
			request.setAttribute("author", "");
			request.setAttribute("category", "");
			request.setAttribute("status", "");
			request.setAttribute("availability", "");

		} catch (InvalidBookDataException | DuplicateBookException | DatabaseOperationException e) {
			request.setAttribute("message", e.getMessage());
			request.setAttribute("formReset", false);

			request.setAttribute("title", bookTitle);
			request.setAttribute("author", bookAuthor);
			request.setAttribute("category", bookcategory);

		}

		doGet(request, response);
	}
}
