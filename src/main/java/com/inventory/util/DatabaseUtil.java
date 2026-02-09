package com.inventory.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import io.github.cdimascio.dotenv.Dotenv;

public class DatabaseUtil {

    // Load .env file
    private static final Dotenv dotenv = Dotenv.configure()
                                               .ignoreIfMissing() // tetap load meski .env tidak ada, tapi akan error nanti
                                               .load();

    // Ambil variabel dari .env, wajib ada
    private static final String URL = dotenv.get("DB_URL");
    private static final String USER = dotenv.get("DB_USER");
    private static final String PASS = dotenv.get("DB_PASS");

    static {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("MySQL JDBC Driver not found.", e);
        }

        if (URL == null || USER == null || PASS == null) {
            throw new RuntimeException("Database configuration missing in .env file!");
        }
    }

    // Method untuk mendapatkan koneksi
    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASS);
    }
}
