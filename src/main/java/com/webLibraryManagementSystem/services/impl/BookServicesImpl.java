package com.webLibraryManagementSystem.services.impl;

import java.util.List;
import java.util.Map;

import com.webLibraryManagementSystem.dao.BookDao;
import com.webLibraryManagementSystem.dao.impl.BookDaoImpl;
import com.webLibraryManagementSystem.domain.Book;
import com.webLibraryManagementSystem.domain.CustomActiveIssuedBooks;
import com.webLibraryManagementSystem.exceptions.BookNotFoundException;
import com.webLibraryManagementSystem.exceptions.DatabaseOperationException;
import com.webLibraryManagementSystem.exceptions.DuplicateBookException;
import com.webLibraryManagementSystem.exceptions.InvalidBookDataException;
import com.webLibraryManagementSystem.services.BookServices;
import com.webLibraryManagementSystem.utilities.BookAvailability;
import com.webLibraryManagementSystem.utilities.Validations;

public class BookServicesImpl implements BookServices {

	private final BookDao bookDao = new BookDaoImpl();

	@Override
	public void addBook(Book book) throws InvalidBookDataException, DuplicateBookException, DatabaseOperationException {

		validateBookData(book);

		if (bookDao.existsByTitleAndAuthor(book.getTitle(), book.getAuthor())) {
			throw new DuplicateBookException("This book already exists.");
		}
		try {
			bookDao.addBook(book);
		} catch (DatabaseOperationException e) {
			throw new DatabaseOperationException(e.getMessage());
		}

	}

	@Override
	public List<Book> getBooks() throws DatabaseOperationException {

		try {
			return bookDao.getAllBooks();
		} catch (DatabaseOperationException e) {
			throw new DatabaseOperationException(e.getMessage());
		}

	}

	@Override
	public void deleteBook(Book book)
			throws BookNotFoundException, DatabaseOperationException, InvalidBookDataException {
		try {
			if (book.getAvailability().equals(BookAvailability.ISSUED)) {
				throw new InvalidBookDataException("Book Availability is Issued.Please colluct Book");
			}
			bookDao.deleteBook(book);

		} catch (DatabaseOperationException | BookNotFoundException e) {
			throw new DatabaseOperationException(e.getMessage());
		}

	}

	@Override
	public void updateBook(Book book, Book oldBook)
			throws BookNotFoundException, InvalidBookDataException, DatabaseOperationException, DuplicateBookException {

		validateBookData(book);

		if (book.getAvailability() == null) {
			throw new InvalidBookDataException("Please select availability.");
		}

		if (bookDao.getBookById(book.getBookId()) == null) {
			throw new BookNotFoundException("No book found with ID " + book.getBookId());
		}

		if (book.equals(oldBook)) {
			throw new InvalidBookDataException("Please edit at least one field");
		}

		if (bookDao.existsByTitleAndAuthorExceptId(book.getTitle(), book.getAuthor(), book.getBookId())) {
			throw new DuplicateBookException(
					"Book with title '" + book.getTitle() + "' and author '" + book.getAuthor() + "' already exists.");
		}

		try {

			bookDao.updateBook(book, oldBook);

		} catch (DatabaseOperationException | BookNotFoundException e) {
			throw new DatabaseOperationException(e.getMessage());
		}

	}

	@Override
	public void updateBookAvailability(Book book, BookAvailability avail)
			throws BookNotFoundException, DatabaseOperationException {
		try {

			bookDao.updateBookAvailability(book, avail, null);

		} catch (DatabaseOperationException | BookNotFoundException e) {
			throw new DatabaseOperationException(e.getMessage());
		}

	}

	@Override
	public Book getBookById(int bookId) throws DatabaseOperationException, BookNotFoundException {
		try {
			return bookDao.getBookById(bookId);
		} catch (DatabaseOperationException | BookNotFoundException e) {
			throw new DatabaseOperationException(e.getMessage());
		}
	}

	@Override
	public Map<String, Integer> getBookCountByCategory() throws DatabaseOperationException {

		try {
			return bookDao.getBookCountByCategory();
		} catch (DatabaseOperationException e) {
			throw new DatabaseOperationException(e.getMessage());
		}

	}

	@Override
	public List<CustomActiveIssuedBooks> getActiveIssuedBooks() throws DatabaseOperationException {

		try {
			return bookDao.getActiveIssuedBooks();
		} catch (DatabaseOperationException e) {
			throw new DatabaseOperationException(e.getMessage());
		}

	}

	@Override
	public List<CustomActiveIssuedBooks> getOverDueBooks() throws DatabaseOperationException {

		try {
			return bookDao.getOverDueBooks();
		} catch (DatabaseOperationException e) {
			throw new DatabaseOperationException(e.getMessage());
		}

	}

	private void validateBookData(Book book) throws InvalidBookDataException {

		if (!Validations.isValidString(book.getTitle())) {
			throw new InvalidBookDataException(
					"Please use only letters, numbers, and allowed punctuation and minimum 3 letters in the title.");
		}
		if (!Validations.isValidString(book.getAuthor())) {
			throw new InvalidBookDataException(
					"Please use only letters and spaces  and minimum 3 letters for the author's name.");
		}
		if (book.getCategory() == null) {
			throw new InvalidBookDataException("Please select a category.");
		}
	}
}
