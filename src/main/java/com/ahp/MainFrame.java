package com.ahp;

import com.ahp.template.ContentPanel;
import com.ahp.template.FooterPanel;
import com.ahp.template.HeaderPanel;
import com.ahp.template.SidebarPanel;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

public class MainFrame extends JFrame{
    private final ContentPanel contentPanel;

    public MainFrame() {
        setTitle("APLIKASI SPK PENILAIAN KARYAWAN BERPRESTASI PT. HAPESINDO OMEGA PENTA");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null); // Tengahkan window
        setLayout(new BorderLayout());

        // Buat komponen
        SidebarPanel sidebar = new SidebarPanel(); 
        contentPanel = new ContentPanel();        
        for (Component comp : sidebar.getComponents()) {
            if (comp instanceof JButton btn) {
                btn.addActionListener(getMenuActionListener());
            }
        }
        add(sidebar, BorderLayout.WEST);
        add(contentPanel, BorderLayout.CENTER);
        add(new HeaderPanel(), BorderLayout.NORTH);
        add(new FooterPanel(), BorderLayout.SOUTH);
    }

    private ActionListener getMenuActionListener() {
        return e -> {
            String command = e.getActionCommand();
            switch (command) {
                case "dashboard":
                    contentPanel.showPanel("dashboard");
                    break;
                case "karyawan":
                    contentPanel.showPanel("karyawan");
                    break;
                case "kriteria":
                    contentPanel.showPanel("kriteria");
                    break;
                    
                case "matrix":
                    contentPanel.showPanel("matrix");
                    break;
                case "penilaian":
                    contentPanel.showPanel("penilaian");
                    break;
                case "laporan":
                    JOptionPane.showMessageDialog(this, "Fitur Laporan belum tersedia.");
                    break;
                case "pengaturan":
                    JOptionPane.showMessageDialog(this, "Pengaturan aplikasi...");
                    break;
            }
        };
    }
}
