package com.ahp.helper;

import java.awt.*;
import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.*;

public class UIComponent {
    public static final Color BACKGROUND_COLOR = new Color(250, 250, 250);
    public static final Color CARD_COLOR = new Color(240, 240, 240);
    public static final Color PRIMARY_COLOR = new Color(0, 120, 215);
    public static final Color SECONDARY_COLOR = new Color(255, 121, 198);
    public static final Color SUCCESS_COLOR = new Color(76, 175, 80);
    public static final Color WARNING_COLOR = new Color(255, 193, 7);
    public static final Color DANGER_COLOR = new Color(220, 53, 69);
    public static final Color ACCENT_COLOR = new Color(153, 255, 153);
    public static final Color ADD_COLOR = new Color(0, 153, 0);
    public static final Color CETAK_COLOR = new Color(0, 155, 153);
    public static final Color SIMPAN_COLOR = new Color(0,128,255);
    public static final Color TITLE_COLOR = new Color(33, 33, 33);
    public static final Color TEXT_COLOR = new Color(0, 0, 0);
    public static final Color TEXT_SECONDARY_COLOR = new Color(66, 66, 66);
    public static final Color BORDER_COLOR = new Color(200, 200, 200);
    public static final Color HIGHLIGHT_COLOR = new Color(230, 230, 230);
    public static final Color DISABLED_COLOR = new Color(160, 160, 160);
    public static final Color TABLE_HEADER_COLOR = new Color(63, 81, 181);
    public static final Color TABLE_HEADER_TEXT_COLOR = Color.WHITE;

    // Font Constants (enhanced with proper initialization)
    public static final Font FONT_PLAIN = new Font("Segoe UI", Font.PLAIN, 14);
    public static final Font FONT_REGULAR = FONT_PLAIN; // Alias for consistency
    public static final Font FONT_BOLD = new Font("Segoe UI", Font.BOLD, 14);
    public static final Font FONT_TITLE = new Font("Segoe UI", Font.BOLD, 16);
    public static final Font FONT_MEDIUM = new Font("Segoe UI", Font.PLAIN, 14);
    public static final Font FONT_SMALL = new Font("Segoe UI", Font.PLAIN, 12);
    public static final Font FONT_LARGE = new Font("Segoe UI", Font.BOLD, 18);
    
    public static final Color COLOR_BG_MENU = new Color(28, 30, 32);
    public static final Color COLOR_HOVER_MENU = new Color(44, 44, 48);
    public static final Color COLOR_SELECTED_MENU = new Color(60, 63, 65);
    public static final Color COLOR_TEXT_MENU = new Color(240, 240, 240);
    public static  final Color COLOR_SUBTEXT_MENU = new Color(180, 180, 180);

    // Fonts
    public static final Font FONT_MENU = new Font("Segoe UI", Font.PLAIN, 14);
    public static final Font FONT_MENU_SELECTED_MENU = new Font("Segoe UI", Font.BOLD, 14);
    public static final Font FONT_HEADER_MENU = new Font("Segoe UI", Font.BOLD, 18);
    public static final Font FONT_SUBHEADER_MENU = new Font("Segoe UI", Font.PLAIN, 11);
    
    // Label dengan style modern
    public static JLabel buatLabel(String text) {
        return buatLabel(text, null);
    }

    public static JLabel buatLabel(String text, Icon icon) {
        JLabel label = new JLabel(text, icon, SwingConstants.LEADING);
        label.setFont(FONT_BOLD);
        label.setForeground(TEXT_COLOR);
        label.setIconTextGap(8);
        return label;
    }

    // TextField modern dengan icon optional
    public static JTextField buatTxt(int columns) {
        return buatTxt(columns, null);
    }

    public static JTextField buatTxt(int columns, Icon icon) {
        JTextField field = new JTextField(columns) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                if (icon != null) {
                    Insets insets = getInsets();
                    int iconWidth = icon.getIconWidth();
                    int iconHeight = icon.getIconHeight();
                    int x = insets.left + 8;
                    int y = (getHeight() - iconHeight) / 2;
                    icon.paintIcon(this, g, x, y);
                    setMargin(new Insets(0, iconWidth + 15, 0, 0)); // memberi ruang untuk icon
                }
            }
        };

        field.setFont(FONT_REGULAR);
        field.setPreferredSize(new Dimension(200, 38));
        field.setForeground(TEXT_COLOR);
        field.setBackground(CARD_COLOR);
        field.setCaretColor(PRIMARY_COLOR);
        field.setBorder(createModernBorder(icon != null ? 38 : 12));
        return field;
    }

    public static JComboBox<String> buatCmb(String[] items, Icon icon) {
        JComboBox<String> comboBox = new JComboBox<>(items);
        setupComboBox(comboBox, icon);
        return comboBox;
    }

    public static <T> JComboBox<T> buatCmbDatabase(ComboBoxModel<T> model, Icon icon) {
        JComboBox<T> comboBox = new JComboBox<>(model);
        setupComboBox(comboBox, icon);
        return comboBox;
    }


    private static <T> void setupComboBox(JComboBox<T> comboBox, Icon icon) {
        comboBox.setFont(FONT_REGULAR);
        comboBox.setForeground(TEXT_COLOR);
        comboBox.setBackground(CARD_COLOR);
        comboBox.setBorder(createModernBorder(icon != null ? 35 : 12));
        
        if (icon != null) {
            comboBox.setRenderer(new DefaultListCellRenderer() {
                @Override
                public Component getListCellRendererComponent(JList<?> list, Object value, 
                        int index, boolean isSelected, boolean cellHasFocus) {
                    JLabel label = (JLabel) super.getListCellRendererComponent(
                        list, value, index, isSelected, cellHasFocus);
                    label.setIcon(index == -1 ? icon : null);
                    label.setIconTextGap(10);
                    label.setHorizontalAlignment(SwingConstants.LEADING);
                    return label;
                }
            });
        }
    }

    static Border createModernBorder(int leftPadding) {
        return BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(BORDER_COLOR, 1),
            BorderFactory.createEmptyBorder(8, leftPadding, 8, 12)
        );
    }

}
