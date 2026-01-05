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
        if (input instanceof JTextField) {
            try {
                String text = ((JTextField)input).getText().trim();
                if (text != null && text.length() > 0) {
                    return true;
                } else {
                    return false;
                }
            } catch (Exception e) {
                return false;
            }
        }
        return false;
    }

    @Override
    public boolean shouldYieldFocus(JComponent input) {
        boolean retval = verify(input);
        if (!retval) {
            JOptionPane.showMessageDialog(input, "Phải nhập dữ liệu trường này ít nhất 1 kí tự !!");
        }
        return retval;
    }
}
