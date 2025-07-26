package com.ahp.helper;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.*;

public class btnModern extends JButton {
    private final Color baseColor;
    private final Color hoverColor;
    private final Color pressColor;
    private final Color textColor = Color.WHITE;
    private int arc = 14;

    public btnModern(String text) {
        this(text, UIComponent.PRIMARY_COLOR);  
    }

    public btnModern(String text, Color color) {
        super(text);
        this.baseColor = color;
        this.hoverColor = color.darker();
        this.pressColor = color.darker().darker();
        initStyle();
        initMouseEffects();
    }

    private void initStyle() {
        setContentAreaFilled(false);
        setFocusPainted(false);
        setBorder(new EmptyBorder(8, 14, 8, 14));
        setForeground(textColor);
        setFont(new Font("Segoe UI", Font.BOLD, 14));
        setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        //setPreferredSize(new Dimension(110, 36));

        // agar teks dan ikon berada di tengah
        setHorizontalAlignment(SwingConstants.CENTER);
        setHorizontalTextPosition(SwingConstants.CENTER);
        setVerticalAlignment(SwingConstants.CENTER);
        setVerticalTextPosition(SwingConstants.CENTER);

        super.setBackground(baseColor);
    }


    private void initMouseEffects() {
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                btnModern.this.setBackground(hoverColor);  
            }

            @Override
            public void mouseExited(MouseEvent e) {
                btnModern.this.setBackground(baseColor);   
            }

            @Override
            public void mousePressed(MouseEvent e) {
                btnModern.this.setBackground(pressColor);  
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                if (contains(e.getPoint())) {
                    btnModern.this.setBackground(hoverColor);
                } else {
                    btnModern.this.setBackground(baseColor);
                }
            }
        });
    }


    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        g2.setColor(getBackground());
        g2.fillRoundRect(0, 0, getWidth(), getHeight(), arc, arc);

        g2.dispose();
        super.paintComponent(g);
    }

    // Menghindari pengubahan baseColor saat dipanggil dari luar
    @Override
    public void setBackground(Color bg) {
        super.setBackground(bg); 
    }

    public void setTextColor(Color color) {
        setForeground(color);
    }

    public void setArc(int arc) {
        this.arc = arc;
        repaint();
    }

    @Override
    public void setEnabled(boolean enabled) {
        super.setEnabled(enabled);
        setForeground(enabled ? textColor : new Color(200, 200, 200));
    }
}
