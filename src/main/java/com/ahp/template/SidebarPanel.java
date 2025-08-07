package com.ahp.template;

import com.formdev.flatlaf.FlatClientProperties;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.List;

public class SidebarPanel extends JPanel {

    private final Color COLOR_BG = new Color(28, 30, 32);
    private final Color COLOR_HOVER = new Color(44, 44, 48);
    private final Color COLOR_SELECTED = new Color(60, 63, 65);
    private final Color COLOR_TEXT = new Color(240, 240, 240);

    private final Font FONT_MENU = new Font("Segoe UI", Font.PLAIN, 14);

    private final List<JButton> menuButtons = new ArrayList<>();
    private JButton activeButton = null;

    public SidebarPanel() {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setBackground(COLOR_BG);
        setPreferredSize(new Dimension(220, Integer.MAX_VALUE));

        add(Box.createVerticalStrut(20)); // spasi atas

        // Tambahkan item menu
        addMenuItem("Dashboard", "dashboard", "/icons/dashboard.png");
        addMenuItem("Karyawan", "karyawan", "/icons/karyawan.png");
        addMenuItem("Kriteria", "kriteria", "/icons/kriteria.png");
        addMenuItem("Perhitungan", "matrix", "/icons/matrix.png");
        addMenuItem("Penilaian", "penilaian", "/icons/penilaian.png");
        addMenuItem("Laporan", "laporan", "/icons/laporan.png");

        // Tambahkan menu Logout dengan actionCommand yang JELAS
        addMenuItem("Logout", "logout", "/icons/logout.png");

        add(Box.createVerticalGlue()); // dorong ke atas
    }

    private void addMenuItem(String label, String actionCommand, String iconPath) {
        ImageIcon icon = loadIcon(iconPath);
        JButton button = new JButton(label, icon);

        // Konfigurasi FlatLaf
        button.putClientProperty(FlatClientProperties.BUTTON_TYPE, FlatClientProperties.BUTTON_TYPE_BORDERLESS);
        button.putClientProperty(FlatClientProperties.STYLE,
                "arc: 0;" +
                        "borderWidth: 0;" +
                        "focusWidth: 0;" +
                        "margin: 10,20,10,10;" +
                        "iconTextGap: 12;" +
                        "foreground: #f0f0f0;" +
                        "background: $Panel.background;" +
                        "hoverBackground: #2c2c30;" +
                        "selectedBackground: #3c3f41");

        button.setActionCommand(actionCommand);
        button.setHorizontalAlignment(SwingConstants.LEFT);
        button.setFont(FONT_MENU);
        button.setForeground(COLOR_TEXT);
        button.setBackground(COLOR_BG);
        button.setFocusPainted(false);
        button.setBorder(new EmptyBorder(10, 20, 10, 10));
        button.setOpaque(true);
        button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        // Hover dan klik
        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                if (button != activeButton)
                    button.setBackground(COLOR_HOVER);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                if (button != activeButton)
                    button.setBackground(COLOR_BG);
            }

            @Override
            public void mousePressed(MouseEvent e) {
                button.setBackground(COLOR_SELECTED);
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                if (button != activeButton)
                    button.setBackground(COLOR_HOVER);
            }
        });

        // Action klik
        button.addActionListener(e -> {
            setActiveButton(button);

            String cmd = e.getActionCommand();
            if ("logout".equals(cmd)) {
                int confirm = JOptionPane.showConfirmDialog(
                        this,
                        "Apakah Anda yakin ingin keluar?",
                        "Konfirmasi Logout",
                        JOptionPane.YES_NO_OPTION
                );

                if (confirm == JOptionPane.YES_OPTION) {
                    System.exit(0); // Keluar dari aplikasi
                }
            }

            // TODO: tambahkan aksi lain di sini sesuai kebutuhan (buka panel, dsb.)
        });

        menuButtons.add(button);
        add(button);
        add(Box.createVerticalStrut(4)); // spasi antar tombol
    }

    private ImageIcon loadIcon(String path) {
        try {
            return new ImageIcon(getClass().getResource(path));
        } catch (Exception e) {
            System.err.println("❌ Icon tidak ditemukan: " + path);
            return null;
        }
    }

    public void setActiveButton(JButton button) {
        if (activeButton != null) {
            activeButton.setBackground(COLOR_BG);
            activeButton.putClientProperty("JButton.selected", false);
        }
        activeButton = button;
        activeButton.setBackground(COLOR_SELECTED);
        activeButton.putClientProperty("JButton.selected", true);
    }

    public List<JButton> getMenuButtons() {
        return menuButtons;
    }
}
