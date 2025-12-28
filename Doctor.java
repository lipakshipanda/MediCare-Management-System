package com.hospital.model;

public class Doctor extends User {
    private String name;
    private String specialization;
    private int departmentId;
    private String phone;
    private int experience;
    private String bio;
    private String departmentName;
    
    public Doctor() {}
    
    public Doctor(int id, String username, String email, String name, String specialization) {
        super(id, username, email, "", "doctor");
        this.name = name;
        this.specialization = specialization;
    }
    
    // Getters and Setters
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    
    public String getSpecialization() { return specialization; }
    public void setSpecialization(String specialization) { this.specialization = specialization; }
    
    public int getDepartmentId() { return departmentId; }
    public void setDepartmentId(int departmentId) { this.departmentId = departmentId; }
    
    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }
    
    public int getExperience() { return experience; }
    public void setExperience(int experience) { this.experience = experience; }
    
    public String getBio() { return bio; }
    public void setBio(String bio) { this.bio = bio; }
    
    public String getDepartmentName() { return departmentName; }
    public void setDepartmentName(String departmentName) { this.departmentName = departmentName; }
}