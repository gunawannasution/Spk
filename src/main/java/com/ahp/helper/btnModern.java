package com.ahp.helper;

import javax.swing.JButton;
import java.awt.*;
import java.awt.event.*;
import javax.swing.border.EmptyBorder;

public class btnModern extends JButton {
    private Color backgroundColor = UIComponent.PRIMARY_COLOR;
    private Color hoverColor = UIComponent.PRIMARY_COLOR.darker();
    private Color pressedColor = UIComponent.PRIMARY_COLOR.darker().darker();
    private Color textColor = Color.WHITE;
    private int arc = 16;
    private boolean showBorderOnHover = true;
    
    public btnModern(String text) {
        super(text);
        initButton();
        initListeners();
    }

    private void initButton() {
        setContentAreaFilled(false);
        setFocusPainted(false);
        setForeground(textColor);
        setFont(new Font("Segoe UI", Font.BOLD, 12));
        setBorder(new EmptyBorder(8, 16, 8, 16));
        setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        setPreferredSize(new Dimension(120, 36));
    }

    private void initListeners() {
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                setBackground(hoverColor);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                setBackground(backgroundColor);
            }

            @Override
            public void mousePressed(MouseEvent e) {
                setBackground(pressedColor);
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                if (contains(e.getPoint())) {
                    setBackground(hoverColor);
                } else {
                    setBackground(backgroundColor);
                }
            }
        });
        
        setBackground(backgroundColor);
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // Pastikan area yang diisi cukup besar
        int width = getWidth();
        int height = getHeight();

        // Clear background (penting untuk parent yang tidak opaque)
        g2.setColor(getParent().getBackground());
        g2.fillRect(0, 0, width, height);

        // Paint background dengan rounded corners
        g2.setColor(getBackground());
        g2.fillRoundRect(0, 0, width, height, arc, arc);

        // Paint border jika diperlukan
        if (showBorderOnHover && getModel().isRollover()) {
            g2.setColor(new Color(255, 255, 255, 80));
            g2.drawRoundRect(0, 0, width-1, height-1, arc, arc);
        }

        g2.dispose();

        // Panggil paintComponent super untuk teks dan ikon
        super.paintComponent(g);
    }

    @Override
    public void setBackground(Color bg) {
        super.setBackground(bg);
        this.backgroundColor = bg;
        this.hoverColor = bg.darker();
        this.pressedColor = bg.darker().darker();
        repaint();
    }

    public void setTextColor(Color color) {
        this.textColor = color;
        setForeground(color);
    }

    public void setArc(int arc) {
        this.arc = arc;
        repaint();
    }
    
    public void setShowBorderOnHover(boolean show) {
        this.showBorderOnHover = show;
        repaint();
    }
    
    public void setShadowOpacity(float opacity) {
        Math.min(1f, Math.max(0f, opacity));
        repaint();
    }
    
    @Override
    public void setEnabled(boolean enabled) {
        super.setEnabled(enabled);
        if (enabled) {
            setForeground(textColor);
        } else {
            setForeground(new Color(textColor.getRed(), textColor.getGreen(), textColor.getBlue(), 128));
        }
    }
}