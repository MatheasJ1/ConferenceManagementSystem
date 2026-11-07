package com.database;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
public class DatabaseConnection {
	private static final String DATABASE_URL = "jdbc:sqlite:conference.db";
	private DatabaseConnection() {}
	
	public static Connection getConnection() throws SQLException{
		Connection connection = DriverManager.getConnection(DATABASE_URL);
		try {
			try(Statement stmt = connection.createStatement()){
				stmt.execute("PRAGMA foreign_keys = ON");
			}
			return connection;
		} catch(SQLException e) {
			try {
				connection.close();
			} catch (SQLException ce) {
				e.addSuppressed(ce);
			}
			throw e;
		}
	}
}
