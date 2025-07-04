package com.ahp.helper;

import java.awt.*;
import java.util.List;
import javax.swing.*;

public class FormHelper {

    // Tema warna & font (FlatLaf Light)
    private static final Color BACKGROUND_COLOR = new Color(250, 250, 250);
    private static final Color LABEL_COLOR = new Color(33, 33, 33);
    private static final Color INPUT_BG_COLOR = new Color(240, 240, 240);
    private static final Color INPUT_FG_COLOR = new Color(66, 66, 66);
    private static final Color BUTTON_BG_COLOR = new Color(0, 120, 215);
    private static final Color BUTTON_HOVER_COLOR = new Color(30, 144, 255);
    private static final Color BUTTON_FG_COLOR = Color.WHITE;

    private static final Font FONT_LABEL = new Font("Segoe UI", Font.PLAIN, 14);
    private static final Font FONT_INPUT = new Font("Segoe UI", Font.PLAIN, 14);
    private static final Font FONT_TITLE = new Font("Segoe UI", Font.BOLD, 20);
    private static final Font FONT_BUTTON = new Font("Segoe UI", Font.BOLD, 14);

    public static JPanel createFieldPanel(String labelText, JComponent inputField) {
        JPanel panel = new JPanel(new BorderLayout(5, 0));
        panel.setBackground(BACKGROUND_COLOR);

        JLabel label = new JLabel(labelText);
        label.setPreferredSize(new Dimension(100, 25));
        label.setForeground(LABEL_COLOR);
        label.setFont(FONT_LABEL);

        inputField.setPreferredSize(new Dimension(200, 25));
        inputField.setMaximumSize(new Dimension(200, 25));

        styleInputField(inputField);

        panel.add(label, BorderLayout.WEST);
        panel.add(inputField, BorderLayout.CENTER);

        panel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 35));

        return panel;
    }

    private static void styleInputField(JComponent inputField) {
        if (inputField instanceof JTextField) {
            JTextField tf = (JTextField) inputField;
            tf.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(new Color(200, 200, 200)),
                    BorderFactory.createEmptyBorder(4, 6, 4, 6)
            ));
            tf.setBackground(INPUT_BG_COLOR);
            tf.setForeground(INPUT_FG_COLOR);
            tf.setCaretColor(INPUT_FG_COLOR);
            tf.setFont(FONT_INPUT);
        } else if (inputField instanceof JComboBox) {
            JComboBox<?> combo = (JComboBox<?>) inputField;
            combo.setFont(FONT_INPUT);
            combo.setForeground(INPUT_FG_COLOR);
            combo.setBackground(INPUT_BG_COLOR);
            combo.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200)));
        } // bisa ditambah lagi tipe lain seperti JTextArea dsb
    }

    public static JPanel createFormPanel(
            String titleText,
            List<JComponent> fieldPanels,
            List<JButton> buttons
    ) {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(BACKGROUND_COLOR);
        panel.setBorder(BorderFactory.createEmptyBorder(12, 12, 12, 12));

        JLabel title = new JLabel(titleText);
        title.setFont(FONT_TITLE);
        title.setForeground(LABEL_COLOR);
        title.setBorder(BorderFactory.createEmptyBorder(0, 0, 18, 0));
        title.setAlignmentX(Component.LEFT_ALIGNMENT);

        JPanel formFields = new JPanel();
        formFields.setLayout(new BoxLayout(formFields, BoxLayout.Y_AXIS));
        formFields.setBackground(BACKGROUND_COLOR);
        formFields.setAlignmentX(Component.LEFT_ALIGNMENT);

        for (JComponent field : fieldPanels) {
            field.setAlignmentX(Component.LEFT_ALIGNMENT);
            formFields.add(field);
            formFields.add(Box.createVerticalStrut(10));
        }

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 12, 6));
        buttonPanel.setBackground(BACKGROUND_COLOR);
        buttonPanel.setAlignmentX(Component.LEFT_ALIGNMENT);

        for (JButton btn : buttons) {
            styleButton(btn);
            buttonPanel.add(btn);
        }

        panel.add(title);
        panel.add(formFields);
        panel.add(buttonPanel);

        return panel;
    }

    private static void styleButton(JButton btn) {
        btn.setFont(FONT_BUTTON);
        btn.setFocusPainted(false);
        btn.setForeground(BUTTON_FG_COLOR);
        btn.setBackground(BUTTON_BG_COLOR);
        btn.setBorder(BorderFactory.createEmptyBorder(8, 16, 8, 16));
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        addHoverEffect(btn, BUTTON_BG_COLOR, BUTTON_HOVER_COLOR);
    }

    private static void addHoverEffect(JButton btn, Color normal, Color hover) {
        btn.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btn.setBackground(hover);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btn.setBackground(normal);
            }
        });
    }
}
