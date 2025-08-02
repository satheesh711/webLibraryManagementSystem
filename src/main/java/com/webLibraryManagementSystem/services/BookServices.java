package com.webLibraryManagementSystem.services;

import java.util.List;

import com.webLibraryManagementSystem.domain.Book;
import com.webLibraryManagementSystem.utilities.BookAvailability;
import com.webLibraryManagementSystemexceptions.InvalidException;

public interface BookServices {

	void addBook(Book book) throws InvalidException;

	List<Book> getBooks() throws InvalidException;

	void deleteBook(Book book) throws InvalidException;

	void updateBook(Book newBook, Book oldBook) throws InvalidException;

	void updateBookAvailability(Book book, BookAvailability avail) throws InvalidException;

//	List<CustomCategoryCount> getBookCountByCategory() throws InvalidException;
//
//	List<CustomActiveIssuedBooks> getActiveIssuedBooks() throws InvalidException;
//
//	List<CustomOverDueBooks> getOverDueBooks() throws InvalidException;

}
