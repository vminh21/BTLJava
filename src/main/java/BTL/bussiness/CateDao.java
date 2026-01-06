package BTL.bussiness;

import BTL.connect.MyConnection;
import BTL.entity.Cate;
import java.sql.*;
import java.util.*;

/**
 * CateDao thực thi Interface Dao<Cate>
 * @author vanminh
 */
public class CateDao implements Dao<Cate> {
    private final String TABLE_NAME = "categories";
    MyConnection myConnection = MyConnection.getInstance();

    @Override
    public List<Cate> getall() {
        List<Cate> list = new ArrayList<>();
        String sql = "SELECT * FROM " + TABLE_NAME;
        try (Connection conn = myConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                // Đảm bảo tên cột trong DB là id, name, description
                list.add(new Cate(rs.getInt("category_id"), rs.getString("name"), rs.getString("description")));
            }
        } catch (Exception e) { e.printStackTrace(); }
        return list;
    }

    @Override
    public Optional<Cate> get(int id) {
        String sql = "SELECT * FROM " + TABLE_NAME + " WHERE category_id= ?";
        try (Connection conn = myConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(new Cate(rs.getInt("id"), rs.getString("name"), rs.getString("description")));
                }
            }
        } catch (Exception e) { e.printStackTrace(); }
        return Optional.empty();
    }

    @Override
    public int insert(Cate t) {
        String sql = "INSERT INTO " + TABLE_NAME + " (name, description) VALUES (?, ?)";
        try (Connection conn = myConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, t.getName());
            ps.setString(2, t.getDescription());
            if (ps.executeUpdate() > 0) {
                ResultSet rs = ps.getGeneratedKeys();
                if (rs.next()) return rs.getInt(1);
            }
        } catch (Exception e) { e.printStackTrace(); }
        return -1;
    }

    @Override
    public boolean update(Cate t) {
        String sql = "UPDATE " + TABLE_NAME + " SET name = ?, description = ? WHERE category_id = ?";
        try (Connection conn = myConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, t.getName());
            ps.setString(2, t.getDescription());
            ps.setInt(3, t.getId());
            return ps.executeUpdate() > 0;
        } catch (Exception e) { e.printStackTrace(); }
        return false;
    }

    @Override
    public boolean delete(Cate t) {
        return delete(t.getId());
    }

    @Override
    public boolean delete(int id) {
        String sql = "DELETE FROM " + TABLE_NAME + " WHERE category_id = ?";
        try (Connection conn = myConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        } catch (Exception e) { e.printStackTrace(); }
        return false;
    }

    
}