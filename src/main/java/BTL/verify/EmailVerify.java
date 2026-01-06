/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package BTL.verify;

import java.util.regex.Pattern;
import javax.swing.InputVerifier;
import javax.swing.JComponent;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

/**
 *
 * @author vanminh
 */
public class EmailVerify extends InputVerifier {
    private static final String EMAIL_PATTERN = 
        "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,6}$";

    @Override
    public boolean verify(JComponent input) {
        if (input instanceof JTextField) {
            String text = ((JTextField) input).getText().trim();
            
            // Cho phép để trống nếu bạn không bắt buộc nhập, 
            // nếu bắt buộc phải có email thì xóa điều kiện text.isEmpty() đi
            if (text.isEmpty()) return true; 

            // Kiểm tra khớp với định dạng Regex
            return Pattern.compile(EMAIL_PATTERN).matcher(text).matches();
        }
        return false;
    }

    @Override
    public boolean shouldYieldFocus(JComponent input) {
        boolean isValid = verify(input);
        if (!isValid) {
            JOptionPane.showMessageDialog(input, 
                "Định dạng email không hợp lệ! (Ví dụ: abc@gmail.com)", 
                "Lỗi nhập liệu", 
                JOptionPane.WARNING_MESSAGE);
        }
        return isValid;
    }
}
