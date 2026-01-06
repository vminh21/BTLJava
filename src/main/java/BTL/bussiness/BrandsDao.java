package BTL.bussiness;

import BTL.connect.MyConnection;
import BTL.entity.Brands;
import java.sql.*;
import java.util.*;

/**
 * @author vanminh
 */
public class BrandsDao implements Dao<Brands> {
    private final String TABLE_NAME = "brands";
    MyConnection myConnection = MyConnection.getInstance();

    @Override
    public List<Brands> getall() {
        List<Brands> list = new ArrayList<>();
        String sql = "SELECT * FROM " + TABLE_NAME;
        try (Connection conn = myConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                // Kiểm tra tên cột trong DB của bạn là 'brand_id' hay 'id'
                list.add(new Brands(rs.getInt("brand_id"), rs.getString("name"), rs.getString("origin")));
            }
        } catch (Exception e) { e.printStackTrace(); }
        return list;
    }

    @Override
    public Optional<Brands> get(int id) {
        String sql = "SELECT * FROM " + TABLE_NAME + " WHERE brand_id = ?";
        try (Connection conn = myConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(new Brands(rs.getInt("brand_id"), rs.getString("name"), rs.getString("origin")));
                }
            }
        } catch (Exception e) { e.printStackTrace(); }
        return Optional.empty();
    }

    @Override
    public int insert(Brands t) {
        String sql = "INSERT INTO " + TABLE_NAME + " (name, origin) VALUES (?, ?)";
        try (Connection conn = myConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, t.getName());
            ps.setString(2, t.getOrigin());
            if (ps.executeUpdate() > 0) {
                ResultSet rs = ps.getGeneratedKeys();
                if (rs.next()) return rs.getInt(1);
            }
        } catch (Exception e) { e.printStackTrace(); }
        return -1;
    }

    @Override
    public boolean update(Brands t) {
        String sql = "UPDATE " + TABLE_NAME + " SET name = ?, origin = ? WHERE brand_id = ?";
        try (Connection conn = myConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, t.getName());
            ps.setString(2, t.getOrigin());
            ps.setInt(3, t.getBrandId()); // Đảm bảo hàm này có trong entity Brands
            return ps.executeUpdate() > 0;
        } catch (Exception e) { e.printStackTrace(); }
        return false;
    }

    @Override 
    public boolean delete(Brands t) { 
        return delete(t.getBrandId()); 
    }

    @Override 
    public boolean delete(int id) {
        String sql = "DELETE FROM " + TABLE_NAME + " WHERE brand_id = ?";
        try (Connection conn = myConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        } catch (Exception e) { e.printStackTrace(); }
        return false;
    }
}