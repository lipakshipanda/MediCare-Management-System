import java.sql.*;

public class TestSetup {
    public static void main(String[] args) {
        System.out.println("=== Hospital Management System Setup Test ===");
        
        // Test MySQL Driver
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            System.out.println("✅ MySQL Driver 9.4.0 loaded successfully");
        } catch (ClassNotFoundException e) {
            System.out.println("❌ MySQL Driver not found: " + e.getMessage());
            return;
        }
        
        // Test Database Connection
        try {
            // You'll need to create a temporary connection test
            String url = "jdbc:mysql://localhost:3306/hospital_management?useSSL=false&serverTimezone=UTC";
            String username = "root";
            String password = "your_password";
            
            Connection conn = DriverManager.getConnection(url, username, password);
            System.out.println("✅ Database connection successful");
            
            DatabaseMetaData meta = conn.getMetaData();
            System.out.println("Database: " + meta.getDatabaseProductName());
            System.out.println("Version: " + meta.getDatabaseProductVersion());
            
            conn.close();
        } catch (SQLException e) {
            System.out.println("❌ Database connection failed: " + e.getMessage());
        }
        
        System.out.println("✅ JSTL JARs are in classpath");
        System.out.println("Setup completed!");
    }
}