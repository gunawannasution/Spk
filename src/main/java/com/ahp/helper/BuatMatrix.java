package com.ahp.helper;

import javax.swing.*;
import java.awt.*;

public class BuatMatrix extends JPanel {
    private final JTextField[][] fields;

    public BuatMatrix(int n, String[] kriteria, boolean editable) {
        this.fields = new JTextField[n][n];
        setLayout(new GridLayout(n + 1, n + 1, 5, 5));
        setBackground(UIComponent.BACKGROUND_COLOR);
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Header kolom (atas)
        add(createEmptyLabel()); // kosong di kiri atas
        
        // Create column headers
        for (String s : kriteria) {
            add(createHeaderLabel(s));
        }

        // Create matrix cells
        for (int i = 0; i < n; i++) {
            add(createHeaderLabel(kriteria[i])); // Row header
            
            for (int j = 0; j < n; j++) {
                fields[i][j] = createMatrixField(i, j, editable);
                add(fields[i][j]);
            }
        }
    }

    private JLabel createEmptyLabel() {
        JLabel label = new JLabel();
        label.setOpaque(true);
        label.setBackground(UIComponent.CARD_COLOR);
        return label;
    }

    private JLabel createHeaderLabel(String text) {
        JLabel label = new JLabel(text, SwingConstants.CENTER);
        label.setOpaque(true);
        label.setBackground(UIComponent.CARD_COLOR);
        label.setForeground(UIComponent.TITLE_COLOR);
        label.setFont(UIComponent.FONT_BOLD);
        label.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(UIComponent.BORDER_COLOR),
            BorderFactory.createEmptyBorder(5, 5, 5, 5)
        ));
        return label;
    }

    private JTextField createMatrixField(int i, int j, boolean editable) {
        JTextField tf = new JTextField();
        tf.setHorizontalAlignment(JTextField.CENTER);
        tf.setEditable(editable && i != j);
        
        // Styling based on cell position
        if (i == j) {
            tf.setText("1");
            tf.setBackground(UIComponent.HIGHLIGHT_COLOR);
            tf.setForeground(UIComponent.TEXT_COLOR);
            tf.setFont(UIComponent.FONT_BOLD);
        } else {
            tf.setBackground(editable ? UIComponent.CARD_COLOR : new Color(60, 63, 65));
            tf.setForeground(UIComponent.TEXT_COLOR);
            tf.setFont(UIComponent.FONT_REGULAR);
        }

        // Common styling
        tf.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(UIComponent.BORDER_COLOR),
            BorderFactory.createEmptyBorder(5, 5, 5, 5)
        ));
        tf.setCaretColor(UIComponent.PRIMARY_COLOR);
        
        // Make diagonal cells non-editable
        if (i == j) {
            tf.setEditable(false);
        }

        return tf;
    }

    public JTextField[][] getFields() {
        return fields;
    }

    // Helper method to get all values as double matrix
    public double[][] getMatrixValues() {
        double[][] matrix = new double[fields.length][fields.length];
        for (int i = 0; i < fields.length; i++) {
            for (int j = 0; j < fields.length; j++) {
                try {
                    matrix[i][j] = Double.parseDouble(fields[i][j].getText());
                } catch (NumberFormatException e) {
                    matrix[i][j] = i == j ? 1.0 : 0.0; // Default to 1 on diagonal, 0 elsewhere
                }
            }
        }
        return matrix;
    }
}