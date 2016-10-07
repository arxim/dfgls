package df.bean.obj.util;

import javax.swing.JOptionPane;

public class DialogBox {
    public DialogBox() {
    }
    static public void ShowInfo(String str) {
        JOptionPane.showMessageDialog(null,  str, "Info",  JOptionPane.INFORMATION_MESSAGE);                
    }
    static public void ShowError(String str) {
        JOptionPane.showMessageDialog(null,  str, "Error",  JOptionPane.ERROR_MESSAGE);                
    }
    static public void ShowWarning(String str) {
        JOptionPane.showMessageDialog(null,  str, "Warning",  JOptionPane.WARNING_MESSAGE);                
    }
}
