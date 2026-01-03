/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package BTL.bussiness;
import BTL.connect.MyConnection;
import BTL.entity.Suppliers;
import java.sql.*;
import java.util.*;

/**
 *
 * @author vanminh
 */


public class SuppliersDao implements Dao<Suppliers> {
    private final String TABLE_NAME = "suppliers";
    MyConnection myConnection = MyConnection.getInstance();

    @Override
    public List<Suppliers> getall() {
        List<Suppliers> list = new ArrayList<>();
        String sql = "SELECT * FROM " + TABLE_NAME;
        try (Connection conn = myConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                list.add(new Suppliers(rs.getInt("supplier_id"), rs.getString("name"), rs.getString("phone_number"), rs.getString("email"), rs.getString("address")));
            }
        } catch (Exception e) { e.printStackTrace(); }
        return list;
    }

    @Override
    public Optional<Suppliers> get(int id) {
        String sql = "SELECT * FROM " + TABLE_NAME + " WHERE supplier_id = ?";
        try (Connection conn = myConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return Optional.of(new Suppliers(rs.getInt("supplier_id"), rs.getString("name"), rs.getString("phone_number"), rs.getString("email"), rs.getString("address")));
            }
        } catch (Exception e) { e.printStackTrace(); }
        return Optional.empty();
    }

    @Override
    public int insert(Suppliers t) {
        String sql = "INSERT INTO " + TABLE_NAME + " (name, phone_number, email, address) VALUES (?,?,?,?)";
        try (Connection conn = myConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, t.getName());
            ps.setString(2, t.getPhoneNumber());
            ps.setString(3, t.getEmail());
            ps.setString(4, t.getAddress());
            if (ps.executeUpdate() > 0) {
                ResultSet rs = ps.getGeneratedKeys();
                if (rs.next()) return rs.getInt(1);
            }
        } catch (Exception e) { e.printStackTrace(); }
        return -1;
    }

    @Override
    public boolean update(Suppliers t) {
        String sql = "UPDATE " + TABLE_NAME + " SET name=?, phone_number=?, email=?, address=? WHERE supplier_id=?";
        try (Connection conn = myConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, t.getName());
            ps.setString(2, t.getPhoneNumber());
            ps.setString(3, t.getEmail());
            ps.setString(4, t.getAddress());
            ps.setInt(5, t.getSupplierId());
            return ps.executeUpdate() > 0;
        } catch (Exception e) { e.printStackTrace(); }
        return false;
    }

    @Override public boolean delete(Suppliers t) { return delete(t.getSupplierId()); }
    @Override public boolean delete(int id) {
        String sql = "DELETE FROM " + TABLE_NAME + " WHERE supplier_id = ?";
        try (Connection conn = myConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        } catch (Exception e) { e.printStackTrace(); }
        return false;
    }
}