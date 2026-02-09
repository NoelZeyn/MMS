package com.inventory.pages.UI;

import com.formdev.flatlaf.FlatClientProperties;
import com.inventory.controller.RequisitionController;
import com.inventory.model.Requisition;
import com.inventory.model.RequisitionStatus;
import com.inventory.model.User;
import com.inventory.service.RequisitionService;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

public class DashboardFrame extends JPanel {
    private final User user;
    private final RequisitionController reqController = new RequisitionController(new RequisitionService());
    
    private DefaultTableModel tableModel;
    private JLabel lblPending, lblProgress, lblRejected, lblApproved;
    private Timer autoRefreshTimer;

    public DashboardFrame(User user) {
        this.user = user;
        initLayout();
        refreshData();
        startLiveUpdate();
    }

    private void initLayout() {
        setLayout(new BorderLayout());
        setBackground(new Color(248, 250, 252));
        setBorder(new EmptyBorder(30, 40, 30, 40));

        JPanel header = new JPanel(new BorderLayout());
        header.setOpaque(false);

        JLabel welcome = new JLabel("Halo, " + user.getUsername() + " ✨");
        welcome.setFont(new Font("Segoe UI Variable Display", Font.BOLD, 28));
        welcome.setForeground(new Color(15, 23, 42));

        JLabel subTitle = new JLabel(getTaskSummaryText());
        subTitle.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        subTitle.setForeground(new Color(100, 116, 139));

        JPanel titlePanel = new JPanel(new GridLayout(2, 1, 0, 5));
        titlePanel.setOpaque(false);
        titlePanel.add(welcome);
        titlePanel.add(subTitle);
        
        JButton btnRefresh = new JButton("Refresh Now");
        btnRefresh.putClientProperty(FlatClientProperties.STYLE, "arc: 10; focusWidth: 0;");
        btnRefresh.setBackground(Color.WHITE);
        btnRefresh.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnRefresh.addActionListener(e -> refreshData());

        header.add(titlePanel, BorderLayout.WEST);
        header.add(btnRefresh, BorderLayout.EAST);
        add(header, BorderLayout.NORTH);

        JPanel centerPanel = new JPanel(new BorderLayout(0, 35));
        centerPanel.setOpaque(false);
        centerPanel.setBorder(new EmptyBorder(30, 0, 0, 0));

        centerPanel.add(createStatsPanel(), BorderLayout.NORTH);
        centerPanel.add(createApprovalTaskPanel(), BorderLayout.CENTER);
        add(centerPanel, BorderLayout.CENTER);
    }

    private void startLiveUpdate() {
        // Refresh otomatis setiap 10 detik
        autoRefreshTimer = new Timer(10000, e -> refreshData());
        autoRefreshTimer.start();
    }

    public void stopLiveUpdate() {
        if (autoRefreshTimer != null) autoRefreshTimer.stop();
    }

    private void refreshData() {
        try {
            List<Requisition> all = reqController.getAllRequisitions(user);
            List<Requisition> myTasks = reqController.getMyPendingTasks(user);

            long pending = countPendingTasks(all);
            long rejected = all.stream().filter(r -> r.getStatus().name().contains("REJECTED")).count();
            long approved = all.stream().filter(r -> r.getStatus() == RequisitionStatus.APPROVED_ADMIN).count();

            lblPending.setText(String.valueOf(pending));
            lblProgress.setText(String.valueOf(all.size() - pending - rejected - approved));
            lblRejected.setText(String.valueOf(rejected));
            lblApproved.setText(String.valueOf(approved));

            tableModel.setRowCount(0);
            for (Requisition r : myTasks) {
                tableModel.addRow(new Object[] { "#" + r.getId(), r.getName(), r.getQuantity(), r.getRequesterName(),
                        "Rp " + String.format("%,d", r.getTotalPrice()), r.getVendor(), r.getJustification() });
            }
        } catch (Exception e) {
            System.err.println("Live Refresh Error: " + e.getMessage());
        }
    }

    private JPanel createStatsPanel() {
        JPanel statsPanel = new JPanel(new GridLayout(1, 4, 25, 0));
        statsPanel.setOpaque(false);

        lblPending = new JLabel("0");
        lblProgress = new JLabel("0");
        lblRejected = new JLabel("0");
        lblApproved = new JLabel("0");

        statsPanel.add(createStatCard("Pending Action", lblPending, new Color(99, 102, 241)));
        statsPanel.add(createStatCard("In Progress", lblProgress, new Color(100, 116, 139)));
        statsPanel.add(createStatCard("Rejected", lblRejected, new Color(244, 63, 94)));
        statsPanel.add(createStatCard("Final Approved", lblApproved, new Color(16, 185, 129)));

        return statsPanel;
    }

    private JPanel createStatCard(String title, JLabel valueLabel, Color accentColor) {
        JPanel card = new JPanel(new BorderLayout());
        card.setBackground(Color.WHITE);
        card.putClientProperty(FlatClientProperties.STYLE, "arc: 24;");
        card.setBorder(new EmptyBorder(20, 25, 20, 25));

        JLabel lblTitle = new JLabel(title);
        lblTitle.setFont(new Font("Segoe UI Semibold", Font.PLAIN, 13));
        lblTitle.setForeground(new Color(148, 163, 184));

        valueLabel.setFont(new Font("Segoe UI Variable Display", Font.BOLD, 36));
        valueLabel.setForeground(new Color(30, 41, 59));

        JPanel dot = new JPanel() {
            @Override protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(accentColor);
                g2.fillOval(0, 0, 8, 8);
                g2.dispose();
            }
        };
        dot.setPreferredSize(new Dimension(8, 8));
        dot.setOpaque(false);

        JPanel top = new JPanel(new BorderLayout());
        top.setOpaque(false);
        top.add(lblTitle, BorderLayout.WEST);
        top.add(dot, BorderLayout.EAST);

        card.add(top, BorderLayout.NORTH);
        card.add(valueLabel, BorderLayout.CENTER);
        return card;
    }

    private JPanel createApprovalTaskPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);
        panel.putClientProperty(FlatClientProperties.STYLE, "arc: 24;");
        panel.setBorder(new EmptyBorder(25, 25, 25, 25));

        JLabel title = new JLabel("Daftar Persetujuan");
        title.setFont(new Font("Segoe UI Variable Display", Font.BOLD, 18));
        title.setBorder(new EmptyBorder(0, 0, 20, 0));
        panel.add(title, BorderLayout.NORTH);

        String[] cols = { "ID", "Nama Barang", "Qty", "Pemohon", "Total", "Vendor", "Alasan" };
        tableModel = new DefaultTableModel(cols, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };

        JTable table = new JTable(tableModel);
        table.setRowHeight(45);
        table.setShowGrid(false);
        table.putClientProperty(FlatClientProperties.STYLE, "selectionBackground: #F1F5F9; selectionForeground: #0F172A;");

        table.addMouseListener(new MouseAdapter() {
            @Override public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2 && table.getSelectedRow() != -1) {
                    int row = table.getSelectedRow();
                    int id = Integer.parseInt(table.getValueAt(row, 0).toString().replace("#", ""));
                    showApprovalAction(id, table.getValueAt(row, 1).toString());
                }
            }
        });

        JScrollPane scroll = new JScrollPane(table);
        scroll.setBorder(BorderFactory.createEmptyBorder());
        scroll.getViewport().setBackground(Color.WHITE);
        panel.add(scroll, BorderLayout.CENTER);

        return panel;
    }

    private void showApprovalAction(int id, String itemName) {
        if (user.isStaff()) return;
        String[] options = { "Setujui", "Tolak", "Batal" };
        int choice = JOptionPane.showOptionDialog(this, "Konfirmasi keputusan untuk " + itemName, "Action",
                0, JOptionPane.PLAIN_MESSAGE, null, options, options[2]);

        try {
            if (choice == 0) executeRoleAction(id, true);
            else if (choice == 1) executeRoleAction(id, false);
            if (choice == 0 || choice == 1) refreshData();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Gagal: " + ex.getMessage());
        }
    }

    private void executeRoleAction(int id, boolean approve) throws Exception {
        if (approve) {
            if (user.isAsman()) reqController.approveAsman(user, id);
            else if (user.isManager()) reqController.approveManager(user, id);
            else if (user.isBudget()) reqController.approveBudget(user, id);
            else if (user.isAdmin() || user.isSuperAdmin()) reqController.approveAdmin(user, id);
        } else {
            if (user.isAsman()) reqController.rejectAsman(user, id);
            else if (user.isManager()) reqController.rejectManager(user, id);
            else if (user.isBudget()) reqController.rejectBudget(user, id);
            else if (user.isAdmin() || user.isSuperAdmin()) reqController.rejectAdmin(user, id);
        }
    }

    private long countPendingTasks(List<Requisition> all) {
        RequisitionStatus target = user.isAsman() ? RequisitionStatus.PENDING_ASMAN
                : user.isManager() ? RequisitionStatus.PENDING_MANAGER
                : user.isBudget() ? RequisitionStatus.PENDING_BUDGET
                : (user.isAdmin() || user.isSuperAdmin()) ? RequisitionStatus.PENDING_ADMIN : null;
        return all.stream().filter(r -> r.getStatus() == target).count();
    }

    private String getTaskSummaryText() {
        return user.isStaff() ? "Status pengajuan barang Anda." : "Permintaan butuh approval Anda.";
    }
}