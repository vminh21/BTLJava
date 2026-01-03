/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package BTL.entity;

/**
 *
 * @author vanminh
 */
public class Products {
    private int productId;
    private String name;
    private int categoryId;
    private int brandId;
    private int supplierId;
    private double price;
    private int stockQuantity;
    private String description;
    private String thumbnail;
    
    public Products() {}

    public Products(int productId, String name, int categoryId, int brandId, int supplierId, double price, int stockQuantity, String description, String thumbnail) {
        this.productId = productId;
        this.name = name;
        this.categoryId = categoryId;
        this.brandId = brandId;
        this.supplierId = supplierId;
        this.price = price;
        this.stockQuantity = stockQuantity;
        this.description = description;
        this.thumbnail = thumbnail;
    }

    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }

    public int getBrandId() {
        return brandId;
    }

    public void setBrandId(int brandId) {
        this.brandId = brandId;
    }

    public int getSupplierId() {
        return supplierId;
    }

    public void setSupplierId(int supplierId) {
        this.supplierId = supplierId;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getStockQuantity() {
        return stockQuantity;
    }

    public void setStockQuantity(int stockQuantity) {
        this.stockQuantity = stockQuantity;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }

    @Override
    public String toString() {
        return "Products{" + "productId=" + productId + ", name=" + name + ", categoryId=" + categoryId + ", brandId=" + brandId + ", supplierId=" + supplierId + ", price=" + price + ", stockQuantity=" + stockQuantity + ", description=" + description + ", thumbnail=" + thumbnail + '}';
    }
    
}
