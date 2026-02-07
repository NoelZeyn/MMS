package main.java.com.inventory.util;
import java.sql.Connection;
import java.sql.DriverManager;

public class DatabaseUtil {
    private static final String url = "jdbc:mysql://localhost:3306/mms?useSSL=false&serverTimezone=UTC";
    private static final String USER = "root";
    private static final String PASS = "";

    public static Connection getConnection() throws Exception {
        return DriverManager.getConnection(url, USER, PASS);
    }
}
