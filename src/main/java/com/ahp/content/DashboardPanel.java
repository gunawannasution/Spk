package com.ahp.content;

import com.ahp.content.dao.HasilAlternatifDAO;
import com.ahp.content.dao.HasilAlternatifDAOImpl;
import com.ahp.content.dao.KaryawanDAO;
import com.ahp.content.dao.KaryawanDAOImpl;
import com.ahp.content.model.HasilAlternatif;
import com.ahp.content.model.Karyawan;
import com.ahp.helper.UIComponent;
import com.ahp.helper.btnModern;
import com.ahp.util.DBConnection;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class DashboardPanel extends JPanel {
    private JTable tableTopHasil;
    private DefaultTableModel modelTopHasil;
    private final HasilAlternatifDAO hasilDAO = new HasilAlternatifDAOImpl();
    private final KaryawanDAO karyawanDAO = new KaryawanDAOImpl();

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

        int totalKaryawan = getCount("alternatif");
        int totalKriteria = getCount("kriteria");
        int penilaianAktif = getCount("hasil_alternatif");
        String lastUpdated = getLastPenilaianDate();

        cardsPanel.add(summaryCard("Total Karyawan", String.valueOf(totalKaryawan), UIComponent.PRIMARY_COLOR, "orang"));
        cardsPanel.add(summaryCard("Kriteria Penilaian", String.valueOf(totalKriteria), UIComponent.SECONDARY_COLOR, "kriteria"));
        cardsPanel.add(summaryCard("Penilaian Aktif", String.valueOf(penilaianAktif), UIComponent.PRIMARY_COLOR, "proses"));
        cardsPanel.add(summaryCard("Hasil Terakhir", lastUpdated, UIComponent.SUCCESS_COLOR, "update"));

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
        activityLog.setText(getActivityLog());

        JScrollPane scrollPane = new JScrollPane(activityLog);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());

        activityPanel.add(activityTitle, BorderLayout.NORTH);
        activityPanel.add(scrollPane, BorderLayout.CENTER);

        JPanel hasilPanel = createTopHasilPanel();
        loadTopHasilData();

        centerWrapper.add(Box.createRigidArea(new Dimension(20, 0))); // spasi
        centerWrapper.add(hasilPanel);

        add(activityPanel, BorderLayout.SOUTH);
    }

    private int getCount(String table) {
        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement()) {

            ResultSet rs = stmt.executeQuery("SELECT COUNT(*) AS total FROM " + table);
            if (rs.next()) {
                return rs.getInt("total");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    private String getLastPenilaianDate() {
        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement()) {

            ResultSet rs = stmt.executeQuery("SELECT MAX(tanggal) AS last FROM hasil_alternatif");
            if (rs.next()) {
                Date date = rs.getDate("last");
                if (date != null) {
                    SimpleDateFormat sdf = new SimpleDateFormat("dd MMM yyyy");
                    return sdf.format(date);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return "-";
    }

    private String getActivityLog() {
        StringBuilder log = new StringBuilder();
        String query = "SELECT aktivitas FROM log_aktivitas ORDER BY waktu DESC LIMIT 5";

        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            int no = 1;
            while (rs.next()) {
                log.append(no++).append(". ").append(rs.getString("aktivitas")).append("\n");
            }

        } catch (Exception e) {
            e.printStackTrace();
            return "Gagal mengambil data aktivitas.";
        }

        return log.length() > 0 ? log.toString() : "Belum ada aktivitas tercatat.";
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

        JLabel lblValue = new JLabel("<html><center>" + value + "</center></html>");
        lblValue.setFont(UIComponent.FONT_BOLD.deriveFont(28f));
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

    private JPanel createTopHasilPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(UIComponent.CARD_COLOR);
        panel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createEmptyBorder(15, 15, 15, 15),
                BorderFactory.createLineBorder(UIComponent.BORDER_COLOR, 1)
        ));
        panel.setPreferredSize(new Dimension(600, 180));

        JLabel title = new JLabel("#3 Terbaik#");
        title.setFont(UIComponent.FONT_BOLD.deriveFont(16f));
        title.setForeground(UIComponent.TEXT_COLOR);
        title.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));

        modelTopHasil = new DefaultTableModel(new Object[]{"Peringkat", "Nama", "Skor"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        tableTopHasil = new JTable(modelTopHasil);
        tableTopHasil.setFont(UIComponent.FONT_REGULAR.deriveFont(13f));
        tableTopHasil.setRowHeight(28);
        tableTopHasil.setShowGrid(false);
        tableTopHasil.setIntercellSpacing(new Dimension(0, 0));
        tableTopHasil.setFillsViewportHeight(true);
        tableTopHasil.setSelectionBackground(UIComponent.PRIMARY_COLOR.brighter());
        tableTopHasil.setSelectionForeground(Color.WHITE);

        // Custom cell renderer untuk zebra striping dan hover effect
        tableTopHasil.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            Color evenColor = new Color(245, 245, 245);
            Color oddColor = Color.WHITE;
            Color hoverColor = UIComponent.PRIMARY_COLOR.brighter().brighter();

            int hoveredRow = -1;

            {
                tableTopHasil.addMouseMotionListener(new java.awt.event.MouseMotionAdapter() {
                    @Override
                    public void mouseMoved(java.awt.event.MouseEvent e) {
                        int row = tableTopHasil.rowAtPoint(e.getPoint());
                        if (row != hoveredRow) {
                            hoveredRow = row;
                            tableTopHasil.repaint();
                        }
                    }
                });
                tableTopHasil.addMouseListener(new java.awt.event.MouseAdapter() {
                    @Override
                    public void mouseExited(java.awt.event.MouseEvent e) {
                        hoveredRow = -1;
                        tableTopHasil.repaint();
                    }
                });
            }

            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected,
                                                           boolean hasFocus, int row, int column) {

                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

                if (isSelected) {
                    c.setBackground(table.getSelectionBackground());
                    c.setForeground(table.getSelectionForeground());
                } else if (row == hoveredRow) {
                    c.setBackground(hoverColor);
                    c.setForeground(Color.BLACK);
                } else {
                    c.setBackground(row % 2 == 0 ? evenColor : oddColor);
                    c.setForeground(Color.BLACK);
                }

                setBorder(new EmptyBorder(0, 10, 0, 10)); // padding kiri kanan

                if (column == 0 || column == 2) { // Center align untuk Peringkat & Skor
                    setHorizontalAlignment(SwingConstants.CENTER);
                } else { // Left align untuk Nama
                    setHorizontalAlignment(SwingConstants.LEFT);
                }

                return c;
            }
        });

        // Custom header style
        JTableHeader header = tableTopHasil.getTableHeader();
        header.setBackground(UIComponent.PRIMARY_COLOR);
        header.setForeground(Color.WHITE);
        header.setFont(UIComponent.FONT_BOLD.deriveFont(14f));
        ((DefaultTableCellRenderer) header.getDefaultRenderer()).setHorizontalAlignment(SwingConstants.CENTER);
        header.setReorderingAllowed(false);
        header.setResizingAllowed(false);

        JScrollPane scrollPane = new JScrollPane(tableTopHasil);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());

        panel.add(title, BorderLayout.NORTH);
        panel.add(scrollPane, BorderLayout.CENTER);

        return panel;
    }

    private void loadTopHasilData() {
        SwingWorker<List<HasilAlternatif>, Void> worker = new SwingWorker<>() {
            @Override
            protected List<HasilAlternatif> doInBackground() {
                return hasilDAO.getTopHasil(3);
            }

            @Override
            protected void done() {
                try {
                    List<HasilAlternatif> topList = get();
                    modelTopHasil.setRowCount(0);

                    for (HasilAlternatif ha : topList) {
                        Karyawan karyawan = karyawanDAO.getById(ha.getIdAlternatif());
                        String nama = (karyawan != null) ? karyawan.getNama() : "N/A";
                        modelTopHasil.addRow(new Object[]{
                                ha.getPeringkat(),
                                nama,
                                String.format("%.4f", ha.getSkor())
                        });
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };

        worker.execute();
    }
}
