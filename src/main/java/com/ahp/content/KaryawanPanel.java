package com.ahp.content;

import com.ahp.content.dao.KaryawanDAO;
import com.ahp.content.dao.KaryawanDAOImpl;
import com.ahp.content.model.Karyawan;
import com.ahp.helper.ReportUtil;
import com.ahp.helper.UIComponent;
import com.ahp.helper.SearchBox;
import com.ahp.helper.btnModern;
import com.ahp.helper.customTable;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.List;
import org.jdesktop.swingx.prompt.PromptSupport;

public class KaryawanPanel extends JPanel {

    private final btnModern btnTambah, btnCetak, btnHapus;
    private customTable<Karyawan> tablePanel;
    private final KaryawanDAO dao = new KaryawanDAOImpl();

    public KaryawanPanel() {
        setLayout(new BorderLayout());
        setBackground(UIComponent.BACKGROUND_COLOR);

        JLabel lblJudul = new JLabel("Data Karyawan");
        lblJudul.setFont(new Font("SansSerif", Font.BOLD, 20));
        lblJudul.setForeground(new Color(33, 33, 33));
        lblJudul.setBorder(new EmptyBorder(10, 15, 5, 15));

        btnTambah = new btnModern("Tambah", UIComponent.ADD_COLOR,new ImageIcon(getClass().getResource("/icons/add.png")));
        btnTambah.addActionListener(e -> inputData(null));

        btnHapus = new btnModern("Hapus", UIComponent.DANGER_COLOR,new ImageIcon(getClass().getResource("/icons/delete.png")));
        btnHapus.setEnabled(false);
        btnHapus.addActionListener(e -> hapusDataTerpilih());

        btnCetak= new btnModern("Cetak", UIComponent.CETAK_COLOR,new ImageIcon(getClass().getResource("/icons/print.png")));
        btnCetak.addActionListener(e -> printReport());

        SearchBox search = new SearchBox("Cari data...", this::filterPencarian);
        Dimension searchDim = new Dimension(250, 36);
        search.setMaximumSize(searchDim);
        search.setPreferredSize(searchDim);

        JPanel panelBtnSearch = new JPanel();
        panelBtnSearch.setLayout(new BoxLayout(panelBtnSearch, BoxLayout.X_AXIS));
        panelBtnSearch.setBackground(UIComponent.BACKGROUND_COLOR);
        panelBtnSearch.setBorder(new EmptyBorder(10, 10, 10, 10));
        panelBtnSearch.add(btnTambah);
        panelBtnSearch.add(Box.createRigidArea(new Dimension(10, 0)));
        panelBtnSearch.add(btnHapus);
        panelBtnSearch.add(Box.createRigidArea(new Dimension(10, 0)));
        panelBtnSearch.add(btnCetak);
        panelBtnSearch.add(Box.createHorizontalGlue());
        panelBtnSearch.add(search);

        JPanel panelHeader = new JPanel(new BorderLayout());
        panelHeader.setBackground(UIComponent.BACKGROUND_COLOR);
        panelHeader.add(lblJudul, BorderLayout.NORTH);
        panelHeader.add(panelBtnSearch, BorderLayout.SOUTH);

        add(panelHeader, BorderLayout.NORTH);
        tableKaryawan();
        add(tablePanel, BorderLayout.CENTER);
    }
    private void inputData(Karyawan k) {
        boolean isEdit = (k != null);
        JDialog dialog = new JDialog(SwingUtilities.getWindowAncestor(this),
                isEdit ? "Edit Karyawan" : "Tambah Karyawan",
                Dialog.ModalityType.APPLICATION_MODAL);

        JTextField txtNik = UIComponent.buatTxt(20);
        JTextField txtNama = UIComponent.buatTxt(20);
        JComboBox<String> cmbJabatan = UIComponent.buatCmb(
                new String[]{"Tenaga Ahli","Petugas K3", "Manager Teknik", "Staf", "Karyawan"},
                null
        );
        JTextField txtAlamat = UIComponent.buatTxt(20);

        PromptSupport.setPrompt("Masukkan NIK", txtNik);
        PromptSupport.setPrompt("Masukkan Nama Lengkap", txtNama);
        PromptSupport.setPrompt("Masukkan Alamat", txtAlamat);

        if (isEdit) {
            txtNik.setText(k.getNik());
            txtNama.setText(k.getNama());
            cmbJabatan.setSelectedItem(k.getJabatan());
            txtAlamat.setText(k.getAlamat());
        }

        btnModern btnSimpan = new btnModern(isEdit ? "Update" : "Simpan", new Color(38, 166, 154));
        btnSimpan.setForeground(Color.WHITE);
        btnSimpan.setFocusPainted(false);
        btnSimpan.setBorder(BorderFactory.createEmptyBorder(8, 20, 8, 20));
        btnSimpan.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        btnSimpan.addActionListener(e -> {
            String nik = txtNik.getText().trim();
            String nama = txtNama.getText().trim();

            if (nik.isEmpty() || nama.isEmpty()) {
                JOptionPane.showMessageDialog(dialog, "NIK dan Nama tidak boleh kosong.");
                return;
            }

            Karyawan data = isEdit ? k : new Karyawan();
            data.setNik(nik);
            data.setNama(nama);
            data.setJabatan((String) cmbJabatan.getSelectedItem());
            data.setAlamat(txtAlamat.getText().trim());

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

        gbc.gridx = 0;
        gbc.gridy = 0;
        formPanel.add(new JLabel("NIK:"), gbc);
        gbc.gridx = 1;
        formPanel.add(txtNik, gbc);

        gbc.gridx = 0;
        gbc.gridy++;
        formPanel.add(new JLabel("Nama:"), gbc);
        gbc.gridx = 1;
        formPanel.add(txtNama, gbc);

        gbc.gridx = 0;
        gbc.gridy++;
        formPanel.add(new JLabel("Jabatan:"), gbc);
        gbc.gridx = 1;
        formPanel.add(cmbJabatan, gbc);

        gbc.gridx = 0;
        gbc.gridy++;
        formPanel.add(new JLabel("Alamat:"), gbc);
        gbc.gridx = 1;
        formPanel.add(txtAlamat, gbc);

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
    private void tableKaryawan() {
        String[] cols = {"ID", "NIK", "Nama", "Jabatan", "Alamat"};
        List<Karyawan> list = dao.getAll();

        tablePanel = new customTable<>(cols, list, k -> new Object[]{
                k.getId(), k.getNik(), k.getNama(), k.getJabatan(), k.getAlamat()
        });

        tablePanel.getTable().addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                JTable t = tablePanel.getTable();
                int row = t.rowAtPoint(evt.getPoint());

                if (row != -1) {
                    if (evt.getClickCount() == 2) {
                        Karyawan k = tablePanel.getRowData(row);
                        btnTambah.setText("Update");
                        btnTambah.setEnabled(false);
                        inputData(k);
                    } else if (evt.getClickCount() == 1) {
                        btnHapus.setEnabled(true);
                    }
                } else {
                    resetBtnTambah();
                    btnHapus.setEnabled(false);
                }
            }
        });
    }
    private void refreshTabel() {
        List<Karyawan> list = dao.getAll();
        tablePanel.refreshData(list, k ->
                new Object[]{k.getId(), k.getNik(), k.getNama(), k.getJabatan(), k.getAlamat()}
        );
    }
    private void resetBtnTambah() {
        btnTambah.setText("Tambah");
        btnTambah.setEnabled(true);
    }
    private void filterPencarian(String keyword) {
        String lower = keyword.toLowerCase();
        List<Karyawan> filtered = dao.getAll().stream()
                .filter(k -> k.getNik().toLowerCase().contains(lower)
                        || k.getNama().toLowerCase().contains(lower))
                .toList();

        tablePanel.refreshData(filtered, k ->
                new Object[]{k.getId(), k.getNik(), k.getNama(), k.getJabatan(), k.getAlamat()}
        );

        // Nonaktifkan tombol hapus kalau hasil pencarian kosong
        if (filtered.isEmpty()) {
            btnHapus.setEnabled(false);
        }
    }
    private void hapusDataTerpilih() {
        int selectedRow = tablePanel.getTable().getSelectedRow();
        if (selectedRow == -1) {
            pesanError.showInfo(this,"Pilih data yang ingin dihapus terlebih dahulu.");
            return;
        }

        Karyawan k = tablePanel.getRowData(selectedRow);

        int confirm = JOptionPane.showConfirmDialog(
                this,
                "Apakah Anda yakin ingin menghapus data karyawan dengan NIK: " + k.getNik() + "?",
                "Konfirmasi Hapus",
                JOptionPane.YES_NO_OPTION
        );

        if (confirm == JOptionPane.YES_OPTION) {
            boolean deleted = dao.delete(k.getId());
            if (deleted) {
                pesanError.showInfo(this,"Data berhasil dihapus.");
                refreshTabel();
                resetBtnTambah();
                //btnHapus.setEnabled(false);
            } else {
                pesanError.showError(this,"Gagal menghapus data");
            }
        }
    }
    public void printReport() {
        try {
            List<Karyawan> list = dao.getAll();
            if (list.isEmpty()) {
                pesanError.showInfo(this,"Tidak ada data karyawan untuk dicetak.");
                return;
            }

            ReportUtil.generatePdfReport(
                    list,
                    new String[]{"No", "NIK", "Nama", "Jabatan", "Alamat"},
                    "Laporan Data Karyawan",
                    "laporan_karyawan",
                    "Jakarta",
                    "Ir. Jannus Simanjuntak"
            );
            pesanError.showInfo(this,"Laporan berhasil dibuat.");
        } catch (Exception e) {
            pesanError.showError(this,"Gagal mencetak laporan:\n" + e.getMessage());
        }
    }
    
}
