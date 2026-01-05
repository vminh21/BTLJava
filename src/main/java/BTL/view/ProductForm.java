/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package BTL.view;

// Import file ComboItem bạn đã tạo trong entity
import BTL.entity.ComboItem;
import BTL.connect.MyConnection;
import java.sql.*;
import java.util.Vector;
import javax.swing.DefaultComboBoxModel; // Thêm cái này để xử lý lỗi đỏ
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import javax.swing.JComboBox;
import javax.swing.JTextArea;

public class ProductForm extends javax.swing.JPanel {

    private DefaultTableModel tblModel;
// Kiểm tra dòng này ở cuối file ProductForm.java
    public ProductForm() {
        initComponents();
        // Lắng nghe thay đổi nội dung ô txtSearch để tìm kiếm ngay lập tức
        txtSearch.getDocument().addDocumentListener(new javax.swing.event.DocumentListener() {
            @Override
            public void insertUpdate(javax.swing.event.DocumentEvent e) {
                searchData();
            }

            @Override
            public void removeUpdate(javax.swing.event.DocumentEvent e) {
                searchData();
            }

            @Override
            public void changedUpdate(javax.swing.event.DocumentEvent e) {
                searchData();
            }
        });
        // --- SỬA LỖI ĐỎ COMBOBOX BẰNG CODE ---
        // Ép kiểu lại model cho 3 ComboBox để nó nhận được ComboItem
        cbLoai.setModel(new DefaultComboBoxModel<>());
        cbThuonghieu.setModel(new DefaultComboBoxModel<>());
        cbNsx.setModel(new DefaultComboBoxModel<>());
        // -------------------------------------

        initTable();
        loadComboBoxes();
        loadDataToTable();
        // Code thêm sự kiện click chuột thủ công
        jTable1.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                // Gọi hàm đổ dữ liệu
                bindingData();
            }
        });
    }

    // 1. Cấu hình bảng
    private void initTable() {
        tblModel = new DefaultTableModel();
        String[] columns = new String[]{"ID", "Tên SP", "Giá", "Số lượng", "Danh mục", "Thương hiệu", "NSX", "Mô tả"};
        tblModel.setColumnIdentifiers(columns);
        jTable1.setModel(tblModel);
    }

    // 2. Load dữ liệu vào ComboBox
    private void loadComboBoxes() {
        try {
            Connection conn = MyConnection.getInstance().getConnection();
            Statement st = conn.createStatement();

            // Load Danh mục
            DefaultComboBoxModel boxModel1 = (DefaultComboBoxModel) cbLoai.getModel();
            boxModel1.removeAllElements();
            ResultSet rs = st.executeQuery("SELECT category_id, name FROM categories");
            while (rs.next()) {
                // Giờ thì dòng này sẽ hết đỏ 100%
                boxModel1.addElement(new ComboItem(rs.getInt("category_id"), rs.getString("name")));
            }

            // Load Thương hiệu
            DefaultComboBoxModel boxModel2 = (DefaultComboBoxModel) cbThuonghieu.getModel();
            boxModel2.removeAllElements();
            rs = st.executeQuery("SELECT brand_id, name FROM brands");
            while (rs.next()) {
                boxModel2.addElement(new ComboItem(rs.getInt("brand_id"), rs.getString("name")));
            }

            // Load NSX
            DefaultComboBoxModel boxModel3 = (DefaultComboBoxModel) cbNsx.getModel();
            boxModel3.removeAllElements();
            rs = st.executeQuery("SELECT supplier_id, name FROM suppliers");
            while (rs.next()) {
                boxModel3.addElement(new ComboItem(rs.getInt("supplier_id"), rs.getString("name")));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // 3. Load dữ liệu lên Bảng
    private void loadDataToTable() {
        try {
            tblModel.setRowCount(0);
            Connection conn = MyConnection.getInstance().getConnection();
            String sql = "SELECT p.product_id, p.name, p.price, p.stock_quantity, p.description, "
                    + "c.name as cat_name, b.name as brand_name, s.name as sup_name "
                    + "FROM products p "
                    + "LEFT JOIN categories c ON p.category_id = c.category_id "
                    + "LEFT JOIN brands b ON p.brand_id = b.brand_id "
                    + "LEFT JOIN suppliers s ON p.supplier_id = s.supplier_id";

            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery(sql);

            while (rs.next()) {
                Vector<Object> row = new Vector<>();
                row.add(rs.getInt("product_id"));
                row.add(rs.getString("name"));
                row.add((long) rs.getDouble("price"));
                row.add(rs.getInt("stock_quantity"));
                row.add(rs.getString("cat_name"));
                row.add(rs.getString("brand_name"));
                row.add(rs.getString("sup_name"));
                row.add(rs.getString("description"));
                tblModel.addRow(row);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void searchData() {
        String keyword = txtSearch.getText().trim();
        try {
            tblModel.setRowCount(0); // Xóa trắng bảng
            Connection conn = MyConnection.getInstance().getConnection();

            // Câu SQL tìm kiếm trên tất cả các cột liên quan
            String sql = "SELECT p.product_id, p.name, p.price, p.stock_quantity, p.description, "
                    + "c.name as cat_name, b.name as brand_name, s.name as sup_name "
                    + "FROM products p "
                    + "LEFT JOIN categories c ON p.category_id = c.category_id "
                    + "LEFT JOIN brands b ON p.brand_id = b.brand_id "
                    + "LEFT JOIN suppliers s ON p.supplier_id = s.supplier_id "
                    + "WHERE p.name LIKE ? "
                    + // Theo tên
                    "OR p.product_id LIKE ? "
                    + // Theo ID
                    "OR c.name LIKE ? "
                    + // Theo thể loại
                    "OR b.name LIKE ? "
                    + // Theo thương hiệu
                    "OR s.name LIKE ?";                // Theo nhà sản xuất

            PreparedStatement ps = conn.prepareStatement(sql);
            String searchKey = "%" + keyword + "%";

            // Gán keyword cho tất cả 5 dấu hỏi chấm
            ps.setString(1, searchKey);
            ps.setString(2, searchKey);
            ps.setString(3, searchKey);
            ps.setString(4, searchKey);
            ps.setString(5, searchKey);

            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Vector<Object> row = new Vector<>();
                row.add(rs.getInt("product_id"));
                row.add(rs.getString("name"));
                row.add(rs.getDouble("price"));
                row.add(rs.getInt("stock_quantity"));
                row.add(rs.getString("cat_name"));
                row.add(rs.getString("brand_name"));
                row.add(rs.getString("sup_name"));
                row.add(rs.getString("description"));
                tblModel.addRow(row);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        jLabel1 = new javax.swing.JLabel();
        txtTensp = new javax.swing.JTextField();
        txtGia = new javax.swing.JTextField();
        txtSoluong = new javax.swing.JTextField();
        txtMota = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        btnRf = new javax.swing.JButton();
        btnSua = new javax.swing.JButton();
        btnXoa = new javax.swing.JButton();
        btnThem = new javax.swing.JButton();
        cbLoai = new javax.swing.JComboBox<>();
        cbThuonghieu = new javax.swing.JComboBox<>();
        cbNsx = new javax.swing.JComboBox<>();
        jLabel9 = new javax.swing.JLabel();
        txtSearch = new javax.swing.JTextField();

        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        jScrollPane1.setViewportView(jTable1);

        jLabel1.setFont(new java.awt.Font("Helvetica", 3, 24)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(153, 0, 153));
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setText("Quản lí sản phẩm");

        jLabel2.setText("Tên sản phẩm");

        jLabel3.setText("Loại sản phẩm");

        jLabel4.setText("Thương hiệu");

        jLabel5.setText("Giá bán");

        jLabel6.setText("Số lượng");

        jLabel7.setText("Nhà sản xuất");

        jLabel8.setText("Mô tả");

        btnRf.setText("Làm mới");
        btnRf.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRfActionPerformed(evt);
            }
        });

        btnSua.setText("Sửa");
        btnSua.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSuaActionPerformed(evt);
            }
        });

        btnXoa.setText("Xoá");
        btnXoa.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnXoaActionPerformed(evt);
            }
        });

        btnThem.setText("Thêm");
        btnThem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnThemActionPerformed(evt);
            }
        });

        cbLoai.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        cbThuonghieu.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        cbNsx.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        jLabel9.setText("Tìm kiếm");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(21, 21, 21)
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 554, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(120, 120, 120)
                        .addComponent(jLabel9)
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel1)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(txtSearch, javax.swing.GroupLayout.PREFERRED_SIZE, 213, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(0, 0, Short.MAX_VALUE)))))
                .addGap(17, 17, 17))
            .addGroup(layout.createSequentialGroup()
                .addGap(41, 41, 41)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                        .addComponent(jLabel2)
                        .addComponent(jLabel3)
                        .addComponent(jLabel4))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addGap(4, 4, 4)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(btnRf, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 82, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(btnXoa, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 82, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                        .addComponent(txtTensp, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(btnThem, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(btnSua, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 82, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addComponent(cbLoai, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(cbThuonghieu, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(63, 63, 63)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel5, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel6, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel7, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel8, javax.swing.GroupLayout.Alignment.TRAILING))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(txtMota)
                    .addComponent(txtGia)
                    .addComponent(txtSoluong, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(cbNsx, javax.swing.GroupLayout.PREFERRED_SIZE, 127, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(52, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(27, 27, 27)
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 39, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel9)
                    .addComponent(txtSearch, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 167, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtTensp, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel2)
                    .addComponent(jLabel7)
                    .addComponent(cbNsx, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtGia, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel3)
                    .addComponent(jLabel5)
                    .addComponent(cbLoai, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(25, 25, 25)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtSoluong, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel4)
                    .addComponent(jLabel6)
                    .addComponent(cbThuonghieu, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(txtMota, javax.swing.GroupLayout.PREFERRED_SIZE, 117, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel8)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(13, 13, 13)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(btnRf)
                            .addComponent(btnSua))
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(btnXoa)
                            .addComponent(btnThem))))
                .addGap(48, 48, 48))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void btnRfActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRfActionPerformed
        txtTensp.setText("");
        txtGia.setText("");
        txtSoluong.setText("");
        txtMota.setText("");

        // Reset về mục đầu tiên
        if (cbLoai.getItemCount() > 0) {
            cbLoai.setSelectedIndex(0);
        }
        if (cbThuonghieu.getItemCount() > 0) {
            cbThuonghieu.setSelectedIndex(0);
        }
        if (cbNsx.getItemCount() > 0) {
            cbNsx.setSelectedIndex(0);
        }
        jLabel1.setText("Quản lí sản phẩm");
        jTable1.clearSelection();
        loadDataToTable(); // Gọi hàm load lại bảng
    }//GEN-LAST:event_btnRfActionPerformed

    private void btnSuaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSuaActionPerformed
        int row = jTable1.getSelectedRow();
        if (row < 0) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn dòng cần sửa!");
            return;
        }

        try {
            int id = Integer.parseInt(tblModel.getValueAt(row, 0).toString());

            BTL.entity.ComboItem itemLoai = (BTL.entity.ComboItem) cbLoai.getSelectedItem();
            BTL.entity.ComboItem itemHieu = (BTL.entity.ComboItem) cbThuonghieu.getSelectedItem();
            BTL.entity.ComboItem itemNsx = (BTL.entity.ComboItem) cbNsx.getSelectedItem();

            Connection conn = MyConnection.getInstance().getConnection();
            String sql = "UPDATE products SET name=?, category_id=?, brand_id=?, supplier_id=?, price=?, stock_quantity=?, description=? WHERE product_id=?";
            PreparedStatement ps = conn.prepareStatement(sql);

            ps.setString(1, txtTensp.getText());
            ps.setInt(2, itemLoai.getId());
            ps.setInt(3, itemHieu.getId());
            ps.setInt(4, itemNsx.getId());
            ps.setDouble(5, Double.parseDouble(txtGia.getText()));
            ps.setInt(6, Integer.parseInt(txtSoluong.getText()));
            ps.setString(7, txtMota.getText());
            ps.setInt(8, id);

            ps.executeUpdate();
            JOptionPane.showMessageDialog(this, "Sửa thành công!");
            btnRf.doClick();

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Lỗi: " + e.getMessage());
        }
    }//GEN-LAST:event_btnSuaActionPerformed

    private void btnXoaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnXoaActionPerformed
        int row = jTable1.getSelectedRow();
        if (row < 0) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn dòng cần xóa!");
            return;
        }

        if (JOptionPane.showConfirmDialog(this, "Bạn chắc chắn muốn xóa?", "Xác nhận", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
            try {
                int id = Integer.parseInt(tblModel.getValueAt(row, 0).toString());
                Connection conn = MyConnection.getInstance().getConnection();
                PreparedStatement ps = conn.prepareStatement("DELETE FROM products WHERE product_id=?");
                ps.setInt(1, id);

                ps.executeUpdate();
                JOptionPane.showMessageDialog(this, "Đã xóa!");
                btnRf.doClick();

            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "Lỗi: " + e.getMessage());
            }
        }
    }//GEN-LAST:event_btnXoaActionPerformed

    private void btnThemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnThemActionPerformed
        try {
            if (txtTensp.getText().isEmpty()) {
                JOptionPane.showMessageDialog(this, "Vui lòng nhập tên sản phẩm!");
                return;
            }

            // Lấy ID từ ComboBox (Nhớ ép kiểu về ComboItem)
            BTL.entity.ComboItem itemLoai = (BTL.entity.ComboItem) cbLoai.getSelectedItem();
            BTL.entity.ComboItem itemHieu = (BTL.entity.ComboItem) cbThuonghieu.getSelectedItem();
            BTL.entity.ComboItem itemNsx = (BTL.entity.ComboItem) cbNsx.getSelectedItem();

            Connection conn = MyConnection.getInstance().getConnection();
            String sql = "INSERT INTO products (name, category_id, brand_id, supplier_id, price, stock_quantity, description) VALUES (?, ?, ?, ?, ?, ?, ?)";
            PreparedStatement ps = conn.prepareStatement(sql);

            ps.setString(1, txtTensp.getText());
            ps.setInt(2, itemLoai.getId());
            ps.setInt(3, itemHieu.getId());
            ps.setInt(4, itemNsx.getId());
            ps.setDouble(5, Double.parseDouble(txtGia.getText()));
            ps.setInt(6, Integer.parseInt(txtSoluong.getText()));
            ps.setString(7, txtMota.getText());

            ps.executeUpdate();
            JOptionPane.showMessageDialog(this, "Thêm thành công!");

            // Gọi nút làm mới để reset form
            btnRf.doClick();

        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Giá và Số lượng phải là số!");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Lỗi: " + e.getMessage());
            e.printStackTrace();
        }
    }//GEN-LAST:event_btnThemActionPerformed

    private void txtMotaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtMotaActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtMotaActionPerformed

    private void bindingData() {
        int row = jTable1.getSelectedRow();
        if (row >= 0) {
            // Cột 0 là ID
            // Cột 1 là Tên SP -> Kiểm tra xem trên bảng cột thứ 2 có phải là Tên SP không?
            txtTensp.setText(jTable1.getValueAt(row, 1).toString());

            // Cột 2 là Giá
            txtGia.setText(jTable1.getValueAt(row, 2).toString());

            // Cột 3 là Số lượng
            txtSoluong.setText(jTable1.getValueAt(row, 3).toString());

            // Cột 4, 5, 6 là các ComboBox
            setSelectedComboItem(cbLoai, jTable1.getValueAt(row, 4).toString());
            setSelectedComboItem(cbThuonghieu, jTable1.getValueAt(row, 5).toString());
            setSelectedComboItem(cbNsx, jTable1.getValueAt(row, 6).toString());

            // Cột 7 là Mô tả
            Object mota = jTable1.getValueAt(row, 7);
            if (mota != null) {
                // Thay thế dấu phẩy (,) thành dấu phẩy + xuống dòng (\n)
                String hienThiMota = mota.toString().replace(",", ",\n");
                txtMota.setText(hienThiMota);
            } else {
                txtMota.setText("");
            }
        }
    }

    // Hàm hỗ trợ chọn ComboBox (Dòng bạn đang bị đỏ)
    private void setSelectedComboItem(javax.swing.JComboBox cb, String nameToSelect) {
        if (nameToSelect == null) {
            return;
        }
        for (int i = 0; i < cb.getItemCount(); i++) {
            if (cb.getItemAt(i).toString().equalsIgnoreCase(nameToSelect)) {
                cb.setSelectedIndex(i);
                return;
            }
        }
    }

    private void jTable1MouseClicked(java.awt.event.MouseEvent evt) {
        // Thêm dòng này
        System.out.println("Đã click vào dòng: " + jTable1.getSelectedRow());

        bindingData(); // Hàm xử lý của bạn
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(ProductForm.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(ProductForm.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(ProductForm.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(ProductForm.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        }

    // Hàm này để gọi từ bên ngoài, giúp lọc sản phẩm theo Category_ID
    public void filterByCategory(int categoryId, String categoryName) {
        // 1. Đổi tên Label tiêu đề cho ngầu
        jLabel1.setText("Danh mục: " + categoryName);

        try {
            // 2. Xóa sạch bảng cũ
            tblModel.setRowCount(0);

            // 3. Kết nối và Query có điều kiện WHERE category_id = ?
            Connection conn = MyConnection.getInstance().getConnection();
            String sql = "SELECT p.product_id, p.name, p.price, p.stock_quantity, p.description, "
                    + "c.name as cat_name, b.name as brand_name, s.name as sup_name "
                    + "FROM products p "
                    + "LEFT JOIN categories c ON p.category_id = c.category_id "
                    + "LEFT JOIN brands b ON p.brand_id = b.brand_id "
                    + "LEFT JOIN suppliers s ON p.supplier_id = s.supplier_id "
                    + "WHERE p.category_id = ?"; // <--- QUAN TRỌNG NHẤT LÀ DÒNG NÀY

            java.sql.PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, categoryId); // Truyền ID danh mục vào dấu ?

            ResultSet rs = ps.executeQuery();

            // 4. Đổ dữ liệu vào bảng
            while (rs.next()) {
                Vector<Object> row = new Vector<>();
                row.add(rs.getInt("product_id"));
                row.add(rs.getString("name"));
                row.add(rs.getDouble("price"));
                row.add(rs.getInt("stock_quantity"));
                row.add(rs.getString("cat_name"));
                row.add(rs.getString("brand_name"));
                row.add(rs.getString("sup_name"));
                row.add(rs.getString("description"));
                tblModel.addRow(row);
            }

            // 5. Chọn sẵn cái ComboBox loại sản phẩm cho đúng logic
            // (Hàm setSelectedComboItem bạn đã có sẵn trong ProductForm rồi)
            setSelectedComboItem(cbLoai, categoryName);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnRf;
    private javax.swing.JButton btnSua;
    private javax.swing.JButton btnThem;
    private javax.swing.JButton btnXoa;
    private javax.swing.JComboBox<String> cbLoai;
    private javax.swing.JComboBox<String> cbNsx;
    private javax.swing.JComboBox<String> cbThuonghieu;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable jTable1;
    private javax.swing.JTextField txtGia;
    private javax.swing.JTextField txtMota;
    private javax.swing.JTextField txtSearch;
    private javax.swing.JTextField txtSoluong;
    private javax.swing.JTextField txtTensp;
    // End of variables declaration//GEN-END:variables
}
