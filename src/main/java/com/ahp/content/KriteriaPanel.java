package com.ahp.content;

import com.ahp.content.dao.KriteriaDAO;
import com.ahp.content.dao.KriteriaDAOImpl;
import com.ahp.content.model.Karyawan;
import com.ahp.content.model.Kriteria;
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

public class KriteriaPanel extends JPanel {

    private final JButton btnTambah, btnCetak;
    private BuatTable<Kriteria> tablePanel;
    private final KriteriaDAO dao = new KriteriaDAOImpl(); 

    public KriteriaPanel() {
        setLayout(new BorderLayout());         
        
        JLabel lblJudul = new JLabel("Data Kriteria Penilaian");
        lblJudul.setFont(new Font("SansSerif", Font.BOLD, 20));
        lblJudul.setForeground(new Color(33, 33, 33)); // Warna gelap
        lblJudul.setBorder(new EmptyBorder(10, 15, 5, 15));
        
        btnTambah = new btnModern("Tambah",new Color(76, 175, 80));
        ImageIcon iconTambah = new ImageIcon(getClass().getResource("/icons/tambah.png"));
        Image scaledImage = iconTambah.getImage().getScaledInstance(16, 16, Image.SCALE_SMOOTH);
        btnTambah.setIcon(new ImageIcon(scaledImage));
        btnTambah.addActionListener(e -> inputData(null));
        btnTambah.setHorizontalAlignment(SwingConstants.LEFT);        
        btnTambah.setHorizontalTextPosition(SwingConstants.RIGHT);     
        btnTambah.setIconTextGap(6);   
        btnTambah.addActionListener(e -> inputData(null)); 
        
        btnCetak=new btnModern("Print",new Color(96, 125, 139));
        ImageIcon iconPrint = new ImageIcon(getClass().getResource("/icons/print.png"));
        Image ukuranIconPrint = iconPrint.getImage().getScaledInstance(16, 16, Image.SCALE_SMOOTH);
        btnCetak.setIcon(new ImageIcon(ukuranIconPrint));
        btnCetak.addActionListener(e -> printReport());
        btnCetak.setIcon(new ImageIcon(ukuranIconPrint));
        btnCetak.setHorizontalAlignment(SwingConstants.LEFT);
        btnCetak.setHorizontalTextPosition(SwingConstants.RIGHT);
        btnCetak.setIconTextGap(6);
        btnCetak.addActionListener(e->printReport());
        
        
        SearchBox search = new SearchBox("Cari data...", keyword -> filterPencarian(keyword));
        search.setMaximumSize(new Dimension(250, 36));  
        search.setPreferredSize(new Dimension(250, 36));

        JPanel atasPanel = new JPanel();
        atasPanel.setLayout(new BoxLayout(atasPanel, BoxLayout.X_AXIS));
        atasPanel.setBackground(UIComponent.BACKGROUND_COLOR);
         
        atasPanel.setBorder(new EmptyBorder(10, 10, 10, 10)); 
        atasPanel.add(btnTambah);
        atasPanel.add(Box.createRigidArea(new Dimension(10, 0)));
        atasPanel.add(btnCetak);
        atasPanel.add(Box.createRigidArea(new Dimension(20, 0))); 
        atasPanel.add(Box.createHorizontalGlue());               
        atasPanel.add(search);

        JPanel panelAtasGabungan = new JPanel();
        panelAtasGabungan.setLayout(new BorderLayout());
        panelAtasGabungan.setBackground(UIComponent.BACKGROUND_COLOR);
        panelAtasGabungan.add(lblJudul, BorderLayout.NORTH);
        panelAtasGabungan.add(atasPanel, BorderLayout.CENTER);

        add(panelAtasGabungan, BorderLayout.NORTH);

        tableKriteria();
        tidakKlikTable();

    }



    private void inputData(Kriteria k) {
        boolean isEdit = (k != null);

        JDialog dialog = new JDialog(SwingUtilities.getWindowAncestor(this),
                isEdit ? "Edit Kriteria" : "Tambah Kriteria",
                Dialog.ModalityType.APPLICATION_MODAL);

        // Create fields
        JTextField txtKode = UIComponent.buatTxt(20);
        JTextField txtNama = UIComponent.buatTxt(20);
        JTextField txtKet = UIComponent.buatTxt(20);
        JTextField txtBobot = UIComponent.buatTxt(20);

        // Placeholder / hint text
        PromptSupport.setPrompt("Masukkan Kode Kriteria", txtKode);
        PromptSupport.setPrompt("Masukkan Nama Kriteria", txtNama);
        PromptSupport.setPrompt("Masukkan Keterangan", txtKet);
        PromptSupport.setPrompt("Masukkan Bobot", txtBobot);

        // Isi form jika edit
        if (isEdit) {
            txtKode.setText(k.getKode());
            txtNama.setText(k.getNama());
            txtNama.setText(k.getKet());
            txtBobot.setText(String.valueOf(k.getBobot()));
        }

        btnModern btnSimpan = new btnModern(isEdit ? "Update" : "Simpan", new Color(38, 166, 154)); // hijau teal

        btnSimpan.setForeground(Color.WHITE);
        btnSimpan.setFocusPainted(false);
        btnSimpan.setBorder(BorderFactory.createEmptyBorder(8, 20, 8, 20));
        btnSimpan.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        btnSimpan.addActionListener(e -> {
            String kode = txtKode.getText().trim();
            String nama = txtNama.getText().trim();
            String ket = txtKet.getText().trim();

            if (kode.isEmpty() || nama.isEmpty()||ket.isEmpty()) {
                JOptionPane.showMessageDialog(dialog, "NIK dan Nama tidak boleh kosong.");
                return;
            }

            Kriteria data = isEdit ? k : new Kriteria();
            data.setKode(kode);
            data.setNama(nama);
            data.setKet(ket);
            // Parsing String ke Double dari txtBobot
            Double bobot = Double.valueOf(txtBobot.getText().trim());
            data.setBobot(bobot);          
            data.setKet(txtKet.getText().trim());
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

        tablePanel = new BuatTable<>(
                cols,
                list,
                k -> new Object[]{k.getId(), k.getKode(), k.getNama(), k.getKet(), k.getBobot()}
        );
        tablePanel.getTable().addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                JTable t = tablePanel.getTable();
                int row = t.rowAtPoint(evt.getPoint());
                if (evt.getClickCount() == 2 && row != -1) {
                    // Double klik baris tabel yang valid
                    Kriteria k = tablePanel.getRowData(row);
                    btnTambah.setText("Update Kriteria");
                    inputData(k);
                } else if (evt.getClickCount() == 1 && row == -1) {
                    // Klik satu kali di area kosong tabel
                    btnTambah.setText("Tambah Kriteria");
                }
            }
        });



        add(tablePanel, BorderLayout.CENTER);
    }

    private void refreshTabel() {
        remove(tablePanel); // hapus panel lama
        tableKriteria();    // panggil ulang
        revalidate();
        repaint();
    }
    
    private ActionListener tidakKlikTable(){
        // Tambahkan mouse listener di panel utama (atau container yang lebih besar) untuk reset tombol ketika klik di luar tabel
        this.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnTambah.setText("Tambah Kriteria");
            }
        });
        return null;
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
        
    private void printReport() {
        try {
            List<Kriteria> list = dao.getAll();
            if (list.isEmpty()) {
                showInfo("Tidak ada data karyawan untuk dicetak.");
                return;
            }

            ReportUtil.generatePdfReport(
                list,
                new String[]{"No","KODE", "NAMA KRITERIA", "KETERANGAN", "BOBOT"},
                "Data Kriteria Penilaian",
                "laporan_kriteria",
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
