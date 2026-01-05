/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package BTL.view;
import BTL.connect.MyConnection;
import BTL.entity.Orders;
import java.sql.*;
import java.util.Vector;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
// Trong class OrdersForm:
/**
 *
 * @author Admin
 */
public class OrdersForm extends javax.swing.JPanel {
    private final MainForm mainForm;
    DefaultTableModel tblModel;
    /**
     * Creates new form OrdersForm
     */
    public OrdersForm(MainForm mainForm) {
        initComponents();
        this.mainForm = mainForm;
        initTable();
        initComboBox();
        loadListNames();
        loadData();
        jList1.addListSelectionListener(new javax.swing.event.ListSelectionListener() {
        public void valueChanged(javax.swing.event.ListSelectionEvent evt) {
            jList1ValueChanged(evt);
        }
    });
    }
    private void initTable() {
    tblModel = new DefaultTableModel();
    tblModel.setColumnIdentifiers(new String[]{"Mã ĐH", "Tên Khách", "Tổng Tiền", "Trạng Thái", "Địa Chỉ", "Thanh Toán"});
    jTable1.setModel(tblModel);
}
    private void loadData() {
    tblModel.setRowCount(0);
    try {
        Connection conn = MyConnection.getInstance().getConnection(); //
        String sql = "SELECT * FROM orders";
        Statement st = conn.createStatement();
        ResultSet rs = st.executeQuery(sql);
        while (rs.next()) {
        Vector row = new Vector();
        row.add(rs.getInt("order_id")); 
        row.add(rs.getString("name")); 
    // Dùng String.valueOf hoặc format để đảm bảo tiền tệ hiển thị đúng dạng chuỗi
        row.add(String.format("%,.0f", rs.getDouble("total_amount"))); 
        row.add(rs.getString("status")); 
        row.add(rs.getString("shipping_address")); 
        row.add(rs.getString("payment_method")); 
        tblModel.addRow(row);
}
    } catch (Exception e) {
        e.printStackTrace();
    }
    }
    private void initComboBox() {
    // 1. Đổ dữ liệu cho Trạng Thái (jComboBox1)
    jComboBox1.removeAllItems();
    jComboBox1.addItem("pending");
    jComboBox1.addItem("confirmed");
    jComboBox1.addItem("shipping");
    jComboBox1.addItem("delivered");
    jComboBox1.addItem("cancelled");

    // 2. Đổ dữ liệu cho Hình Thức Thanh Toán (jComboBox2)
    jComboBox2.removeAllItems();
    jComboBox2.addItem("COD");
    jComboBox2.addItem("Banking");
}
    private void loadListNames() {
    javax.swing.DefaultListModel<Orders> listModel = new javax.swing.DefaultListModel<>();
    try {
        Connection conn = MyConnection.getInstance().getConnection();
        String sql = "SELECT * FROM orders ORDER BY order_id DESC"; 
        Statement st = conn.createStatement();
        ResultSet rs = st.executeQuery(sql);
        while (rs.next()) {
    Orders order = new Orders();
    order.setOrderId(rs.getInt("order_id"));
    order.setUserId(rs.getInt("user_id"));
    
    // PHẢI CÓ DÒNG NÀY: Nạp tên khách hàng vào đối tượng
    order.setName(rs.getString("name")); 
    
    // Nạp đúng địa chỉ vào shippingAddress
    order.setShippingAddress(rs.getString("shipping_address")); 
    
    order.setTotalAmount(rs.getDouble("total_amount"));
    order.setStatus(rs.getString("status"));
    order.setPaymentMethod(rs.getString("payment_method"));
    
    listModel.addElement(order);
}
// Ép kiểu chuẩn để hết gạch đỏ ở setModel
jList1.setModel((javax.swing.ListModel<String>)(Object)listModel);
    } catch (Exception e) { e.printStackTrace(); }
}
    private void jList1ValueChanged(javax.swing.event.ListSelectionEvent evt) {
    if (!evt.getValueIsAdjusting()) {
        Object selected = jList1.getSelectedValue();
        if (selected instanceof Orders) {
            Orders s = (Orders) selected;
            
            // Gán đúng thuộc tính vào đúng JTextField
            jTextField1.setText(s.getName());            // Ô Họ Tên
            jTextField3.setText(s.getShippingAddress()); // Ô Địa Chỉ (Chuẩn địa chỉ từ DB)
            jTextField2.setText(String.format("%.0f", s.getTotalAmount())); // Ô Số Tiền
            
            jComboBox1.setSelectedItem(s.getStatus());
            jComboBox2.setSelectedItem(s.getPaymentMethod());
            
            // Gọi hàm tải sản phẩm với ID duy nhất
            loadProductsByOrderId(s.getOrderId());
        }
    }
}
    private void loadProductsByOrderId(int id) {
    tblModel.setColumnIdentifiers(new String[]{"Tên Sản Phẩm", "Số Lượng", "Đơn Giá", "Thành Tiền"});
    tblModel.setRowCount(0);
    try {
        Connection conn = MyConnection.getInstance().getConnection();
        // Sửa od.price thành od.price_at_purchase
        String sql = "SELECT p.name, od.quantity, od.price_at_purchase " +
                     "FROM order_details od " +
                     "JOIN products p ON od.product_id = p.product_id " +
                     "WHERE od.order_id = ?";
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setInt(1, id);
        ResultSet rs = ps.executeQuery();
        while (rs.next()) {
            Vector row = new Vector();
            row.add(rs.getString(1));
            row.add(rs.getInt(2));
            double price = rs.getDouble(3); 
            row.add(String.format("%,.0f", price));
            row.add(String.format("%,.0f", rs.getInt(2) * price));
            tblModel.addRow(row);
        }
    } catch (Exception e) {
        JOptionPane.showMessageDialog(this, "Lỗi truy vấn: " + e.getMessage());
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

        jLabel1 = new javax.swing.JLabel();
        jPanel1 = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        jList1 = new javax.swing.JList<>();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jTextField1 = new javax.swing.JTextField();
        jTextField2 = new javax.swing.JTextField();
        jComboBox1 = new javax.swing.JComboBox<>();
        jComboBox2 = new javax.swing.JComboBox<>();
        jTextField3 = new javax.swing.JTextField();
        btnadd = new javax.swing.JButton();
        btnfix = new javax.swing.JButton();
        btndel = new javax.swing.JButton();
        btnreset = new javax.swing.JButton();
        btnconfirm = new javax.swing.JButton();
        jLabel7 = new javax.swing.JLabel();

        setPreferredSize(new java.awt.Dimension(600, 600));

        jLabel1.setFont(new java.awt.Font("Segoe UI", 0, 36)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(0, 51, 255));
        jLabel1.setText("QUẢN LÝ HÓA ĐƠN");

        jScrollPane2.setViewportView(jList1);

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 158, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 262, Short.MAX_VALUE)
                .addContainerGap())
        );

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

        jLabel2.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel2.setText("Họ Tên :");

        jLabel3.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel3.setText("Số Tiền :");

        jLabel4.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel4.setText("Trạng Thái :");

        jLabel5.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel5.setText("Địa Chỉ :");

        jLabel6.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel6.setText("Hình Thức Thanh Toán :");

        jTextField3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextField3ActionPerformed(evt);
            }
        });

        btnadd.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        btnadd.setText("Thêm");
        btnadd.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnaddActionPerformed(evt);
            }
        });

        btnfix.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        btnfix.setText("Sửa");
        btnfix.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnfixActionPerformed(evt);
            }
        });

        btndel.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        btndel.setText("Xóa");
        btndel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btndelActionPerformed(evt);
            }
        });

        btnreset.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        btnreset.setText("Làm mới");
        btnreset.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnresetActionPerformed(evt);
            }
        });

        btnconfirm.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        btnconfirm.setText("Thêm Sản Phẩm");
        btnconfirm.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnconfirmActionPerformed(evt);
            }
        });

        jLabel7.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel7.setText("Thông Tin Chi Tiết");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(btnconfirm, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(btnadd)
                                    .addGap(18, 18, 18)
                                    .addComponent(btnfix)
                                    .addGap(18, 18, 18)
                                    .addComponent(btndel)
                                    .addGap(18, 18, 18)
                                    .addComponent(btnreset))
                                .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                                    .addGap(6, 6, 6)
                                    .addComponent(jLabel6)
                                    .addGap(18, 18, 18)
                                    .addComponent(jComboBox2, javax.swing.GroupLayout.PREFERRED_SIZE, 205, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addGroup(layout.createSequentialGroup()
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 399, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGroup(layout.createSequentialGroup()
                                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(jLabel2)
                                            .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 56, javax.swing.GroupLayout.PREFERRED_SIZE))
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addGroup(layout.createSequentialGroup()
                                                .addComponent(jTextField2, javax.swing.GroupLayout.PREFERRED_SIZE, 123, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(jLabel4))
                                            .addGroup(layout.createSequentialGroup()
                                                .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, 123, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(jLabel5)))
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                            .addComponent(jTextField3)
                                            .addComponent(jComboBox1, 0, 136, Short.MAX_VALUE))))))
                        .addContainerGap(19, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(jLabel1)
                        .addGap(153, 153, 153))))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel7)
                .addGap(186, 186, 186))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(38, 38, 38)
                .addComponent(jLabel1)
                .addGap(57, 57, 57)
                .addComponent(jLabel7)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(42, 42, 42)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(btnconfirm, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(btnadd)
                            .addComponent(btnfix)
                            .addComponent(btndel)
                            .addComponent(btnreset)))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 124, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(33, 33, 33)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel5)
                            .addComponent(jTextField3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel2))
                        .addGap(30, 30, 30)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 18, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jTextField2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel4)
                            .addComponent(jComboBox1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(29, 29, 29)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel6)
                            .addComponent(jComboBox2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addContainerGap(98, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void btnconfirmActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnconfirmActionPerformed
        // TODO add your handling code here:
        int selectedRow = jTable1.getSelectedRow();
    
    if (selectedRow == -1) {
        javax.swing.JOptionPane.showMessageDialog(this, "Vui lòng chọn một hóa đơn từ bảng trước!");
        return;
    }

    try {
        // Lấy ID hóa đơn từ cột đầu tiên (cột 0) của dòng đang chọn
        int idDonHang = Integer.parseInt(jTable1.getValueAt(selectedRow, 0).toString());

        // Mở Dialog và truyền ID sang
        // 'null' có thể thay bằng Frame cha nếu bạn đang chạy từ một JFrame
        OrdersDetail detailDialog = new OrdersDetail(null, true, idDonHang);
        detailDialog.setLocationRelativeTo(null); // Hiển thị giữa màn hình
        detailDialog.setVisible(true);
        
        // Sau khi đóng dialog, load lại bảng để cập nhật tổng tiền (nếu cần)
        loadData(); 
        
    } catch (Exception e) {
        javax.swing.JOptionPane.showMessageDialog(this, "Lỗi khi mở chi tiết: " + e.getMessage());
    }
    // --- KẾT THÚC DÁN ---
    }//GEN-LAST:event_btnconfirmActionPerformed

    private void btnaddActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnaddActionPerformed
        // TODO add your handling code here:
        try {
        Connection conn = MyConnection.getInstance().getConnection();
        String sql = "INSERT INTO orders (name, total_amount, status, shipping_address, payment_method) VALUES (?, ?, ?, ?, ?)";
        PreparedStatement ps = conn.prepareStatement(sql);
        
        // Lấy tên từ ô nhập liệu (jTextField1)
        ps.setString(1, jTextField1.getText()); 
        
        ps.setDouble(2, Double.parseDouble(jTextField2.getText()));
        
        // Lấy giá trị đã chọn từ ComboBox
        ps.setString(3, jComboBox1.getSelectedItem().toString()); 
        
        ps.setString(4, jTextField3.getText());
        ps.setString(5, jComboBox2.getSelectedItem().toString());
        
        ps.executeUpdate();
        JOptionPane.showMessageDialog(this, "Thêm hóa đơn thành column thành công!");
        loadData();
    } catch (Exception e) {
        JOptionPane.showMessageDialog(this, "Lỗi: " + e.getMessage());
    }
    }//GEN-LAST:event_btnaddActionPerformed

    private void btnfixActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnfixActionPerformed
        // TODO add your handling code here:
        try {
        // 1. Khai báo và lấy giá trị từ JList
        Object selectedValue = jList1.getSelectedValue(); 
        
        // 2. Kiểm tra nếu chưa chọn gì
        if (selectedValue == null) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn một hóa đơn từ danh sách!");
            return;
        }

        // 3. Khai báo biến selectedOrder bằng cách ép kiểu chuẩn
        // Lưu ý: JList phải được nạp bằng DefaultListModel<Orders> thì mới ép kiểu được
        Orders selectedOrder = (Orders) selectedValue; 

        // 4. Lấy dữ liệu từ giao diện
        int orderId = selectedOrder.getOrderId(); // Bây giờ biến selectedOrder đã tồn tại
        String name = jTextField1.getText();
        String totalStr = jTextField2.getText().trim();
        
        if (totalStr.isEmpty()) {
            throw new NumberFormatException("Empty");
        }
        
        double totalAmount = Double.parseDouble(totalStr);
        String address = jTextField3.getText();
        String status = jComboBox1.getSelectedItem().toString();
        String payment = jComboBox2.getSelectedItem().toString();

        // 5. Thực thi SQL Update
        Connection conn = MyConnection.getInstance().getConnection();
        String sql = "UPDATE orders SET name=?, total_amount=?, status=?, shipping_address=?, payment_method=? WHERE order_id=?";
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setString(1, name);
        ps.setDouble(2, totalAmount);
        ps.setString(3, status);
        ps.setString(4, address);
        ps.setString(5, payment);
        ps.setInt(6, orderId);

        if (ps.executeUpdate() > 0) {
            JOptionPane.showMessageDialog(this, "Cập nhật thành công!");
            loadListNames(); // Làm mới danh sách để thấy thay đổi
        }
    } catch (NumberFormatException e) {
        JOptionPane.showMessageDialog(this, "Số tiền không hợp lệ hoặc bị trống!");
    } catch (Exception e) {
        e.printStackTrace();
    }
    }//GEN-LAST:event_btnfixActionPerformed

    private void btndelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btndelActionPerformed
        // TODO add your handling code here:
        int selectedRow = jTable1.getSelectedRow();
    if (selectedRow == -1) {
        JOptionPane.showMessageDialog(this, "Vui lòng chọn hóa đơn cần xóa!");
        return;
    }
    
    int confirm = JOptionPane.showConfirmDialog(this, "Xóa hóa đơn này sẽ xóa tất cả chi tiết liên quan. Bạn chắc chứ?");
    if (confirm == JOptionPane.YES_OPTION) {
        try {
            int orderId = Integer.parseInt(jTable1.getValueAt(selectedRow, 0).toString());
            Connection conn = MyConnection.getInstance().getConnection();
            String sql = "DELETE FROM orders WHERE order_id = ?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, orderId);
            ps.executeUpdate();
            loadData();
            btnresetActionPerformed(evt);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    }//GEN-LAST:event_btndelActionPerformed

    private void btnresetActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnresetActionPerformed
        // TODO add your handling code here:
        jTextField1.setText("");
        jTextField2.setText("");
        jTextField3.setText("");
        jComboBox1.setSelectedIndex(0);
        jComboBox2.setSelectedIndex(0);
        jTable1.clearSelection();
        jList1.clearSelection();
    
        // Quay lại hiển thị danh sách hóa đơn ban đầu
        initTable(); // Reset lại các cột (Mã ĐH, Tên Khách...)
        loadData();  // Tải lại toàn bộ hóa đơn
    }//GEN-LAST:event_btnresetActionPerformed

    private void jTextField3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField3ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextField3ActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnadd;
    private javax.swing.JButton btnconfirm;
    private javax.swing.JButton btndel;
    private javax.swing.JButton btnfix;
    private javax.swing.JButton btnreset;
    private javax.swing.JComboBox<String> jComboBox1;
    private javax.swing.JComboBox<String> jComboBox2;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JList<String> jList1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JTable jTable1;
    private javax.swing.JTextField jTextField1;
    private javax.swing.JTextField jTextField2;
    private javax.swing.JTextField jTextField3;
    // End of variables declaration//GEN-END:variables
}
