package com.chatapp.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Logger;

public class DatabaseInitializer {
    private static final Logger _logger = Logger.getLogger(DatabaseInitializer.class.getName());

    public static void initializeDB() {
        try (Connection conn = DatabaseConnection.connect()) {
            if (conn != null) {
                String createUsersTable = "CREATE TABLE IF NOT EXISTS users ("
                        + "id SERIAL PRIMARY KEY, "
                        + "username VARCHAR(50) NOT NULL UNIQUE, "
                        + "email VARCHAR(254) NOT NULL UNIQUE, "
                        + "password VARCHAR(255) NOT NULL, "
                        + "created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP"
                        + ")";

                String createFriendRequestsTable = "CREATE TABLE IF NOT EXISTS friend_requests ("
                        + "id SERIAL PRIMARY KEY, "
                        + "sender_id INT NOT NULL REFERENCES users(id) ON DELETE CASCADE, "
                        + "receiver_id INT NOT NULL REFERENCES users(id) ON DELETE CASCADE, "
                        + "status VARCHAR(20) NOT NULL DEFAULT 'pending', "
                        + "created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP, "
                        + "UNIQUE(sender_id, receiver_id)"
                        + ")";

                String createFriendsTable = "CREATE TABLE IF NOT EXISTS friends ("
                        + "id SERIAL PRIMARY KEY, "
                        + "user1_id INT NOT NULL REFERENCES users(id) ON DELETE CASCADE, "
                        + "user2_id INT NOT NULL REFERENCES users(id) ON DELETE CASCADE, "
                        + "created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP, "
                        + "UNIQUE(user1_id, user2_id)"
                        + ")";

                try (Statement stmt = conn.createStatement()) {
                    stmt.executeUpdate(createUsersTable);
                    stmt.executeUpdate(createFriendRequestsTable);
                    stmt.executeUpdate(createFriendsTable);
                }

                _logger.info("Database initialized successfully.");
            }
        } catch (SQLException e) {
            throw new RuntimeException("Failed to initialize database.", e);
        }
    }
}
