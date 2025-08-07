package com.ahp.content;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import com.ahp.helper.UIComponent;

public class LaporanPanel1 extends JPanel {
    private PenilaianPanel penilaianPanel;
    private KaryawanPanel karyawanPanel;
    private MatrixPanel matrixPanel;
    private KriteriaPanel kriteriaPanel;

    public LaporanPanel1(PenilaianPanel penilaianPanel, KaryawanPanel karyawanPanel, MatrixPanel matrixPanel, KriteriaPanel kriteriaPanel) {
        this.penilaianPanel = penilaianPanel;
        this.karyawanPanel=karyawanPanel;
        this.matrixPanel=matrixPanel;
        this.kriteriaPanel=kriteriaPanel;
        initComponents();
    }

    private void initComponents() {
        setLayout(new GridBagLayout());
        setBackground(UIComponent.BACKGROUND_COLOR);
        setBorder(new EmptyBorder(20, 20, 20, 20));

        JButton printNilaiKaryawan = createButton("Cetak Nilai Karyawan","/icons/print.png",
            () -> {
                if (penilaianPanel != null) {
                    penilaianPanel.printReport();
                } else {
                    JOptionPane.showMessageDialog(this, "PenilaianPanel belum tersedia!", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        );
        

        add(printNilaiKaryawan);
        add(createButton("Cetak Data Karyawan", "/icons/print.png", () -> karyawanPanel.printReport()));
        add(createButton("Cetak Nilai Bobot Perbandingan", "/icons/print.png", () -> matrixPanel.printReport()));
        add(createButton("Cetak Kriteria", "/icons/print.png", () -> kriteriaPanel.printReport()));
    }

    private JButton createButton(String title, String iconPath, Runnable action) {
        JButton button = new JButton(title, new ImageIcon(getClass().getResource(iconPath)));
        button.setFont(UIComponent.FONT_SMALL); // kecilkan font
        button.setPreferredSize(new Dimension(220, 36)); // kecilkan ukuran tombol
        button.setForeground(Color.WHITE);
        button.setBackground(UIComponent.PRIMARY_COLOR);
        button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        button.setFocusPainted(false);
        button.setHorizontalAlignment(SwingConstants.LEFT);
        button.setIconTextGap(8);
        button.setMargin(new Insets(5, 12, 5, 12));

        button.addActionListener(e -> {
            try {
                action.run();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Gagal mencetak: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        return button;
    }
}
