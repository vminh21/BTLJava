package BTL.entity;

public class Brands {
    private int brand_id; // Hoặc private int id;
    private String name;
    private String origin;

    public Brands() {
    }

    public Brands(int brand_id, String name, String origin) {
        this.brand_id = brand_id;
        this.name = name;
        this.origin = origin;
    }

    // Quan trọng: DAO gọi hàm này
    public int getBrand_id() { 
        return brand_id; 
    }
    
    // Nếu bạn dùng getId() thì sửa DAO thành .getId() nhé
    public int getId() {
        return brand_id;
    }

    public void setBrand_id(int brand_id) {
        this.brand_id = brand_id;
    }
    
    public void setId(int id) {
        this.brand_id = id;
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
}