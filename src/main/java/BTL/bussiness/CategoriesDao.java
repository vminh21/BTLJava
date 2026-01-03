/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package BTL.bussiness;

import BTL.connect.MyConnection;
import BTL.entity.Categories;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 *
 * @author vanminh
 */
public class CategoriesDao implements Dao<Categories> {
    private final String TABLE_NAME = "categories";
    MyConnection myConnection = MyConnection.getInstance();

    @Override
    public List<Categories> getall() {
        List<Categories> list = new ArrayList<>();
        String sql = "SELECT * FROM " + TABLE_NAME;
        try (Connection conn = myConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                list.add(new Categories(rs.getInt("category_id"), rs.getString("name"), rs.getString("description")));
            }
        } catch (Exception e) { e.printStackTrace(); }
        return list;
    }

    @Override
    public Optional<Categories> get(int id) {
        String sql = "SELECT * FROM " + TABLE_NAME + " WHERE category_id = ?";
        try (Connection conn = myConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(new Categories(rs.getInt("category_id"), rs.getString("name"), rs.getString("description")));
                }
            }
        } catch (Exception e) { e.printStackTrace(); }
        return Optional.empty();
    }

    @Override
    public int insert(Categories t) {
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
    public boolean update(Categories t) {
        String sql = "UPDATE " + TABLE_NAME + " SET name = ?, description = ? WHERE category_id = ?";
        try (Connection conn = myConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, t.getName());
            ps.setString(2, t.getDescription());
            ps.setInt(3, t.getCategoryId());
            return ps.executeUpdate() > 0;
        } catch (Exception e) { e.printStackTrace(); }
        return false;
    }

    @Override
    public boolean delete(Categories t) { 
        return delete(t.getCategoryId()); 
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
