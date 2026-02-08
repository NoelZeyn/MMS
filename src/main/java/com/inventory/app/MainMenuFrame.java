package main.java.com.inventory.app;

import main.java.com.inventory.model.User;
import main.java.com.inventory.pages.UI.DashboardFrame; // Import Dashboard sungguhan
import main.java.com.inventory.pages.Inventory.BarangMenuFrame;
import main.java.com.inventory.pages.Requisition.RequisitionFrame;
import main.java.com.inventory.pages.Management.UserManagementFrame;
import main.java.com.inventory.pages.Report.ReportMenuFrame;
import main.java.com.inventory.pages.UI.UIConfig;

import javax.swing.*;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
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
        setSize(1280, 800); // Sedikit lebih lebar untuk kenyamanan Dashboard
        setLocationRelativeTo(null);
    }

    private void initContent() {
        JPanel mainPanel = new JPanel(new BorderLayout());
        setContentPane(mainPanel);

        // --- SIDEBAR ---
        mainPanel.add(createSidebar(), BorderLayout.WEST);

        // --- CONTENT AREA (CARD LAYOUT) ---
        cardLayout = new CardLayout();
        contentArea = new JPanel(cardLayout);
        contentArea.setBackground(UIConfig.BG_LIGHT);

        // Mendaftarkan Halaman Sungguhan
        contentArea.add(new DashboardFrame(user), "DASHBOARD");
        contentArea.add(new BarangMenuFrame(user), "INVENTORY");
        contentArea.add(new RequisitionFrame(user), "REQUISITION");
        contentArea.add(new ReportMenuFrame(user), "REPORTS");

        if (user.isAdmin() || user.isManager()) {
            contentArea.add(new UserManagementFrame(user), "MANAGE_USER");
        }

        mainPanel.add(contentArea, BorderLayout.CENTER);
    }

    private JPanel createSidebar() {
        JPanel sidebar = new JPanel(new BorderLayout());
        sidebar.setBackground(new Color(15, 23, 42)); // Warna Slate 900 (Lebih modern)
        sidebar.setPreferredSize(new Dimension(260, 0));

        // Brand Area (Logo/Nama Perusahaan)
        JPanel brandPanel = new JPanel(new BorderLayout());
        brandPanel.setOpaque(false);
        brandPanel.setBorder(new EmptyBorder(30, 25, 30, 25));
        
        JLabel brandLabel = new JLabel("MMS ENTERPRISE");
        brandLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
        brandLabel.setForeground(Color.WHITE);
        brandPanel.add(brandLabel, BorderLayout.CENTER);

        // Menu Area
        JPanel menuPanel = new JPanel();
        menuPanel.setOpaque(false);
        menuPanel.setLayout(new BoxLayout(menuPanel, BoxLayout.Y_AXIS));
        menuPanel.setBorder(new EmptyBorder(0, 15, 20, 15));

        // Menambahkan Navigasi
        menuPanel.add(createNavButton("DASHBOARD", "DASHBOARD"));
        menuPanel.add(Box.createVerticalStrut(8));
        menuPanel.add(createNavButton("INVENTORY", "INVENTORY"));
        menuPanel.add(Box.createVerticalStrut(8));
        menuPanel.add(createNavButton("PURCHASE REQUEST", "REQUISITION"));
        menuPanel.add(Box.createVerticalStrut(8));
        menuPanel.add(createNavButton("REPORTS", "REPORTS"));

        if (user.isAdmin() || user.isManager()) {
            menuPanel.add(Box.createVerticalStrut(25)); // Spasi antar grup
            JLabel lblAdmin = new JLabel(" ADMINISTRATION");
            lblAdmin.setFont(new Font("Segoe UI", Font.BOLD, 10));
            lblAdmin.setForeground(new Color(71, 85, 105));
            lblAdmin.setBorder(new EmptyBorder(0, 15, 10, 0));
            menuPanel.add(lblAdmin);
            menuPanel.add(createNavButton("USER MANAGEMENT", "MANAGE_USER"));
        }

        sidebar.add(brandPanel, BorderLayout.NORTH);
        sidebar.add(menuPanel, BorderLayout.CENTER);
        sidebar.add(createFooter(), BorderLayout.SOUTH);

        return sidebar;
    }

    private JPanel createFooter() {
        JPanel footer = new JPanel(new BorderLayout());
        footer.setOpaque(false);
        footer.setBorder(new CompoundBorder(
            new MatteBorder(1, 0, 0, 0, new Color(30, 41, 59)),
            new EmptyBorder(20, 20, 20, 20)
        ));

        JPanel userDetail = new JPanel(new GridLayout(2, 1));
        userDetail.setOpaque(false);
        
        JLabel nameLabel = new JLabel(user.getUsername().toUpperCase());
        nameLabel.setFont(new Font("Segoe UI", Font.BOLD, 13));
        nameLabel.setForeground(Color.WHITE);

        JLabel roleLabel = new JLabel(user.getRole());
        roleLabel.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        roleLabel.setForeground(new Color(148, 163, 184));

        userDetail.add(nameLabel);
        userDetail.add(roleLabel);

        JButton btnLogout = new JButton("Logout");
        btnLogout.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnLogout.addActionListener(e -> {
            int confirm = JOptionPane.showConfirmDialog(this, "Logout from system?", "Exit", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) System.exit(0);
        });

        footer.add(userDetail, BorderLayout.WEST);
        footer.add(btnLogout, BorderLayout.EAST);
        
        return footer;
    }

    private JButton createNavButton(String text, String cardName) {
        JButton btn = new JButton(text);
        btn.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        btn.setAlignmentX(Component.LEFT_ALIGNMENT);
        btn.setForeground(new Color(148, 163, 184));
        btn.setBackground(new Color(15, 23, 42));
        btn.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setHorizontalAlignment(SwingConstants.LEFT);
        btn.setMargin(new Insets(0, 15, 0, 0));

        btn.addActionListener(e -> {
            cardLayout.show(contentArea, cardName);
            // Reset semua tombol ke warna standar
            for (Component c : btn.getParent().getComponents()) {
                if (c instanceof JButton) {
                    c.setForeground(new Color(148, 163, 184));
                    c.setBackground(new Color(15, 23, 42));
                }
            }
            // Highlight tombol yang aktif
            btn.setForeground(Color.WHITE);
            btn.setBackground(new Color(30, 41, 59));
        });

        return btn;
    }
}