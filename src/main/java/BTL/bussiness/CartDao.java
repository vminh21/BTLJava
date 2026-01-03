/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package BTL.bussiness;
import BTL.connect.MyConnection;
import BTL.entity.Cart;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
 /*
 * @author vanminh
 */


public class CartDao implements Dao<Cart> {

    private final String TABLE_NAME = "cart";
    MyConnection myConnection = MyConnection.getInstance();

    @Override
    public List<Cart> getall() {
        List<Cart> list = new ArrayList<>();
        Connection conn = myConnection.getConnection();
        PreparedStatement ps = null;
        String sql = "select c.*, p.name, p.price from " + TABLE_NAME + " c "
                   + "join products p on c.product_id = p.product_id";
        ResultSet rs = null;
        try {
            ps = conn.prepareStatement(sql);
            rs = ps.executeQuery();
            while (rs.next()) {
                Cart c = new Cart();
                c.setCartId(rs.getInt("cart_id"));
                c.setUserId(rs.getInt("user_id"));
                c.setProductId(rs.getInt("product_id"));
                c.setQuantity(rs.getInt("quantity"));
                c.setProductName(rs.getString("name"));
                c.setPrice(rs.getDouble("price"));
                list.add(c);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    // Lấy giỏ hàng theo User ID (Dùng để hiển thị giỏ hàng của khách đang đăng nhập)
    public List<Cart> getByUser(int userId) {
        List<Cart> list = new ArrayList<>();
        Connection conn = myConnection.getConnection();
        PreparedStatement ps = null;
        String sql = "select c.*, p.name, p.price from " + TABLE_NAME + " c "
                   + "join products p on c.product_id = p.product_id where c.user_id = ?";
        ResultSet rs = null;
        try {
            ps = conn.prepareStatement(sql);
            ps.setInt(1, userId);
            rs = ps.executeQuery();
            while (rs.next()) {
                Cart c = new Cart();
                c.setCartId(rs.getInt("cart_id"));
                c.setUserId(rs.getInt("user_id"));
                c.setProductId(rs.getInt("product_id"));
                c.setQuantity(rs.getInt("quantity"));
                c.setProductName(rs.getString("name"));
                c.setPrice(rs.getDouble("price"));
                list.add(c);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    @Override
    public Optional<Cart> get(int id) {
        Cart c = null;
        Connection conn = myConnection.getConnection();
        PreparedStatement ps = null;
        String sql = "select * from " + TABLE_NAME + " where cart_id = ?";
        ResultSet rs = null;
        try {
            ps = conn.prepareStatement(sql);
            ps.setInt(1, id);
            rs = ps.executeQuery();
            if (rs.next()) {
                c = new Cart();
                c.setCartId(rs.getInt("cart_id"));
                c.setUserId(rs.getInt("user_id"));
                c.setProductId(rs.getInt("product_id"));
                c.setQuantity(rs.getInt("quantity"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Optional.ofNullable(c);
    }

    @Override
    public int insert(Cart t) {
        Connection conn = myConnection.getConnection();
        PreparedStatement ps = null;
        // Nếu trùng sản phẩm thì cộng dồn số lượng (giữ đúng yêu cầu thêm nhiều món)
        String sql = "insert into " + TABLE_NAME + "(user_id, product_id, quantity) values(?, ?, ?) "
                   + "on duplicate key update quantity = quantity + ?";
        ResultSet rs = null;
        try {
            ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setInt(1, t.getUserId());
            ps.setInt(2, t.getProductId());
            ps.setInt(3, t.getQuantity());
            ps.setInt(4, t.getQuantity());
            if (ps.executeUpdate() > 0) {
                rs = ps.getGeneratedKeys();
                if (rs.next()) return rs.getInt(1);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return -1;
    }

    // Hàm thêm nhiều sản phẩm một lúc (Batch Processing)
    public boolean addMultiple(List<Cart> list) {
        Connection conn = myConnection.getConnection();
        PreparedStatement ps = null;
        String sql = "insert into " + TABLE_NAME + "(user_id, product_id, quantity) values(?, ?, ?) "
                   + "on duplicate key update quantity = quantity + ?";
        try {
            conn.setAutoCommit(false);
            ps = conn.prepareStatement(sql);
            for (Cart c : list) {
                ps.setInt(1, c.getUserId());
                ps.setInt(2, c.getProductId());
                ps.setInt(3, c.getQuantity());
                ps.setInt(4, c.getQuantity());
                ps.addBatch();
            }
            ps.executeBatch();
            conn.commit();
            conn.setAutoCommit(true);
            return true;
        } catch (Exception e) {
            try { conn.rollback(); } catch (SQLException ex) { ex.printStackTrace(); }
            e.printStackTrace();
        }
        return false;
    }

    // Hàm Thanh toán: Cart -> Orders -> OrderDetails -> Xóa Cart
    public boolean checkout(int userId, String address, String method) {
        Connection conn = myConnection.getConnection();
        try {
            conn.setAutoCommit(false); // Bắt đầu Transaction

            // 1. Tính tổng tiền
            double total = 0;
            String sqlTotal = "select sum(c.quantity * p.price) from cart c join products p on c.product_id = p.product_id where c.user_id = ?";
            PreparedStatement ps1 = conn.prepareStatement(sqlTotal);
            ps1.setInt(1, userId);
            ResultSet rs1 = ps1.executeQuery();
            if (rs1.next()) total = rs1.getDouble(1);
            if (total <= 0) return false;

            // 2. Tạo đơn hàng (Orders)
            int orderId = -1;
            String sqlOrder = "insert into orders(user_id, total_amount, shipping_address, payment_method) values(?, ?, ?, ?)";
            PreparedStatement ps2 = conn.prepareStatement(sqlOrder, Statement.RETURN_GENERATED_KEYS);
            ps2.setInt(1, userId);
            ps2.setDouble(2, total);
            ps2.setString(3, address);
            ps2.setString(4, method);
            ps2.executeUpdate();
            ResultSet rs2 = ps2.getGeneratedKeys();
            if (rs2.next()) orderId = rs2.getInt(1);

            // 3. Chuyển sang Order Details
            String sqlDetail = "insert into order_details(order_id, product_id, quantity, price_at_purchase) "
                             + "select ?, c.product_id, c.quantity, p.price from cart c "
                             + "join products p on c.product_id = p.product_id where c.user_id = ?";
            PreparedStatement ps3 = conn.prepareStatement(sqlDetail);
            ps3.setInt(1, orderId);
            ps3.setInt(2, userId);
            ps3.executeUpdate();

            // 4. Xóa giỏ hàng
            String sqlDelete = "delete from cart where user_id = ?";
            PreparedStatement ps4 = conn.prepareStatement(sqlDelete);
            ps4.setInt(1, userId);
            ps4.executeUpdate();

            conn.commit();
            conn.setAutoCommit(true);
            return true;
        } catch (Exception e) {
            try { conn.rollback(); } catch (SQLException ex) { ex.printStackTrace(); }
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean update(Cart t) {
        Connection conn = myConnection.getConnection();
        PreparedStatement ps = null;
        String sql = "update " + TABLE_NAME + " set quantity = ? where cart_id = ?";
        try {
            ps = conn.prepareStatement(sql);
            ps.setInt(1, t.getQuantity());
            ps.setInt(2, t.getCartId());
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean delete(Cart t) {
        return delete(t.getCartId());
    }

    @Override
    public boolean delete(int id) {
        Connection conn = myConnection.getConnection();
        PreparedStatement ps = null;
        String sql = "delete from " + TABLE_NAME + " where cart_id = ?";
        try {
            ps = conn.prepareStatement(sql);
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
}