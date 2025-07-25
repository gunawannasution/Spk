package com.ahp.content;

import com.ahp.helper.UIComponent;
import com.ahp.helper.btnModern;
import javax.swing.*;
import java.awt.*;

public class DashboardPanel extends JPanel {
    public DashboardPanel() {
        setBackground(UIComponent.BACKGROUND_COLOR);
        setLayout(new BorderLayout(20, 20));

        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(UIComponent.BACKGROUND_COLOR);
        headerPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 10, 20));

        JPanel actionPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        actionPanel.setBackground(UIComponent.BACKGROUND_COLOR);
        
        btnModern btnNewAssessment = new btnModern("Penilaian Baru");
        btnModern btnViewReport = new btnModern("Lihat Laporan");
        
        actionPanel.add(btnNewAssessment);
        actionPanel.add(btnViewReport);

        headerPanel.add(actionPanel, BorderLayout.EAST);
        add(headerPanel, BorderLayout.NORTH);

        JPanel cardsPanel = new JPanel();
        cardsPanel.setBackground(UIComponent.BACKGROUND_COLOR);
        cardsPanel.setLayout(new GridLayout(1, 4, 20, 0));

        cardsPanel.add(summaryCard("Total Karyawan", "42", UIComponent.PRIMARY_COLOR, "orang"));
        cardsPanel.add(summaryCard("Kriteria Penilaian", "5", UIComponent.SECONDARY_COLOR, "kriteria"));
        cardsPanel.add(summaryCard("Penilaian Aktif", "3", UIComponent.PRIMARY_COLOR, "proses"));
        cardsPanel.add(summaryCard("Hasil Terakhir", "15 Jul", UIComponent.SUCCESS_COLOR, "update"));

        JPanel centerWrapper = new JPanel(new FlowLayout(FlowLayout.LEFT));
        centerWrapper.setBackground(UIComponent.BACKGROUND_COLOR);
        centerWrapper.setBorder(BorderFactory.createEmptyBorder(0, 20, 20, 20));
        centerWrapper.add(cardsPanel);
        add(centerWrapper, BorderLayout.CENTER);

        JPanel activityPanel = new JPanel(new BorderLayout());
        activityPanel.setBackground(UIComponent.BACKGROUND_COLOR);
        activityPanel.setBorder(BorderFactory.createEmptyBorder(0, 20, 20, 20));
        
        JLabel activityTitle = new JLabel("Aktivitas Terakhir");
        activityTitle.setFont(UIComponent.FONT_BOLD.deriveFont(18f));
        activityTitle.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));
        
        JTextArea activityLog = new JTextArea();
        activityLog.setEditable(false);
        activityLog.setBackground(UIComponent.CARD_COLOR);
        activityLog.setFont(UIComponent.FONT_REGULAR);
        activityLog.setText("""
                            1. Penilaian karyawan Juli 2023 selesai
                            2. Karyawan terbaik Juni: Ahmad Fauzi
                            3. Pembaruan bobot kriteria dilakukan""");
        
        JScrollPane scrollPane = new JScrollPane(activityLog);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        
        activityPanel.add(activityTitle, BorderLayout.NORTH);
        activityPanel.add(scrollPane, BorderLayout.CENTER);
        add(activityPanel, BorderLayout.SOUTH);
    }

    private JPanel summaryCard(String title, String value, Color color, String unit) {
        JPanel card = new JPanel();
        card.setBackground(UIComponent.CARD_COLOR);
        card.setLayout(new BorderLayout());
        card.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createEmptyBorder(15, 15, 15, 15),
            BorderFactory.createLineBorder(UIComponent.BORDER_COLOR, 1)
        ));
        card.setPreferredSize(new Dimension(200, 120));

        JPanel valuePanel = new JPanel(new BorderLayout());
        valuePanel.setBackground(UIComponent.CARD_COLOR);
        
        JLabel lblValue = new JLabel(value);
        lblValue.setFont(UIComponent.FONT_BOLD.deriveFont(32f));
        lblValue.setForeground(color);
        lblValue.setHorizontalAlignment(SwingConstants.CENTER);
        
        JLabel lblUnit = new JLabel(unit);
        lblUnit.setFont(UIComponent.FONT_REGULAR.deriveFont(14f));
        lblUnit.setForeground(UIComponent.TEXT_COLOR);
        lblUnit.setHorizontalAlignment(SwingConstants.CENTER);
        
        valuePanel.add(lblValue, BorderLayout.CENTER);
        valuePanel.add(lblUnit, BorderLayout.SOUTH);

        JLabel lblTitle = new JLabel(title);
        lblTitle.setFont(UIComponent.FONT_BOLD.deriveFont(16f));
        lblTitle.setForeground(UIComponent.TEXT_COLOR);
        lblTitle.setHorizontalAlignment(SwingConstants.CENTER);

        card.add(valuePanel, BorderLayout.CENTER);
        card.add(lblTitle, BorderLayout.SOUTH);
        
        //hover untuk button
        card.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent e) {
                card.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createEmptyBorder(15, 15, 15, 15),
                    BorderFactory.createLineBorder(color, 2)
                ));
            }
            @Override
            public void mouseExited(java.awt.event.MouseEvent e) {
                card.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createEmptyBorder(15, 15, 15, 15),
                    BorderFactory.createLineBorder(UIComponent.BORDER_COLOR, 1)
                ));
            }
        });
        return card;
    }
}