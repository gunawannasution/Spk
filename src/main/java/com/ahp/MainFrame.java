package com.ahp;

import com.ahp.template.SidebarPanel;
import com.ahp.template.ContentPanel;
import com.ahp.template.HeaderPanel;
import com.ahp.template.FooterPanel;
import javax.swing.*;
import java.awt.*;

public class MainFrame extends JFrame {
    private final ContentPanel contentPanel;
    private final SidebarPanel sidebar;

    public MainFrame() {
        setTitle("APLIKASI SPK PENILAIAN KARYAWAN BERPRESTASI PT. HAPESINDO OMEGA PENTA");
        setSize(1200, 800);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // Initialize components
        sidebar = new SidebarPanel();
        contentPanel = new ContentPanel();

        // Setup sidebar menu listener
        sidebar.addMenuListener(this::handleMenuSelection);

        // Add components to frame
        add(new HeaderPanel(), BorderLayout.NORTH);
        add(sidebar, BorderLayout.WEST);
        add(contentPanel, BorderLayout.CENTER);
        add(new FooterPanel(), BorderLayout.SOUTH);

        // Set initial view
        sidebar.selectMenu("dashboard");
    }

    private void handleMenuSelection(String menuCommand) {
        switch (menuCommand) {
            case "dashboard":
                contentPanel.showPanel("dashboard");
                break;
            case "karyawan":
                contentPanel.showPanel("karyawan");
                break;
            case "kriteria":
                contentPanel.showPanel("kriteria");
                break;
            case "matrix":
                contentPanel.showPanel("matrix");
                break;
            case "penilaian":
                contentPanel.showPanel("penilaian");
                break;
            case "laporan":
                contentPanel.showPanel("laporan");
                break;
            case "logout":
                confirmLogout();
                break;
            default:
                System.out.println("Unknown menu command: " + menuCommand);
        }
    }

    private void confirmLogout() {
        int confirm = JOptionPane.showConfirmDialog(
            this,
            "<html><div style='width:250px;padding:10px;text-align:center;'>" +
            "<p style='font-weight:bold;margin-bottom:10px;'>Konfirmasi Logout</p>" +
            "<p>Apakah Anda yakin ingin keluar dari aplikasi?</p>" +
            "</div></html>",
            "Konfirmasi Logout",
            JOptionPane.YES_NO_OPTION,
            JOptionPane.QUESTION_MESSAGE);

        if (confirm == JOptionPane.YES_OPTION) {
            System.exit(0);
        } else {
            // Restore previous selection
            String currentPanel = contentPanel.getCurrentPanelName();
            if (currentPanel != null) {
                sidebar.selectMenu(currentPanel);
            }
        }
    }

//    public static void main(String[] args) {
//        SwingUtilities.invokeLater(() -> {
//            try {
//                // Set FlatLaf dark theme
//                UIManager.setLookAndFeel(new com.formdev.flatlaf.FlatDarkLaf());
//            } catch (Exception ex) {
//                System.err.println("Failed to initialize FlatLaf");
//            }
//
//            MainFrame frame = new MainFrame();
//            frame.setVisible(true);
//        });
//    }
}