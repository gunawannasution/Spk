package com.ahp.template;

import com.ahp.helper.UIComponent;
import com.ahp.helper.btnModern;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class HeaderPanel extends JPanel {

    private final JLabel labelJudul;
    private final JPanel panelKanan;

    public HeaderPanel() {
        setLayout(new BorderLayout());
        setPreferredSize(new Dimension(0, 60));
        setBorder(new EmptyBorder(8, 20, 8, 20));
        setBackground(new Color(45, 120, 210)); 

        labelJudul = UIComponent.buatLabel("Sistem Penilaian Karyawan Berprestasi PT. Hapesindo Omega Penta");
        labelJudul.setFont(new Font("Segoe UI", Font.BOLD, 22));
        labelJudul.setForeground(Color.WHITE);
        labelJudul.setHorizontalAlignment(SwingConstants.LEFT);
        labelJudul.setOpaque(false);
        add(labelJudul, BorderLayout.CENTER);

        panelKanan = new JPanel(new FlowLayout(FlowLayout.RIGHT, 5, 0));
        panelKanan.setOpaque(false);
        add(panelKanan, BorderLayout.EAST);

        
        btnModern btnLogout = new btnModern("Logout",new Color(255,0,0));
        btnLogout.setFocusPainted(false);
        btnLogout.setForeground(Color.WHITE);
        btnLogout.setBackground(new Color(220, 53, 69));
        btnLogout.setBorder(BorderFactory.createEmptyBorder(6, 15, 6, 15));
        btnLogout.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        panelKanan.add(btnLogout);
    }

    // Set judul header secara dinamis
    public void setTitle(String title) {
        labelJudul.setText(title);
    }

    public void addRightComponent(Component comp) {
        panelKanan.add(comp);
        panelKanan.revalidate();
        panelKanan.repaint();
    }

    public void clearRightComponents() {
        panelKanan.removeAll();
        panelKanan.revalidate();
        panelKanan.repaint();
    }
}
