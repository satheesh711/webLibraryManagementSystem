package com.webLibraryManagementSystem.controllers;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.webLibraryManagementSystem.domain.Book;
import com.webLibraryManagementSystem.exceptions.BookNotFoundException;
import com.webLibraryManagementSystem.exceptions.DatabaseOperationException;
import com.webLibraryManagementSystem.exceptions.DuplicateBookException;
import com.webLibraryManagementSystem.exceptions.InvalidBookDataException;
import com.webLibraryManagementSystem.services.impl.BookServicesImpl;
import com.webLibraryManagementSystem.utilities.BookAvailability;
import com.webLibraryManagementSystem.utilities.BookCategory;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/updateBook")
public class UpdateBookServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	BookServicesImpl bookService = new BookServicesImpl();
	Book selectedBook = null;

	public UpdateBookServlet() {
		super();
	}

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		String selectedTitle = request.getParameter("book");

		if (selectedTitle != null) {
			try {
				Book book = bookService.getBooks().stream()
						.filter(b -> (b.getTitle() + " - " + b.getAuthor()).equals(selectedTitle))
						.filter(b -> b.getAvailability().equals(BookAvailability.AVAILABLE)).findFirst().orElse(null);
				selectedBook = book;
				if (book != null) {

					setAtributesSelected(request, book);
				}
			} catch (DatabaseOperationException e) {
				e.printStackTrace();
			}
		}

		setAtributes(request);

		RequestDispatcher dispatcher = request.getRequestDispatcher("updateBook.jsp");
		dispatcher.forward(request, response);

	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		String bookTitle = request.getParameter("bookTitle");
		String bookAuthor = request.getParameter("author");
		int bookId = Integer.parseInt(request.getParameter("bookId"));
		BookCategory bookcategory = BookCategory.valueOf(request.getParameter("category"));

		Book newBook = new Book(bookId, bookTitle, bookAuthor, bookcategory);

		try {
			if (selectedBook.equals(newBook)) {
				request.setAttribute("message", "at least one change required");
				request.setAttribute("formReset", false);
				setAtributesSelected(request, newBook);
				setAtributes(request);
			} else {
				bookService.updateBook(newBook, selectedBook);

				request.setAttribute("message", bookTitle + " Book added Successfully");
				request.setAttribute("formReset", true);

				setAtributes(request);
			}

		} catch (BookNotFoundException | InvalidBookDataException | DatabaseOperationException
				| DuplicateBookException e) {
			request.setAttribute("message", e.getMessage());

			request.setAttribute("formReset", false);
			setAtributesSelected(request, newBook);

			setAtributes(request);
		}

		RequestDispatcher dispatcher = request.getRequestDispatcher("updateBook.jsp");
		dispatcher.forward(request, response);

	}

	public void setAtributes(HttpServletRequest request) {
		try {
			List<Book> books = new ArrayList<>(bookService.getBooks().stream()
					.filter(b -> b.getAvailability().equals(BookAvailability.AVAILABLE)).toList());

			List<BookCategory> categories = new ArrayList<>(Arrays.asList(BookCategory.values()));

			request.setAttribute("books", books);
			request.setAttribute("categories", categories);

		} catch (DatabaseOperationException e) {
			e.printStackTrace();
		}
	}

	public void setAtributesSelected(HttpServletRequest request, Book book) {
		request.setAttribute("book", (selectedBook.getTitle() + " - " + selectedBook.getAuthor()));
		request.setAttribute("bookId", book.getBookId());
		request.setAttribute("bookTitle", book.getTitle());
		request.setAttribute("author", book.getAuthor());
		request.setAttribute("category", book.getCategory());

	}
}
