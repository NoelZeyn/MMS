package main.java.com.inventory.pages.UI;

import com.formdev.flatlaf.FlatClientProperties;
import main.java.com.inventory.model.User;
import main.java.com.inventory.controller.UserActivityController;
import main.java.com.inventory.service.UserActivityService;
import main.java.com.inventory.model.UserActivity;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.MatteBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class DashboardFrame extends JPanel {
    private final User user;
    private final UserActivityController activityController = new UserActivityController(new UserActivityService());

    public DashboardFrame(User user) {
        this.user = user;
        initContent();
    }

    private void initContent() {
        setLayout(new BorderLayout());
        setBackground(UIConfig.BG_LIGHT);
        setBorder(new EmptyBorder(25, 25, 25, 25));

        // --- 1. HEADER SECTION ---
        JPanel header = new JPanel(new GridLayout(2, 1));
        header.setOpaque(false);
        JLabel welcome = new JLabel("Welcome back, " + user.getUsername() + "!");
        welcome.setFont(new Font("Segoe UI", Font.BOLD, 24));
        welcome.setForeground(UIConfig.TEXT_DARK);
        
        JLabel date = new JLabel("Here's what's happening with your inventory today.");
        date.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        date.setForeground(new Color(100, 116, 139));
        
        header.add(welcome);
        header.add(date);
        add(header, BorderLayout.NORTH);

        // --- 2. CENTER CONTENT (Cards & Table) ---
        JPanel centerPanel = new JPanel(new BorderLayout(0, 25));
        centerPanel.setOpaque(false);
        centerPanel.setBorder(new EmptyBorder(25, 0, 0, 0));

        // Top Stats Cards
        JPanel statsPanel = new JPanel(new GridLayout(1, 4, 20, 0));
        statsPanel.setOpaque(false);
        statsPanel.add(createStatCard("Total Items", "1,284", new Color(79, 70, 229)));
        statsPanel.add(createStatCard("Low Stock", "12", new Color(225, 29, 72)));
        statsPanel.add(createStatCard("Requests", "5", new Color(5, 150, 105)));
        statsPanel.add(createStatCard("Active Users", "24", new Color(217, 119, 6)));
        
        centerPanel.add(statsPanel, BorderLayout.NORTH);

        // Recent Activity Table
        centerPanel.add(createActivityPanel(), BorderLayout.CENTER);

        add(centerPanel, BorderLayout.CENTER);
    }

    private JPanel createStatCard(String title, String value, Color accentColor) {
        JPanel card = new JPanel(new BorderLayout());
        card.setBackground(Color.WHITE);
        card.putClientProperty(FlatClientProperties.STYLE, "arc: 15");
        card.setBorder(new EmptyBorder(20, 20, 20, 20));

        // Accent Border (Garis warna di samping kiri)
        card.setBorder(BorderFactory.createCompoundBorder(
            new MatteBorder(0, 5, 0, 0, accentColor),
            new EmptyBorder(15, 15, 15, 15)
        ));

        JLabel lblTitle = new JLabel(title);
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 13));
        lblTitle.setForeground(new Color(100, 116, 139));

        JLabel lblValue = new JLabel(value);
        lblValue.setFont(new Font("Segoe UI", Font.BOLD, 22));
        lblValue.setForeground(UIConfig.TEXT_DARK);

        card.add(lblTitle, BorderLayout.NORTH);
        card.add(lblValue, BorderLayout.SOUTH);

        return card;
    }

    private JPanel createActivityPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);
        panel.putClientProperty(FlatClientProperties.STYLE, "arc: 15");
        panel.setBorder(new EmptyBorder(20, 20, 20, 20));

        JLabel title = new JLabel("Recent System Activities");
        title.setFont(new Font("Segoe UI", Font.BOLD, 16));
        title.setBorder(new EmptyBorder(0, 0, 15, 0));
        panel.add(title, BorderLayout.NORTH);

        // Table Activity
        String[] cols = {"User", "Action", "Module", "Timestamp", "Details"};
        DefaultTableModel model = new DefaultTableModel(cols, 0);
        JTable table = new JTable(model);
        
        // Style Table
        table.putClientProperty(FlatClientProperties.STYLE, "rowHeight: 35; showHorizontalLines: true; selectionBackground: #f1f5f9;");
        table.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 12));
        table.getTableHeader().setBackground(Color.WHITE);

        // Load Data (Ambil 10 aktivitas terakhir)
        try {
            List<UserActivity> activities = activityController.getAllUserActivities(user);
            int count = 0;
            for (UserActivity ua : activities) {
                if (count++ > 10) break; 
                model.addRow(new Object[]{
                    ua.getUserId(), ua.getActivity(), ua.getTargetTable(), ua.getTimestamp(), ua.getDescription()
                });
            }
        } catch (Exception e) {
            model.addRow(new Object[]{"No data", "-", "-", "-", "-"});
        }

        JScrollPane scroll = new JScrollPane(table);
        scroll.setBorder(BorderFactory.createEmptyBorder());
        scroll.getViewport().setBackground(Color.WHITE);
        panel.add(scroll, BorderLayout.CENTER);

        return panel;
    }
}