package com.ahp.content;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import com.ahp.helper.UIComponent;

public class LaporanPanel extends JPanel {
    private final PenilaianPanel penilaianPanel;
    private final KaryawanPanel karyawanPanel;
    private final MatrixPanel matrixPanel;
    private final KriteriaPanel kriteriaPanel;

    public LaporanPanel(PenilaianPanel penilaianPanel, KaryawanPanel karyawanPanel, 
                       MatrixPanel matrixPanel, KriteriaPanel kriteriaPanel) {
        this.penilaianPanel = penilaianPanel;
        this.karyawanPanel = karyawanPanel;
        this.matrixPanel = matrixPanel;
        this.kriteriaPanel = kriteriaPanel;
        initComponents();
    }

    private void initComponents() {
        setLayout(new BorderLayout(0, 0));
        setBackground(UIComponent.BACKGROUND_COLOR);
        
        // Main content panel with padding
        JPanel contentPanel = new JPanel(new GridBagLayout());
        contentPanel.setBackground(UIComponent.BACKGROUND_COLOR);
        contentPanel.setBorder(new EmptyBorder(30, 30, 30, 30));
        add(contentPanel, BorderLayout.CENTER);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(15, 15, 15, 15);
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;

        // Header panel with shadow effect
        JPanel headerPanel = new JPanel();
        headerPanel.setLayout(new BoxLayout(headerPanel, BoxLayout.Y_AXIS));
        headerPanel.setBackground(UIComponent.BACKGROUND_COLOR);
        headerPanel.setBorder(new EmptyBorder(0, 0, 20, 0));
        
        JLabel header = new JLabel("Laporan");
        header.setFont(UIComponent.FONT_BOLD.deriveFont(28f));
        header.setForeground(UIComponent.PRIMARY_COLOR.darker());
        header.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        JLabel subHeader = new JLabel("Pilih jenis laporan yang ingin dicetak");
        subHeader.setFont(UIComponent.FONT_PLAIN.deriveFont(14f));
        subHeader.setForeground(new Color(100, 100, 100));
        subHeader.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        headerPanel.add(header);
        headerPanel.add(Box.createRigidArea(new Dimension(0, 5)));
        headerPanel.add(subHeader);
        
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.NORTH;
        contentPanel.add(headerPanel, gbc);

        // Button grid
        gbc.gridwidth = 1;
        gbc.insets = new Insets(10, 10, 10, 10);
        
        // Row 1
        gbc.gridy = 1;
        gbc.gridx = 0;
        contentPanel.add(createModernButton("Cetak Nilai Karyawan", "/icons/print.png", 
            "Cetak Nilai Karyawan Berdasarkan Kriteria", () -> {
                if (penilaianPanel != null) penilaianPanel.printReport();
                else pesanError.showError(this,"PenilaianPanel belum tersedia!");
            }), gbc);

        gbc.gridx = 1;
        contentPanel.add(createModernButton("Cetak Data Karyawan", "/icons/print.png", 
            "Cetak daftar karyawan", () -> karyawanPanel.printReport()), gbc);

        // Row 2
        gbc.gridy = 2;
        gbc.gridx = 0;
        contentPanel.add(createModernButton("Cetak Bobot Perbandingan", "/icons/print.png", 
            "Cetak Bobot perbandingan Kriteria", () -> matrixPanel.printReport()), gbc);

        gbc.gridx = 1;
        contentPanel.add(createModernButton("Cetak Kriteria", "/icons/print.png", 
            "Cetak Kriteria dan Bobot", () -> kriteriaPanel.printReport()), gbc);
        
        // Row 3 - Centered single button
        gbc.gridy = 3;
        gbc.gridx = 0;
        gbc.gridwidth = 2;
        contentPanel.add(createAccentButton("Cetak Hasil AHP", "/icons/print.png", 
            "Cetak hasil akhir perhitungan AHP", () -> penilaianPanel.printReportHasil()), gbc);
    }

    private JButton createModernButton(String title, String iconPath, String tooltip, Runnable action) {
        JButton button = new JButton(title);
        try {
            button.setIcon(new ImageIcon(getClass().getResource(iconPath)));
        } catch (Exception e) {
            // Fallback if icon not found
            button.setIcon(UIManager.getIcon("OptionPane.informationIcon"));
        }
        
        button.setFont(UIComponent.FONT_MEDIUM.deriveFont(14f));
        button.setForeground(new Color(60, 60, 60));
        button.setBackground(Color.WHITE);
        button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        button.setFocusPainted(false);
        button.setHorizontalAlignment(SwingConstants.LEFT);
        button.setIconTextGap(15);
        button.setBorder(BorderFactory.createCompoundBorder(
            new LineBorder(new Color(220, 220, 220), 1),
            new EmptyBorder(15, 20, 15, 20)
        ));
        button.setToolTipText(tooltip);
        
        // Hover effect
        button.addMouseListener(new MouseAdapter() {
            @Override public void mouseEntered(MouseEvent e) {
                button.setBackground(new Color(245, 245, 245));
                button.setBorder(BorderFactory.createCompoundBorder(
                    new LineBorder(UIComponent.PRIMARY_COLOR.brighter(), 1),
                    new EmptyBorder(15, 20, 15, 20)
                ));
            }
            @Override public void mouseExited(MouseEvent e) {
                button.setBackground(Color.WHITE);
                button.setBorder(BorderFactory.createCompoundBorder(
                    new LineBorder(new Color(220, 220, 220), 1),
                    new EmptyBorder(15, 20, 15, 20)
                ));
            }
        });

        button.addActionListener(e -> {
            try {
                action.run();
            } catch (Exception ex) {
                pesanError.showCustomError("Gagal mencetak: " + ex.getMessage(),"Error");
            }
        });

        return button;
    }
    
    private JButton createAccentButton(String title, String iconPath, String tooltip, Runnable action) {
        JButton button = createModernButton(title, iconPath, tooltip, action);
        button.setForeground(Color.WHITE);
        button.setBackground(UIComponent.PRIMARY_COLOR);
        button.setBorder(BorderFactory.createCompoundBorder(
            new LineBorder(UIComponent.PRIMARY_COLOR.darker(), 1),
            new EmptyBorder(15, 20, 15, 20)
        ));
        
        // Hover effect for accent button
        button.addMouseListener(new MouseAdapter() {
            @Override public void mouseEntered(MouseEvent e) {
                button.setBackground(UIComponent.PRIMARY_COLOR.brighter());
            }
            @Override public void mouseExited(MouseEvent e) {
                button.setBackground(UIComponent.PRIMARY_COLOR);
            }
        });
        
        return button;
    }
}