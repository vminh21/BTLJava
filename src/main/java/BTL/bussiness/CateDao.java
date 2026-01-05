package BTL.bussiness;

import BTL.connect.MyConnection;
import BTL.entity.Cate;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

public class CateDao {

    // ================== 1. LẤY DANH SÁCH ==================
    public ArrayList<Cate> getAll() {
        ArrayList<Cate> list = new ArrayList<>();
        String sql = "SELECT * FROM categories";

        try {
            Connection conn = MyConnection.getInstance().getConnection();
            PreparedStatement ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Cate c = new Cate();
                c.setId(rs.getInt("category_id"));   // ⭐ ĐỔI TÊN CỘT Ở ĐÂY NẾU DB KHÁC
                c.setName(rs.getString("name"));
                c.setDescription(rs.getString("description"));
                list.add(c);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    // ================== 2. THÊM ==================
    public int insert(Cate c) {
        String sql = "INSERT INTO categories (name, description) VALUES (?, ?)";
        try {
            Connection conn = MyConnection.getInstance().getConnection();
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, c.getName());
            ps.setString(2, c.getDescription());
            return ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    // ================== 3. SỬA ==================
    public boolean update(Cate c) {
        String sql = "UPDATE categories SET name = ?, description = ? WHERE category_id = ?";
        try {
            Connection conn = MyConnection.getInstance().getConnection();
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, c.getName());
            ps.setString(2, c.getDescription());
            ps.setInt(3, c.getId());
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    // ================== 4. XÓA ==================
    public boolean delete(int id) {
        String sql = "DELETE FROM categories WHERE category_id = ?";
        try {
            Connection conn = MyConnection.getInstance().getConnection();
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
}
