/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package BTL.bussiness;
import BTL.connect.MyConnection;
import BTL.entity.Orders;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
/**
 *
 * @author Administrator
 */
public class StatisticalDao implements Dao<Orders> {
    private final String TABLE_NAME = "orders"; 
    private MyConnection myConnection = MyConnection.getInstance();

    public List<Orders> getFiltered(String type, int d, int m, int y) {
        List<Orders> list = new ArrayList<>();
        String sql = "";
        
 
        

        if (type.equals("Ngay")) {
            sql = "SELECT * FROM " + TABLE_NAME + " WHERE DAY(created_at)=? AND MONTH(created_at)=? AND YEAR(created_at)=?";
        } else if (type.equals("Thang")) {
            sql = "SELECT * FROM " + TABLE_NAME + " WHERE MONTH(created_at)=? AND YEAR(created_at)=?";
        } else {
            sql = "SELECT * FROM " + TABLE_NAME + " WHERE YEAR(created_at)=?";
        }

        
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            conn = myConnection.getConnection(); 
            ps = conn.prepareStatement(sql);
            
            System.out.println("Executing SQL: " + type + " -> " + d + "/" + m + "/" + y);

            if (type.equals("Ngay")) {
                ps.setInt(1, d); ps.setInt(2, m); ps.setInt(3, y);
            } else if (type.equals("Thang")) {
                ps.setInt(1, m); ps.setInt(2, y);
            } else {
                ps.setInt(1, y);
            }

            rs = ps.executeQuery();
            while (rs.next()) {
                list.add(new Orders(
                    rs.getInt("order_id"),
                    rs.getInt("user_id"),
                    rs.getDouble("total_amount"),
                    rs.getString("status"),
                    rs.getString("shipping_address"),
                    rs.getString("payment_method"),
                    rs.getTimestamp("created_at")
                ));
            }
        } catch (Exception e) {
            System.err.println("Lỗi tại StatisticalDao: " + e.getMessage());
            e.printStackTrace();
        } finally {
            try {
                if (rs != null) rs.close();
                if (ps != null) ps.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return list;
    
    }
    @Override
    public List<Orders> getall() {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public Optional<Orders> get(int id) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public int insert(Orders t) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public boolean update(Orders t) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public boolean delete(Orders t) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public boolean delete(int id) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }
    
}
