package com.hospital.dao;

import com.hospital.model.Patient;
import com.hospital.exception.DatabaseException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PatientDAO {
    
    public boolean createPatient(Patient patient) throws DatabaseException {
        String sql = "INSERT INTO patients (user_id, name, date_of_birth, gender, phone, address, emergency_contact) VALUES (?, ?, ?, ?, ?, ?, ?)";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            stmt.setInt(1, patient.getId()); // user_id
            stmt.setString(2, patient.getFullName());
            stmt.setDate(3, patient.getDateOfBirth() != null ? Date.valueOf(patient.getDateOfBirth()) : null);
            stmt.setString(4, patient.getGender());
            stmt.setString(5, patient.getPhone());
            stmt.setString(6, patient.getAddress());
            stmt.setString(7, patient.getEmergencyContact());
            
            int affectedRows = stmt.executeUpdate();
            return affectedRows > 0;
        } catch (SQLException e) {
            throw new DatabaseException("Error creating patient", e);
        }
    }
    
    public List<Patient> getAllPatients() throws DatabaseException {
        List<Patient> patients = new ArrayList<>();
        String sql = "SELECT p.*, u.username, u.email, u.role FROM patients p JOIN users u ON p.user_id = u.id";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            
            while (rs.next()) {
                Patient patient = extractPatientFromResultSet(rs);
                patients.add(patient);
            }
        } catch (SQLException e) {
            throw new DatabaseException("Error retrieving patients", e);
        }
        return patients;
    }
    
    public Patient getPatientByUserId(int userId) throws DatabaseException {
        String sql = "SELECT p.*, u.username, u.email, u.role FROM patients p JOIN users u ON p.user_id = u.id WHERE p.user_id = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, userId);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                return extractPatientFromResultSet(rs);
            }
            return null;
        } catch (SQLException e) {
            throw new DatabaseException("Error retrieving patient by user ID", e);
        }
    }
    
    private Patient extractPatientFromResultSet(ResultSet rs) throws SQLException {
        Patient patient = new Patient();
        patient.setId(rs.getInt("user_id")); // Using user_id as ID for Patient object
        patient.setUsername(rs.getString("username"));
        patient.setEmail(rs.getString("email"));
        patient.setRole(rs.getString("role"));
        patient.setFullName(rs.getString("name"));
        
        Date dob = rs.getDate("date_of_birth");
        if (dob != null) {
            patient.setDateOfBirth(dob.toLocalDate());
        }
        
        patient.setGender(rs.getString("gender"));
        patient.setPhone(rs.getString("phone"));
        patient.setAddress(rs.getString("address"));
        patient.setEmergencyContact(rs.getString("emergency_contact"));
        return patient;
    }
}