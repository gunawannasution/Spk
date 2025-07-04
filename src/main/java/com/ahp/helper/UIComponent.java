package com.ahp.helper;

import java.awt.*;
import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.*;

public class UIComponent {
    // Warna sesuai FlatLaf Light theme & modern clean look
    public static final Color BACKGROUND_COLOR    = new Color(250, 250, 250);  // putih bersih
    public static final Color CARD_COLOR          = new Color(240, 240, 240);  // abu-abu terang
    public static final Color PRIMARY_COLOR       = new Color(0, 120, 215);    // biru terang khas FlatLaf light
    public static final Color SECONDARY_COLOR     = new Color(255, 121, 198);  // pink/ungu cerah
    public static final Color SUCCESS_COLOR       = new Color(76, 175, 80);    // hijau
    public static final Color WARNING_COLOR       = new Color(255, 193, 7);    // kuning hangat
    public static final Color DANGER_COLOR        = new Color(220, 53, 69);    // merah soft
    public static final Color TITLE_COLOR         = new Color(33, 33, 33);     // hitam gelap
    public static final Color TEXT_COLOR          = new Color(66, 66, 66);     // abu-abu gelap
    public static final Color BORDER_COLOR        = new Color(200, 200, 200);  // abu-abu muda
    public static final Color HIGHLIGHT_COLOR     = new Color(230, 230, 230);  // abu-abu sangat muda
    public static final Color DISABLED_COLOR      = new Color(160, 160, 160);  // abu-abu redup
    public static final Color TABLE_HEADER_COLOR = new Color(63, 81, 181);
    public static final Color TABLE_HEADER_TEXT_COLOR = Color.WHITE;
    // Font modern
    public static final Font FONT_REGULAR = new Font("Segoe UI", Font.PLAIN, 14);
    public static final Font FONT_BOLD = new Font("Segoe UI", Font.BOLD, 14);
    public static final Font FONT_TITLE = new Font("Segoe UI", Font.BOLD, 16);

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

    // ComboBox modern dengan icon optional
    /**
     * Untuk data String[] sederhana
     */
    public static JComboBox<String> buatCmb(String[] items, Icon icon) {
        JComboBox<String> comboBox = new JComboBox<>(items);
        setupComboBox(comboBox, icon);
        return comboBox;
    }

    /**
     * Untuk model custom dari database (generik)
     */
    public static <T> JComboBox<T> buatCmbDatabase(ComboBoxModel<T> model, Icon icon) {
        JComboBox<T> comboBox = new JComboBox<>(model);
        setupComboBox(comboBox, icon);
        return comboBox;
    }

    /**
     * Konfigurasi dasar untuk semua JComboBox
     */
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
    

    // Checkbox modern
    public static JCheckBox buatCheckBox(String text) {
        return buatCheckBox(text, null);
    }

    public static JCheckBox buatCheckBox(String text, Icon icon) {
        JCheckBox checkBox = new JCheckBox(text, icon);
        checkBox.setFont(FONT_REGULAR);
        checkBox.setForeground(TEXT_COLOR);
        checkBox.setBackground(BACKGROUND_COLOR);
        checkBox.setIconTextGap(10);
        checkBox.setFocusPainted(false);
        checkBox.setBorderPainted(false);
        checkBox.setOpaque(false);
        return checkBox;
    }

    // Table modern dengan striping warna dan header custom
    public static JTable buatTable(Object[][] data, Object[] columnNames) {
        return buatTable(data, columnNames, null);
    }

    public static JTable buatTable(Object[][] data, Object[] columnNames, Icon defaultIcon) {
        JTable table = new JTable(data, columnNames) {
            @Override
            public Component prepareRenderer(TableCellRenderer renderer, int row, int column) {
                Component c = super.prepareRenderer(renderer, row, column);
                
                if (defaultIcon != null && c instanceof JLabel) {
                    ((JLabel) c).setIcon(defaultIcon);
                    ((JLabel) c).setIconTextGap(8);
                }

                // Baris genap ganjil warna soft untuk striping
                c.setBackground(row % 2 == 0 ? new Color(250, 250, 250) : new Color(240, 240, 240));
                
                if (isRowSelected(row)) {
                    c.setBackground(new Color(0, 120, 215, 100));
                    c.setForeground(Color.WHITE);
                } else {
                    c.setForeground(TEXT_COLOR);
                }
                
                return c;
            }
        };

        table.setFont(FONT_REGULAR);
        table.setRowHeight(32);
        table.setGridColor(BORDER_COLOR);
        table.setSelectionBackground(new Color(0, 120, 215, 100));
        table.setSelectionForeground(Color.WHITE);
        table.setShowVerticalLines(false);
        table.setShowHorizontalLines(true);
        table.setIntercellSpacing(new Dimension(0, 0));
        table.setFillsViewportHeight(true);

        // Header table modern
        JTableHeader header = table.getTableHeader();
        header.setFont(FONT_BOLD);
        header.setBackground(PRIMARY_COLOR);
        header.setForeground(Color.WHITE);
        header.setPreferredSize(new Dimension(header.getWidth(), 36));
        header.setBorder(BorderFactory.createEmptyBorder(0, 5, 0, 0));

        return table;
    }

    // Border modern untuk field dan combobox
    static Border createModernBorder(int leftPadding) {
        return BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(BORDER_COLOR, 1),
            BorderFactory.createEmptyBorder(8, leftPadding, 8, 12)
        );
    }

    // Tombol modern untuk FlatLaf Light
    public static JButton createModernButton(String text) {
        JButton button = new JButton(text);
        button.setFont(FONT_BOLD);
        button.setForeground(Color.WHITE);
        button.setBackground(PRIMARY_COLOR);
        button.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        button.setFocusPainted(false);
        button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        button.setContentAreaFilled(true); // biar FlatLaf styling jalan dengan baik
        return button;
    }
}
