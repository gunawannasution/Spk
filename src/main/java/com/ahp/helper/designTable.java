package com.ahp.helper;

import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

public class designTable {

    public static void buatTable(JTable table) {
        // Basic appearance
        table.setRowHeight(30);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        table.setFillsViewportHeight(true);
        table.setAutoCreateRowSorter(true);
        table.setShowGrid(false);
        table.setIntercellSpacing(new Dimension(5, 5));
        table.setSelectionBackground(new Color(220, 220, 255));
        table.setSelectionForeground(Color.BLACK);
        table.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        table.setForeground(Color.BLACK);

        // Header styling
        JTableHeader header = table.getTableHeader();
        header.setBackground(new Color(63, 81, 181));
        header.setForeground(Color.WHITE);
        header.setFont(new Font("Segoe UI", Font.BOLD, 12));
        header.setReorderingAllowed(false);
        header.setPreferredSize(new Dimension(header.getWidth(), 36));

        // Cell renderer: center align and zebra color
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected,
                                                           boolean hasFocus, int row, int column) {
                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                c.setForeground(Color.BLACK);
                c.setFont(table.getFont());
                if (!isSelected) {
                    c.setBackground(row % 2 == 0 ? Color.WHITE : new Color(248, 248, 248));
                }
                if (c instanceof JLabel label) {
                    label.setHorizontalAlignment(SwingConstants.CENTER);
                }
                return c;
            }
        };

        applyRendererToAllColumns(table, centerRenderer);
    }

    private static void applyRendererToAllColumns(JTable table, DefaultTableCellRenderer renderer) {
        if (table.getColumnCount() > 0) {
            for (int i = 0; i < table.getColumnCount(); i++) {
                table.getColumnModel().getColumn(i).setCellRenderer(renderer);
            }
        } else {
            // Wait until model is set
            table.addPropertyChangeListener("model", (PropertyChangeEvent evt) -> {
                SwingUtilities.invokeLater(() -> {
                    for (int i = 0; i < table.getColumnCount(); i++) {
                        table.getColumnModel().getColumn(i).setCellRenderer(renderer);
                    }
                });
            });
        }
    }
}
