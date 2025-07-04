package com.ahp.content;

import com.ahp.helper.UIComponent;
import com.ahp.helper.libButton;
import com.ahp.helper.PerhitunganAHP;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.util.Locale;

public class MatrixPanel extends JPanel {
    private final int n = 4;
    private final String[] kriteria = {"Kompetensi", "Disiplin", "Tanggung Jawab", "Kerja Sama"};
    private final JTextField[][] inputFields = new JTextField[n][n];
    private final JTextField[][] normFields = new JTextField[n][n];
    private final JTextField[] priorityFields = new JTextField[n];
    private final JTextField ciField = new JTextField();
    private final JTextField crField = new JTextField();
    private final JButton btnHitung;
    private final JButton btnSimpan;
    private double[] hasilPrioritas;

    public MatrixPanel() {
        setLayout(new BorderLayout());
        setBackground(UIComponent.BACKGROUND_COLOR);

        // Panel input matrix perbandingan
        JPanel inputPanel = buatMatrixPanel("MATRIX PERBANDINGAN BERPASANGAN", true);

        // Panel normalisasi
        JPanel normPanel = buatMatrixPanel("MATRIX NORMALISASI", false);

        // Panel prioritas + CI + CR
        JPanel hasilPanel = buatPanelHasil();

        // Tombol hitung & simpan
        btnHitung = libButton.buatBtn(libButton.ButtonType.HITUNG);
        btnSimpan = libButton.buatBtn(libButton.ButtonType.SIMPAN);
        btnSimpan.setEnabled(false);

        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        btnPanel.setBackground(UIComponent.BACKGROUND_COLOR);
        btnPanel.add(btnHitung);
        btnPanel.add(btnSimpan);

        // Gabungkan semua panel utama
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.add(inputPanel);
        mainPanel.add(Box.createVerticalStrut(10));
        mainPanel.add(normPanel);
        mainPanel.add(Box.createVerticalStrut(10));
        mainPanel.add(hasilPanel);
        mainPanel.setBackground(UIComponent.BACKGROUND_COLOR);

        // Tambahkan scroll untuk responsif saat layar kecil
        JScrollPane scrollPane = new JScrollPane(mainPanel);
        scrollPane.setBorder(null);
        add(scrollPane, BorderLayout.CENTER);
        add(btnPanel, BorderLayout.SOUTH);

        isiDiagonal();

        // Pasang listener tombol hitung dan simpan
        btnHitung.addActionListener(e -> hitungAHP());
        btnSimpan.addActionListener(e -> simpanHasil());

        // Pasang listener reciprocal supaya otomatis update nilai kebalikan
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
                        boolean updating = false;
                        private void updateReciprocal() {
                            if (updating) return;
                            updating = true;
                            try {
                                String text = inputFields[row][col].getText().trim();
                                if (text.isEmpty()) {
                                    inputFields[col][row].setText("");
                                    return;
                                }

                                double val = parseInput(text);
                                if (val <= 0) {
                                    inputFields[col][row].setText("");
                                    return;
                                }
                                double reciprocal = 1.0 / val;
                                inputFields[col][row].setText(formatDouble(reciprocal));
                            } catch (NumberFormatException ignored) {
                                inputFields[col][row].setText("");
                            } finally {
                                updating = false;
                            }
                        }

                        @Override
                        public void insertUpdate(javax.swing.event.DocumentEvent e) { updateReciprocal(); }
                        @Override
                        public void removeUpdate(javax.swing.event.DocumentEvent e) { updateReciprocal(); }
                        @Override
                        public void changedUpdate(javax.swing.event.DocumentEvent e) { updateReciprocal(); }
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
        // Placeholder simpan, bisa ditambah sesuai kebutuhan
        JOptionPane.showMessageDialog(this,
                "Hasil berhasil disimpan (placeholder).",
                "Info", JOptionPane.INFORMATION_MESSAGE);
    }
}
