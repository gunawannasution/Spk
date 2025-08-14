package com.ahp.template;

import com.ahp.content.*;
import com.formdev.flatlaf.FlatClientProperties;
import javax.swing.*;
import java.awt.*;

public class ContentPanel extends JPanel {
    private final CardLayout cardLayout = new CardLayout();
    private final JPanel cardPanel = new JPanel(cardLayout);
    private String currentPanelName = "dashboard"; // Track current panel name
    
    // Panel instances
    private final DashboardPanel dashboardPanel;
    private final KaryawanPanel karyawanPanel;
    private final KriteriaPanel kriteriaPanel;
    private final MatrixPanel matrixPanel;
    private final PenilaianPanel penilaianPanel;
    private final LaporanPanel laporanPanel;

    public ContentPanel() {
        // Initialize all panels
        dashboardPanel = new DashboardPanel();
        karyawanPanel = new KaryawanPanel();
        kriteriaPanel = new KriteriaPanel();
        matrixPanel = new MatrixPanel();
        penilaianPanel = new PenilaianPanel();
        laporanPanel = new LaporanPanel(penilaianPanel, karyawanPanel, matrixPanel, kriteriaPanel);

        setLayout(new BorderLayout());
        applyBackgroundStyle(this);
        
        cardPanel.setOpaque(false);
        applyBackgroundStyle(cardPanel);
        add(cardPanel, BorderLayout.CENTER);
        
        initializePanels();
    }

    private void initializePanels() {
        addPanel(dashboardPanel, "dashboard");
        addPanel(karyawanPanel, "karyawan");
        addPanel(kriteriaPanel, "kriteria");
        addPanel(matrixPanel, "matrix");
        addPanel(penilaianPanel, "penilaian");
        addPanel(laporanPanel, "laporan");
    }

    private void addPanel(JPanel panel, String name) {
        applyBackgroundStyle(panel);
        cardPanel.add(panel, name);
    }

    private void applyBackgroundStyle(JComponent component) {
        component.putClientProperty(
            FlatClientProperties.STYLE,
            "background: $Panel.background"
        );
    }

    public void showPanel(String name) {
        try {
            cardLayout.show(cardPanel, name);
            currentPanelName = name; // Update current panel name
            refreshPanel();
        } catch (IllegalArgumentException e) {
            System.err.println("Panel not found: " + name);
            // Fallback to dashboard
            cardLayout.show(cardPanel, "dashboard");
            currentPanelName = "dashboard";
            refreshPanel();
        }
    }

    public String getCurrentPanelName() {
        return currentPanelName;
    }

    private void refreshPanel() {
        cardPanel.revalidate();
        cardPanel.repaint();
    }

    // ... (keep all other existing methods) ...
}