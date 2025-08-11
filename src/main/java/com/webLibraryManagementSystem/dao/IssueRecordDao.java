package com.webLibraryManagementSystem.dao;

import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDate;

import com.webLibraryManagementSystem.domain.Book;
import com.webLibraryManagementSystem.domain.IssueRecord;
import com.webLibraryManagementSystem.exceptions.InvalidException;

public interface IssueRecordDao {

	void issueBook(IssueRecord newIssue, Book book) throws InvalidException;

	void returnBook(Book book, int id, LocalDate date) throws InvalidException;

	void issueLog(IssueRecord issue, Connection con) throws SQLException;
}
