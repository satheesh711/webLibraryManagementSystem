package com.webLibraryManagementSystem.utilities;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import com.webLibraryManagementSystemexceptions.InvalidException;

public class PreparedStatementManager {

	private PreparedStatementManager() {

	}

	private static Map<String, PreparedStatement> preparedStatementCache = new HashMap<>();

	public static PreparedStatement getPreparedStatement(String query) throws SQLException, InvalidException {

		if (!preparedStatementCache.containsKey(query)) {
			Connection conn;
			conn = ConnectionPoolingServlet.getDataSource().getConnection();
			PreparedStatement stmt = conn.prepareStatement(query);
			preparedStatementCache.put(query, stmt);
		}

		return preparedStatementCache.get(query);
	}

	public static void closeAllStatements() {

		for (PreparedStatement stmt : preparedStatementCache.values()) {
			try {
				stmt.close();
			} catch (SQLException e) {
				System.out.println("Error closing statement: " + e.getMessage());
			}
		}
		preparedStatementCache.clear();

	}
}
