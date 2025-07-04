package com.ahp.helper;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.util.function.Consumer;

public class SearchBox extends JPanel {
    private final JTextField txtSearch;

    public SearchBox(String placeholder, Consumer<String> onTextChange) {
        setLayout(new BorderLayout(5, 0)); // spacing antar ikon dan teks
        setOpaque(false);

        // Ikon pencarian (FlatLaf mendukung SVG atau bisa pakai unicode/lain)
        JLabel icon = new JLabel("\uD83D\uDD0D"); // ikon 🔍
        icon.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 16));
        icon.setForeground(Color.GRAY);
        icon.setBorder(BorderFactory.createEmptyBorder(0, 5, 0, 0));
        icon.setVerticalAlignment(SwingConstants.CENTER);

        txtSearch = new JTextField(20);
        txtSearch.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        txtSearch.setToolTipText(placeholder);
        txtSearch.putClientProperty("JTextField.placeholderText", placeholder);
        txtSearch.putClientProperty("JTextField.showClearButton", true);

        txtSearch.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) { notifyChange(); }
            @Override
            public void removeUpdate(DocumentEvent e) { notifyChange(); }
            @Override
            public void changedUpdate(DocumentEvent e) { notifyChange(); }

            private void notifyChange() {
                onTextChange.accept(txtSearch.getText().trim());
            }
        });

        add(icon, BorderLayout.WEST);
        add(txtSearch, BorderLayout.CENTER);
    }

    public String getText() {
        return txtSearch.getText();
    }

    public void setText(String text) {
        txtSearch.setText(text);
    }
}
