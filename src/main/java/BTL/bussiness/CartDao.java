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


/**
 *
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
        ResultSet rs = null;
        String sql = "select c.*, p.name, p.price from " + TABLE_NAME + " c "
                   + "join products p on c.product_id = p.product_id";
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

    // Lấy giỏ hàng theo User ID
    public List<Cart> getByUser(int userId) {
        List<Cart> list = new ArrayList<>();
        Connection conn = myConnection.getConnection();
        PreparedStatement ps = null;
        ResultSet rs = null;
        String sql = "select c.*, p.name, p.price from " + TABLE_NAME + " c "
                   + "join products p on c.product_id = p.product_id where c.user_id = ?";
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
        ResultSet rs = null;
        String sql = "select * from " + TABLE_NAME + " where cart_id = ?";
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
        ResultSet rs = null;
        String sql = "insert into " + TABLE_NAME + "(user_id, product_id, quantity) values(?, ?, ?) "
                   + "on duplicate key update quantity = quantity + ?";
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
            try { conn.rollback(); conn.setAutoCommit(true); } catch (SQLException ex) {}
            e.printStackTrace();
        }
        return false;
    }

    // Sửa tiêu đề hàm để nhận thêm tên khách hàng
    public boolean checkout(int userId, String address, String method, String customerName) {
    Connection conn = myConnection.getConnection();
    try {
        conn.setAutoCommit(false); // Bắt đầu giao dịch

        // 1. Tính tổng tiền giỏ hàng
        double total = 0;
        String sqlTotal = "SELECT SUM(c.quantity * p.price) FROM cart c JOIN products p ON c.product_id = p.product_id WHERE c.user_id = ?";
        PreparedStatement ps1 = conn.prepareStatement(sqlTotal);
        ps1.setInt(1, userId);
        ResultSet rs1 = ps1.executeQuery();
        if (rs1.next()) total = rs1.getDouble(1);

        if (total <= 0) {
            conn.setAutoCommit(true);
            return false;
        }

        // 2. Tạo hóa đơn mới (Đã sửa đúng thứ tự cột: user_id, name, total_amount, status, address, method)
        int orderId = -1;
        String sqlOrder = "INSERT INTO orders(user_id, name, total_amount, status, shipping_address, payment_method) VALUES(?, ?, ?, 'pending', ?, ?)";
        PreparedStatement ps2 = conn.prepareStatement(sqlOrder, Statement.RETURN_GENERATED_KEYS);
        ps2.setInt(1, userId);
        ps2.setString(2, customerName); // Cột 'name' đứng thứ 2 trong DB của bạn
        ps2.setDouble(3, total);
        ps2.setString(4, address);
        ps2.setString(5, method);
        ps2.executeUpdate();

        ResultSet rs2 = ps2.getGeneratedKeys();
        if (rs2.next()) orderId = rs2.getInt(1);

        // 3. TRỪ SỐ LƯỢNG KHO (Đã sửa stock -> stock_quantity)
        String sqlGetCart = "SELECT product_id, quantity FROM cart WHERE user_id = ?";
        PreparedStatement psGetCart = conn.prepareStatement(sqlGetCart);
        psGetCart.setInt(1, userId);
        ResultSet rsCart = psGetCart.executeQuery();

        // Sử dụng stock_quantity cho cả set và where
        String sqlUpdateStock = "UPDATE products SET stock_quantity = stock_quantity - ? WHERE product_id = ? AND stock_quantity >= ?";
        PreparedStatement psUpdateStock = conn.prepareStatement(sqlUpdateStock);

        while (rsCart.next()) {
            int pId = rsCart.getInt("product_id");
            int qty = rsCart.getInt("quantity");

            psUpdateStock.setInt(1, qty);
            psUpdateStock.setInt(2, pId);
            psUpdateStock.setInt(3, qty);

            int rowsAffected = psUpdateStock.executeUpdate();
            if (rowsAffected == 0) {
                throw new Exception("Sản phẩm ID " + pId + " không đủ số lượng trong kho!");
            }
        }

        // 4. Chuyển dữ liệu sang chi tiết hóa đơn
        String sqlDetail = "INSERT INTO order_details(order_id, product_id, quantity, price_at_purchase) "
                         + "SELECT ?, c.product_id, c.quantity, p.price FROM cart c "
                         + "JOIN products p ON c.product_id = p.product_id WHERE c.user_id = ?";
        PreparedStatement ps3 = conn.prepareStatement(sqlDetail);
        ps3.setInt(1, orderId);
        ps3.setInt(2, userId);
        ps3.executeUpdate();

        // 5. Xóa giỏ hàng
        String sqlDelete = "DELETE FROM cart WHERE user_id = ?";
        PreparedStatement ps4 = conn.prepareStatement(sqlDelete);
        ps4.setInt(1, userId);
        ps4.executeUpdate();

        conn.commit(); 
        conn.setAutoCommit(true);
        return true;
    } catch (Exception e) {
        try { if (conn != null) conn.rollback(); } catch (SQLException ex) { ex.printStackTrace(); }
        System.err.println("Lỗi đặt hàng chi tiết: " + e.getMessage());
        return false;
    }
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
    public boolean deleteByUserAndProduct(int userId, int productId) {
    Connection conn = myConnection.getConnection();
    String sql = "DELETE FROM " + TABLE_NAME + " WHERE user_id = ? AND product_id = ?";
    try (PreparedStatement ps = conn.prepareStatement(sql)) {
        ps.setInt(1, userId);
        ps.setInt(2, productId);    
        return ps.executeUpdate() > 0;
    } catch (Exception e) {
        e.printStackTrace();
    }
    return false;
}
    // Hàm thêm vào giỏ hàng
    public boolean addToCart(int userId, int productId, int quantityToAdd) {
        // Lấy kết nối
        Connection conn = MyConnection.getInstance().getConnection();
        
        try {
            // 1. Kiểm tra xem sản phẩm đã có trong giỏ của user này chưa
            String checkSql = "SELECT quantity FROM cart WHERE user_id = ? AND product_id = ?";
            PreparedStatement checkPs = conn.prepareStatement(checkSql);
            checkPs.setInt(1, userId);
            checkPs.setInt(2, productId);
            ResultSet rs = checkPs.executeQuery();

            if (rs.next()) {
                // TRƯỜNG HỢP 1: Đã có -> Update số lượng (cộng dồn)
                String updateSql = "UPDATE cart SET quantity = quantity + ? WHERE user_id = ? AND product_id = ?";
                PreparedStatement updatePs = conn.prepareStatement(updateSql);
                updatePs.setInt(1, quantityToAdd);
                updatePs.setInt(2, userId);
                updatePs.setInt(3, productId);
                return updatePs.executeUpdate() > 0;
            } else {
                // TRƯỜNG HỢP 2: Chưa có -> Insert mới
                String insertSql = "INSERT INTO cart(user_id, product_id, quantity) VALUES (?, ?, ?)";
                PreparedStatement insertPs = conn.prepareStatement(insertSql);
                insertPs.setInt(1, userId);
                insertPs.setInt(2, productId);
                insertPs.setInt(3, quantityToAdd);
                return insertPs.executeUpdate() > 0;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}