/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package BTL.bussiness;
import BTL.connect.MyConnection;
import BTL.entity.Orders;
import java.sql.*;
import java.util.*;
/**
 *
 * @author vanminh
 */

public class OrdersDao implements Dao<Orders> {
    private final String TABLE_NAME = "orders";
    MyConnection myConnection = MyConnection.getInstance();

    @Override
    public List<Orders> getall() {
        List<Orders> list = new ArrayList<>();
        String sql = "SELECT * FROM " + TABLE_NAME;
        try (Connection conn = myConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                list.add(new Orders(rs.getInt("order_id"), rs.getInt("user_id"), rs.getDouble("total_amount"), rs.getString("status"), rs.getString("shipping_address"), rs.getString("payment_method"), rs.getTimestamp("created_at")));
            }
        } catch (Exception e) { e.printStackTrace(); }
        return list;
    }

    @Override
    public Optional<Orders> get(int id) {
        String sql = "SELECT * FROM " + TABLE_NAME + " WHERE order_id = ?";
        try (Connection conn = myConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return Optional.of(new Orders(rs.getInt("order_id"), rs.getInt("user_id"), rs.getDouble("total_amount"), rs.getString("status"), rs.getString("shipping_address"), rs.getString("payment_method"), rs.getTimestamp("created_at")));
            }
        } catch (Exception e) { e.printStackTrace(); }
        return Optional.empty();
    }

    @Override
    public int insert(Orders t) {
        String sql = "INSERT INTO " + TABLE_NAME + " (user_id, total_amount, status, shipping_address, payment_method) VALUES (?,?,?,?,?)";
        try (Connection conn = myConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, t.getUserId());
            ps.setDouble(2, t.getTotalAmount());
            ps.setString(3, t.getStatus());
            ps.setString(4, t.getShippingAddress());
            ps.setString(5, t.getPaymentMethod());
            if (ps.executeUpdate() > 0) {
                ResultSet rs = ps.getGeneratedKeys();
                if (rs.next()) return rs.getInt(1);
            }
        } catch (Exception e) { e.printStackTrace(); }
        return -1;
    }

    @Override
    public boolean update(Orders t) {
        String sql = "UPDATE " + TABLE_NAME + " SET status = ? WHERE order_id = ?";
        try (Connection conn = myConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, t.getStatus());
            ps.setInt(2, t.getOrderId());
            return ps.executeUpdate() > 0;
        } catch (Exception e) { e.printStackTrace(); }
        return false;
    }

    @Override public boolean delete(Orders t) { return delete(t.getOrderId()); }
    @Override public boolean delete(int id) {
        String sql = "DELETE FROM " + TABLE_NAME + " WHERE order_id = ?";
        try (Connection conn = myConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        } catch (Exception e) { e.printStackTrace(); }
        return false;
    }
}