package com.ahp.helper;

import java.awt.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

public class libButton {

    public enum ButtonType {
        SIMPAN,
        UPDATE,
        DELETE,
        BATAL,
        HITUNG,
        TAMBAH
    }

    public static JButton buatBtn(ButtonType type) {
        JButton button = new JButton();
        button.setFont(new Font("Segoe UI", Font.BOLD, 14));
        button.setFocusPainted(false);
        button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        button.setBorder(new EmptyBorder(10, 18, 10, 18));
        button.setOpaque(true);

        switch (type) {
            case SIMPAN:
                button.setText("Simpan");
                button.setBackground(new Color(40, 170, 60));
                button.setForeground(Color.WHITE);
                button.setActionCommand("simpan");
                break;

            case UPDATE:
                button.setText("Update");
                button.setBackground(new Color(255, 165, 0));
                button.setForeground(Color.BLACK);
                button.setActionCommand("update");
                break;

            case DELETE:
                button.setText("Hapus");
                button.setBackground(new Color(220, 53, 69));
                button.setForeground(Color.WHITE);
                button.setActionCommand("delete");
                break;

            case BATAL:
                button.setText("Batal");
                button.setBackground(new Color(100, 100, 100));
                button.setForeground(Color.WHITE);
                button.setActionCommand("batal");
                break;

            case HITUNG:
                button.setText("Hitung");
                button.setBackground(new Color(0, 120, 215));
                button.setForeground(Color.WHITE);
                button.setActionCommand("hitung");
                break;

            case TAMBAH:
                // Tombol tambah hanya ikon "+"
                button.setText("TAMBAH");
                button.setPreferredSize(new Dimension(40, 40));
                button.setFont(new Font("Segoe UI", Font.BOLD, 18));
                button.setBackground(new Color(0, 120, 215));
                button.setForeground(Color.WHITE);
                button.setActionCommand("tambah");
                break;

            default:
                button.setText("Tombol");
                button.setBackground(UIManager.getColor("Button.background"));
                button.setForeground(UIManager.getColor("Button.foreground"));
        }

        return button;
    }
}
