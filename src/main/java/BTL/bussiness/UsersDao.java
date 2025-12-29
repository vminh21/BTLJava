/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package BTL.bussiness;

import BTL.connect.MyConnection;
import BTL.entity.Users;
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
public class UsersDao implements Dao<Users> {

    private final String TABLE_NAME = "Users";
    MyConnection myConnection = MyConnection.getInstance();

    @Override
    public List<Users> getall() {
        List<Users> list = new ArrayList<>();
        Connection conn = myConnection.getConnection();
        PreparedStatement ps = null;
        String sql = "select * from " + TABLE_NAME;
        ResultSet rs = null;
        try {
            ps = conn.prepareStatement(sql);
            rs = ps.executeQuery();
            while (rs.next()) {
                list.add(new Users(
                    rs.getInt("user_id"),
                    rs.getString("full_name"),
                    rs.getString("email"),
                    rs.getString("password_hash"),
                    rs.getString("phone_number"),
                    rs.getString("address"),
                    rs.getString("gender"),
                    rs.getString("role"),
                    rs.getTimestamp("created_at")
                ));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    @Override
    public Optional<Users> get(int id) {
        Users user = new Users();
        Connection conn = myConnection.getConnection();
        PreparedStatement ps = null;
        String sql = "select * from " + TABLE_NAME + " where user_id = ?";
        ResultSet rs = null;
        try {
            ps = conn.prepareStatement(sql);
            ps.setInt(1, id);
            rs = ps.executeQuery();
            while (rs.next()) {
                user = new Users(
                    rs.getInt("user_id"),
                    rs.getString("full_name"),
                    rs.getString("email"),
                    rs.getString("password_hash"),
                    rs.getString("phone_number"),
                    rs.getString("address"),
                    rs.getString("gender"),
                    rs.getString("role"),
                    rs.getTimestamp("created_at")
                );
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Optional.of(user);
    }

    // Overload: Tìm user theo email (Dùng cho đăng nhập)
    public Optional<Users> get(String email) {
        Users user = null; // 1. Khởi tạo là null thay vì new Users()
        Connection conn = myConnection.getConnection();
        PreparedStatement ps = null;
        String sql = "select * from " + TABLE_NAME + " where email = ?";
        ResultSet rs = null;
        try {
            ps = conn.prepareStatement(sql);
            ps.setString(1, email);
            rs = ps.executeQuery();
            while (rs.next()) {
                // 2. Chỉ khi tìm thấy dòng dữ liệu, ta mới khởi tạo đối tượng Users
                user = new Users(
                    rs.getInt("user_id"),
                    rs.getString("full_name"),
                    rs.getString("email"),
                    rs.getString("password_hash"),
                    rs.getString("phone_number"),
                    rs.getString("address"),
                    rs.getString("gender"),
                    rs.getString("role"),
                    rs.getTimestamp("created_at")
                );
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        // 3. Trả về ofNullable: Nếu user là null thì trả về Optional rỗng (chuẩn bài!)
        return Optional.ofNullable(user);
    }

    @Override
    public int insert(Users t) {
        Connection conn = myConnection.getConnection();
        PreparedStatement ps = null;
        String sql = "insert into " + TABLE_NAME + "(full_name, email, password_hash, phone_number, address, gender, role) values(?, ?, ?, ?, ?, ?, ?)";
        ResultSet rs = null;
        try {
            ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, t.getFullName());
            ps.setString(2, t.getEmail());
            ps.setString(3, t.getPasswordHash());
            ps.setString(4, t.getPhoneNumber());
            ps.setString(5, t.getAddress());
            ps.setString(6, t.getGender());
            ps.setString(7, t.getRole());
            
            if (ps.executeUpdate() > 0) {
                rs = ps.getGeneratedKeys();
                while (rs.next()) {
                    return rs.getInt(1);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return -1;
    }

    @Override
    public boolean update(Users t) {
        Connection conn = myConnection.getConnection();
        PreparedStatement ps = null;
        String sql = "update " + TABLE_NAME + " set full_name = ?, email = ?, password_hash = ?, phone_number = ?, address = ?, gender = ?, role = ? where user_id = ?";
        try {
            ps = conn.prepareStatement(sql);
            ps.setString(1, t.getFullName());
            ps.setString(2, t.getEmail());
            ps.setString(3, t.getPasswordHash());
            ps.setString(4, t.getPhoneNumber());
            ps.setString(5, t.getAddress());
            ps.setString(6, t.getGender());
            ps.setString(7, t.getRole());
            ps.setInt(8, t.getUserId());
            
            if (ps.executeUpdate() > 0) {
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean delete(Users t) {
        Connection conn = myConnection.getConnection();
        PreparedStatement ps = null;
        String sql = "delete from " + TABLE_NAME + " where user_id = ?";
        try {
            ps = conn.prepareStatement(sql);
            ps.setInt(1, t.getUserId());
            if (ps.executeUpdate() > 0) {
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
    @Override
    public boolean delete(int id) {
        Connection conn = myConnection.getConnection();
        PreparedStatement ps = null;
        String sql = "delete from " + TABLE_NAME + " where user_id = ?";
        try {
            ps = conn.prepareStatement(sql);
            ps.setInt(1, id);
            if (ps.executeUpdate() > 0) {
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
    
    public static void main(String[] args) {
        UsersDao usersDao = new UsersDao();
        System.out.println("Tat ca du lieu");
        for(Users u : usersDao.getall()){
            System.out.println(u);
        }
        // insert
//        System.out.println("test insert");
//        Users u1 = new Users(0,"Test", "test@gmail.com", "est123", "0985772330", "Thanh Hóa", "Nam");
//        System.out.println(usersDao.insert(u1) );
        //Lay theo id
        System.out.println("Lay doi tuong theo id");
        System.out.println(usersDao.get(5).get());
        //update
//        System.out.println("update");
//        Users u2 = new Users(4,"hehe1", "hehe@gmail.com", "hehe1223", "123124324", "Nam Định", "Khác");
//        System.out.println("update " +usersDao.update(u2));
        //delete
//        System.out.println("Delete");
//        System.out.println("Delete "+usersDao.delete(u2));
            System.out.println("Delete theo id");
            System.out.println("Delete "+ usersDao.delete(5));
    }
}
