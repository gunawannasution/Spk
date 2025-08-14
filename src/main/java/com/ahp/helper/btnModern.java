package com.ahp.helper;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.RoundRectangle2D;
import java.awt.image.BufferedImage;

public class btnModern extends JButton {
    private Color baseColor;
    private Color hoverColor;
    private Color pressColor;
    private Color textColor = Color.WHITE;
    private int arc = 16;
    private boolean showShadow = true;
    private float shadowOpacity = 0.15f;
    private int shadowSize = 4;
    private int iconTextGap = 6;

    private boolean isHover = false;
    private boolean isPressed = false;

    public btnModern(String text) {
        this(text, UIComponent.PRIMARY_COLOR);
    }

    public btnModern(String text, Color color) {
        this(text, color, null);
    }

    public btnModern(String text, Color color, Icon icon) {
        super(text);
        this.baseColor = color;
        this.hoverColor = color.darker();
        this.pressColor = color.darker().darker();
        if (icon != null) {
            setIcon(scaleIcon(icon, 18, 18)); // Auto-resize icon
        }
        initStyle();
        initMouseEffects();
    }

    private void initStyle() {
        setContentAreaFilled(false);
        setFocusPainted(false);
        setBorder(new EmptyBorder(10, 20, 10, 20));
        setForeground(textColor);
        setFont(UIComponent.FONT_BOLD.deriveFont(14f));
        setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        setHorizontalAlignment(SwingConstants.CENTER);
        setHorizontalTextPosition(SwingConstants.RIGHT);
        setVerticalAlignment(SwingConstants.CENTER);
        setVerticalTextPosition(SwingConstants.CENTER);
        setIconTextGap(iconTextGap);

        setBackground(baseColor);
    }

    private void initMouseEffects() {
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                isHover = true;
                repaint();
            }

            @Override
            public void mouseExited(MouseEvent e) {
                isHover = false;
                isPressed = false;
                repaint();
            }

            @Override
            public void mousePressed(MouseEvent e) {
                isPressed = true;
                repaint();
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                isPressed = false;
                repaint();
            }
        });
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        Color currentColor = baseColor;
        if (isPressed) {
            currentColor = pressColor;
        } else if (isHover) {
            currentColor = hoverColor;
        }

        if (showShadow && isHover && isEnabled()) {
            g2.setColor(new Color(0, 0, 0, (int) (255 * shadowOpacity)));
            g2.fill(new RoundRectangle2D.Double(
                    shadowSize, shadowSize,
                    getWidth() - shadowSize * 2, getHeight() - shadowSize * 2,
                    arc, arc
            ));
        }

        g2.setColor(currentColor);
        g2.fillRoundRect(0, 0, getWidth(), getHeight(), arc, arc);

        g2.dispose();
        super.paintComponent(g);
    }

    private Icon scaleIcon(Icon icon, int width, int height) {
        if (icon instanceof ImageIcon) {
            Image img = ((ImageIcon) icon).getImage();
            Image scaled = img.getScaledInstance(width, height, Image.SCALE_SMOOTH);
            return new ImageIcon(scaled);
        }
        return icon;
    }

    // === Optional setters ===
    public void setBaseColor(Color color) {
        this.baseColor = color;
        this.hoverColor = color.darker();
        this.pressColor = color.darker().darker();
        setBackground(baseColor);
        repaint();
    }

    public void setTextColor(Color color) {
        this.textColor = color;
        setForeground(color);
        repaint();
    }

    public void setArc(int arc) {
        this.arc = arc;
        repaint();
    }

    public void setShowShadow(boolean showShadow) {
        this.showShadow = showShadow;
        repaint();
    }

    public void setShadowOpacity(float opacity) {
        this.shadowOpacity = Math.min(1, Math.max(0, opacity));
        repaint();
    }

    public void setShadowSize(int size) {
        this.shadowSize = size;
        repaint();
    }

    public void setIconTextGap(int gap) {
        this.iconTextGap = gap;
        super.setIconTextGap(gap);
    }

    @Override
    public void setEnabled(boolean enabled) {
        super.setEnabled(enabled);
        setForeground(enabled ? textColor : new Color(150, 150, 150));
        repaint();
    }

    @Override
    public Dimension getPreferredSize() {
        Dimension size = super.getPreferredSize();
        return new Dimension(size.width + 24, size.height + 12);
    }

    @Override
    public Dimension getMinimumSize() {
        return getPreferredSize();
    }

    @Override
    public Dimension getMaximumSize() {
        return getPreferredSize();
    }
}
