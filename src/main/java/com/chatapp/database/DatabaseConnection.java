package com.chatapp.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import io.github.cdimascio.dotenv.Dotenv;
import org.slf4j.*;

public class DatabaseConnection {
        private static final Logger _logger = LoggerFactory.getLogger(DatabaseConnection.class);
        private static final Dotenv dotenv = Dotenv.load();
        private static final String DB_URL = "jdbc:postgresql://localhost:5432/chatappdb";
        private static final String User = dotenv.get("DB_USER");
        private static final String Password = dotenv.get("DB_PASSWORD"); //c_S99I~87

        public static Connection connect() throws SQLException{
            return DriverManager.getConnection(DB_URL, User, Password);
        }

        public void disconnect() throws SQLException{
            try(Connection conn = DriverManager.getConnection(DB_URL, User, Password)){
                if(conn != null){
                    conn.close();
                    _logger.info("Database connection closed");
                }
            }catch (SQLException e){
                throw new RuntimeException("Failed to close database connection", e);
            }
        }
}
