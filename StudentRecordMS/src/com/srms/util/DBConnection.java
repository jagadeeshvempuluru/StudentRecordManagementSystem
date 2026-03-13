package com.srms.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Singleton Database Connection Utility
 * Manages JDBC connection to MySQL database.
 */
public class DBConnection {

    // ── Change these to match your MySQL setup ──────────────────────────────
    private static final String URL      = "jdbc:mysql://localhost:3306/student_record_db"
                                         + "?useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true";
    private static final String USERNAME = "root";
    private static final String PASSWORD = "your_password";   // ← change this
    // ────────────────────────────────────────────────────────────────────────

    private static Connection connection = null;

    // Private constructor — prevents instantiation (Singleton)
    private DBConnection() {}

    /**
     * Returns a single shared Connection instance.
     * If the connection is null or closed, a new one is created.
     */
    public static Connection getConnection() throws SQLException {
        if (connection == null || connection.isClosed()) {
            try {
                Class.forName("com.mysql.cj.jdbc.Driver");
                connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);
            } catch (ClassNotFoundException e) {
                throw new SQLException("MySQL JDBC Driver not found. "
                    + "Add mysql-connector-java to your classpath.\n" + e.getMessage());
            }
        }
        return connection;
    }

    /**
     * Closes the database connection gracefully.
     */
    public static void closeConnection() {
        if (connection != null) {
            try {
                if (!connection.isClosed()) {
                    connection.close();
                    System.out.println("  Database connection closed.");
                }
            } catch (SQLException e) {
                System.err.println("  Error closing connection: " + e.getMessage());
            }
        }
    }

    /**
     * Tests whether the DB connection is alive.
     */
    public static boolean testConnection() {
        try {
            Connection conn = getConnection();
            return conn != null && !conn.isClosed();
        } catch (SQLException e) {
            return false;
        }
    }
}
