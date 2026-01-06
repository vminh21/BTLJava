/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package BTL.bussiness;
import BTL.connect.MyConnection;
import BTL.entity.OrderDetails;
import java.sql.*;
import java.util.*;
/**
 *
 * @author vanminh
 */


public class OrderDetailsDao implements Dao<OrderDetails> {
    private final String TABLE_NAME = "order_details";
    MyConnection myConnection = MyConnection.getInstance();

    @Override
    public List<OrderDetails> getall() {
        List<OrderDetails> list = new ArrayList<>();
        String sql = "SELECT * FROM " + TABLE_NAME;
        try (Connection conn = myConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                list.add(new OrderDetails(rs.getInt("detail_id"), rs.getInt("order_id"), rs.getInt("product_id"), rs.getInt("quantity"), rs.getDouble("price_at_purchase")));
            }
        } catch (Exception e) { e.printStackTrace(); }
        return list;
    }

    @Override
    public Optional<OrderDetails> get(int id) {
        String sql = "SELECT * FROM " + TABLE_NAME + " WHERE detail_id = ?";
        try (Connection conn = myConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return Optional.of(new OrderDetails(rs.getInt("detail_id"), rs.getInt("order_id"), rs.getInt("product_id"), rs.getInt("quantity"), rs.getDouble("price_at_purchase")));
            }
        } catch (Exception e) { e.printStackTrace(); }
        return Optional.empty();
    }

    // Trong lớp OrderDetailsDao
    @Override
    public int insert(OrderDetails t) {
        String sqlInsert = "INSERT INTO " + TABLE_NAME + " (order_id, product_id, quantity, price_at_purchase) VALUES (?,?,?,?)";
        String sqlUpdateStock = "UPDATE products SET stock_quantity = stock_quantity - ? WHERE product_id = ?";
        String sqlUpdateTotal = "UPDATE orders SET total_amount = (SELECT SUM(quantity * price_at_purchase) FROM order_details WHERE order_id = ?) WHERE order_id = ?";

        Connection conn = null;
        try {
            conn = myConnection.getConnection();
            conn.setAutoCommit(false); // Bắt đầu Transaction

            // 1. Thêm chi tiết đơn hàng
            try (PreparedStatement ps = conn.prepareStatement(sqlInsert, Statement.RETURN_GENERATED_KEYS)) {
                ps.setInt(1, t.getOrderId());
                ps.setInt(2, t.getProductId());
                ps.setInt(3, t.getQuantity());
                ps.setDouble(4, t.getPriceAtPurchase());

                if (ps.executeUpdate() > 0) {
                    // 2. Cập nhật kho
                    try (PreparedStatement psStock = conn.prepareStatement(sqlUpdateStock)) {
                        psStock.setInt(1, t.getQuantity());
                        psStock.setInt(2, t.getProductId());
                        psStock.executeUpdate();
                    }

                    // 3. Cập nhật tổng tiền đơn hàng
                    try (PreparedStatement psTotal = conn.prepareStatement(sqlUpdateTotal)) {
                        psTotal.setInt(1, t.getOrderId());
                        psTotal.setInt(2, t.getOrderId());
                        psTotal.executeUpdate();
                    }

                    ResultSet rs = ps.getGeneratedKeys();
                    if (rs.next()) {
                        int generatedId = rs.getInt(1);
                        conn.commit(); // Lưu tất cả thay đổi
                        return generatedId;
                    }
                }
            }
        } catch (Exception e) {
            if (conn != null) try { conn.rollback(); } catch (SQLException ex) { ex.printStackTrace(); }
            e.printStackTrace();
        } finally {
            if (conn != null) try { conn.setAutoCommit(true); conn.close(); } catch (SQLException e) { e.printStackTrace(); }
        }
        return -1;
    }

    @Override public boolean update(OrderDetails t) { return false; } // Thường không update chi tiết đơn hàng
    @Override public boolean delete(OrderDetails t) { return delete(t.getDetailId()); }
    @Override public boolean delete(int id) {
        String sql = "DELETE FROM " + TABLE_NAME + " WHERE detail_id = ?";
        try (Connection conn = myConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        } catch (Exception e) { e.printStackTrace(); }
        return false;
    }
}
