/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package BTL.bussiness;
import BTL.connect.MyConnection;
import BTL.entity.Products;
import java.sql.*;
import java.util.*;
/**
 *
 * @author vanminh
 */

public class ProductsDao implements Dao<Products> {
    private final String TABLE_NAME = "products";
    MyConnection myConnection = MyConnection.getInstance();

    @Override
    public List<Products> getall() {
        List<Products> list = new ArrayList<>();
        String sql = "SELECT * FROM " + TABLE_NAME;
        try (Connection conn = myConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                list.add(mapResultSetToProduct(rs));
            }
        } catch (Exception e) { e.printStackTrace(); }
        return list;
    }

    @Override
    public Optional<Products> get(int id) {
        String sql = "SELECT * FROM " + TABLE_NAME + " WHERE product_id = ?";
        try (Connection conn = myConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return Optional.of(mapResultSetToProduct(rs));
            }
        } catch (Exception e) { e.printStackTrace(); }
        return Optional.empty();
    }

    @Override
    public int insert(Products t) {
        String sql = "INSERT INTO " + TABLE_NAME + " (name, category_id, brand_id, supplier_id, price, stock_quantity, description, thumbnail) VALUES (?,?,?,?,?,?,?,?)";
        try (Connection conn = myConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            setProductParams(ps, t);
            if (ps.executeUpdate() > 0) {
                ResultSet rs = ps.getGeneratedKeys();
                if (rs.next()) return rs.getInt(1);
            }
        } catch (Exception e) { e.printStackTrace(); }
        return -1;
    }

    @Override
    public boolean update(Products t) {
        String sql = "UPDATE " + TABLE_NAME + " SET name=?, category_id=?, brand_id=?, supplier_id=?, price=?, stock_quantity=?, description=?, thumbnail=? WHERE product_id=?";
        try (Connection conn = myConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            setProductParams(ps, t);
            ps.setInt(9, t.getProductId());
            return ps.executeUpdate() > 0;
        } catch (Exception e) { e.printStackTrace(); }
        return false;
    }

    @Override public boolean delete(Products t) { return delete(t.getProductId()); }
    @Override public boolean delete(int id) {
        String sql = "DELETE FROM " + TABLE_NAME + " WHERE product_id = ?";
        try (Connection conn = myConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        } catch (Exception e) { e.printStackTrace(); }
        return false;
    }

    private Products mapResultSetToProduct(ResultSet rs) throws SQLException {
        return new Products(
            rs.getInt("product_id"), rs.getString("name"), rs.getInt("category_id"),
            rs.getInt("brand_id"), rs.getInt("supplier_id"), rs.getDouble("price"),
            rs.getInt("stock_quantity"), rs.getString("description"), rs.getString("thumbnail")
        );
    }

    private void setProductParams(PreparedStatement ps, Products t) throws SQLException {
        ps.setString(1, t.getName());
        ps.setInt(2, t.getCategoryId());
        ps.setInt(3, t.getBrandId());
        ps.setInt(4, t.getSupplierId());
        ps.setDouble(5, t.getPrice());
        ps.setInt(6, t.getStockQuantity());
        ps.setString(7, t.getDescription());
        ps.setString(8, t.getThumbnail());
    }
    // ================== THÊM VÀO CUỐI FILE ProductsDao.java ==================
    
    // 5. TÌM KIẾM
    public List<Products> search(String keyword) {
        List<Products> list = new ArrayList<>();
        String sql = "SELECT * FROM products WHERE name LIKE ? OR description LIKE ?";
        try (Connection conn = myConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            String key = "%" + keyword + "%";
            ps.setString(1, key);
            ps.setString(2, key);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) list.add(mapResultSetToProduct(rs));
            }
        } catch (Exception e) { e.printStackTrace(); }
        return list;
    }

    // 6. LỌC THEO DANH MỤC (Dùng cho HomePage click sang)
    public List<Products> getByCategory(int catId) {
        List<Products> list = new ArrayList<>();
        String sql = "SELECT * FROM products WHERE category_id = ?";
        try (Connection conn = myConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, catId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) list.add(mapResultSetToProduct(rs));
            }
        } catch (Exception e) { e.printStackTrace(); }
        return list;
    }
}

