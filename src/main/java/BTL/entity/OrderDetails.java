/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package BTL.entity;

/**
 *
 * @author vanminh
 */
public class OrderDetails {
    private int detailId;
    private int orderId;
    private int productId;
    private int quantity;
    private double priceAtPurchase;

    public OrderDetails() {}
    public OrderDetails(int detailId, int orderId, int productId, int quantity, double priceAtPurchase) {
        this.detailId = detailId;
        this.orderId = orderId;
        this.productId = productId;
        this.quantity = quantity;
        this.priceAtPurchase = priceAtPurchase;
    }

    public int getDetailId() {
        return detailId;
    }

    public void setDetailId(int detailId) {
        this.detailId = detailId;
    }

    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public double getPriceAtPurchase() {
        return priceAtPurchase;
    }

    public void setPriceAtPurchase(double priceAtPurchase) {
        this.priceAtPurchase = priceAtPurchase;
    }

    @Override
    public String toString() {
        return "OrderDetails{" + "detailId=" + detailId + ", orderId=" + orderId + ", productId=" + productId + ", quantity=" + quantity + ", priceAtPurchase=" + priceAtPurchase + '}';
    }
    
}
