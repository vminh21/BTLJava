/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package BTL.entity;

/**
 *
 * @author vanminh
 */
public class Suppliers {
    private int supplierId;
    private String name;
    private String phoneNumber;
    private String email;
    private String address;

    public Suppliers() {}

    public Suppliers(int supplierId, String name, String phoneNumber, String email, String address) {
        this.supplierId = supplierId;
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.address = address;
    }

    public int getSupplierId() {
        return supplierId;
    }

    public void setSupplierId(int supplierId) {
        this.supplierId = supplierId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    @Override
    public String toString() {
        return "Suppliers{" + "supplierId=" + supplierId + ", name=" + name + ", phoneNumber=" + phoneNumber + ", email=" + email + ", address=" + address + '}';
    }
    
}
