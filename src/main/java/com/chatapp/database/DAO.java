package com.chatapp.database;

import com.chatapp.utils.HashPassword;
import java.sql.*;
import java.util.regex.Pattern;

public class DAO {

    // enumeration to store Error types.
    public enum InsertResult {
        SUCCESS("Success"),
        FAILED("Failed"),
        USERNAME_TAKEN("Username is already taken."),
        Database_Error("Database error during the operation."),
        Email_Taken("Email is already taken."),
        Email_Not_Valid("Email is not valid."),
        Password_Not_Valid("Weak password."),
        Incorrect_Password("Incorrect password, please try again."),
        User_Not_Found("User not found, enter correct credentials."),
        User_does_not_Exists("User does not exists, Please enter correct Username."),
        Friend_Request_SUCCESS("Friend request sent successfully."),
        Friend_Request_ACCEPTED("Friend request accepted."),
        Friend_Request_REJECTED("Friend request rejected."),
        Friend_Request_DELETED("Friend request deleted."),
        Friend_Request_FAILED("Failed to send friend request, try again."),
        Friend_Deleted_Success(" deleted successfuly."),
        Friend_Deleted_Failed("Failed to delete : "),
        Message_Invalid("Message can't be empty"),
        Message_Length_Invalid("Message lenght should be > 1 and < 30");

        private final String message;
        InsertResult(String message){
            this.message = message;
        }
        public String getMessage() {
            return message;
        }
    }


    public enum friendRequestState {
        Accept_Friend,
        Reject_Friend,
        Cancel;
    }

    public static InsertResult isUsernameTaken(String username) {
        try (Connection conn = DatabaseConnection.connect()) {
            String query = "SELECT COUNT(*) FROM users WHERE username = ?";
            try (PreparedStatement stmt = conn.prepareStatement(query)) {
                stmt.setString(1, username);
                try (ResultSet rs = stmt.executeQuery()) {
                    return rs.next() && rs.getInt(1) > 0
                            ? InsertResult.USERNAME_TAKEN
                            : InsertResult.SUCCESS;
                }
            }
        } catch (SQLException e) {
            System.err.println("Error in isUsernameTaken(): " + e.getMessage());
            return InsertResult.Database_Error;
        }
    }

    public static InsertResult isEmailTaken(String email) {
        try (Connection conn = DatabaseConnection.connect()){
            String query = "SELECT COUNT(*) FROM users WHERE email = ?";
            try(PreparedStatement stmt = conn.prepareStatement(query)) {
                stmt.setString(1, email);
                try(ResultSet rs = stmt.executeQuery()){
                    return rs.next() && rs.getInt(1) > 0
                            ? InsertResult.Email_Taken
                            : InsertResult.SUCCESS;
                }
            }
        }catch(SQLException e) {
            System.err.println("Error in isEmailTaken: " + e.getMessage());
            return InsertResult.Database_Error;
        }
    }

    public static InsertResult isEmailValid(String email) {
        Pattern EMAIL_PATTERN = Pattern.compile(
                "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@" +
                        "(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$"
        );

        if(email == null || email.trim().isEmpty()) {
            return InsertResult.Email_Not_Valid;
        }

        if(email.length() > 254) {
            return InsertResult.Email_Not_Valid;
        }

        if(!EMAIL_PATTERN.matcher(email).matches()) {
            return InsertResult.Email_Not_Valid;
        }

        return InsertResult.SUCCESS;
    }

    public static int getUserIdByUsername(String username) {
        try (Connection conn = DatabaseConnection.connect()){
            String query = "SELECT id FROM users WHERE username = ?";
            try(PreparedStatement stmt = conn.prepareStatement(query)){
                stmt.setString(1, username);
                try(ResultSet rs = stmt.executeQuery()){
                    if(rs.next()) {
                        return rs.getInt("id");
                    }else {
                        return -1;
                    }
                }
            }
        }catch(SQLException e) {
            System.err.println("Database error while connecting the User. "+ e.getMessage());
            return -1;
        }
    }

    public static String getUserEmailByUsername(String username) {
        try (Connection conn = DatabaseConnection.connect()){
            String query = "SELECT email FROM users WHERE username = ?";
            try(PreparedStatement stmt = conn.prepareStatement(query)){
                stmt.setString(1, username);
                try(ResultSet rs = stmt.executeQuery()){
                    if(rs.next()) {
                        return rs.getString("email");
                    }else {
                        return null;
                    }
                }
            }
        }catch(SQLException e) {
            System.err.println("Database error while connecting the User. "+ e.getMessage());
            return null;
        }
    }

    public static InsertResult registerUser(String username, String email, String password) {
        try(Connection conn = DatabaseConnection.connect()){
            if(isUsernameTaken(username) != InsertResult.SUCCESS)
                return InsertResult.USERNAME_TAKEN;

            if(isEmailValid(email) != InsertResult.SUCCESS) {
                return InsertResult.Email_Not_Valid;
            }

            if(isEmailTaken(email) != InsertResult.SUCCESS) {
                return InsertResult.Email_Taken;
            }

            if(password.length() < 5) {
                return InsertResult.Password_Not_Valid;
            }


            String HashedPassword = HashPassword.hashPassword(password);
            String query = "INSERT INTO users(username, email, password) VALUES(?, ?, ?)";
            try(PreparedStatement stmt = conn.prepareStatement(query)){
                stmt.setString(1, username.trim());
                stmt.setString(2, email.trim());
                stmt.setString(3, HashedPassword);

                int rowsAffected = stmt.executeUpdate();

                return rowsAffected > 0 ? InsertResult.SUCCESS : InsertResult.FAILED;
            }


        }catch(SQLException e) {
            System.err.println("Database error while registring the User. "+ e.getMessage());
            return InsertResult.Database_Error;
        }
    }

    public static InsertResult loginUser(String username, String password) {
        try(Connection conn = DatabaseConnection.connect()){
            String query = "SELECT password FROM users WHERE username = ? ";
            try(PreparedStatement stmt = conn.prepareStatement(query)){
                stmt.setString(1, username);
                try(ResultSet rs = stmt.executeQuery()){
                    if(rs.next()) {
                        // get the hashed password from the database.
                        String StoredhashedPass = rs.getString("password");
                        if(HashPassword.verifyPassword(password, StoredhashedPass)) {
                            return InsertResult.SUCCESS;
                        }else {
                            return InsertResult.Incorrect_Password;
                        }
                    }else {
                        return InsertResult.User_Not_Found;
                    }
                }
            }
        }catch(SQLException e) {
            System.err.println("Database error while connecting the User. "+ e.getMessage());
            return InsertResult.Database_Error;
        }
    }
}
