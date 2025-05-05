package db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;


public class MyConnection {
    private static final String username = "root";
    private static final String password = "Luongchidung1";
    private static final String dataConn = "jdbc:mysql://localhost:3306/students_management";
    private static Connection con = null;

    private MyConnection() {}

    public static Connection getConnection() {
        if (con == null) {
            initializeConnection();
        }
        return con;
    }

    private static synchronized void initializeConnection() {
        if (con == null) {
            try {
                Class.forName("com.mysql.cj.jdbc.Driver");
                con = DriverManager.getConnection(dataConn, username, password);
            } catch (ClassNotFoundException e) {
                System.err.println("❌ MySQL JDBC Driver not found.");
                e.printStackTrace();
            } catch (SQLException e) {
                System.err.println("❌ Failed to connect to the database.");
                e.printStackTrace();
            }
        }
    }
}