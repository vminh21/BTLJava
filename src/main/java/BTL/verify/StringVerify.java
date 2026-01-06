/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package BTL.verify;

import javax.swing.InputVerifier;
import javax.swing.JComponent;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

/**
 *
 * @author vanminh
 */
public class StringVerify extends InputVerifier {
    @Override
    public boolean verify(JComponent input) {
        String text = ((JTextField) input).getText().trim();
        if (text.isEmpty()) return true;
        
        return text.matches("^[a-zA-Z\\sÀ-ỹ]+$");
    }

    @Override
    public boolean shouldYieldFocus(JComponent input) {
        if (!verify(input)) {
            JOptionPane.showMessageDialog(input, "Trường này chỉ được nhập chữ!");
            return false;
        }
        return true;
    }
}