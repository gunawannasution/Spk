package com.ahp.content;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import com.ahp.helper.UIComponent;

public class LaporanPanel extends JPanel {
    private final PenilaianPanel penilaianPanel;
    private final KaryawanPanel karyawanPanel;
    private final MatrixPanel matrixPanel;
    private final KriteriaPanel kriteriaPanel;

    public LaporanPanel(PenilaianPanel penilaianPanel, KaryawanPanel karyawanPanel, MatrixPanel matrixPanel, KriteriaPanel kriteriaPanel) {
        this.penilaianPanel = penilaianPanel;
        this.karyawanPanel = karyawanPanel;
        this.matrixPanel = matrixPanel;
        this.kriteriaPanel = kriteriaPanel;
        initComponents();
    }

    private void initComponents() {
        setLayout(new GridBagLayout());
        setBackground(UIComponent.BACKGROUND_COLOR);
        setBorder(new EmptyBorder(30, 30, 30, 30));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(15, 15, 15, 15);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;

        // Header judul panel
        JLabel header = new JLabel("Laporan");
        header.setFont(UIComponent.FONT_BOLD.deriveFont(24f));
        header.setForeground(UIComponent.PRIMARY_COLOR);
        header.setHorizontalAlignment(SwingConstants.CENTER);
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        gbc.weighty = 0;
        gbc.anchor = GridBagConstraints.PAGE_START;
        add(header, gbc);

        gbc.gridwidth = 1;
        gbc.weighty = 1.0;
        gbc.anchor = GridBagConstraints.CENTER;

        // Tombol baris 1
        gbc.gridy = 1;
        gbc.gridx = 0;
        add(createButton("Cetak Matriks Alternatif", "/icons/print.png", () -> {
            if (penilaianPanel != null) {
                penilaianPanel.printReport();
            } else {
                JOptionPane.showMessageDialog(this, "PenilaianPanel belum tersedia!", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }), gbc);

        gbc.gridx = 1;
        add(createButton("Cetak Karyawan", "/icons/print.png", () -> karyawanPanel.printReport()), gbc);

        // Tombol baris 2
        gbc.gridy = 2;
        gbc.gridx = 0;
        add(createButton("Cetak Matrix Alternatif", "/icons/print.png", () -> matrixPanel.printReport()), gbc);

        gbc.gridx = 1;
        add(createButton("Cetak Kriteria", "/icons/print.png", () -> kriteriaPanel.printReport()), gbc);
    }

    private JButton createButton(String title, String iconPath, Runnable action) {
        JButton button = new JButton(title, new ImageIcon(getClass().getResource(iconPath)));
        button.setFont(UIComponent.FONT_BOLD.deriveFont(14f));
        button.setPreferredSize(new Dimension(260, 45));
        button.setForeground(Color.WHITE);
        button.setBackground(UIComponent.PRIMARY_COLOR);
        button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        button.setFocusPainted(false);
        button.setHorizontalAlignment(SwingConstants.LEFT);
        button.setIconTextGap(12);
        button.setMargin(new Insets(8, 16, 8, 16));
        button.setOpaque(true);
        button.setBorder(BorderFactory.createEmptyBorder(6, 12, 6, 12));

        // Hover effect: warna background jadi sedikit lebih gelap
        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                button.setBackground(UIComponent.PRIMARY_COLOR.darker());
            }
            @Override
            public void mouseExited(MouseEvent e) {
                button.setBackground(UIComponent.PRIMARY_COLOR);
            }
        });

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
