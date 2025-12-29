/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package BTL.entity;

import java.sql.Timestamp;

/**
 *
 * @author vanminh
 */
public class Users {
    private int userId;
    private String fullName;
    private String email;
    private String passwordHash;
    private String phoneNumber;
    private String address;
    private String gender;
    private String role; // customer, admin, staff
    private Timestamp createdAt;

    public Users() {
    }

    public Users(int userId, String fullName, String email, String passwordHash, String phoneNumber, String address, String gender, String role, Timestamp createdAt) {
        this.userId = userId;
        this.fullName = fullName;
        this.email = email;
        this.passwordHash = passwordHash;
        this.phoneNumber = phoneNumber;
        this.address = address;
        this.gender = gender;
        this.role = role;
        this.createdAt = createdAt;
    }
    // Thêm hàm này vào lớp Users.java của anh
    public Users(int userId,String fullName, String email, String passwordHash, String phoneNumber, String address, String gender) {
        this.userId = userId;
        this.fullName = fullName;
        this.email = email;
        this.passwordHash = passwordHash;
        this.phoneNumber = phoneNumber;
        this.address = address;
        this.gender = gender;
        this.role = "customer"; // Mặc định là khách hàng
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }

    @Override
    public String toString() {
        return "Users{" + "userId=" + userId + ", fullName=" + fullName + ", email=" + email + ", passwordHash=" + passwordHash + ", phoneNumber=" + phoneNumber + ", address=" + address + ", gender=" + gender + ", role=" + role + ", createdAt=" + createdAt + '}';
    }

    
}
