package com.ahp.template;

import com.ahp.helper.UIComponent;
import com.ahp.helper.btnModern;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;

public class HeaderPanel extends JPanel {
    private final JLabel labelJudul;
    private final JPanel panelKanan;
    private final JPanel panelKiri;
    //private final btnModern btnLogout;

    public HeaderPanel() {
        setLayout(new BorderLayout());
        setPreferredSize(new Dimension(0, 70));
        setBackground(UIComponent.PRIMARY_COLOR);
        setBorder(BorderFactory.createCompoundBorder(
            new MatteBorder(0, 0, 2, 0, new Color(0, 0, 0, 20)),
            new EmptyBorder(0, 25, 0, 25)
        ));

        panelKiri = createTransparentPanel(FlowLayout.LEFT);
        add(panelKiri, BorderLayout.WEST);

        // Tengah (judul)
        labelJudul = new JLabel("SISTEM PENILAIAN KARYAWAN PT. HAPESINDO OMEGA PENTA");
        labelJudul.setFont(UIComponent.FONT_LARGE);
        labelJudul.setForeground(Color.WHITE);
        labelJudul.setBorder(new EmptyBorder(0, 10, 0, 0));
        add(labelJudul, BorderLayout.CENTER);

        // Kanan (tombol)
        panelKanan = createTransparentPanel(FlowLayout.RIGHT);
        add(panelKanan, BorderLayout.EAST);
    }

    private JPanel createTransparentPanel(int alignment) {
        JPanel panel = new JPanel(new FlowLayout(alignment, 10, 0));
        panel.setOpaque(false);
        return panel;
    }

    private void setupButtonHover(btnModern button) {
        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                button.setBackground(UIComponent.DANGER_COLOR.brighter());
            }

            @Override
            public void mouseExited(MouseEvent e) {
                button.setBackground(UIComponent.DANGER_COLOR);
            }
        });
    }

    // ---- Public utility methods ----
    public void setTitle(String title) {
        labelJudul.setText(title);
    }

    public void setTitleFont(Font font) {
        labelJudul.setFont(font);
    }

    public void addRightComponent(Component comp) {
        panelKanan.add(comp, panelKanan.getComponentCount() - 1);
        panelKanan.revalidate();
        panelKanan.repaint();
    }

    public void addLeftComponent(Component comp) {
        panelKiri.add(comp);
        panelKiri.revalidate();
        panelKiri.repaint();
    }

    public void clearRightComponents() {
        for (int i = panelKanan.getComponentCount() - 2; i >= 0; i--) {
            panelKanan.remove(i);
        }
        panelKanan.revalidate();
        panelKanan.repaint();
    }

    public void clearLeftComponents() {
        panelKiri.removeAll();
        panelKiri.revalidate();
        panelKiri.repaint();
    }

    public void setLogo(Icon icon) {
        for (Component comp : panelKiri.getComponents()) {
            if (comp instanceof JLabel label) {
                label.setIcon(icon);
                return;
            }
        }

        // Jika belum ada JLabel untuk logo, tambahkan baru
        JLabel logoLabel = new JLabel(icon);
        logoLabel.setBorder(new EmptyBorder(0, 0, 0, 15));
        panelKiri.add(logoLabel, 0);
        panelKiri.revalidate();
        panelKiri.repaint();
    }
}
