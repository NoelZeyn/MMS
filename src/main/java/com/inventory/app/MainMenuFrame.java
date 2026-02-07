package main.java.com.inventory.app;

import main.java.com.inventory.model.User;
import main.java.com.inventory.pages.BarangMenuFrame;
import main.java.com.inventory.pages.ReportMenuFrame;
import main.java.com.inventory.pages.UIConfig;
import javax.swing.*;
import javax.swing.border.MatteBorder;
import java.awt.*;

public class MainMenuFrame extends JFrame {
    private final User user;
    private JPanel contentArea;
    private CardLayout cardLayout;

    public MainMenuFrame(User user) {
        this.user = user;
        setupFrame();
        initContent();
    }

    private void setupFrame() {
        setTitle("MMS ENTERPRISE - Management System");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(1100, 700);
        setLocationRelativeTo(null);
    }

private void initContent() {
    JPanel mainPanel = new JPanel(new BorderLayout());
    setContentPane(mainPanel);

    mainPanel.add(createSidebar(), BorderLayout.WEST);

    cardLayout = new CardLayout();
    contentArea = new JPanel(cardLayout);
    contentArea.setBackground(UIConfig.BG_LIGHT);

    // --- REGISTER PAGES ---
    contentArea.add(createPlaceholderPage("DASHBOARD OVERVIEW"), "DASHBOARD");
    
    // MASUKKAN BARANG MENU KE SINI SEBAGAI PANEL
    contentArea.add(new BarangMenuFrame(user), "INVENTORY");
    
    contentArea.add(new ReportMenuFrame(user), "REPORTS");

    mainPanel.add(contentArea, BorderLayout.CENTER);
}
    private JPanel createSidebar() {
        JPanel sidebar = new JPanel(new BorderLayout());
        sidebar.setBackground(new Color(30, 41, 59));
        sidebar.setPreferredSize(new Dimension(260, 0));

        JPanel brandPanel = new JPanel(new BorderLayout());
        brandPanel.setOpaque(false);
        brandPanel.setBorder(new MatteBorder(0, 0, 1, 0, new Color(51, 65, 85)));
        brandPanel.setPreferredSize(new Dimension(0, 80));
        
        JLabel brandLabel = new JLabel("MMS ENTERPRISE", SwingConstants.CENTER);
        brandLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
        brandLabel.setForeground(Color.WHITE);
        brandPanel.add(brandLabel, BorderLayout.CENTER);

        JPanel menuPanel = new JPanel();
        menuPanel.setOpaque(false);
        menuPanel.setLayout(new BoxLayout(menuPanel, BoxLayout.Y_AXIS));
        menuPanel.setBorder(BorderFactory.createEmptyBorder(20, 15, 20, 15));

        menuPanel.add(createNavButton("DASHBOARD", "DASHBOARD"));
        menuPanel.add(Box.createVerticalStrut(10));
        menuPanel.add(createInventoryButton());
        menuPanel.add(Box.createVerticalStrut(10));
        menuPanel.add(createReportButton());

        JPanel footerPanel = new JPanel(new GridBagLayout());
        footerPanel.setOpaque(false);
        footerPanel.setBorder(new MatteBorder(1, 0, 0, 0, new Color(51, 65, 85)));
        footerPanel.setPreferredSize(new Dimension(0, 100));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0; gbc.fill = GridBagConstraints.HORIZONTAL; gbc.weightx = 1;

        JLabel nameLabel = new JLabel(user.getUsername().toUpperCase());
        nameLabel.setFont(new Font("Segoe UI", Font.BOLD, 12));
        nameLabel.setForeground(Color.WHITE);
        
        JLabel roleLabel = new JLabel(user.getRole());
        roleLabel.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        roleLabel.setForeground(new Color(148, 163, 184));

        JButton btnLogout = new JButton("LOGOUT");
        btnLogout.setFont(new Font("Segoe UI", Font.BOLD, 10));
        btnLogout.setForeground(new Color(248, 113, 113));
        btnLogout.setContentAreaFilled(false);
        btnLogout.setBorder(BorderFactory.createLineBorder(new Color(248, 113, 113)));
        btnLogout.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnLogout.addActionListener(e -> System.exit(0));

        gbc.gridy = 0; gbc.insets = new Insets(0, 20, 2, 20); footerPanel.add(nameLabel, gbc);
        gbc.gridy = 1; gbc.insets = new Insets(0, 20, 10, 20); footerPanel.add(roleLabel, gbc);
        gbc.gridy = 2; gbc.insets = new Insets(0, 20, 0, 20); footerPanel.add(btnLogout, gbc);

        sidebar.add(brandPanel, BorderLayout.NORTH);
        sidebar.add(menuPanel, BorderLayout.CENTER);
        sidebar.add(footerPanel, BorderLayout.SOUTH);

        return sidebar;
    }

    private JButton createNavButton(String text, String cardName) {
        JButton btn = new JButton(text);
        btn.setMaximumSize(new Dimension(Integer.MAX_VALUE, 45));
        btn.setAlignmentX(Component.LEFT_ALIGNMENT);
        btn.setForeground(new Color(203, 213, 225));
        btn.setBackground(new Color(30, 41, 59));
        btn.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setHorizontalAlignment(SwingConstants.LEFT);
        btn.setMargin(new Insets(0, 20, 0, 0));

        btn.addActionListener(e -> cardLayout.show(contentArea, cardName));
        
        btn.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btn.setBackground(new Color(51, 65, 85));
                btn.setForeground(Color.WHITE);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btn.setBackground(new Color(30, 41, 59));
                btn.setForeground(new Color(203, 213, 225));
            }
        });

        return btn;
    }

    private JButton createInventoryButton() {
        JButton btn = createNavButton("INVENTORY", "INVENTORY");
        btn.setMaximumSize(new Dimension(Integer.MAX_VALUE, 45));
        btn.setAlignmentX(Component.LEFT_ALIGNMENT);
        btn.setForeground(new Color(203, 213, 225));
        btn.setBackground(new Color(30, 41, 59));
        btn.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setHorizontalAlignment(SwingConstants.LEFT);
        btn.setMargin(new Insets(0, 20, 0, 0));

        btn.addActionListener(e -> new BarangMenuFrame(user).setVisible(true));
        
        btn.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btn.setBackground(new Color(51, 65, 85));
                btn.setForeground(Color.WHITE);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btn.setBackground(new Color(30, 41, 59));
                btn.setForeground(new Color(203, 213, 225));
            }
        });

        return btn;
    }

        private JButton createReportButton() {
        JButton btn = createNavButton("REPORTS", "REPORTS");
        btn.setMaximumSize(new Dimension(Integer.MAX_VALUE, 45));
        btn.setAlignmentX(Component.LEFT_ALIGNMENT);
        btn.setForeground(new Color(203, 213, 225));
        btn.setBackground(new Color(30, 41, 59));
        btn.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setHorizontalAlignment(SwingConstants.LEFT);
        btn.setMargin(new Insets(0, 20, 0, 0));

        btn.addActionListener(e -> new BarangMenuFrame(user).setVisible(true));
        
        btn.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btn.setBackground(new Color(51, 65, 85));
                btn.setForeground(Color.WHITE);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btn.setBackground(new Color(30, 41, 59));
                btn.setForeground(new Color(203, 213, 225));
            }
        });

        return btn;
    }

    private JPanel createPlaceholderPage(String title) {
        JPanel p = new JPanel(new BorderLayout());
        p.setBackground(UIConfig.BG_LIGHT);
        JLabel l = new JLabel(title, SwingConstants.CENTER);
        l.setFont(new Font("Segoe UI", Font.BOLD, 24));
        l.setForeground(new Color(148, 163, 184));
        p.add(l, BorderLayout.CENTER);
        return p;
    }

    
}