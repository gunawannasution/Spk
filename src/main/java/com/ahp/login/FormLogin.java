package com.ahp.login;

import com.ahp.helper.LogService;
import com.ahp.util.DBConnection;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class FormLogin extends JDialog {

    private JTextField usernameField;
    private JPasswordField passwordField;
    private JButton loginBtn;
    private boolean succeeded = false;
    private String role;

    public boolean isSucceeded() {
        return succeeded;
    }

    public String getRole() {
        return role;
    }

    public FormLogin(JFrame parent) {
        super(parent, true);
        setTitle("Login");
        setSize(700, 400);
        setLocationRelativeTo(parent);
        setUndecorated(true);
        setLayout(new BorderLayout());

        // Split Pane
        JPanel leftPanel = panelKiri();
        JPanel rightPanel = panelKanan();

        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, leftPanel, rightPanel);
        splitPane.setDividerLocation(320);
        splitPane.setEnabled(false); // non-resizable
        add(splitPane, BorderLayout.CENTER);
        //tombol login bisa di enter
        SwingUtilities.getRootPane(loginBtn).setDefaultButton(loginBtn);
    }

    private JPanel panelKiri() {
        JPanel panel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                // Optional: buat background gradasi modern
                Graphics2D g2d = (Graphics2D) g;
                Color color1 = new Color(0, 123, 255);
                Color color2 = new Color(0, 180, 255);
                GradientPaint gp = new GradientPaint(0, 0, color1, 0, getHeight(), color2);
                g2d.setPaint(gp);
                g2d.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(new EmptyBorder(40, 30, 40, 30));

        // Nama perusahaan - font modern dan tegas
        JLabel companyName = new JLabel("PT. HAPESINDO OMEGA PENTA", SwingConstants.CENTER);
        companyName.setFont(new Font("Montserrat", Font.BOLD, 18));
        companyName.setForeground(Color.WHITE);
        companyName.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Slogan kekinian
        JLabel slogan = new JLabel("<html><center>Membangun Masa Depan<br>Dengan Inovasi & Keandalan</center></html>", SwingConstants.CENTER);
        slogan.setFont(new Font("Montserrat", Font.PLAIN, 16));
        slogan.setForeground(new Color(220, 230, 255));
        slogan.setAlignmentX(Component.CENTER_ALIGNMENT);
        slogan.setBorder(new EmptyBorder(15, 0, 40, 0));

        // Gambar kekinian (contoh: ilustrasi konstruksi modern)
        ImageIcon imageIcon = new ImageIcon(getClass().getResource("/icons/logo.png")); // pastikan ada gambarnya
        JLabel imageLabel = new JLabel(imageIcon);
        imageLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Spacer atas dan bawah untuk estetika
        panel.add(Box.createVerticalGlue());
        panel.add(companyName);
        panel.add(slogan);
        panel.add(imageLabel);
        panel.add(Box.createVerticalGlue());

        return panel;
    }


    private JPanel panelKanan() {
        // Panel utama dengan BorderLayout
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);
        panel.setBorder(new EmptyBorder(0, 0, 0, 0)); // hilangkan padding supaya close pas pojok

        // Panel atas khusus tombol close, align kanan
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 0));
        topPanel.setBackground(Color.WHITE);
        topPanel.setPreferredSize(new Dimension(0, 30)); // tinggi sesuai kebutuhan

        JLabel closeLabel = new JLabel("X");
        closeLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
        closeLabel.setForeground(Color.GRAY);
        closeLabel.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        closeLabel.setToolTipText("Close");
        closeLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 10)); // jarak kanan sedikit
        closeLabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                closeLabel.setForeground(Color.RED);
            }
            @Override
            public void mouseExited(MouseEvent e) {
                closeLabel.setForeground(Color.GRAY);
            }
            @Override
            public void mouseClicked(MouseEvent e) {
                dispose();
            }
        });

        topPanel.add(closeLabel);
        panel.add(topPanel, BorderLayout.NORTH);

        // Panel form dengan GridBagLayout (isi form login)
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBackground(Color.WHITE);
        formPanel.setBorder(new EmptyBorder(30, 40, 30, 40));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        JLabel title = new JLabel("Login");
        title.setFont(new Font("SansSerif", Font.BOLD, 22));
        formPanel.add(title, gbc);

        // Username label
        gbc.gridy++;
        gbc.gridwidth = 1;
        gbc.anchor = GridBagConstraints.WEST;
        formPanel.add(new JLabel("Username:"), gbc);

        // Username field
        gbc.gridx = 1;
        usernameField = new JTextField();
        formPanel.add(usernameField, gbc);

        // Password label
        gbc.gridx = 0;
        gbc.gridy++;
        formPanel.add(new JLabel("Password:"), gbc);

        // Password field
        gbc.gridx = 1;
        passwordField = new JPasswordField();
        formPanel.add(passwordField, gbc);

        gbc.gridx = 0;
        gbc.gridy++;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        loginBtn = new JButton("Login");
        loginBtn.setBackground(new Color(0, 123, 255));
        loginBtn.setForeground(Color.WHITE);
        loginBtn.setFocusPainted(false);
        loginBtn.setPreferredSize(new Dimension(200, 35));
        loginBtn.addActionListener(e -> doLogin());   
        formPanel.add(loginBtn, gbc);

        // buat link daftar
//        gbc.gridy++;
//        JLabel create = new JLabel("<html><a href='#'>Create an account?</a></html>");
//        create.setHorizontalAlignment(SwingConstants.CENTER);
//        create.setCursor(new Cursor(Cursor.HAND_CURSOR));
//        formPanel.add(create, gbc);
//        create.addMouseListener(new MouseAdapter() {
//            @Override
//            public void mouseClicked(MouseEvent e) {
//                Window parent = SwingUtilities.getWindowAncestor(panel);
//                new RegisterUser(parent).setVisible(true);
//            }
//        });

        panel.add(formPanel, BorderLayout.CENTER);

        return panel;
    }



    private void doLogin() {
        String username = usernameField.getText();
        String password = new String(passwordField.getPassword());

        String query = "SELECT role FROM users WHERE username = ? AND password = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, username);
            stmt.setString(2, password);

            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                role = rs.getString("role");
                succeeded = true;

                JOptionPane.showMessageDialog(this, "Login sukses sebagai " + role.toUpperCase());

                // Logging lengkap
                LogService.insertLog(username, "Login berhasil", "Sebagai: " + role.toUpperCase());

                dispose();
            } else {
                succeeded = false;
                JOptionPane.showMessageDialog(this, "Username atau password salah.", "Login Gagal", JOptionPane.ERROR_MESSAGE);
            }

        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Gagal koneksi ke database.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
