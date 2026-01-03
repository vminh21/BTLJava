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

    @Override
    public int insert(OrderDetails t) {
        String sql = "INSERT INTO " + TABLE_NAME + " (order_id, product_id, quantity, price_at_purchase) VALUES (?,?,?,?)";
        try (Connection conn = myConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, t.getOrderId());
            ps.setInt(2, t.getProductId());
            ps.setInt(3, t.getQuantity());
            ps.setDouble(4, t.getPriceAtPurchase());
            if (ps.executeUpdate() > 0) {
                ResultSet rs = ps.getGeneratedKeys();
                if (rs.next()) return rs.getInt(1);
            }
        } catch (Exception e) { e.printStackTrace(); }
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
