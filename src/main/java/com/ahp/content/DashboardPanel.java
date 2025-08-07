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
import com.formdev.flatlaf.FlatClientProperties;

import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.*;
import java.awt.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class DashboardPanel extends JPanel {
    private JTable tableTopHasil;
    private DefaultTableModel modelTopHasil;
    private JTextArea activityLog;
    private JPanel statsPanel;
    private JButton btnRefreshActivity;
    private JButton btnNewAssessment;
    private JButton btnViewReport;
    
    private final HasilAlternatifDAO hasilDAO = new HasilAlternatifDAOImpl();
    private final KaryawanDAO karyawanDAO = new KaryawanDAOImpl();

    public DashboardPanel() {
        initComponents();
        initActionListeners();
        loadInitialData();
    }

    private void initComponents() {
        // Main panel setup
        setBackground(UIComponent.BACKGROUND_COLOR);
        setLayout(new BorderLayout(0, 0));
        setBorder(new EmptyBorder(20, 20, 20, 20));

        // Header Panel
        JPanel headerPanel = createHeaderPanel();
        add(headerPanel, BorderLayout.NORTH);

        // Main Content Panel
        JPanel contentPanel = new JPanel(new BorderLayout(0, 20));
        contentPanel.setBackground(UIComponent.BACKGROUND_COLOR);
        
        // Stats Cards Panel
        statsPanel = createStatsPanel();
        contentPanel.add(statsPanel, BorderLayout.NORTH);

        // Bottom Panel (Activity Log + Top Results)
        JPanel bottomPanel = new JPanel(new GridLayout(1, 2, 20, 0));
        bottomPanel.setBackground(UIComponent.BACKGROUND_COLOR);
        bottomPanel.setBorder(new EmptyBorder(20, 0, 0, 0));
        
        bottomPanel.add(createActivityPanel());
        bottomPanel.add(createTopResultsPanel());
        
        contentPanel.add(bottomPanel, BorderLayout.CENTER);
        add(contentPanel, BorderLayout.CENTER);
    }

    private JPanel createHeaderPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(UIComponent.BACKGROUND_COLOR);
        panel.setBorder(new EmptyBorder(0, 0, 20, 0));

        // Title
        JLabel title = new JLabel("Dashboard");
        title.setFont(UIComponent.FONT_BOLD.deriveFont(24f));
        title.setForeground(UIComponent.PRIMARY_COLOR);
        panel.add(title, BorderLayout.WEST);

        // Action Buttons
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 0));
        buttonPanel.setBackground(UIComponent.BACKGROUND_COLOR);

        btnNewAssessment = new btnModern("");
        btnNewAssessment.setIcon(new ImageIcon(getClass().getResource("/icons/penilaian.png")));
        btnNewAssessment.putClientProperty(FlatClientProperties.BUTTON_TYPE, "borderless");

        btnViewReport = new btnModern("");
        btnViewReport.setIcon(new ImageIcon(getClass().getResource("/icons/laporan.png")));
        btnViewReport.putClientProperty(FlatClientProperties.BUTTON_TYPE, "borderless");

        buttonPanel.add(btnNewAssessment);
        buttonPanel.add(btnViewReport);

        panel.add(buttonPanel, BorderLayout.EAST);
        return panel;
    }

    private JPanel createStatsPanel() {
        JPanel panel = new JPanel(new GridLayout(1, 4, 15, 0));
        panel.setBackground(UIComponent.BACKGROUND_COLOR);
        return panel;
    }

    private JPanel createStatCard(String title, Object value, Color color, String iconPath) {
        JPanel card = new JPanel();
        card.setBackground(UIComponent.CARD_COLOR);
        card.setLayout(new BorderLayout());
        card.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createMatteBorder(0, 0, 0, 0, UIComponent.BORDER_COLOR),
            new EmptyBorder(20, 20, 20, 20)
        ));
        card.putClientProperty(FlatClientProperties.STYLE, "arc: 10");

        // Icon
        JLabel iconLabel = new JLabel(new ImageIcon(getClass().getResource(iconPath)));
        iconLabel.setBorder(new EmptyBorder(0, 0, 15, 0));
        card.add(iconLabel, BorderLayout.NORTH);

        // Value
        JLabel valueLabel = new JLabel(value.toString());
        valueLabel.setFont(UIComponent.FONT_BOLD.deriveFont(28f));
        valueLabel.setForeground(color);
        card.add(valueLabel, BorderLayout.CENTER);

        // Title
        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(UIComponent.FONT_MEDIUM.deriveFont(14f));
        titleLabel.setForeground(UIComponent.TEXT_SECONDARY_COLOR);
        titleLabel.setBorder(new EmptyBorder(10, 0, 0, 0));
        card.add(titleLabel, BorderLayout.SOUTH);

        // Hover effect
        card.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent e) {
                card.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createMatteBorder(1, 1, 1, 1, color),
                    new EmptyBorder(19, 19, 19, 19)
                ));
            }

            @Override
            public void mouseExited(java.awt.event.MouseEvent e) {
                card.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createMatteBorder(0, 0, 0, 0, UIComponent.BORDER_COLOR),
                    new EmptyBorder(20, 20, 20, 20)
                ));
            }
        });

        return card;
    }

    private JPanel createActivityPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(UIComponent.CARD_COLOR);
        panel.setBorder(new RoundBorder(10, UIComponent.BORDER_COLOR));

        // Header
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(UIComponent.CARD_COLOR);
        header.setBorder(new EmptyBorder(15, 20, 15, 20));

        JLabel title = new JLabel("Aktivitas Terakhir");
        title.setFont(UIComponent.FONT_BOLD.deriveFont(16f));
        title.setForeground(UIComponent.TEXT_COLOR);

        btnRefreshActivity = new JButton(new ImageIcon(getClass().getResource("/icons/date.png")));
        btnRefreshActivity.putClientProperty(FlatClientProperties.BUTTON_TYPE, "borderless");
        btnRefreshActivity.setToolTipText("Refresh");
        
        JPanel headerRight = new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 0));
        headerRight.setBackground(UIComponent.CARD_COLOR);
        headerRight.add(btnRefreshActivity);

        header.add(title, BorderLayout.WEST);
        header.add(headerRight, BorderLayout.EAST);

        // Content
        activityLog = new JTextArea();
        activityLog.setEditable(false);
        activityLog.setFont(UIComponent.FONT_REGULAR.deriveFont(14f));
        activityLog.setForeground(UIComponent.TEXT_COLOR);
        activityLog.setBackground(UIComponent.CARD_COLOR);
        activityLog.setBorder(new EmptyBorder(0, 20, 20, 20));
        activityLog.setLineWrap(true);
        activityLog.setWrapStyleWord(true);

        JScrollPane scrollPane = new JScrollPane(activityLog);
        scrollPane.setBorder(null);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);

        panel.add(header, BorderLayout.NORTH);
        panel.add(scrollPane, BorderLayout.CENTER);

        return panel;
    }

    private JPanel createTopResultsPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(UIComponent.CARD_COLOR);
        panel.setBorder(new RoundBorder(10, UIComponent.BORDER_COLOR));

        // Header
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(UIComponent.CARD_COLOR);
        header.setBorder(new EmptyBorder(15, 20, 15, 20));

        JLabel title = new JLabel("Top 3 Karyawan");
        title.setFont(UIComponent.FONT_BOLD.deriveFont(16f));
        title.setForeground(UIComponent.TEXT_COLOR);

        header.add(title, BorderLayout.WEST);
        panel.add(header, BorderLayout.NORTH);

        // Table
        modelTopHasil = new DefaultTableModel(new Object[]{"Rank", "Nama Karyawan", "Skor"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        tableTopHasil = new JTable(modelTopHasil);
        customizeTableAppearance();

        JScrollPane scrollPane = new JScrollPane(tableTopHasil);
        scrollPane.setBorder(null);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);

        panel.add(scrollPane, BorderLayout.CENTER);
        return panel;
    }

    private void customizeTableAppearance() {
        tableTopHasil.setFont(UIComponent.FONT_REGULAR.deriveFont(14f));
        tableTopHasil.setRowHeight(40);
        tableTopHasil.setShowGrid(false);
        tableTopHasil.setIntercellSpacing(new Dimension(0, 0));
        tableTopHasil.setFillsViewportHeight(true);
        tableTopHasil.setSelectionBackground(UIComponent.PRIMARY_COLOR.brighter());
        tableTopHasil.setSelectionForeground(Color.WHITE);

        // Custom renderer
        tableTopHasil.setDefaultRenderer(Object.class, new ModernTableRenderer());

        // Header styling
        JTableHeader headerTable = tableTopHasil.getTableHeader();
        headerTable.setFont(UIComponent.FONT_BOLD.deriveFont(14f));
        headerTable.setBackground(UIComponent.PRIMARY_COLOR);
        headerTable.setForeground(Color.WHITE);
        headerTable.setReorderingAllowed(false);
    }

    private class ModernTableRenderer extends DefaultTableCellRenderer {
        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, 
                                                     boolean hasFocus, int row, int column) {
            super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
            
            setBorder(new EmptyBorder(0, 15, 0, 15));
            
            if (isSelected) {
                setBackground(table.getSelectionBackground());
                setForeground(table.getSelectionForeground());
            } else {
                setBackground(row % 2 == 0 ? UIComponent.CARD_COLOR : new Color(245, 245, 245));
                setForeground(UIComponent.TEXT_COLOR);
            }
            
            // Special styling for rank column
            if (column == 0) {
                setHorizontalAlignment(SwingConstants.CENTER);
                setFont(UIComponent.FONT_BOLD.deriveFont(14f));
                if (value.toString().contains("🥇")) setForeground(UIComponent.SUCCESS_COLOR);
                else if (value.toString().contains("🥈")) setForeground(UIComponent.WARNING_COLOR);
                else if (value.toString().contains("🥉")) setForeground(UIComponent.ACCENT_COLOR);
            } else if (column == 2) { // Score column
                setHorizontalAlignment(SwingConstants.RIGHT);
            } else {
                setHorizontalAlignment(SwingConstants.LEFT);
            }
            
            return this;
        }
    }

    private void loadInitialData() {
        showLoading(true);
        
        SwingWorker<Void, Void> worker = new SwingWorker<>() {
            @Override
            protected Void doInBackground() {
                refreshStats();
                refreshActivityLog();
                loadTopHasilData();
                return null;
            }

            @Override
            protected void done() {
                showLoading(false);
            }
        };
        worker.execute();
    }

    private void refreshStats() {
        SwingUtilities.invokeLater(() -> {
            statsPanel.removeAll();
            
            int totalKaryawan = getCount("alternatif");
            int totalKriteria = getCount("kriteria");
            int penilaianAktif = getCount("hasil_alternatif");
            String lastUpdated = getLastPenilaianDate();

            statsPanel.add(createStatCard("Total Karyawan", totalKaryawan, UIComponent.PRIMARY_COLOR, "/icons/karyawan.png"));
            statsPanel.add(createStatCard("Total Kriteria", totalKriteria, UIComponent.SECONDARY_COLOR, "/icons/kriteria.png"));
            statsPanel.add(createStatCard("Penilaian Aktif", penilaianAktif, UIComponent.ACCENT_COLOR, "/icons/penilaian.png"));
            statsPanel.add(createStatCard("Update Terakhir", lastUpdated, UIComponent.SUCCESS_COLOR, "/icons/update.png"));
            
            statsPanel.revalidate();
            statsPanel.repaint();
        });
    }

    private void refreshActivityLog() {
        SwingWorker<String, Void> worker = new SwingWorker<>() {
            @Override
            protected String doInBackground() {
                try {
                    List<String> logList = getActivityLog();
                    return String.join("\n\u2022 ", logList); // Bullet list formatting
                } catch (SQLException e) {
                    e.printStackTrace();
                    return "Gagal memuat aktivitas terbaru.";
                }
            }

            @Override
            protected void done() {
                try {
                    activityLog.setText("\u2022 " + get());
                } catch (Exception e) {
                    activityLog.setText("Gagal memuat aktivitas terbaru.");
                }
            }
        };
        worker.execute();
    }

        public List<String> getActivityLog() throws SQLException {
        List<String> logList = new ArrayList<>();

        String sql = "SELECT aktivitas FROM log_aktivitas ORDER BY waktu DESC";

        try (
            Connection conn = DBConnection.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery()
        ) {
            while (rs.next()) {
                logList.add(rs.getString("aktivitas"));
            }
        }

        return logList;
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
                        
                        // Add medal emoji based on rank
                        String rankEmoji = "";
                        if (ha.getPeringkat() == 1) rankEmoji = "🥇 ";
                        else if (ha.getPeringkat() == 2) rankEmoji = "🥈 ";
                        else if (ha.getPeringkat() == 3) rankEmoji = "🥉 ";
                        
                        modelTopHasil.addRow(new Object[]{
                            rankEmoji + ha.getPeringkat(),
                            nama,
                            String.format("%.4f", ha.getSkor())
                        });
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                    modelTopHasil.addRow(new Object[]{"Error", "Gagal memuat data", ""});
                }
            }
        };
        worker.execute();
    }

    private void initActionListeners() {
        btnRefreshActivity.addActionListener(e -> refreshActivityLog());
        
        btnNewAssessment.addActionListener(e -> {
            JOptionPane.showMessageDialog(this, 
                "Membuka form penilaian baru...",
                "Penilaian Baru",
                JOptionPane.INFORMATION_MESSAGE);
        });
        
        btnViewReport.addActionListener(e -> {
            JOptionPane.showMessageDialog(this, 
                "Membuka laporan penilaian...",
                "Laporan Penilaian",
                JOptionPane.INFORMATION_MESSAGE);
        });
    }

    private void showLoading(boolean show) {
        // Implement loading indicator if needed
        setCursor(show ? Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR) : Cursor.getDefaultCursor());
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

    private class RoundBorder extends AbstractBorder {
        private final int radius;
        private final Color color;

        public RoundBorder(int radius, Color color) {
            this.radius = radius;
            this.color = color;
        }

        @Override
        public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(color);
            g2.drawRoundRect(x, y, width-1, height-1, radius, radius);
            g2.dispose();
        }

        @Override
        public Insets getBorderInsets(Component c) {
            return new Insets(radius+1, radius+1, radius+1, radius+1);
        }

        @Override
        public Insets getBorderInsets(Component c, Insets insets) {
            insets.left = insets.right = insets.bottom = insets.top = radius+1;
            return insets;
        }
    }
}