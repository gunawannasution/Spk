package com.ahp.content;

import com.ahp.content.dao.KriteriaDAO;
import com.ahp.content.dao.KriteriaDAOImpl;
import com.ahp.content.dao.MatrixDAO;
import com.ahp.content.dao.MatrixDAOImpl;
import com.ahp.content.model.Kriteria;
import com.ahp.content.model.Matrix;
import com.ahp.helper.UIComponent;
import com.ahp.helper.PerhitunganAHP;
import com.ahp.helper.ReportUtil;
import com.ahp.helper.btnModern;
import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.util.List;
import java.awt.*;
import java.util.Locale;
import javax.swing.border.EmptyBorder;

public class MatrixPanel extends JPanel {
    private final int n = 4;
    private final String[] kriteria = {"Kompetensi", "Disiplin", "Tanggung Jawab", "Kerja Sama"};
    private final JTextField[][] inputFields = new JTextField[n][n];
    private final JTextField[][] normFields = new JTextField[n][n];
    private final JTextField[] priorityFields = new JTextField[n];
    private final JTextField ciField = new JTextField();
    private final JTextField crField = new JTextField();
    private final btnModern btnSimpan,btnHitung,btnClear, btnCetak;
    private final boolean[][] isUpdating = new boolean[n][n];
    private double[] hasilPrioritas;

    private final MatrixDAO dao = new MatrixDAOImpl(); 

    public MatrixPanel() {
        setLayout(new BorderLayout());
        setBackground(UIComponent.BACKGROUND_COLOR);

        JLabel lblJudul = new JLabel("Matrix Perhitungan - Analisis AHP ");
        lblJudul.setFont(new Font("SansSerif", Font.BOLD, 20));
        lblJudul.setForeground(new Color(33, 33, 33));
        lblJudul.setBorder(new EmptyBorder(15, 15, 10, 15));
        add(lblJudul, BorderLayout.NORTH);  
        
        JPanel inputPanel = buatMatrixPanel("MATRIX PERBANDINGAN BERPASANGAN", true);
        JPanel normPanel = buatMatrixPanel("MATRIX NORMALISASI", false);
        JPanel hasilPanel = buatPanelHasil();

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBackground(UIComponent.BACKGROUND_COLOR);
        mainPanel.setBorder(new EmptyBorder(10, 15, 10, 15));  

        mainPanel.add(inputPanel);
        mainPanel.add(Box.createVerticalStrut(10));
        mainPanel.add(normPanel);
        mainPanel.add(Box.createVerticalStrut(10));
        mainPanel.add(hasilPanel);

        JScrollPane scrollPane = new JScrollPane(mainPanel);
        scrollPane.setBorder(null);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16); 
        add(scrollPane, BorderLayout.CENTER);

        btnSimpan = new btnModern("Simpan",UIComponent.ADD_COLOR,new ImageIcon(getClass().getResource("/icons/simpan.png")));
        btnSimpan.setEnabled(false);
        btnHitung = new btnModern("Hitung",UIComponent.PRIMARY_COLOR,new ImageIcon(getClass().getResource("/icons/hitung.png")));
        btnClear = new btnModern("Clear", UIComponent.DANGER_COLOR,new ImageIcon(getClass().getResource("/icons/clear.png")));
        btnCetak=new btnModern("Cetak",UIComponent.CETAK_COLOR,new ImageIcon(getClass().getResource("/icons/print.png")));   
        
        btnHitung.addActionListener(e -> hitungAHP());
        btnSimpan.addActionListener(e -> simpanHasil());
        btnClear.addActionListener(e -> kosongkanForm());
        btnCetak.addActionListener(e -> printReport());
        
        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        btnPanel.setBackground(UIComponent.BACKGROUND_COLOR);
        btnPanel.setBorder(new EmptyBorder(10, 15, 10, 15));
        btnPanel.add(btnHitung);
        btnPanel.add(btnSimpan);
        btnPanel.add(btnClear);
        btnPanel.add(btnCetak);
        add(btnPanel, BorderLayout.SOUTH);


        
        tampilkanNormalisasiDariDB();
        isiDiagonal();
        pasangListenerReciprocal();
    }

    private JPanel buatMatrixPanel(String title, boolean editable) {
        JPanel panel = new JPanel(new GridLayout(n + 1, n + 1, 5, 5));
        panel.setBackground(UIComponent.BACKGROUND_COLOR);
        panel.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(UIComponent.BORDER_COLOR),
                title, TitledBorder.LEFT, TitledBorder.TOP,
                new Font("Segoe UI", Font.BOLD, 13), UIComponent.TITLE_COLOR));

        // Header kolom
        panel.add(new JLabel("")); // kosong pojok kiri atas
        for (int j = 0; j < n; j++) {
            JLabel lbl = new JLabel(kriteria[j], SwingConstants.CENTER);
            lbl.setForeground(UIComponent.TEXT_COLOR);
            lbl.setFont(new Font("Segoe UI", Font.BOLD, 12));
            panel.add(lbl);
        }

        for (int i = 0; i < n; i++) {
            // Header baris
            JLabel lblRow = new JLabel(kriteria[i], SwingConstants.CENTER);
            lblRow.setForeground(UIComponent.TEXT_COLOR);
            lblRow.setFont(new Font("Segoe UI", Font.BOLD, 12));
            panel.add(lblRow);

            for (int j = 0; j < n; j++) {
                JTextField tf = new JTextField();
                tf.setHorizontalAlignment(JTextField.CENTER);
                tf.setFont(new Font("Segoe UI", Font.PLAIN, 12));
                tf.setEditable(editable);

                if (editable) {
                    inputFields[i][j] = tf;
                } else {
                    normFields[i][j] = tf;
                    tf.setBackground(UIComponent.CARD_COLOR);
                    tf.setEditable(false);
                }
                panel.add(tf);
            }
        }
        return panel;
    }
    private JPanel buatPanelHasil() {
        JPanel panel = new JPanel(new GridLayout(n + 3, 2, 10, 8));
        panel.setBackground(UIComponent.BACKGROUND_COLOR);
        panel.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(UIComponent.BORDER_COLOR),
                "HASIL PERHITUNGAN AHP", TitledBorder.LEFT, TitledBorder.TOP,
                new Font("Segoe UI", Font.BOLD, 13), UIComponent.TITLE_COLOR));

        // Tambah label dan field bobot prioritas
        for (int i = 0; i < n; i++) {
            JLabel label = new JLabel(kriteria[i] + ": ");
            label.setForeground(UIComponent.TEXT_COLOR);
            label.setFont(UIComponent.FONT_REGULAR);
            panel.add(label);

            JTextField tf = new JTextField();
            tf.setEditable(false);
            tf.setBackground(UIComponent.CARD_COLOR);
            tf.setHorizontalAlignment(JTextField.CENTER);
            tf.setFont(UIComponent.FONT_BOLD);
            tf.setForeground(UIComponent.TEXT_COLOR);
            priorityFields[i] = tf;
            panel.add(tf);
        }

        // Tambah label & field CI
        JLabel ciLabel = new JLabel("Consistency Index (CI): ");
        ciLabel.setForeground(UIComponent.TEXT_COLOR);
        ciLabel.setFont(UIComponent.FONT_REGULAR);
        panel.add(ciLabel);

        ciField.setEditable(false);
        ciField.setBackground(UIComponent.CARD_COLOR);
        ciField.setHorizontalAlignment(JTextField.CENTER);
        ciField.setFont(UIComponent.FONT_BOLD);
        ciField.setForeground(UIComponent.TEXT_COLOR);
        panel.add(ciField);

        // Tambah label & field CR
        JLabel crLabel = new JLabel("Consistency Ratio (CR): ");
        crLabel.setForeground(UIComponent.TEXT_COLOR);
        crLabel.setFont(UIComponent.FONT_REGULAR);
        panel.add(crLabel);

        crField.setEditable(false);
        crField.setBackground(UIComponent.CARD_COLOR);
        crField.setHorizontalAlignment(JTextField.CENTER);
        crField.setFont(UIComponent.FONT_BOLD);
        crField.setForeground(UIComponent.TEXT_COLOR);
        panel.add(crField);

        return panel;
    }
    private void isiDiagonal() {
        for (int i = 0; i < n; i++) {
            inputFields[i][i].setText("1");
            inputFields[i][i].setEditable(false);
            inputFields[i][i].setBackground(new Color(240, 240, 240));
            inputFields[i][i].setFont(new Font("Segoe UI", Font.BOLD, 12));
        }
    }
    private void pasangListenerReciprocal() {
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                if (i != j) {
                    final int row = i;
                    final int col = j;

                    inputFields[row][col].getDocument().addDocumentListener(new javax.swing.event.DocumentListener() {

                        private void updateReciprocal() {
                            if (isUpdating[row][col]) return;

                            try {
                                String text = inputFields[row][col].getText().trim();
                                if (text.isEmpty()) {
                                    isUpdating[col][row] = true;
                                    inputFields[col][row].setText("");
                                    isUpdating[col][row] = false;
                                    return;
                                }

                                double val = parseInput(text);
                                if (val <= 0) {
                                    isUpdating[col][row] = true;
                                    inputFields[col][row].setText("");
                                    isUpdating[col][row] = false;
                                    return;
                                }

                                double reciprocal = 1.0 / val;
                                String reciprocalText = formatDouble(reciprocal);

                                if (!inputFields[col][row].getText().equals(reciprocalText)) {
                                    isUpdating[col][row] = true;
                                    inputFields[col][row].setText(reciprocalText);
                                    isUpdating[col][row] = false;
                                }

                            } catch (NumberFormatException ignored) {
                                isUpdating[col][row] = true;
                                inputFields[col][row].setText("");
                                isUpdating[col][row] = false;
                            }
                        }

                        @Override
                        public void insertUpdate(javax.swing.event.DocumentEvent e) {
                            updateReciprocal();
                        }

                        @Override
                        public void removeUpdate(javax.swing.event.DocumentEvent e) {
                            updateReciprocal();
                        }

                        @Override
                        public void changedUpdate(javax.swing.event.DocumentEvent e) {
                            updateReciprocal();
                        }
                    });
                }
            }
        }
    }

    private double parseInput(String text) throws NumberFormatException {
        // Terima format 1/3 atau angka biasa
        text = text.replace(",", ".");
        if (text.contains("/")) {
            String[] parts = text.split("/");
            if (parts.length == 2) {
                double pembilang = Double.parseDouble(parts[0].trim());
                double penyebut = Double.parseDouble(parts[1].trim());
                if (penyebut == 0) throw new NumberFormatException("Pembagi 0");
                return pembilang / penyebut;
            } else {
                throw new NumberFormatException("Format pecahan tidak valid");
            }
        } else {
            return Double.parseDouble(text);
        }
    }
    private String formatDouble(double val) {
        return String.format(Locale.US, "%.4f", val);
    }
    private void resetWarnaInput() {
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                if (i == j) {
                    inputFields[i][j].setBackground(new Color(240, 240, 240));
                } else {
                    inputFields[i][j].setBackground(Color.WHITE);
                }
            }
        }
    }
    private void hitungAHP() {
        resetWarnaInput();

        try {
            double[][] matrix = new double[n][n];

            // Validasi dan parsing input
            for (int i = 0; i < n; i++) {
                for (int j = 0; j < n; j++) {
                    String text = inputFields[i][j].getText().trim();
                    if (text.isEmpty()) {
                        inputFields[i][j].setBackground(new Color(255, 204, 204));
                        throw new Exception("Semua nilai harus diisi");
                    }
                    double val = parseInput(text);
                    if (val <= 0) {
                        inputFields[i][j].setBackground(new Color(255, 204, 204));
                        throw new Exception("Nilai harus > 0");
                    }
                    matrix[i][j] = val;
                }
            }

            PerhitunganAHP ahp = new PerhitunganAHP(matrix);
            hasilPrioritas = ahp.getPrioritas();
            double[][] normMatrix = ahp.getMatrixNormalisasi();
            double ci = ahp.getCI();
            double cr = ahp.getCR();

            // Tampilkan matrix normalisasi
            for (int i = 0; i < n; i++) {
                for (int j = 0; j < n; j++) {
                    normFields[i][j].setText(formatDouble(normMatrix[i][j]));
                }
            }

            // Tampilkan prioritas
            for (int i = 0; i < n; i++) {
                priorityFields[i].setText(formatDouble(hasilPrioritas[i]));
            }

            // Tampilkan CI dan CR
            ciField.setText(formatDouble(ci));
            crField.setText(formatDouble(cr));
            
            btnSimpan.setEnabled(true);

            if (cr > 0.1) {
                JOptionPane.showMessageDialog(this,
                        "Perbandingan tidak konsisten!\nCR = " + formatDouble(cr),
                        "Warning", JOptionPane.WARNING_MESSAGE);
            }

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this,
                    "Input matrix tidak valid:\n" + ex.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    private void simpanHasil() {
        if (hasilPrioritas == null || hasilPrioritas.length != kriteria.length) {
            JOptionPane.showMessageDialog(this,
                    "Belum ada hasil AHP yang dapat disimpan.",
                    "Peringatan", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            KriteriaDAO kriteriaDAO = new KriteriaDAOImpl(); // ganti dengan cara instansiasi sesuai project kamu
            List<Kriteria> existingKriteria = kriteriaDAO.getAll(); // ambil semua kriteria dari DB

            if (existingKriteria.size() != hasilPrioritas.length) {
                JOptionPane.showMessageDialog(this,
                        "Jumlah kriteria di database tidak sesuai hasil AHP.",
                        "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Update bobot di objek kriteria
            for (int i = 0; i < existingKriteria.size(); i++) {
                existingKriteria.get(i).setBobot(hasilPrioritas[i]);
            }

            boolean berhasil = kriteriaDAO.updateAllBobot(existingKriteria);

            if (berhasil) {
                JOptionPane.showMessageDialog(this,
                        "Bobot berhasil diperbarui di database.",
                        "Sukses", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this,
                        "Gagal menyimpan bobot ke database.",
                        "Gagal", JOptionPane.ERROR_MESSAGE);
            }

        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this,
                    "Terjadi kesalahan saat menyimpan:\n" + e.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void tampilkanNormalisasiDariDB() {
        try {
            // Ganti dengan DAO sesuai struktur project kamu
            MatrixDAO normalisasiDAO = new MatrixDAOImpl();

            List<String> kriteriaList = java.util.Arrays.asList(kriteria); // array to list
            double[][] normMatrix = normalisasiDAO.ambilMatrixNormalisasi(kriteriaList);

            for (int i = 0; i < n; i++) {
                for (int j = 0; j < n; j++) {
                    normFields[i][j].setText(formatDouble(normMatrix[i][j]));
                }
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                    "Gagal menampilkan matrix normalisasi dari database:\n" + e.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }
    
    private void kosongkanForm() {
        // Kosongkan input fields
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                if (i != j) {
                    inputFields[i][j].setText("");
                    inputFields[i][j].setBackground(Color.WHITE);
                }
                normFields[i][j].setText("");
            }
        }

        // Reset diagonal tetap 1
        isiDiagonal();

        // Kosongkan hasil prioritas, CI, CR
        for (int i = 0; i < n; i++) {
            priorityFields[i].setText("");
        }
        ciField.setText("");
        crField.setText("");

        // Nonaktifkan tombol simpan
        btnSimpan.setEnabled(false);

        // Hapus hasil prioritas yang tersimpan
        hasilPrioritas = null;
    }
    public void muatDataNormalisasiDariDatabase() {
        try {
            MatrixDAO dao = new MatrixDAOImpl();
            List<String> kriteriaList = java.util.Arrays.asList(kriteria);
            double[][] dataNormalisasi = dao.ambilMatrixNormalisasi(kriteriaList);

            for (int i = 0; i < dataNormalisasi.length; i++) {
                for (int j = 0; j < dataNormalisasi[i].length; j++) {
                    normFields[i][j].setText(String.format(Locale.US, "%.4f", dataNormalisasi[i][j]));
                }
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                "Gagal memuat data normalisasi dari database:\n" + e.getMessage(),
                "Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }
    public void printReport() {
        try {
            List<Matrix> list = dao.getAll();
            if (list.isEmpty()) {
                pesanError.showInfo(this,"Tidak ada data karyawan untuk dicetak.");
                return;
            }

            ReportUtil.generatePdfReport(
                list,
                new String[]{"No", "Kriteria Baris", "Kriteria Kolom", "Nilai"},
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
