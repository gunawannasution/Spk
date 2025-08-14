package com.ahp.helper;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;
import java.util.ArrayList;
import java.util.function.Consumer;
import java.util.function.Function;

public class customTable<T> extends JPanel {
    private final JTable table;
    private final DefaultTableModel model;
    private final List<T> dataList;
    private Consumer<T> onRowDoubleClick;

    public customTable(String[] columns, List<T> data, Function<T, Object[]> mapper) {
        setLayout(new BorderLayout());
        setBackground(Color.WHITE);  // Default background

        this.dataList = new ArrayList<>();
        model = new DefaultTableModel(columns, 0) {
            @Override public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        this.table = new JTable(model);
        designTable.buatTable(table); // apply styling

        JScrollPane scroll = new JScrollPane(table);
        scroll.setBorder(new EmptyBorder(10, 10, 10, 10));
        scroll.getViewport().setBackground(Color.WHITE);
        add(scroll, BorderLayout.CENTER);

        refreshData(data, mapper);  // initial fill

        // Double click listener
        table.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                if (evt.getClickCount() == 2 && onRowDoubleClick != null) {
                    int viewRow = table.getSelectedRow();
                    if (viewRow >= 0 && viewRow < dataList.size()) {
                        int modelRow = table.convertRowIndexToModel(viewRow);
                        onRowDoubleClick.accept(dataList.get(modelRow));
                    }
                }
            }
        });
    }

    public JTable getTable() {
        return table;
    }

    public T getSelectedRowData() {
        int viewRow = table.getSelectedRow();
        if (viewRow >= 0 && viewRow < dataList.size()) {
            int modelRow = table.convertRowIndexToModel(viewRow);
            return dataList.get(modelRow);
        }
        return null;
    }

    public void setOnRowDoubleClick(Consumer<T> listener) {
        this.onRowDoubleClick = listener;
    }

    public void refreshData(List<T> newData, Function<T, Object[]> mapper) {
        model.setRowCount(0);
        dataList.clear();
        if (newData != null) {
            dataList.addAll(newData);
            for (T item : newData) {
                model.addRow(mapper.apply(item));
            }
        }
        clearSelection();
    }

    public void addRow(T data, Function<T, Object[]> mapper) {
        if (data != null) {
            dataList.add(data);
            model.addRow(mapper.apply(data));
        }
    }

    public void clearSelection() {
        table.clearSelection();
    }

    public T getRowData(int rowIndex) {
        if (rowIndex >= 0 && rowIndex < dataList.size()) {
            return dataList.get(rowIndex);
        }
        return null;
    }
}
