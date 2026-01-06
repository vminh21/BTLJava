package BTL.entity;

/**
 * Entity Brands chuẩn hóa để chạy với BrandsDao (Generic Dao)
 */
public class Brands {
    private int brandId; // Dùng lạc đà (camelCase) cho chuẩn Java
    private String name;
    private String origin;

    public Brands() {
    }

    public Brands(int brandId, String name, String origin) {
        this.brandId = brandId;
        this.name = name;
        this.origin = origin;
    }

    // ⭐ Hàm quan trọng nhất: Phải khớp với gọi lệnh trong BrandsDao.update
    public int getBrandId() {
        return brandId;
    }

    public void setBrandId(int brandId) {
        this.brandId = brandId;
    }

    // Các hàm getter/setter còn lại
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