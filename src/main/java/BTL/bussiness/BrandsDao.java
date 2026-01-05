package BTL.bussiness;

import BTL.connect.MyConnection;
import BTL.entity.Brands;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

public class BrandsDao {

    // 1. LẤY DANH SÁCH (QUAN TRỌNG NHẤT: Sửa tên cột cho đúng SQL)
    public ArrayList<Brands> getAll() {
        ArrayList<Brands> list = new ArrayList<>();
        // Câu lệnh SQL lấy tất cả
        String sql = "SELECT * FROM brands";

        try {
            Connection conn = MyConnection.getInstance().getConnection();
            PreparedStatement ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Brands b = new Brands();
                
                // --- SỬA LỖI TẠI ĐÂY ---
                // Trong SQL cột ID thường tên là "id", nhưng bạn đang getInt("brand_id") nên lỗi.
                // Tôi đã sửa thành "id". Nếu trong SQL của bạn khác, hãy sửa chữ trong ngoặc kép.
                try {
                    b.setBrand_id(rs.getInt("id")); 
                } catch (Exception e) {
                    // Phòng trường hợp trong SQL bạn đặt là "brand_id" thật
                    b.setBrand_id(rs.getInt("brand_id"));
                }

                b.setName(rs.getString("name"));     // Cột tên trong SQL
                b.setOrigin(rs.getString("origin")); // Cột xuất xứ trong SQL
                
                list.add(b);
            }
        } catch (Exception e) {
            System.out.println("Lỗi lấy danh sách thương hiệu: " + e.getMessage());
            e.printStackTrace();
        }
        return list;
    }

    // 2. THÊM MỚI
    public int insert(Brands b) {
        // ID tự tăng nên không cần thêm vào câu INSERT
        String sql = "INSERT INTO brands (name, origin) VALUES (?, ?)";
        try {
            Connection conn = MyConnection.getInstance().getConnection();
            PreparedStatement ps = conn.prepareStatement(sql);
            
            ps.setString(1, b.getName());
            ps.setString(2, b.getOrigin());

            return ps.executeUpdate(); // Trả về 1 nếu thành công
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    // 3. SỬA (CẬP NHẬT)
    public boolean update(Brands b) {
        // Sửa lại WHERE id = ? cho khớp với SQL
        String sql = "UPDATE brands SET name = ?, origin = ? WHERE id = ?"; 
        try {
            Connection conn = MyConnection.getInstance().getConnection();
            PreparedStatement ps = conn.prepareStatement(sql);

            ps.setString(1, b.getName());
            ps.setString(2, b.getOrigin());
            ps.setInt(3, b.getBrand_id()); // Lấy ID từ đối tượng Java để tìm dòng cần sửa

            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            // Nếu lỗi, thử lại với trường hợp tên cột là brand_id
            try {
                String sqlBackup = "UPDATE brands SET name = ?, origin = ? WHERE brand_id = ?";
                Connection conn = MyConnection.getInstance().getConnection();
                PreparedStatement ps = conn.prepareStatement(sqlBackup);
                ps.setString(1, b.getName());
                ps.setString(2, b.getOrigin());
                ps.setInt(3, b.getBrand_id());
                return ps.executeUpdate() > 0;
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        return false;
    }

    // 4. XÓA
    public boolean delete(int id) {
        // Sửa lại WHERE id = ?
        String sql = "DELETE FROM brands WHERE id = ?";
        try {
            Connection conn = MyConnection.getInstance().getConnection();
            PreparedStatement ps = conn.prepareStatement(sql);

            ps.setInt(1, id);

            return ps.executeUpdate() > 0;
        } catch (Exception e) {
             // Nếu lỗi, thử lại với brand_id
            try {
                String sql2 = "DELETE FROM brands WHERE brand_id = ?";
                Connection conn = MyConnection.getInstance().getConnection();
                PreparedStatement ps = conn.prepareStatement(sql2);
                ps.setInt(1, id);
                return ps.executeUpdate() > 0;
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        return false;
    }
}