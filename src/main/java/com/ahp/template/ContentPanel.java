package com.ahp.template;

import com.ahp.content.DashboardPanel;
import com.ahp.content.KaryawanPanel;
import com.ahp.content.KriteriaPanel;
import com.ahp.content.MatrixPanel;
import com.ahp.content.PenilaianPanel;
import java.awt.BorderLayout;
import java.awt.CardLayout;
import javax.swing.JPanel;
import com.formdev.flatlaf.FlatClientProperties;

public class ContentPanel extends JPanel {
    private final CardLayout cardLayout = new CardLayout();
    private final JPanel cardPanel = new JPanel(cardLayout);
    
    // Simpan referensi MatrixPanel agar tidak null saat dipanggil
    private MatrixPanel matrixPanel;

    public ContentPanel() {
        setLayout(new BorderLayout());
        putClientProperty(FlatClientProperties.STYLE,"background: $Panel.background");
        
        cardPanel.putClientProperty(FlatClientProperties.STYLE,"background: $Panel.background");
        add(cardPanel, BorderLayout.CENTER);
        buatPanel();
    }

    private void buatPanel() {
        addPanelWithFade(new DashboardPanel(), "dashboard");
        addPanelWithFade(new KaryawanPanel(), "karyawan");
        addPanelWithFade(new KriteriaPanel(), "kriteria");

        // Inisialisasi dan simpan instance MatrixPanel
        matrixPanel = new MatrixPanel();
        addPanelWithFade(matrixPanel, "matrix");

        addPanelWithFade(new PenilaianPanel(), "penilaian");
    }

    private void addPanelWithFade(JPanel panel, String name) {
        panel.putClientProperty(FlatClientProperties.STYLE,"background: $Panel.background");
        cardPanel.add(panel, name);
    }

    public void showPanel(String name) {
        try {
            cardLayout.show(cardPanel, name);
            cardPanel.revalidate();
            cardPanel.repaint();
        } catch (IllegalArgumentException e) {
            System.err.println("Panel not found: " + name);
        }
    }

    public void navigateTo(String panelName) {
        if (cardPanel.getComponentCount() > 0) {
            showPanel(panelName);
        }
    }

    public JPanel getCurrentPanel() {
        for (java.awt.Component comp : cardPanel.getComponents()) {
            if (comp.isVisible()) {
                return (JPanel) comp;
            }
        }
        return null;
    }

    // Getter matrixPanel yang sudah diinisialisasi
    public MatrixPanel getMatrixPanel() {
        return matrixPanel;
    }
}
