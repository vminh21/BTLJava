/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package BTL.view;

import BTL.bussiness.CateDao;
import BTL.entity.Cate;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingConstants;

/**
 *
 * @author macbook
 */
public class HomePage extends javax.swing.JPanel {

    private MainForm mainForm;
    private CustomersForm customersForm;
    private final CateDao cateDao = new CateDao();

    // --- MÀU SẮC CHỦ ĐẠO (Sửa ở đây để đổi màu toàn bộ trang) ---
    private final Color PRIMARY_COLOR = new Color(0, 102, 204); // Xanh dương đậm
    private final Color BG_COLOR = new Color(245, 245, 250);    // Xám nhạt (nền)
    private final Color HOVER_COLOR = new Color(230, 240, 255); // Màu khi rê chuột vào nút

    public HomePage(MainForm main) {
        this.mainForm = main;
        initCustomLayout();
        loadCategoryButtons();
    }

    public HomePage(CustomersForm customers) {
        this.customersForm = customers;
        initCustomLayout();
        loadCategoryButtons();
    }

    public HomePage() {
        initCustomLayout();
        loadCategoryButtons();
    }

    // --- HÀM TỰ CẤU HÌNH GIAO DIỆN (Thay thế initComponents mặc định) ---
    private void initCustomLayout() {
        this.setLayout(new BorderLayout()); // Layout chính là BorderLayout
        
        // 1. TẠO HEADER ĐẸP
        JPanel pnlHeader = new JPanel();
        pnlHeader.setBackground(PRIMARY_COLOR);
        pnlHeader.setPreferredSize(new Dimension(800, 80)); // Chiều cao header
        pnlHeader.setLayout(new BorderLayout());
        
        JLabel lblTitle = new JLabel("CỬA HÀNG CƯỜNG THUẬN", SwingConstants.CENTER);
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 28));
        lblTitle.setForeground(Color.WHITE);
        pnlHeader.add(lblTitle, BorderLayout.CENTER);
        
        this.add(pnlHeader, BorderLayout.NORTH); // Gắn Header lên trên cùng

        // 2. TẠO PHẦN CHỨA NÚT (BODY)
        jPanel1 = new JPanel();
        jPanel1.setBackground(BG_COLOR);
        // Padding: Cách lề trên dưới trái phải 20px cho thoáng
        jPanel1.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20)); 
        jPanel1.setLayout(new GridLayout(0, 2, 20, 20)); // Lưới 2 cột, khoảng cách 20

        // 3. TẠO SCROLL PANE
        jScrollPane2 = new JScrollPane(jPanel1);
        jScrollPane2.setBorder(null); // Bỏ viền xấu xí mặc định
        jScrollPane2.getVerticalScrollBar().setUnitIncrement(16); // Cuộn mượt
        
        this.add(jScrollPane2, BorderLayout.CENTER); // Gắn Scroll vào giữa
    }

    private void loadCategoryButtons() {
        try {
            jPanel1.removeAll();
            List<Cate> list = cateDao.getall();

            for (Cate c : list) {
                // --- THIẾT KẾ NÚT DẠNG THẺ (CARD) ---
                JButton btn = new JButton();
                
                // Dùng HTML để format chữ: Tên to, Mô tả nhỏ xuống dòng
                String htmlText = "<html><center>"
                        + "<span style='font-size:16px; color:#333333; font-weight:bold'>" + c.getName().toUpperCase() + "</span><br>"
                        + "<span style='font-size:10px; color:#666666'><i>" + c.getDescription() + "</i></span>"
                        + "</center></html>";
                
                btn.setText(htmlText);
                btn.setPreferredSize(new Dimension(200, 150));
                
                // Style cho nút
                btn.setBackground(Color.WHITE);
                btn.setFocusPainted(false); // Bỏ viền focus khi bấm
                btn.setBorder(BorderFactory.createLineBorder(new Color(220, 220, 220), 1)); // Viền mỏng nhẹ
                btn.setCursor(new Cursor(Cursor.HAND_CURSOR));

                // --- HIỆU ỨNG HOVER (RÊ CHUỘT) ---
                btn.addMouseListener(new MouseAdapter() {
                    @Override
                    public void mouseEntered(MouseEvent e) {
                        btn.setBackground(HOVER_COLOR); // Đổi màu nền khi rê vào
                        btn.setBorder(BorderFactory.createLineBorder(PRIMARY_COLOR, 2)); // Viền đậm lên
                    }

                    @Override
                    public void mouseExited(MouseEvent e) {
                        btn.setBackground(Color.WHITE); // Trả lại màu trắng
                        btn.setBorder(BorderFactory.createLineBorder(new Color(220, 220, 220), 1));
                    }
                });

                // Sự kiện click
                btn.addActionListener(e -> onCategoryClick(c.getId(), c.getName()));

                jPanel1.add(btn);
            }

            jPanel1.revalidate();
            jPanel1.repaint();

        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Lỗi tải danh mục: " + e.getMessage());
        }
    }

    private void onCategoryClick(int categoryId, String categoryName) {
        if (customersForm != null) {
            customersForm.showProductByCategory(categoryId, categoryName);
        } else if (mainForm != null) {
            mainForm.showProductByCategory(categoryId, categoryName);
        } else {
            JOptionPane.showMessageDialog(this, "Lỗi: Không tìm thấy Form kết nối!");
        }
    }
    public void reset(){
        loadCategoryButtons();
    }
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor .       */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane2 = new javax.swing.JScrollPane();
        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();

        jScrollPane2.setPreferredSize(new java.awt.Dimension(600, 400));

        jPanel1.setLayout(new java.awt.GridLayout(0, 2, 10, 10));
        jScrollPane2.setViewportView(jPanel1);

        jLabel1.setFont(new java.awt.Font("Helvetica", 3, 36)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(0, 0, 255));
        jLabel1.setText("Cửa hàng Cường Thuận");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(layout.createSequentialGroup()
                .addGap(87, 87, 87)
                .addComponent(jLabel1)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addGap(46, 46, 46)
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 505, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel jLabel1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane2;
    // End of variables declaration//GEN-END:variables
}
