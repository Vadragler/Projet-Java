package org.example.Connection;

import java.sql.*;

public class ConnectionJDBC {
    // JDBC driver name and database URL
    static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";
    static final String DB_URL = "jdbc:mysql://localhost:3306/mabd";

    // Database credentials
    static final String USER = "root";
    static final String PASS = "root";
    public static void conncetionJDBC() {
    Connection conn = null;
    Statement stmt = null;

    try {
        // Register JDBC driver
        Class.forName("com.mysql.jdbc.Driver");

        // Open a connection
        System.out.println("Connecting to database...");
        conn = DriverManager.getConnection(DB_URL, USER, PASS);

        // Execute a query
        System.out.println("Creating statement...");
        stmt = conn.createStatement();
        String sql;
        sql = "SELECT id, title, author FROM books";
        ResultSet rs = stmt.executeQuery(sql);

        // Extract data from result set
        while (rs.next()) {
            // Retrieve by column name
            int id = rs.getInt("id");
            String title = rs.getString("title");
            String author = rs.getString("author");

            // Display values
            System.out.print("ID: " + id);
            System.out.print(", Title: " + title);
            System.out.println(", Author: " + author);
        }
        // Clean-up environment
        rs.close();
        stmt.close();
        conn.close();
    } catch (SQLException se) {
        // Handle errors for JDBC
        se.printStackTrace();
    } catch (Exception e) {
        // Handle errors for Class.forName
        e.printStackTrace();
    } finally {
        // finally block used to close resources
        try {
            if (stmt != null)
                stmt.close();
        } catch (SQLException se2) {
        } // nothing we can do
        try {
            if (conn != null)
                conn.close();
        } catch (SQLException se) {
            se.printStackTrace();
        } // end finally try
    } // end try
        System.out.println("Goodbye!");
}
}
