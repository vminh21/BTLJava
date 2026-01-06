package BTL.view;

import java.util.ArrayList;
import javax.swing.table.DefaultTableModel;
import javax.swing.JOptionPane;
import BTL.entity.Cate;
import BTL.bussiness.CateDao;
import java.util.List;




/**
 * Quản lý danh mục - Cấu trúc code tương tự BrandFrom
 * @author admin
 */
public class CateCRUD extends javax.swing.JPanel {
    // 1. KHAI BÁO BIẾN DÙNG CHUNG
    private BTL.bussiness.CateDao cateDao = new BTL.bussiness.CateDao();
    private DefaultTableModel model;
    private final MainForm mainForm;

    // 2. HÀM TẠO CỘT CHO BẢNG
    private void initTable() {
       model = new DefaultTableModel();
    model.setColumnIdentifiers(new Object[]{
        "ID", "Tên Danh Mục", "Mô Tả"
    });
    tblCate.setModel(model); // ⭐ DÒNG QUAN TRỌNG NHẤT
}   
    public void reset(){
        txtNameCate.setText("");
        txtDescCate.setText("");
        txtTimKiem.setText("");
        tblCate.clearSelection();
        loadData();
    }

    // 3. HÀM ĐỔ DỮ LIỆU TỪ CSDL VÀO BẢNG
    private void loadData() {
        model.setRowCount(0);
        // ⭐ Sửa getAll() thành getall() để khớp với Interface Dao
        List<Cate> list = cateDao.getall(); 
        if (list != null) {
            for (Cate c : list) {
                model.addRow(new Object[]{
                    c.getId(),
                    c.getName(),
                    c.getDescription()
                });
            }
        }
    }

    // Hàm này sẽ được gọi khi bấm nút Tìm
    private void searchCate() {
        String keyword = txtTimKiem.getText().trim().toLowerCase();
        model.setRowCount(0);
        
        // ⭐ Sửa getAll() thành getall()
        List<Cate> list = cateDao.getall(); 
        if (list != null) {
            for (Cate c : list) {
                // Lọc theo Tên HOẶC Mô tả
                if (c.getName().toLowerCase().contains(keyword) || 
                    c.getDescription().toLowerCase().contains(keyword)) {
                    model.addRow(new Object[]{ c.getId(), c.getName(), c.getDescription() });
                }
            }
        }
    }

    /**
     * Creates new form CateCRUD
     */
    public CateCRUD(MainForm mainForm) {
        initComponents();
        this.mainForm = mainForm;
        initTable(); 
        loadData();
    }

    /**
     * This method is called from within the constructor to initialize the form.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        btnTimKiem = new javax.swing.JButton();
        txtTimKiem = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        btnThem = new javax.swing.JButton();
        btnSua = new javax.swing.JButton();
        btnXoa = new javax.swing.JButton();
        btnLamMoi = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblCate = new javax.swing.JTable();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        txtNameCate = new javax.swing.JTextField();
        txtDescCate = new javax.swing.JTextField();

        setPreferredSize(new java.awt.Dimension(600, 600));

        jLabel1.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(0, 0, 255));
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setText("QUẢN LÝ DANH MỤC");

        btnTimKiem.setText("Tìm");
        btnTimKiem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnTimKiemActionPerformed(evt);
            }
        });

        jLabel2.setText("Nhập");

        btnThem.setText("Thêm");
        btnThem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnThemActionPerformed(evt);
            }
        });

        btnSua.setText("Sửa");
        btnSua.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSuaActionPerformed(evt);
            }
        });

        btnXoa.setText("Xóa");
        btnXoa.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnXoaActionPerformed(evt);
            }
        });

        btnLamMoi.setText("Làm mới");
        btnLamMoi.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnLamMoiActionPerformed(evt);
            }
        });

        tblCate.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null}
            },
            new String [] {
                "ID", "Tên Danh Mục", "Mô Tả"
            }
        ));
        tblCate.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblCateMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(tblCate);
        if (tblCate.getColumnModel().getColumnCount() > 0) {
            tblCate.getColumnModel().getColumn(0).setResizable(false);
        }

        jLabel3.setText("Tên Danh Mục");

        jLabel4.setText("Mô Tả");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(171, 171, 171)
                        .addComponent(jLabel1))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(29, 29, 29)
                        .addComponent(btnThem)
                        .addGap(77, 77, 77)
                        .addComponent(btnSua)
                        .addGap(81, 81, 81)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jLabel4)
                                .addGap(18, 18, 18)
                                .addComponent(txtDescCate, javax.swing.GroupLayout.PREFERRED_SIZE, 131, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(0, 39, Short.MAX_VALUE))
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(btnXoa)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(btnLamMoi)))))
                .addGap(47, 47, 47))
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(107, 107, 107)
                        .addComponent(jLabel2)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(txtTimKiem, javax.swing.GroupLayout.PREFERRED_SIZE, 193, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(26, 26, 26)
                        .addComponent(btnTimKiem))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(37, 37, 37)
                        .addComponent(jLabel3)
                        .addGap(18, 18, 18)
                        .addComponent(txtNameCate, javax.swing.GroupLayout.PREFERRED_SIZE, 127, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addComponent(jScrollPane1, javax.swing.GroupLayout.Alignment.TRAILING)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(32, 32, 32)
                .addComponent(jLabel1)
                .addGap(57, 57, 57)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnTimKiem)
                    .addComponent(txtTimKiem, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel2))
                .addGap(36, 36, 36)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 243, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 36, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(jLabel4)
                    .addComponent(txtNameCate, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtDescCate, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(71, 71, 71)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnThem)
                    .addComponent(btnSua)
                    .addComponent(btnXoa)
                    .addComponent(btnLamMoi))
                .addGap(24, 24, 24))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void btnThemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnThemActionPerformed
      String name = txtNameCate.getText().trim();
    String desc = txtDescCate.getText().trim();

    if (name.isEmpty()) {
        JOptionPane.showMessageDialog(this, "Chưa nhập tên danh mục");
        return;
    }

    Cate c = new Cate(0, name, desc);

    if (cateDao.insert(c) > 0) {
         JOptionPane.showMessageDialog(this, "Thêm thành công");
    loadData();
    clearForm();
}
    }//GEN-LAST:event_btnThemActionPerformed

    private void btnSuaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSuaActionPerformed
     int row = tblCate.getSelectedRow();
    if (row == -1) {
        JOptionPane.showMessageDialog(this, "Vui lòng chọn dòng cần sửa!");
        return;
    }

    int id = Integer.parseInt(tblCate.getValueAt(row, 0).toString());

    BTL.entity.Cate c = new BTL.entity.Cate(
        id,
        txtNameCate.getText().trim(),
        txtDescCate.getText().trim()
    );

    if (cateDao.update(c)) {
        JOptionPane.showMessageDialog(this, "Cập nhật thành công!");
        loadData();
        btnLamMoiActionPerformed(null);
    } else {
        JOptionPane.showMessageDialog(this, "Cập nhật thất bại!");
    }
    
    }//GEN-LAST:event_btnSuaActionPerformed

    private void btnXoaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnXoaActionPerformed
       int row = tblCate.getSelectedRow();
    if (row == -1) {
        JOptionPane.showMessageDialog(this, "Vui lòng chọn dòng để xóa!");
        return;
    }

    int confirm = JOptionPane.showConfirmDialog(
        this,
        "Bạn có chắc chắn muốn xóa danh mục này?",
        "Xác nhận xóa",
        JOptionPane.YES_NO_OPTION
    );

    if (confirm == JOptionPane.YES_OPTION) {
        int id = Integer.parseInt(tblCate.getValueAt(row, 0).toString());
        if (cateDao.delete(id)) {
            JOptionPane.showMessageDialog(this, "Xóa thành công!");
            loadData();
            btnLamMoiActionPerformed(null);
        } else {
            JOptionPane.showMessageDialog(this, "Xóa thất bại!");
        }
    }
    
    }//GEN-LAST:event_btnXoaActionPerformed

    private void btnLamMoiActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnLamMoiActionPerformed
    txtNameCate.setText("");
    txtDescCate.setText("");
    txtTimKiem.setText("");
    tblCate.clearSelection();
    loadData();
    }//GEN-LAST:event_btnLamMoiActionPerformed

    private void tblCateMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblCateMouseClicked
     int row = tblCate.getSelectedRow();
    if (row != -1) {
        txtNameCate.setText(tblCate.getValueAt(row, 1).toString());
        txtDescCate.setText(tblCate.getValueAt(row, 2).toString());
    }
    }//GEN-LAST:event_tblCateMouseClicked

    private void btnTimKiemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnTimKiemActionPerformed
    searchCate();
    }//GEN-LAST:event_btnTimKiemActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnLamMoi;
    private javax.swing.JButton btnSua;
    private javax.swing.JButton btnThem;
    private javax.swing.JButton btnTimKiem;
    private javax.swing.JButton btnXoa;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable tblCate;
    private javax.swing.JTextField txtDescCate;
    private javax.swing.JTextField txtNameCate;
    private javax.swing.JTextField txtTimKiem;
    // End of variables declaration//GEN-END:variables

    private void clearForm() {
       txtNameCate.setText("");
    txtDescCate.setText("");
}
}
