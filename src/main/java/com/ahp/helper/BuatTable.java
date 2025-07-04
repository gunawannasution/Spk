package com.ahp.helper;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;

/**
 * Generic reusable table component with styling for FlatLaf Light theme.
 * @param <T> Type data model
 */
public class BuatTable<T> extends JPanel {
    private final JTable table;
    private final DefaultTableModel model;
    private final List<T> dataList;
    private Consumer<T> onRowClick;

    /**
     * Constructor BuatTable
     * @param columnNames nama kolom tabel
     * @param initialData data awal untuk diisi ke tabel (boleh null)
     * @param mapper fungsi mapping dari object T ke Object[] baris tabel
     */
    public BuatTable(String[] columnNames, List<T> initialData, Function<T, Object[]> mapper) {
        setLayout(new BorderLayout());
        setBackground(UIComponent.BACKGROUND_COLOR);

        // Pastikan dataList mutable
        this.dataList = initialData != null ? new ArrayList<>(initialData) : new ArrayList<>();

        model = new DefaultTableModel(columnNames, 0) {
            @Override public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        table = new JTable(model) {
            @Override
            public Component prepareRenderer(TableCellRenderer renderer, int row, int column) {
                Component c = super.prepareRenderer(renderer, row, column);
                if (!isRowSelected(row)) {
                    // Warna ganjil genap untuk tema light
                    c.setBackground(row % 2 == 0 ? UIComponent.CARD_COLOR : UIComponent.HIGHLIGHT_COLOR);
                    c.setForeground(UIComponent.TEXT_COLOR);
                } else {
                    c.setBackground(UIComponent.PRIMARY_COLOR);
                    c.setForeground(UIComponent.TITLE_COLOR);
                }
                return c;
            }
        };

        // Setup styling tabel
        table.setFont(UIComponent.FONT_REGULAR);
        table.setRowHeight(32);
        table.setGridColor(UIComponent.BORDER_COLOR);
        table.setSelectionBackground(UIComponent.PRIMARY_COLOR);
        table.setSelectionForeground(UIComponent.TITLE_COLOR);
        table.setShowVerticalLines(false);
        table.setIntercellSpacing(new Dimension(0, 0));
        table.setFillsViewportHeight(true);
        table.setBackground(UIComponent.BACKGROUND_COLOR);
        table.setForeground(UIComponent.TEXT_COLOR);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        JTableHeader header = table.getTableHeader();
        header.setFont(UIComponent.FONT_BOLD);
        header.setBackground(UIComponent.CARD_COLOR);
        header.setForeground(UIComponent.TITLE_COLOR);
        header.setPreferredSize(new Dimension(header.getWidth(), 36));
        header.setReorderingAllowed(false);

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(new EmptyBorder(10, 10, 10, 10));
        scrollPane.getViewport().setBackground(UIComponent.BACKGROUND_COLOR);
        scrollPane.setBackground(UIComponent.BACKGROUND_COLOR);
        add(scrollPane, BorderLayout.CENTER);

        // Isi data awal
        if (!this.dataList.isEmpty()) {
            for (T obj : this.dataList) {
                model.addRow(mapper.apply(obj));
            }
        }

        // Event double click baris
        table.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                if (evt.getClickCount() == 2 && onRowClick != null) {
                    int viewRow = table.getSelectedRow();
                    if (viewRow >= 0 && viewRow < dataList.size()) {
                        int modelRow = table.convertRowIndexToModel(viewRow);
                        T data = dataList.get(modelRow);
                        onRowClick.accept(data);
                    }
                }
            }
        });
    }

    public JTable getTable() {
        return table;
    }

    public T getRowData(int rowIndex) {
        if (rowIndex >= 0 && rowIndex < dataList.size()) {
            return dataList.get(rowIndex);
        }
        return null;
    }

    public void setOnRowClick(Consumer<T> listener) {
        this.onRowClick = listener;
    }

    /**
     * Refresh data tabel dan tampilkan ulang
     * @param newData list data baru
     * @param mapper fungsi mapping ke baris tabel
     */
    public void refreshData(List<T> newData, Function<T, Object[]> mapper) {
        model.setRowCount(0);
        dataList.clear();
        if (newData != null) {
            dataList.addAll(newData);
            for (T obj : newData) {
                model.addRow(mapper.apply(obj));
            }
        }
        clearSelection();
    }

    /**
     * Ambil data baris yang sedang dipilih
     * @return data model T atau null jika tidak ada
     */
    public T getSelectedRowData() {
        int viewRow = table.getSelectedRow();
        if (viewRow >= 0 && viewRow < dataList.size()) {
            int modelRow = table.convertRowIndexToModel(viewRow);
            return dataList.get(modelRow);
        }
        return null;
    }

    /**
     * Hapus pilihan baris yang sedang aktif
     */
    public void clearSelection() {
        table.clearSelection();
    }

    /**
     * Tambah satu baris data baru ke tabel dan list
     * @param obj data baru
     * @param mapper fungsi mapping data ke row
     */
    public void addRow(T obj, Function<T, Object[]> mapper) {
        if (obj != null) {
            dataList.add(obj);
            model.addRow(mapper.apply(obj));
        }
    }
}
