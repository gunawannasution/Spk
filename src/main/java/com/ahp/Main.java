package com.ahp;

import com.formdev.flatlaf.FlatLightLaf;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

public class Main extends JFrame{
    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(new FlatLightLaf());
            UIManager.put("TextComponent.arc", 15);
            UIManager.put("Component.focusWidth", 1);
        } catch (UnsupportedLookAndFeelException ex) {
            System.getLogger(Main.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
            System.out.println("Template tidak dapat di load");
        }
        SwingUtilities.invokeLater(()->{
            MainFrame mainFrame=new MainFrame();
            mainFrame.setVisible(true);
        });
    }
}
