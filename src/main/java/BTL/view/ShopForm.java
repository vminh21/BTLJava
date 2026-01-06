/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package BTL.view;

import BTL.bussiness.CartDao;
import BTL.bussiness.CateDao;
import BTL.bussiness.ProductsDao;
import BTL.entity.Cate;
import BTL.entity.Products;
import BTL.entity.Session; // Import Session để lấy User ID
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.swing.JOptionPane;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableModel;

public class ShopForm extends javax.swing.JPanel {

    private DefaultTableModel model;
    private CustomersForm customersForm;
    
    // --- 1. KHAI BÁO 3 DAO CẦN DÙNG ---
    private final ProductsDao productsDao = new ProductsDao();
    private final CateDao cateDao = new CateDao();
    private final CartDao cartDao = new CartDao();
    
    // Format tiền tệ cho đẹp (Ví dụ: 15,000,000)
    private final DecimalFormat df = new DecimalFormat("#,###");

    // --- BÍ KÍP: Map để tra cứu nhanh (ID -> Tên Thể Loại) ---
    // Cái này thay thế cho việc JOIN bảng trong SQL
    private Map<Integer, String> categoryMap = new HashMap<>();

    // Constructor chính
    public ShopForm(CustomersForm customersForm) {
        initComponents();
        this.customersForm = customersForm;
        initAll(""); // Load tất cả sản phẩm
    }
    
    // Constructor hỗ trợ lọc từ HomePage (khi bấm vào danh mục bên ngoài)
    public ShopForm(String categoryName) {
        initComponents();
        txtTimkiem.setText(categoryName);
        initAll(categoryName);
    }
    // Hàm khởi tạo chung
    private void initAll(String keyword) {
        loadCategoryMap(); 
        setupTable();      
        loadDataToTable(keyword); 
        setupSearch();     
        
        // --- THÊM ĐOẠN NÀY ---
        // Cấu hình cho ô Mô tả đẹp hơn
        txtMota.setLineWrap(true);        // Tự động xuống dòng khi hết khung
        txtMota.setWrapStyleWord(true);   // Ngắt dòng theo từ (không cắt đôi chữ)
        txtMota.setEditable(false);       // Chỉ cho đọc, không cho người dùng sửa
        
        // Gọi hàm bắt sự kiện click chuột
        setupTableEvents();
    }
    // --- BẮT SỰ KIỆN CLICK CHUỘT VÀO BẢNG ---
    private void setupTableEvents() {
        jTable1.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                // 1. Lấy dòng đang chọn
                int row = jTable1.getSelectedRow();
                
                if (row >= 0) {
                    // 2. Lấy dữ liệu cột Mô tả (Cột thứ 5 - Index 5)
                    // Thứ tự cột: 0:ID, 1:Tên, 2:Loại, 3:Giá, 4:Kho, 5:Mô tả
                    Object motaObj = jTable1.getValueAt(row, 5);
                    
                    // 3. Kiểm tra null và hiển thị lên txtMota
                    if (motaObj != null) {
                        txtMota.setText(motaObj.toString());
                    } else {
                        txtMota.setText("Không có mô tả cho sản phẩm này.");
                    }
                    
                    // (Tùy chọn) Kéo thanh cuộn lên đầu nếu mô tả quá dài
                    txtMota.setCaretPosition(0);
                }
            }
        });
    }

    // --- Bước 1: Lấy toàn bộ danh mục từ CateDao lưu vào Map ---
    private void loadCategoryMap() {
        List<Cate> list = cateDao.getall();
        for (Cate c : list) {
            // Lưu cặp: ID là khóa, Tên là giá trị (VD: 1 -> "Laptop")
            categoryMap.put(c.getId(), c.getName());
        }
    }

    // --- Bước 2: Cấu hình bảng ---
    private void setupTable() {
        // Cột: 0:ID, 1:Tên SP, 2:Thể loại, 3:Giá, 4:Kho, 5:Mô tả
        String[] headers = {"ID", "Tên Sản Phẩm", "Thể loại", "Giá (VND)", "Còn lại", "Mô tả"};
        model = new DefaultTableModel(headers, 0) {
            @Override
            public boolean isCellEditable(int row, int col) { return false; } // Không cho sửa
        };
        jTable1.setModel(model);

        // Ẩn cột ID (Cột 0) để giao diện đẹp nhưng vẫn lấy được ID để mua hàng
        jTable1.getColumnModel().getColumn(0).setMinWidth(0);
        jTable1.getColumnModel().getColumn(0).setMaxWidth(0);
        jTable1.getColumnModel().getColumn(0).setPreferredWidth(0);
    }

    // --- Bước 3: Đổ dữ liệu sản phẩm (Dùng ProductsDao) ---
    private void loadDataToTable(String keyword) {
        model.setRowCount(0);
        
        // Gọi DAO tìm kiếm sản phẩm
        List<Products> list = productsDao.search(keyword);
        
        for (Products p : list) {
            // --- KỸ THUẬT GHÉP TÊN ---
            // Dựa vào categoryId của sản phẩm, tra trong Map để lấy Tên Thể Loại
            // Nếu không tìm thấy thì hiện "Khác"
            String catName = categoryMap.getOrDefault(p.getCategoryId(), "Khác");
            
            model.addRow(new Object[]{
                p.getProductId(),
                p.getName(),
                catName,             // Hiển thị Tên loại (Laptop) chứ không phải số (1)
                df.format(p.getPrice()), // Format giá
                p.getStockQuantity(),
                p.getDescription()
            });
        }
    }

    // --- Bước 4: Sự kiện tìm kiếm ---
    private void setupSearch() {
        txtTimkiem.getDocument().addDocumentListener(new DocumentListener() {
            public void insertUpdate(DocumentEvent e) { loadDataToTable(txtTimkiem.getText()); }
            public void removeUpdate(DocumentEvent e) { loadDataToTable(txtTimkiem.getText()); }
            public void changedUpdate(DocumentEvent e) { loadDataToTable(txtTimkiem.getText()); }
        });
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
        btnThem = new javax.swing.JButton();
        txtTimkiem = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        txtMota = new javax.swing.JTextArea();

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

        jLabel1.setFont(new java.awt.Font("Helvetica Neue", 3, 24)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(204, 153, 0));
        jLabel1.setText("Sản Phẩm");

        btnThem.setText("Thêm vào giỏ hàng");
        btnThem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnThemActionPerformed(evt);
            }
        });

        jLabel2.setText("Tìm kiếm:");

        txtMota.setColumns(20);
        txtMota.setRows(5);
        jScrollPane2.setViewportView(txtMota);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 600, Short.MAX_VALUE)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(225, 225, 225)
                        .addComponent(jLabel1))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(141, 141, 141)
                        .addComponent(jLabel2)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(txtTimkiem, javax.swing.GroupLayout.PREFERRED_SIZE, 185, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(174, 174, 174)
                        .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(219, 219, 219)
                        .addComponent(btnThem)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(19, 19, 19)
                .addComponent(jLabel1)
                .addGap(40, 40, 40)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtTimkiem, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel2))
                .addGap(18, 18, 18)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 232, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(32, 32, 32)
                .addComponent(btnThem)
                .addContainerGap(86, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void btnThemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnThemActionPerformed
    // 1. Lấy ID người dùng
        int currentUserId = Session.userId;
        if (currentUserId == 0) {
            JOptionPane.showMessageDialog(this, "Bạn chưa đăng nhập!");
            return;
        }

        // 2. Kiểm tra chọn dòng
        int selectedRow = jTable1.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn sản phẩm!");
            return;
        }

        // 3. Lấy thông tin sản phẩm
        int productId = Integer.parseInt(jTable1.getValueAt(selectedRow, 0).toString());
        String productName = jTable1.getValueAt(selectedRow, 1).toString();
        
        // Lấy số lượng tồn kho (Cột index 4)
        int stock = Integer.parseInt(jTable1.getValueAt(selectedRow, 4).toString());

        // 4. Kiểm tra tồn kho
        if (stock <= 0) {
            JOptionPane.showMessageDialog(this, "Sản phẩm này tạm hết hàng!");
            return;
        }

        // 5. Thêm vào giỏ hàng
        if (cartDao.addToCart(currentUserId, productId, 1)) {
            JOptionPane.showMessageDialog(this, "Đã thêm '" + productName + "' vào giỏ hàng!");
            
            // --- ĐOẠN CODE MỚI: TRỪ SỐ LƯỢNG TRÊN BẢNG ---
            // Cập nhật lại ô số lượng (Cột 4) thành stock - 1
            jTable1.setValueAt(stock - 1, selectedRow, 4);
            
            // (Tùy chọn) Nếu muốn bảng đẹp hơn, mày có thể cập nhật lại biến model
            // model.setValueAt(stock - 1, selectedRow, 4);
            // ----------------------------------------------
            
        } else {
            JOptionPane.showMessageDialog(this, "Lỗi hệ thống! Không thể thêm vào giỏ.");
        }
    }//GEN-LAST:event_btnThemActionPerformed

    /**
     * @param args the command line arguments
     */
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnThem;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JTable jTable1;
    private javax.swing.JTextArea txtMota;
    private javax.swing.JTextField txtTimkiem;
    // End of variables declaration//GEN-END:variables
}
