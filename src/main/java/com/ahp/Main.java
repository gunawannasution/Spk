package com.ahp;

import com.ahp.login.FormLogin;
import com.formdev.flatlaf.FlatLightLaf;

import javax.swing.*;

public class Main extends JFrame {
    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(new FlatLightLaf());
            UIManager.put("TextComponent.arc", 15);
            UIManager.put("Component.focusWidth", 1);
        } catch (UnsupportedLookAndFeelException ex) {
            System.out.println("Template tidak dapat di-load");
        }

        SwingUtilities.invokeLater(() -> {
            FormLogin loginDialog = new FormLogin(null); 
            loginDialog.setVisible(true);

            if (loginDialog.isSucceeded()) {
                MainFrame mainFrame = new MainFrame();
                mainFrame.setVisible(true);
            } else {
                System.exit(0);
            }
        });
    }
}
