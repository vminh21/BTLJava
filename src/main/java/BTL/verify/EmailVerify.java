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
    @Override
    public boolean verify(JComponent input) {
        String text = ((JTextField) input).getText().trim();
        if (text.isEmpty()) return true;
        
        String pattern = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,6}$";
        return text.matches(pattern);
    }

    @Override
    public boolean shouldYieldFocus(JComponent input) {
        if (!verify(input)) {
            JOptionPane.showMessageDialog(input, "Email không hợp lệ (ví dụ: abc@gmail.com)!");
            return false;
        }
        return true;
    }
}
