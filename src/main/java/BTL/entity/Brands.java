/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package BTL.entity;

/**
 *
 * @author vanminh
 */
public class Brands {
    private int brandId;
    private String name;
    private String origin;

    public Brands() {}
    public Brands(int brandId, String name, String origin) {
        this.brandId = brandId;
        this.name = name;
        this.origin = origin;
    }

    public int getBrandId() {
        return brandId;
    }

    public void setBrandId(int brandId) {
        this.brandId = brandId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getOrigin() {
        return origin;
    }

    public void setOrigin(String origin) {
        this.origin = origin;
    }

    @Override
    public String toString() {
        return "Brands{" + "brandId=" + brandId + ", name=" + name + ", origin=" + origin + '}';
    }
    
}
