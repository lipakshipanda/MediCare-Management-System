package com.hospital.dao;

import com.hospital.model.User;
import com.hospital.exception.DatabaseException;
import com.hospital.exception.UserNotFoundException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserDAO {
    
    public User authenticate(String username, String password) throws DatabaseException, UserNotFoundException {
        String sql = "SELECT * FROM users WHERE username = ? AND password = ? AND is_active = true";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, username);
            stmt.setString(2, password);
            
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return extractUserFromResultSet(rs);
            } else {
                throw new UserNotFoundException("Invalid username or password");
            }
        } catch (SQLException e) {
            throw new DatabaseException("Database error during authentication", e);
        }
    }
    
    public boolean createUser(User user) throws DatabaseException {
        String sql = "INSERT INTO users (username, email, password, role) VALUES (?, ?, ?, ?)";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            stmt.setString(1, user.getUsername());
            stmt.setString(2, user.getEmail());
            stmt.setString(3, user.getPassword());
            stmt.setString(4, user.getRole());
            
            int affectedRows = stmt.executeUpdate();
            if (affectedRows > 0) {
                ResultSet rs = stmt.getGeneratedKeys();
                if (rs.next()) {
                    user.setId(rs.getInt(1));
                    return true;
                }
            }
            return false;
        } catch (SQLException e) {
            throw new DatabaseException("Error creating user", e);
        }
    }
    
    public boolean isUsernameExists(String username) throws DatabaseException {
        String sql = "SELECT COUNT(*) FROM users WHERE username = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, username);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
            return false;
        } catch (SQLException e) {
            throw new DatabaseException("Error checking username existence", e);
        }
    }
    
    private User extractUserFromResultSet(ResultSet rs) throws SQLException {
        User user = new User();
        user.setId(rs.getInt("id"));
        user.setUsername(rs.getString("username"));
        user.setEmail(rs.getString("email"));
        user.setPassword(rs.getString("password"));
        user.setRole(rs.getString("role"));
        user.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());
        user.setActive(rs.getBoolean("is_active"));
        return user;
    }
}