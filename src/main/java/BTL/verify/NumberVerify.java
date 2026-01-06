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
public class NumberVerify extends InputVerifier {
    @Override
    public boolean verify(JComponent input) {
        String text = ((JTextField) input).getText().trim();
        if (text.isEmpty()) return true;
        
        return text.matches("^[0-9]+$");
    }

    @Override
    public boolean shouldYieldFocus(JComponent input) {
        if (!verify(input)) {
            JOptionPane.showMessageDialog(input, "Trường này bắt buộc phải là số!");
            return false;
        }
        return true;
    }
}