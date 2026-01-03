/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package BTL.bussiness;

import BTL.connect.MyConnection;
import BTL.entity.Notification;
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

public class NotificationDao implements Dao<Notification> {

    private final String TABLE_NAME = "notifications";
    MyConnection myConnection = MyConnection.getInstance();

    @Override
    public List<Notification> getall() {
        List<Notification> list = new ArrayList<>();
        Connection conn = myConnection.getConnection();
        String sql = "select * from " + TABLE_NAME + " order by created_at desc";
        try (PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                list.add(new Notification(
                    rs.getInt("notification_id"),
                    rs.getInt("user_id"),
                    rs.getString("title"),
                    rs.getString("message"),
                    rs.getString("image"),
                    rs.getTimestamp("created_at")
                ));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    @Override
    public Optional<Notification> get(int id) {
        Notification note = null;
        Connection conn = myConnection.getConnection();
        String sql = "select * from " + TABLE_NAME + " where notification_id = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    note = new Notification(
                        rs.getInt("notification_id"),
                        rs.getInt("user_id"),
                        rs.getString("title"),
                        rs.getString("message"),
                        rs.getString("image"),
                        rs.getTimestamp("created_at")
                    );
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Optional.ofNullable(note);
    }

    // Hàm bổ sung: Lấy thông báo theo User ID (Vì thông báo thường đi theo từng khách)
    public List<Notification> getByUserId(int userId) {
        List<Notification> list = new ArrayList<>();
        Connection conn = myConnection.getConnection();
        String sql = "select * from " + TABLE_NAME + " where user_id = ? order by created_at desc";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, userId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(new Notification(
                        rs.getInt("notification_id"),
                        rs.getInt("user_id"),
                        rs.getString("title"),
                        rs.getString("message"),
                        rs.getString("image"),
                        rs.getTimestamp("created_at")
                    ));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    @Override
    public int insert(Notification t) {
        Connection conn = myConnection.getConnection();
        String sql = "insert into " + TABLE_NAME + "(user_id, title, message, image) values(?, ?, ?, ?)";
        try (PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, t.getUserId());
            ps.setString(2, t.getTitle());
            ps.setString(3, t.getMessage());
            ps.setString(4, t.getImage());
            
            if (ps.executeUpdate() > 0) {
                try (ResultSet rs = ps.getGeneratedKeys()) {
                    if (rs.next()) {
                        return rs.getInt(1);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return -1;
    }

    @Override
    public boolean update(Notification t) {
        Connection conn = myConnection.getConnection();
        String sql = "update " + TABLE_NAME + " set user_id = ?, title = ?, message = ?, image = ? where notification_id = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, t.getUserId());
            ps.setString(2, t.getTitle());
            ps.setString(3, t.getMessage());
            ps.setString(4, t.getImage());
            ps.setInt(5, t.getNotificationId());
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean delete(Notification t) {
        return delete(t.getNotificationId());
    }

    @Override
    public boolean delete(int id) {
        Connection conn = myConnection.getConnection();
        String sql = "delete from " + TABLE_NAME + " where notification_id = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
}