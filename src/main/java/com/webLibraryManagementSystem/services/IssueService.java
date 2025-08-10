package com.webLibraryManagementSystem.services;

import java.time.LocalDate;

import com.webLibraryManagementSystem.domain.Book;
import com.webLibraryManagementSystem.domain.IssueRecord;
import com.webLibraryManagementSystem.exceptions.BookNotFoundException;
import com.webLibraryManagementSystem.exceptions.DatabaseOperationException;
import com.webLibraryManagementSystem.exceptions.InvalidException;

public interface IssueService {

	void addIssue(IssueRecord newIssue) throws InvalidException, BookNotFoundException, DatabaseOperationException;

	void returnBook(Book book, int id, LocalDate date) throws InvalidException;
}
