package com.webLibraryManagementSystem.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.webLibraryManagementSystem.dao.BookDao;
import com.webLibraryManagementSystem.domain.Book;
import com.webLibraryManagementSystem.domain.CustomActiveIssuedBooks;
import com.webLibraryManagementSystem.exceptions.BookNotFoundException;
import com.webLibraryManagementSystem.exceptions.DatabaseOperationException;
import com.webLibraryManagementSystem.exceptions.InvalidEnumValueException;
import com.webLibraryManagementSystem.utilities.BookAvailability;
import com.webLibraryManagementSystem.utilities.BookCategory;
import com.webLibraryManagementSystem.utilities.BookStatus;
import com.webLibraryManagementSystem.utilities.ConnectionPoolingServlet;
import com.webLibraryManagementSystem.utilities.SQLQueries;

public class BookDaoImpl implements BookDao {

	@Override
	public void addBook(Book book) throws DatabaseOperationException {

		try (Connection con = ConnectionPoolingServlet.getDataSource().getConnection();
				PreparedStatement stmt = con.prepareStatement(SQLQueries.BOOK_INSERT);) {

			stmt.setString(1, book.getTitle());
			stmt.setString(2, book.getAuthor());
			stmt.setString(3, book.getCategory().getCategory());
			stmt.setString(4, book.getStatus().getDbName());
			stmt.setString(5, book.getAvailability().getDbName());

			int rows = stmt.executeUpdate();

			if (rows <= 0) {
				throw new DatabaseOperationException("Book could not be added to the database.");
			}

		} catch (SQLException e) {
			throw new DatabaseOperationException("Error while adding book to the database.", e);
		}

	}

	@Override
	public boolean existsByTitleAndAuthor(String title, String author) throws DatabaseOperationException {

		try (Connection con = ConnectionPoolingServlet.getDataSource().getConnection();
				PreparedStatement stmt = con.prepareStatement(SQLQueries.BOOK_SELECT_BY_TITLE_AUTHOR);) {

			stmt.setString(1, title.trim().toLowerCase());
			stmt.setString(2, author.trim().toLowerCase());

			ResultSet rs = stmt.executeQuery();

			if (rs.next()) {

				return true;
			}

			return false;

		} catch (SQLException e) {

			throw new DatabaseOperationException("Error checking book existence: " + e.getMessage(), e);
		}

	}

	@Override
	public List<Book> getAllBooks() throws DatabaseOperationException {
		List<Book> books = new ArrayList<>();

		try (Connection con = ConnectionPoolingServlet.getDataSource().getConnection();
				PreparedStatement stmt = con.prepareStatement(SQLQueries.BOOK_SELECT_ALL);) {

			ResultSet rs = stmt.executeQuery();

			while (rs.next()) {

				Book book = extractBookFromResultSet(rs);
				books.add(book);
			}

		} catch (SQLException e) {
			throw new DatabaseOperationException("Error retrieving all books: " + e.getMessage(), e);
		}

		return books;
	}

	@Override
	public Book getBookById(int id) throws BookNotFoundException, DatabaseOperationException {

		try (Connection con = ConnectionPoolingServlet.getDataSource().getConnection();
				PreparedStatement stmt = con.prepareStatement(SQLQueries.BOOK_SELECT_BY_ID);) {

			stmt.setInt(1, id);

			ResultSet rs = stmt.executeQuery();

			if (rs.next()) {

				return extractBookFromResultSet(rs);

			} else {
				throw new BookNotFoundException("No book found with ID " + id);
			}

		} catch (SQLException e) {
			throw new DatabaseOperationException("Invalid enum value in DB: " + e.getMessage(), e);
		}

	}

	@Override
	public boolean existsByTitleAndAuthorExceptId(String title, String author, int excludeId)
			throws DatabaseOperationException {
		try (Connection con = ConnectionPoolingServlet.getDataSource().getConnection();
				PreparedStatement stmt = con.prepareStatement(SQLQueries.BOOK_EXISTS_BY_TITLE_AUTHOR_EXCEPT_ID);) {

			stmt.setString(1, title);
			stmt.setString(2, author);
			stmt.setInt(3, excludeId);

			try (ResultSet rs = stmt.executeQuery()) {
				if (rs.next()) {
					return rs.getInt(1) > 0;
				}
			}
		} catch (SQLException e) {
			throw new DatabaseOperationException("Error checking for duplicate book (excluding ID): " + e.getMessage(),
					e);
		}
		return false;
	}

	@Override
	public void updateBook(Book book, Book oldBook) throws BookNotFoundException, DatabaseOperationException {
		Connection con = null;
		PreparedStatement stmt = null;
		PreparedStatement stmt1 = null;
		try {
			con = ConnectionPoolingServlet.getDataSource().getConnection();
			stmt = con.prepareStatement(SQLQueries.BOOK_UPDATE);
			stmt1 = con.prepareStatement(SQLQueries.BOOKS_LOG_INSERT);

			stmt.setString(1, book.getTitle());
			stmt.setString(2, book.getAuthor());
			stmt.setString(3, book.getCategory().getCategory());
			stmt.setString(4, book.getStatus().getDbName());
			stmt.setString(5, book.getAvailability().getDbName());
			stmt.setInt(6, book.getBookId());

			con.setAutoCommit(false);
			int rowsUpdated = stmt.executeUpdate();

			if (rowsUpdated <= 0) {
				con.rollback();
				throw new BookNotFoundException("No book found with the given ID. Update failed.");
			}
			bookLog(oldBook, con, stmt1);

			con.commit();
			con.setAutoCommit(true);

		} catch (SQLException e) {
			try {
				con.rollback();
				con.setAutoCommit(true);
			} catch (SQLException rollbackEx) {
				System.err.println("Rollback failed: " + rollbackEx.getMessage());
			}
			throw new DatabaseOperationException("Failed to update book: " + e.getMessage(), e);
		} finally {
			try {
				con.close();
				stmt.close();
				stmt1.close();
				con.setAutoCommit(true);
			} catch (SQLException e) {
				System.err.println("Failed to reset auto-commit: " + e.getMessage());
			}
		}
	}

	@Override
	public void updateBookAvailability(Book book, BookAvailability availability)
			throws BookNotFoundException, DatabaseOperationException {
		Connection con = null;
		PreparedStatement stmt = null;
		PreparedStatement stmt1 = null;
		try {
			con = ConnectionPoolingServlet.getDataSource().getConnection();
			stmt = con.prepareStatement(SQLQueries.BOOK_UPDATE_AVAILABILITY);
			stmt1 = con.prepareStatement(SQLQueries.BOOKS_LOG_INSERT);

			stmt.setString(1, availability.getDbName());
			stmt.setInt(2, book.getBookId());

			con.setAutoCommit(false);
			int rowsUpdated = stmt.executeUpdate();

			if (rowsUpdated <= 0) {
				con.rollback();
				throw new BookNotFoundException(
						"Book with ID " + book.getBookId() + " not found. Availability not updated.");
			}

			bookLog(book, con, stmt1);
			con.commit();
			con.setAutoCommit(true);

		} catch (SQLException e) {
			try {
				con.rollback();
				con.setAutoCommit(true);
			} catch (SQLException e1) {
				throw new DatabaseOperationException("Error updating book availability: " + e.getMessage(), e);
			}
			throw new DatabaseOperationException("Error updating book availability: " + e.getMessage(), e);
		} finally {
			try {
				con.close();
				stmt.close();
				stmt1.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

	@Override
	public void deleteBook(Book book) throws BookNotFoundException, DatabaseOperationException {
		Connection con = null;
		PreparedStatement stmt = null;
		PreparedStatement stmt1 = null;

		try {
			con = ConnectionPoolingServlet.getDataSource().getConnection();
			stmt = con.prepareStatement(SQLQueries.BOOK_DELETE);
			stmt1 = con.prepareStatement(SQLQueries.BOOKS_LOG_INSERT);

			stmt.setInt(1, book.getBookId());
			con.setAutoCommit(false);
			int rowsDeleted = stmt.executeUpdate();

			if (rowsDeleted <= 0) {
				con.rollback();
				throw new BookNotFoundException("No Book found with Title: " + book.getTitle());
			}

			bookLog(book, con, stmt1);
			con.commit();
			con.setAutoCommit(true);
		} catch (SQLException e) {
			try {
				con.rollback();
				con.setAutoCommit(true);
			} catch (SQLException e1) {
				throw new DatabaseOperationException("Failed to update book: " + e.getMessage(), e);
			}
			throw new DatabaseOperationException("Failed to update book: " + e.getMessage(), e);
		} finally {
			try {
				con.close();
				stmt.close();
				stmt1.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

	@Override
	public void bookLog(Book book, Connection con, PreparedStatement stmt) throws SQLException {

		try {

			stmt.setInt(1, book.getBookId());
			stmt.setString(2, book.getTitle());
			stmt.setString(3, book.getAuthor());
			stmt.setString(4, book.getCategory().getCategory());
			stmt.setString(5, book.getStatus().getDbName());
			stmt.setString(6, book.getAvailability().getDbName());
			int rowsInserted = stmt.executeUpdate();

			if (rowsInserted <= 0) {
				throw new SQLException("Failed to insert book log.");
			}

		} catch (SQLException e) {
			throw new SQLException("Failed to insert book log.");
		}

	}

	@Override
	public Map<String, Integer> getBookCountByCategory() throws DatabaseOperationException {

		Map<String, Integer> books = new HashMap<>();

		try (Connection con = ConnectionPoolingServlet.getDataSource().getConnection();
				PreparedStatement ps = con.prepareStatement(SQLQueries.GET_BOOK_BY_CATEGORY);) {

			ResultSet rs = ps.executeQuery();

			while (rs.next()) {
				String category = rs.getString("category");
				int count = rs.getInt("book_count");
				books.put(category, count);

			}

		} catch (SQLException e) {

			throw new DatabaseOperationException("Error fetching book count by category: " + e.getMessage(), e);
		}
		return books;

	}

	@Override
	public List<CustomActiveIssuedBooks> getActiveIssuedBooks() throws DatabaseOperationException {
		List<CustomActiveIssuedBooks> activeBooks = new ArrayList<>();
		try (Connection con = ConnectionPoolingServlet.getDataSource().getConnection();
				PreparedStatement ps = con.prepareStatement(SQLQueries.GET_ACTIVE_ISSUED_BOOKS)) {

			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				int memberId = rs.getInt("member_id");
				String memberName = rs.getString("member_name");
				int bookId = rs.getInt("book_id");
				String bookTitle = rs.getString("book_title");
				LocalDate issuedDate = rs.getDate("issue_date").toLocalDate();

				CustomActiveIssuedBooks customActiveIssuedBooks = new CustomActiveIssuedBooks(memberId, memberName,
						bookId, bookTitle, issuedDate);

				activeBooks.add(customActiveIssuedBooks);

			}
		} catch (SQLException e) {
			throw new DatabaseOperationException("Error fetching book count by category: " + e.getMessage(), e);
		}
		return activeBooks;
	}

	@Override
	public List<CustomActiveIssuedBooks> getOverDueBooks() throws DatabaseOperationException {
		List<CustomActiveIssuedBooks> overDueBooks = new ArrayList<>();
		try (Connection con = ConnectionPoolingServlet.getDataSource().getConnection();
				PreparedStatement ps = con.prepareStatement(SQLQueries.GET_OVER_DUE_BOOKS)) {
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				int bookId = rs.getInt("book_id");
				String bookName = rs.getString("book_name");
				int memberId = rs.getInt("member_id");
				String memberName = rs.getString("member_name");
				LocalDate issuedDate = rs.getDate("issue_date").toLocalDate();

				CustomActiveIssuedBooks customOverDueBooks = new CustomActiveIssuedBooks(memberId, memberName, bookId,
						bookName, issuedDate);

				overDueBooks.add(customOverDueBooks);
			}
		} catch (SQLException e) {
			throw new DatabaseOperationException("Error fetching book count by category: " + e.getMessage(), e);
		}
		return overDueBooks;
	}

	private Book extractBookFromResultSet(ResultSet rs) throws SQLException, InvalidEnumValueException {
		int bookId = rs.getInt("book_id");
		String title = rs.getString("title");
		String author = rs.getString("author");
		BookCategory category = BookCategory.fromDisplayName(rs.getString("category"));
		BookStatus status = BookStatus.fromDbName(rs.getString("status"));
		BookAvailability availability = BookAvailability.fromDbName(rs.getString("availability"));

		return new Book(bookId, title, author, category, status, availability);
	}

}
