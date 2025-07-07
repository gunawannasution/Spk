package com.ahp.content;

import com.ahp.content.dao.KaryawanDAO;
import com.ahp.content.dao.KaryawanDAOImpl;
import com.ahp.content.model.Karyawan;
import com.ahp.helper.BuatTable;
import com.ahp.helper.ReportUtil;
import com.ahp.helper.UIComponent;
import com.ahp.helper.libButton;
import com.ahp.helper.SearchBox;
import com.ahp.helper.btnModern;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.util.List;
import javax.swing.border.EmptyBorder;
import org.jdesktop.swingx.prompt.PromptSupport;

public class KaryawanPanel extends JPanel {

    private final JButton btnTambah, btnCetak;
    private BuatTable<Karyawan> tablePanel;
    private final KaryawanDAO dao = new KaryawanDAOImpl(); 

    public KaryawanPanel() {
        setLayout(new BorderLayout());
         
        btnTambah = new btnModern("Tambah");
        btnTambah.addActionListener(e -> inputData(null));
        btnTambah.setBackground(Color.decode("#4CAF50")); 
        btnCetak=new btnModern("Print");
        btnCetak.addActionListener(e->printReport());
      
        
        SearchBox search = new SearchBox("Cari data...", keyword -> filterPencarian(keyword));
        search.setMaximumSize(new Dimension(250, 36));  
        search.setPreferredSize(new Dimension(250, 36));

        JPanel atasPanel = new JPanel();
        atasPanel.setLayout(new BoxLayout(atasPanel, BoxLayout.X_AXIS));
        atasPanel.setBackground(UIComponent.BACKGROUND_COLOR);
         
        atasPanel.setBorder(new EmptyBorder(10, 10, 10, 10)); 
        atasPanel.add(btnTambah);
        atasPanel.add(btnCetak);
        atasPanel.add(Box.createRigidArea(new Dimension(20, 0))); 
        atasPanel.add(Box.createHorizontalGlue());               
        atasPanel.add(search);

        add(atasPanel, BorderLayout.NORTH);

        tableKaryawan();
        tidakKlikTable();

    }
    
    private void inputData(Karyawan k) {
        boolean isEdit = (k != null);

        JDialog dialog = new JDialog(SwingUtilities.getWindowAncestor(this),
                isEdit ? "Edit Karyawan" : "Tambah Karyawan",
                Dialog.ModalityType.APPLICATION_MODAL);

        JTextField txtNik = UIComponent.buatTxt(20);
        JTextField txtNama = UIComponent.buatTxt(20);
        JComboBox<String> cmbJabatan = UIComponent.buatCmb(
            new String[]{"Manager Keuangan", "Manager Teknik", "Staf", "Karyawan"},
            new ImageIcon("")
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

        JButton btnSimpan = libButton.buatBtn(isEdit ? libButton.ButtonType.UPDATE : libButton.ButtonType.SIMPAN);
        btnSimpan.setBackground(new Color(38, 166, 154));  // warna hijau teal
        btnSimpan.setForeground(Color.WHITE);
        btnSimpan.setFocusPainted(false);
        btnSimpan.setBorder(BorderFactory.createEmptyBorder(8, 20, 8, 20));
        btnSimpan.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btnSimpan.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btnSimpan.setBackground(new Color(26, 128, 120));
            }
            @Override
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btnSimpan.setBackground(new Color(38, 166, 154));
            }
        });

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

            gbc.gridx = 0; gbc.gridy = 0;
            formPanel.add(new JLabel("NIK:"), gbc);
            gbc.gridx = 1;
            formPanel.add(txtNik, gbc);

            gbc.gridx = 0; gbc.gridy++;
            formPanel.add(new JLabel("Nama:"), gbc);
            gbc.gridx = 1;
            formPanel.add(txtNama, gbc);

            gbc.gridx = 0; gbc.gridy++;
            formPanel.add(new JLabel("Jabatan:"), gbc);
            gbc.gridx = 1;
            formPanel.add(cmbJabatan, gbc);

            gbc.gridx = 0; gbc.gridy++;
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

        tablePanel = new BuatTable<>(
                cols,
                list,
                k -> new Object[]{k.getId(), k.getNik(), k.getNama(), k.getJabatan(), k.getAlamat()}
        );
        
        tablePanel.getTable().addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                JTable t = tablePanel.getTable();
                int row = t.rowAtPoint(evt.getPoint());
                if (evt.getClickCount() == 2 && row != -1) {
                    // Double klik baris tabel yang valid
                    Karyawan k = tablePanel.getRowData(row);
                    btnTambah.setText("Update Karyawan");
                    inputData(k);
                } else if (evt.getClickCount() == 1 && row == -1) {
                    // Klik satu kali di area kosong tabel
                    btnTambah.setText("Tambah Karyawan");
                }
            }
        });

        add(tablePanel, BorderLayout.CENTER);
    }

    private void refreshTabel() {
        remove(tablePanel); 
        tableKaryawan();    
        revalidate();
        repaint();
    }
    
    private ActionListener tidakKlikTable(){
        // Tambahkan mouse listener di panel utama (atau container yang lebih besar) untuk reset tombol ketika klik di luar tabel
        this.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnTambah.setText("Tambah Karyawan");
            }
        });
        return null;
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
    }
    
    private void printReport() {
        try {
            List<Karyawan> list = dao.getAll();
            if (list.isEmpty()) {
                showInfo("Tidak ada data karyawan untuk dicetak.");
                return;
            }

            ReportUtil.generatePdfReport(
                list,
                new String[]{"No","NIK", "Nama", "Jabatan", "Alamat"},
                "Laporan Data Karyawan",
                "laporan_karyawan",
                "Jakarta",
                "GUNAWAN"
            );
            showInfo("Laporan berhasil dibuat.");
        } catch (Exception e) {
            showError("Gagal mencetak laporan:\n" + e.getMessage(), "Gagal Cetak");
        }
    }
    private void showInfo(String msg) {
    JOptionPane.showMessageDialog(this, msg, "Informasi", JOptionPane.INFORMATION_MESSAGE);
}

    private void showError(String msg, String title) {
        JOptionPane.showMessageDialog(this, msg, title, JOptionPane.ERROR_MESSAGE);
    }
}
