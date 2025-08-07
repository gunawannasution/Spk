package com.ahp.template;

import com.ahp.helper.UIComponent;
import com.formdev.flatlaf.FlatClientProperties;
import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

public class SidebarPanel extends JPanel {



    // Icon sizing
    private final int ICON_SIZE = 20;
    private final int LOGO_SIZE = 40;

    // Components
    private final List<JButton> menuButtons = new ArrayList<>();
    private JButton activeButton = null;
    private final List<MenuListener> menuListeners = new ArrayList<>();

    public interface MenuListener {
        void menuSelected(String menuCommand);
    }

    public SidebarPanel() {
        initComponents();
    }

    private void initComponents() {
        setLayout(new BorderLayout());
        setBackground(UIComponent.COLOR_BG_MENU);
        setPreferredSize(new Dimension(250, Integer.MAX_VALUE));
        setBorder(new MatteBorder(0, 0, 0, 1, new Color(40, 40, 40)));

        // Header panel with logo
        JPanel headerPanel = createHeaderPanel();
        add(headerPanel, BorderLayout.NORTH);

        // Menu items panel
        JPanel menuPanel = new JPanel();
        menuPanel.setLayout(new BoxLayout(menuPanel, BoxLayout.Y_AXIS));
        menuPanel.setBackground(UIComponent.COLOR_BG_MENU);
        menuPanel.setBorder(new EmptyBorder(10, 0, 10, 0));

        // Add menu items
        addMenuItem(menuPanel, "Dashboard", "dashboard", "/icons/dashboard.png");
        addMenuItem(menuPanel, "Karyawan", "karyawan", "/icons/karyawan.png");
        addMenuItem(menuPanel, "Kriteria", "kriteria", "/icons/kriteria.png");
        addMenuItem(menuPanel, "Perhitungan", "matrix", "/icons/matrix.png");
        addMenuItem(menuPanel, "Penilaian", "penilaian", "/icons/penilaian.png");
        addMenuItem(menuPanel, "Laporan", "laporan", "/icons/laporan.png");

        // Add scroll pane
        JScrollPane scrollPane = new JScrollPane(menuPanel);
        scrollPane.setBorder(null);
        scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.getVerticalScrollBar().setUI(new ModernScrollBarUI());
        scrollPane.setOpaque(false);
        scrollPane.getViewport().setOpaque(false);
        add(scrollPane, BorderLayout.CENTER);

        // Footer panel with logout
        JPanel footerPanel = new JPanel();
        footerPanel.setLayout(new BoxLayout(footerPanel, BoxLayout.Y_AXIS));
        footerPanel.setBackground(UIComponent.COLOR_BG_MENU);
        footerPanel.setBorder(new EmptyBorder(0, 0, 20, 0));
        addMenuItem(footerPanel, "Logout", "logout", "/icons/logout.png");
        add(footerPanel, BorderLayout.SOUTH);
    }

    private JPanel createHeaderPanel() {
        JPanel headerPanel = new JPanel();
        headerPanel.setLayout(new BoxLayout(headerPanel, BoxLayout.Y_AXIS));
        headerPanel.setBackground(UIComponent.COLOR_BG_MENU);
        headerPanel.setBorder(new EmptyBorder(20, 20, 25, 20));

        // App logo
        ImageIcon logoIcon = loadAndResizeIcon("/icons/logo.png", LOGO_SIZE, LOGO_SIZE);
        JLabel logoLabel = new JLabel(logoIcon);
        logoLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        logoLabel.setBorder(new EmptyBorder(0, 0, 15, 0));
        headerPanel.add(logoLabel);

        // App name
        JLabel appName = new JLabel("AHP System");
        appName.setFont(UIComponent.FONT_HEADER_MENU);
        appName.setForeground(UIComponent.COLOR_TEXT_MENU);
        appName.setAlignmentX(Component.CENTER_ALIGNMENT);
        headerPanel.add(appName);

        // App version
        JLabel appVersion = new JLabel("v2.0.0");
        appVersion.setFont(UIComponent.FONT_SUBHEADER_MENU);
        appVersion.setForeground(UIComponent.COLOR_SUBTEXT_MENU);
        appVersion.setAlignmentX(Component.CENTER_ALIGNMENT);
        appVersion.setBorder(new EmptyBorder(5, 0, 0, 0));
        headerPanel.add(appVersion);

        return headerPanel;
    }

    private void addMenuItem(JPanel parent, String label, String actionCommand, String iconPath) {
        ImageIcon icon = loadAndResizeIcon(iconPath, ICON_SIZE, ICON_SIZE);
        
        JButton button = new JButton(label, icon);
        button.setActionCommand(actionCommand);
        
        // Styling
        button.putClientProperty(FlatClientProperties.BUTTON_TYPE, FlatClientProperties.BUTTON_TYPE_BORDERLESS);
        button.putClientProperty(FlatClientProperties.STYLE, "" +
            "arc: 0;" +
            "borderWidth: 0;" +
            "focusWidth: 0;" +
            "margin: 8,20,8,20;" +
            "iconTextGap: 12;" +
            "foreground: #f0f0f0;" +
            "background: $Panel.background;" +
            "hoverBackground: #2c2c30;" +
            "selectedBackground: #3c3f41");

        button.setHorizontalAlignment(SwingConstants.LEFT);
        button.setFont(UIComponent.FONT_MENU);
        button.setForeground(UIComponent.COLOR_TEXT_MENU);
        button.setBackground(UIComponent.COLOR_BG_MENU);
        button.setFocusPainted(false);
        button.setBorder(new EmptyBorder(12, 25, 12, 20));
        button.setOpaque(true);
        button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        button.setFocusable(true);

        // Hover effects
        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                if (button != activeButton) {
                    button.setBackground(UIComponent.COLOR_HOVER_MENU);
                }
            }

            @Override
            public void mouseExited(MouseEvent e) {
                if (button != activeButton) {
                    button.setBackground(UIComponent.COLOR_BG_MENU);
                }
            }
        });

        // Click action
        button.addActionListener(e -> {
            System.out.println("Menu clicked: " + actionCommand); // Debug
            setActiveButton(button);
            handleMenuAction(actionCommand);
            notifyMenuListeners(actionCommand);
        });

        menuButtons.add(button);
        parent.add(button);
        parent.add(Box.createVerticalStrut(2));
    }

    private ImageIcon loadAndResizeIcon(String path, int width, int height) {
        try {
            ImageIcon originalIcon = new ImageIcon(getClass().getResource(path));
            if (originalIcon.getIconWidth() > 0) {
                Image resizedImage = originalIcon.getImage().getScaledInstance(
                    width, height, Image.SCALE_SMOOTH);
                return new ImageIcon(resizedImage);
            }
        } catch (Exception e) {
            System.err.println("Error loading icon: " + path);
        }
        // Return blank icon if original not found
        BufferedImage blankImage = new BufferedImage(
            width, height, BufferedImage.TYPE_INT_ARGB);
        return new ImageIcon(blankImage);
    }

    private void handleMenuAction(String actionCommand) {
        if ("logout".equals(actionCommand)) {
            confirmLogout();
        }
    }

    private void confirmLogout() {
        int confirm = JOptionPane.showConfirmDialog(
            this,
            "<html><div style='width:200px;padding:10px;text-align:center;'>" +
            "<p style='font-weight:bold;margin-bottom:10px;'>Konfirmasi Logout</p>" +
            "<p>Apakah Anda yakin ingin keluar?</p>" +
            "</div></html>",
            "Konfirmasi",
            JOptionPane.YES_NO_OPTION,
            JOptionPane.QUESTION_MESSAGE);

        if (confirm == JOptionPane.YES_OPTION) {
            System.exit(0);
        } else if (activeButton != null) {
            // Restore selection if logout cancelled
            activeButton.setBackground(UIComponent.COLOR_SELECTED_MENU);
        }
    }

    public void setActiveButton(JButton button) {
        if (activeButton != null) {
            activeButton.setBackground(UIComponent.COLOR_BG_MENU);
            activeButton.setFont(UIComponent.FONT_MENU);
        }
        
        activeButton = button;
        if (activeButton != null) {
            activeButton.setBackground(UIComponent.COLOR_SELECTED_MENU);
            activeButton.setFont(UIComponent.FONT_MENU_SELECTED_MENU);
        }
    }

    public void addMenuListener(MenuListener listener) {
        menuListeners.add(listener);
    }

    public void removeMenuListener(MenuListener listener) {
        menuListeners.remove(listener);
    }

    private void notifyMenuListeners(String menuCommand) {
        for (MenuListener listener : menuListeners) {
            listener.menuSelected(menuCommand);
        }
    }

    public List<JButton> getMenuButtons() {
        return menuButtons;
    }

    public void selectMenu(String menuCommand) {
        for (JButton button : menuButtons) {
            if (menuCommand.equals(button.getActionCommand())) {
                setActiveButton(button);
                break;
            }
        }
    }

    // Custom scrollbar UI
    private static class ModernScrollBarUI extends javax.swing.plaf.basic.BasicScrollBarUI {
        private final Color THUMB_COLOR = new Color(60, 63, 65);
        private final Color THUMB_HOVER_COLOR = new Color(80, 83, 85);

        @Override
        protected JButton createDecreaseButton(int orientation) {
            return createInvisibleButton();
        }

        @Override
        protected JButton createIncreaseButton(int orientation) {
            return createInvisibleButton();
        }

        private JButton createInvisibleButton() {
            JButton button = new JButton();
            button.setPreferredSize(new Dimension(0, 0));
            button.setMinimumSize(new Dimension(0, 0));
            button.setMaximumSize(new Dimension(0, 0));
            return button;
        }

        @Override
        protected void paintTrack(Graphics g, JComponent c, Rectangle trackBounds) {
            // No track painting
        }

        @Override
        protected void paintThumb(Graphics g, JComponent c, Rectangle thumbBounds) {
            Graphics2D g2 = (Graphics2D)g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(isThumbRollover() ? THUMB_HOVER_COLOR : THUMB_COLOR);
            g2.fillRect(thumbBounds.x, thumbBounds.y, thumbBounds.width, thumbBounds.height);
            g2.dispose();
        }
    }
}