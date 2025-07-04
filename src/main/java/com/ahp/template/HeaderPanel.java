package com.ahp.template;

import com.ahp.helper.UIComponent;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class HeaderPanel extends JPanel {

    private final JLabel titleLabel;
    private final JPanel rightPanel;

    public HeaderPanel() {
        setLayout(new BorderLayout());
        setPreferredSize(new Dimension(0, 60));
        setBorder(new EmptyBorder(8, 20, 8, 20));

        // Background solid color
        setBackground(new Color(45, 120, 210)); // bisa diganti sesuai selera

        // Label judul
        titleLabel = UIComponent.buatLabel("Sistem Penilaian Karyawan Berprestasi PT. Hapesindo Omega Penta");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 22));
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        titleLabel.setOpaque(false);
        add(titleLabel, BorderLayout.CENTER);

        // Panel kanan untuk tombol/ikon
        rightPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 5, 0));
        rightPanel.setOpaque(false);  // biar transparan ikut background header
        add(rightPanel, BorderLayout.EAST);

        // Contoh tombol logout
        JButton btnLogout = new JButton("Logout");
        btnLogout.setFocusPainted(false);
        btnLogout.setForeground(Color.WHITE);
        btnLogout.setBackground(new Color(220, 53, 69));
        btnLogout.setBorder(BorderFactory.createEmptyBorder(6, 15, 6, 15));
        btnLogout.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        rightPanel.add(btnLogout);
    }

    // Set judul header secara dinamis
    public void setTitle(String title) {
        titleLabel.setText(title);
    }

    public void addRightComponent(Component comp) {
        rightPanel.add(comp);
        rightPanel.revalidate();
        rightPanel.repaint();
    }

    public void clearRightComponents() {
        rightPanel.removeAll();
        rightPanel.revalidate();
        rightPanel.repaint();
    }
}
