package com.chatapp.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Logger;

public class DatabaseInitializer {
    private static final Logger _logger = Logger.getLogger(DatabaseInitializer.class.getName());
    public static void initializeDB() {
        try(Connection conn = DatabaseConnection.connect()){
            if(conn != null){
                String createUsersTable = "CREATE TABLE IF NOT EXISTS users ("
                        + "id INTEGER PRIMARY KEY AUTO_INCREMENT, "
                        + "username VARCHAR(50) NOT NULL UNIQUE, "
                        + "email VARCHAR(254) NOT NULL UNIQUE, "
                        + "password VARCHAR(255) NOT NULL, "
                        + "created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP"
                        + ")";


                try(Statement stmt = conn.createStatement()){
                    stmt.executeUpdate(createUsersTable);

                }
                _logger.info("Database initialized successfully.");
            }
        }catch (SQLException e){
            throw new RuntimeException("Failed to initialize database.", e);
        }
    }
}
