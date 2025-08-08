package com.ahp.content;

import com.ahp.content.dao.KriteriaDAO;
import com.ahp.content.dao.KriteriaDAOImpl;
import com.ahp.content.model.Kriteria;
import com.ahp.helper.ReportUtil;
import com.ahp.helper.UIComponent;
import com.ahp.helper.btnModern;
import com.ahp.helper.SearchBox;
import com.ahp.helper.customTable;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.List;
import javax.swing.event.AncestorEvent;
import javax.swing.event.AncestorListener;
import org.jdesktop.swingx.prompt.PromptSupport;

public class KriteriaPanel extends JPanel {

    private final btnModern btnTambah, btnCetak, btnHapus;
    private customTable<Kriteria> tablePanel;
    private final KriteriaDAO dao = new KriteriaDAOImpl();

    public KriteriaPanel() {
        setLayout(new BorderLayout());

        JLabel lblJudul = new JLabel("Data Kriteria Penilaian");
        lblJudul.setFont(new Font("SansSerif", Font.BOLD, 20));
        lblJudul.setForeground(new Color(33, 33, 33));
        lblJudul.setBorder(new EmptyBorder(10, 15, 5, 15));

        btnTambah = new btnModern("Tambah", UIComponent.ADD_COLOR,new ImageIcon(getClass().getResource("/icons/add.png")));
        btnTambah.addActionListener(e -> inputData(null));

        btnCetak = new btnModern("Cetak", UIComponent.CETAK_COLOR,new ImageIcon(getClass().getResource("/icons/print.png")));
        btnCetak.addActionListener(e -> printReport());

        btnHapus = new btnModern("Hapus", UIComponent.DANGER_COLOR,new ImageIcon(getClass().getResource("/icons/delete.png")));
        btnHapus.setEnabled(false);
        btnHapus.addActionListener(e -> hapusDataTerpilih());
//        setupButtonAlignment(btnHapus);

        SearchBox search = new SearchBox("Cari data...", this::filterPencarian);
        search.setMaximumSize(new Dimension(250, 36));
        search.setPreferredSize(new Dimension(250, 36));

        JPanel atasPanel = new JPanel();
        atasPanel.setLayout(new BoxLayout(atasPanel, BoxLayout.X_AXIS));
        atasPanel.setBackground(UIComponent.BACKGROUND_COLOR);
        atasPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
        atasPanel.add(btnTambah);
        atasPanel.add(Box.createRigidArea(new Dimension(10, 0)));
        atasPanel.add(btnHapus);
        atasPanel.add(Box.createRigidArea(new Dimension(10, 0)));
        atasPanel.add(btnCetak);
        atasPanel.add(Box.createRigidArea(new Dimension(20, 0)));
        atasPanel.add(Box.createHorizontalGlue());
        atasPanel.add(search);

        JPanel panelAtasGabungan = new JPanel(new BorderLayout());
        panelAtasGabungan.setBackground(UIComponent.BACKGROUND_COLOR);
        panelAtasGabungan.add(lblJudul, BorderLayout.NORTH);
        panelAtasGabungan.add(atasPanel, BorderLayout.CENTER);

        add(panelAtasGabungan, BorderLayout.NORTH);

        tableKriteria();
        initListener();
    }
    private void initListener() {
        this.addAncestorListener(new AncestorListener() {
            @Override
            public void ancestorAdded(AncestorEvent event) {
                refreshTabel();
            }
            @Override
            public void ancestorRemoved(AncestorEvent event) {}
            @Override
            public void ancestorMoved(AncestorEvent event) {}
        });        
    } 
    
    private void inputData(Kriteria k) {
        boolean isEdit = (k != null);
        JDialog dialog = new JDialog(SwingUtilities.getWindowAncestor(this),
                isEdit ? "Edit Kriteria" : "Tambah Kriteria",
                Dialog.ModalityType.APPLICATION_MODAL);

        JTextField txtKode = UIComponent.buatTxt(20);
        JTextField txtNama = UIComponent.buatTxt(20);
        JTextField txtKet = UIComponent.buatTxt(20);
        JTextField txtBobot = UIComponent.buatTxt(20);

        PromptSupport.setPrompt("Masukkan Kode Kriteria", txtKode);
        PromptSupport.setPrompt("Masukkan Nama Kriteria", txtNama);
        PromptSupport.setPrompt("Masukkan Keterangan", txtKet);
        PromptSupport.setPrompt("Masukkan Bobot", txtBobot);

        if (isEdit) {
            txtKode.setText(k.getKode());
            txtNama.setText(k.getNama());
            txtKet.setText(k.getKet());
            txtBobot.setText(String.valueOf(k.getBobot()));
        }

        btnModern btnSimpan = new btnModern(isEdit ? "Update" : "Simpan", new Color(38, 166, 154));
        btnSimpan.setForeground(Color.WHITE);
        btnSimpan.setFocusPainted(false);
        btnSimpan.setBorder(BorderFactory.createEmptyBorder(8, 20, 8, 20));
        btnSimpan.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        btnSimpan.addActionListener(e -> {
            String kode = txtKode.getText().trim();
            String nama = txtNama.getText().trim();
            String ket = txtKet.getText().trim();
            String bobotStr = txtBobot.getText().trim();

            if (kode.isEmpty() || nama.isEmpty() || ket.isEmpty() || bobotStr.isEmpty()) {
                JOptionPane.showMessageDialog(dialog, "Semua field harus diisi.");
                return;
            }

            double bobot;
            try {
                bobot = Double.parseDouble(bobotStr);
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(dialog, "Bobot harus berupa angka.");
                return;
            }

            Kriteria data = isEdit ? k : new Kriteria();
            data.setKode(kode);
            data.setNama(nama);
            data.setKet(ket);
            data.setBobot(bobot);

            boolean result = isEdit ? dao.update(data) : dao.insert(data);

            if (result) {
                JOptionPane.showMessageDialog(dialog, isEdit ? "Data berhasil diubah." : "Data berhasil ditambahkan.");
                dialog.dispose();
                refreshTabel();
            } else {
                JOptionPane.showMessageDialog(dialog, "Terjadi kesalahan saat menyimpan data.");
            }
        });

        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBackground(UIComponent.CARD_COLOR);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 10, 8, 10);
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1;

        gbc.gridx = 0; gbc.gridy = 0;
        formPanel.add(new JLabel("KODE:"), gbc);
        gbc.gridx = 1;
        formPanel.add(txtKode, gbc);

        gbc.gridx = 0; gbc.gridy++;
        formPanel.add(new JLabel("KRITERIA:"), gbc);
        gbc.gridx = 1;
        formPanel.add(txtNama, gbc);

        gbc.gridx = 0; gbc.gridy++;
        formPanel.add(new JLabel("KETERANGAN:"), gbc);
        gbc.gridx = 1;
        formPanel.add(txtKet, gbc);

        gbc.gridx = 0; gbc.gridy++;
        formPanel.add(new JLabel("BOBOT:"), gbc);
        gbc.gridx = 1;
        formPanel.add(txtBobot, gbc);

        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        btnPanel.setBackground(UIComponent.CARD_COLOR);
        btnPanel.add(btnSimpan);

        JPanel mainPanel = new JPanel(new BorderLayout(15, 15));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        mainPanel.setBackground(UIComponent.CARD_COLOR);
        mainPanel.add(formPanel, BorderLayout.CENTER);
        mainPanel.add(btnPanel, BorderLayout.SOUTH);

        JScrollPane scrollPane = new JScrollPane(mainPanel);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);

        dialog.setContentPane(scrollPane);
        dialog.setSize(480, 360);
        dialog.setMinimumSize(new Dimension(400, 320));
        dialog.setLocationRelativeTo(this);
        dialog.setResizable(true);
        dialog.setVisible(true);
    }

    private void tableKriteria() {
        String[] cols = {"ID", "KODE", "NAMA KRITERIA", "KETERANGAN", "BOBOT"};
        List<Kriteria> list = dao.getAll();

        tablePanel = new customTable<>(
                cols,
                list,
                k -> new Object[]{k.getId(), k.getKode(), k.getNama(), k.getKet(), String.format("%.4f", k.getBobot())}
        );

        JTable table = tablePanel.getTable();

        // ✅ Terapkan custom styling dari helper
        com.ahp.helper.designTable.buatTable(table);

        // ✅ Tambahkan mouse listener seperti biasa
        table.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                int row = table.rowAtPoint(evt.getPoint());
                if (evt.getClickCount() == 2 && row != -1) {
                    int modelRow = table.convertRowIndexToModel(row);
                    Kriteria k = tablePanel.getRowData(modelRow);
                    btnTambah.setText("Update");
                    inputData(k);
                }
            }
        });

        // ✅ Enable tombol hapus saat baris dipilih
        table.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                btnHapus.setEnabled(table.getSelectedRow() != -1);
            }
        });

        add(tablePanel, BorderLayout.CENTER);
    }


    private void refreshTabel() {
        List<Kriteria> list = dao.getAll();
        tablePanel.refreshData(list, k ->
                new Object[]{k.getId(), k.getKode(), k.getNama(), k.getKet(), String.format("%.4f", k.getBobot())}
        );
    }

    private void filterPencarian(String keyword) {
        String lower = keyword.toLowerCase();
        List<Kriteria> filtered = dao.getAll().stream()
                .filter(k -> k.getKode().toLowerCase().contains(lower)
                        || k.getNama().toLowerCase().contains(lower)
                        || k.getKet().toLowerCase().contains(lower))
                .toList();

        tablePanel.refreshData(filtered, k ->
                new Object[]{k.getId(), k.getKode(), k.getNama(), k.getKet(), k.getBobot()}
        );
    }

    private void hapusDataTerpilih() {
        int viewRow = tablePanel.getTable().getSelectedRow();
        if (viewRow == -1) {
            pesanError.showInfo(this,"Pilih data yang ingin dihapus terlebih dahulu.");
            return;
        }

        int modelRow = tablePanel.getTable().convertRowIndexToModel(viewRow);
        Kriteria k = tablePanel.getRowData(modelRow);

        int confirm = JOptionPane.showConfirmDialog(
                this,
                "Apakah Anda yakin ingin menghapus data kriteria dengan id: " + k.getId() + "?",
                "Konfirmasi Hapus",
                JOptionPane.YES_NO_OPTION
        );

        if (confirm == JOptionPane.YES_OPTION) {
            boolean deleted = dao.delete(k.getId());
            if (deleted) {
                pesanError.showInfo(this,"Data berhasil dihapus.");
                refreshTabel();
                btnHapus.setEnabled(false);
                btnTambah.setText("Tambah");
            } else {
                pesanError.showError(this,"Gagal menghapus data");
            }
        }
    }

    public void printReport() {
        try {
            List<Kriteria> list = dao.getAll();
            if (list.isEmpty()) {
                pesanError.showInfo(this,"Tidak ada data kriteria untuk dicetak.");
                return;
            }

            ReportUtil.generatePdfReport(
                    list,
                    new String[]{"No", "Kode", "NAMA KRITERIA", "KETERANGAN", "BOBOT"},
                    "Data Kriteria Penilaian",
                    "laporan_kriteria",
                    "Jakarta",
                    "GUNAWAN"
            );
            pesanError.showInfo(this,"Laporan berhasil dibuat.");
        } catch (Exception e) {
            pesanError.showCustomError("Gagal mencetak laporan:\n" + e.getMessage(), "Gagal Cetak");
        }
    }
}
