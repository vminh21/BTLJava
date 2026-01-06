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
                list.add(new Orders(rs.getInt("order_id"), rs.getInt("user_id"),(long) rs.getDouble("total_amount"), rs.getString("status"), rs.getString("shipping_address"), rs.getString("payment_method"), rs.getTimestamp("created_at")));
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
    // Lấy danh sách hóa đơn sắp xếp mới nhất (cho JList)
    public List<Orders> getAllOrdersDesc() {
        List<Orders> list = new ArrayList<>();
        String sql = "SELECT * FROM orders ORDER BY order_id DESC";
        try (Connection conn = myConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                Orders order = new Orders();
                order.setOrderId(rs.getInt("order_id"));
                order.setUserId(rs.getInt("user_id"));
                order.setName(rs.getString("name"));
                order.setTotalAmount(rs.getDouble("total_amount"));
                order.setStatus(rs.getString("status"));
                order.setShippingAddress(rs.getString("shipping_address"));
                order.setPaymentMethod(rs.getString("payment_method"));
                list.add(order);
            }
        } catch (Exception e) { e.printStackTrace(); }
        return list;
    }

    // Lấy chi tiết sản phẩm của một hóa đơn (cho Table khi click vào JList)
    public List<Vector> getProductDetailsByOrderId(int orderId) {
        List<Vector> list = new ArrayList<>();
        String sql = "SELECT p.name, od.quantity, od.price_at_purchase " +
                     "FROM order_details od " +
                     "JOIN products p ON od.product_id = p.product_id " +
                     "WHERE od.order_id = ?";
        try (Connection conn = myConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, orderId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Vector row = new Vector();
                    row.add(rs.getString(1)); // Tên SP
                    row.add(rs.getInt(2));    // Số lượng
                    double price = rs.getDouble(3);
                    row.add(String.format("%,.0f", price)); // Đơn giá
                    row.add(String.format("%,.0f", rs.getInt(2) * price)); // Thành tiền
                    list.add(row);
                }
            }
        } catch (Exception e) { e.printStackTrace(); }
        return list;
    }

    @Override
    public int insert(Orders t) {
        String sql = "INSERT INTO " + TABLE_NAME + " (user_id, name, total_amount, status, shipping_address, payment_method) VALUES (?,?,?,?,?,?)";
        try (Connection conn = myConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, t.getUserId());
            ps.setString(2, t.getName());
            ps.setDouble(3, t.getTotalAmount());
            ps.setString(4, t.getStatus());
            ps.setString(5, t.getShippingAddress());
            ps.setString(6, t.getPaymentMethod());
            if (ps.executeUpdate() > 0) {
                ResultSet rs = ps.getGeneratedKeys();
                if (rs.next()) return rs.getInt(1);
            }
        } catch (Exception e) { e.printStackTrace(); }
        return -1;
    }

    @Override
    public boolean update(Orders t) {
        String sql = "UPDATE " + TABLE_NAME + " SET name = ?, total_amount = ?, status = ?, shipping_address = ?, payment_method = ? WHERE order_id = ?";
        try (Connection conn = myConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, t.getName());
            ps.setDouble(2, t.getTotalAmount());
            ps.setString(3, t.getStatus());
            ps.setString(4, t.getShippingAddress());
            ps.setString(5, t.getPaymentMethod());
            ps.setInt(6, t.getOrderId());
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