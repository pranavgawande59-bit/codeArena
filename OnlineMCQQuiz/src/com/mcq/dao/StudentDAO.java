package com.mcq.dao;

import com.mcq.database.DBConnection;
import com.mcq.model.Student;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * StudentDAO.java
 * Handles student account creation, credential verification, and profile retrieval.
 */
public class StudentDAO {

    /**
     * Registers a new student account in MySQL.
     * 
     * @param student Student model containing username, password, fullName, email
     * @return true if successfully inserted, false if username/email collision or error
     */
    public boolean registerStudent(Student student) {
        String query = "INSERT INTO students (username, password, full_name, email) VALUES (?, ?, ?, ?)";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, student.getUsername().trim());
            ps.setString(2, student.getPassword().trim());
            ps.setString(3, student.getFullName().trim());
            ps.setString(4, student.getEmail().trim());

            int affected = ps.executeUpdate();
            if (affected > 0) {
                try (ResultSet rs = ps.getGeneratedKeys()) {
                    if (rs.next()) {
                        student.setId(rs.getInt(1));
                    }
                }
                return true;
            }
        } catch (SQLException e) {
            System.err.println("[StudentDAO Error] registerStudent failed: " + e.getMessage());
        }
        return false;
    }

    /**
     * Authenticates student credentials.
     * 
     * @param username Student username
     * @param password Student password
     * @return Student object if valid, null otherwise
     */
    public Student authenticate(String username, String password) {
        String query = "SELECT id, username, password, full_name, email, created_at FROM students "
                     + "WHERE username = ? AND password = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {

            ps.setString(1, username.trim());
            ps.setString(2, password.trim());

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new Student(
                        rs.getInt("id"),
                        rs.getString("username"),
                        rs.getString("password"),
                        rs.getString("full_name"),
                        rs.getString("email"),
                        rs.getTimestamp("created_at")
                    );
                }
            }
        } catch (SQLException e) {
            System.err.println("[StudentDAO Error] authenticate failed: " + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Checks if a username is already registered.
     */
    public boolean isUsernameTaken(String username) {
        String query = "SELECT id FROM students WHERE username = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {

            ps.setString(1, username.trim());
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        } catch (SQLException e) {
            System.err.println("[StudentDAO Error] isUsernameTaken failed: " + e.getMessage());
        }
        return false;
    }

    /**
     * Checks if an email is already registered.
     */
    public boolean isEmailTaken(String email) {
        String query = "SELECT id FROM students WHERE email = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {

            ps.setString(1, email.trim());
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        } catch (SQLException e) {
            System.err.println("[StudentDAO Error] isEmailTaken failed: " + e.getMessage());
        }
        return false;
    }

    /**
     * Retrieves student by ID.
     */
    public Student getStudentById(int id) {
        String query = "SELECT id, username, password, full_name, email, created_at FROM students WHERE id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {

            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new Student(
                        rs.getInt("id"),
                        rs.getString("username"),
                        rs.getString("password"),
                        rs.getString("full_name"),
                        rs.getString("email"),
                        rs.getTimestamp("created_at")
                    );
                }
            }
        } catch (SQLException e) {
            System.err.println("[StudentDAO Error] getStudentById failed: " + e.getMessage());
        }
        return null;
    }
}
