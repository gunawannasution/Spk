package com.ahp.content;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import com.ahp.helper.UIComponent;

public class LaporanPanel extends JPanel {

    private JTextArea laporanArea;
    private JButton btnPrint;

    public LaporanPanel() {
        initComponents();
    }

    private void initComponents() {
        setLayout(new BorderLayout());
        setBackground(UIComponent.BACKGROUND_COLOR);

        // Panel atas berisi judul dan tombol print di kanan
        JPanel atasPanel = new JPanel(new BorderLayout());
        atasPanel.setBackground(UIComponent.BACKGROUND_COLOR);
        atasPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JLabel title = new JLabel("Laporan AHP");
        title.setFont(UIComponent.FONT_TITLE);
        title.setForeground(UIComponent.TITLE_COLOR);

        // Tombol print dengan icon
        btnPrint = new JButton();
        btnPrint.setToolTipText("Print Laporan");
        btnPrint.setBackground(UIComponent.BACKGROUND_COLOR);
        btnPrint.setFocusPainted(false);
        btnPrint.setBorder(BorderFactory.createEmptyBorder());
        btnPrint.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        // Ganti icon ini dengan path icon Anda, atau pakai font icon
        // Contoh pakai Unicode printer icon (FontAwesome-like)
        btnPrint.setFont(new Font("Segoe UI Symbol", Font.PLAIN, 18));
        btnPrint.setForeground(UIComponent.PRIMARY_COLOR);
        btnPrint.setText("\uD83D\uDDA8"); // 🖨 printer emoji sebagai ikon sederhana

        btnPrint.addActionListener(e -> printLaporan());

        atasPanel.add(title, BorderLayout.WEST);
        atasPanel.add(btnPrint, BorderLayout.EAST);

        laporanArea = new JTextArea();
        laporanArea.setEditable(false);
        laporanArea.setFont(UIComponent.FONT_REGULAR);
        laporanArea.setForeground(UIComponent.TEXT_COLOR);
        laporanArea.setBackground(UIComponent.CARD_COLOR);
        laporanArea.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(UIComponent.BORDER_COLOR),
                BorderFactory.createEmptyBorder(10, 10, 10, 10)));
        laporanArea.setLineWrap(true);
        laporanArea.setWrapStyleWord(true);

        JScrollPane scrollPane = new JScrollPane(laporanArea);
        scrollPane.setBorder(BorderFactory.createEmptyBorder(0, 10, 10, 10));

        add(atasPanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
    }

    private void printLaporan() {
        try {
            boolean done = laporanArea.print();
            if (done) {
                JOptionPane.showMessageDialog(this, "Print berhasil.", "Info", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this, "Print dibatalkan.", "Info", JOptionPane.INFORMATION_MESSAGE);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Terjadi kesalahan saat print: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public void setLaporanText(String text) {
        laporanArea.setText(text);
    }

    public void appendLaporanText(String text) {
        laporanArea.append(text + "\n");
    }

    public void clearLaporan() {
        laporanArea.setText("");
    }
}
