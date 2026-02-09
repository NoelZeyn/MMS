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

    public DashboardFrame(User user) {
        this.user = user;
        initContent();
    }

    private void initContent() {
        removeAll();
        setLayout(new BorderLayout());
        setBackground(new Color(248, 250, 252)); // Slate 50 (Warna latar sangat lembut)
        setBorder(new EmptyBorder(30, 40, 30, 40));

        // --- HEADER ---
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
        header.add(titlePanel, BorderLayout.WEST);

        add(header, BorderLayout.NORTH);

        // --- CENTER CONTENT ---
        JPanel centerPanel = new JPanel(new BorderLayout(0, 35));
        centerPanel.setOpaque(false);
        centerPanel.setBorder(new EmptyBorder(30, 0, 0, 0));

        centerPanel.add(createStatsPanel(), BorderLayout.NORTH);
        centerPanel.add(createApprovalTaskPanel(), BorderLayout.CENTER);

        add(centerPanel, BorderLayout.CENTER);
        revalidate();
        repaint();
    }

    private JPanel createStatsPanel() {
        JPanel statsPanel = new JPanel(new GridLayout(1, 4, 25, 0));
        statsPanel.setOpaque(false);

        try {
            List<Requisition> all = reqController.getAllRequisitions(user);
            long pending = countPendingTasks(all);
            long rejected = all.stream().filter(r -> r.getStatus().name().contains("REJECTED")).count();
            long approved = all.stream().filter(r -> r.getStatus() == RequisitionStatus.APPROVED_ADMIN).count();

            // Menggunakan palet pastel yang "lembut"
            statsPanel.add(createStatCard("Pending Action", String.valueOf(pending), new Color(99, 102, 241))); // Indigo
            statsPanel.add(createStatCard("In Progress", String.valueOf(all.size() - pending - rejected - approved),
                    new Color(100, 116, 139))); // Slate
            statsPanel.add(createStatCard("Rejected", String.valueOf(rejected), new Color(244, 63, 94))); // Rose
            statsPanel.add(createStatCard("Final Approved", String.valueOf(approved), new Color(16, 185, 129))); // Emerald
        } catch (Exception e) {
            statsPanel.add(createStatCard("Status", "!", Color.GRAY));
        }
        return statsPanel;
    }

    private JPanel createStatCard(String title, String value, Color accentColor) {
        JPanel card = new JPanel(new BorderLayout());
        card.setBackground(Color.WHITE);
        card.putClientProperty(FlatClientProperties.STYLE, "arc: 24;"); // Sudut sangat bulat (modern)
        card.setBorder(new EmptyBorder(20, 25, 15, 25));

        // Judul kecil di atas
        JLabel lblTitle = new JLabel(title);
        lblTitle.setFont(new Font("Segoe UI Semibold", Font.PLAIN, 13));
        lblTitle.setForeground(new Color(148, 163, 184));

        // Angka besar di tengah
        JLabel lblValue = new JLabel(value);
        lblValue.setFont(new Font("Segoe UI Variable Display", Font.BOLD, 36));
        lblValue.setForeground(new Color(30, 41, 59));

        // Label kecil "Dibuat oleh AI"
        JLabel lblAi = new JLabel("Hanya Tampilan Card ini dibuat oleh AI");
        lblAi.setFont(new Font("Segoe UI", Font.ITALIC, 9));
        lblAi.setForeground(new Color(203, 213, 225)); // Sangat samar
        lblAi.setHorizontalAlignment(SwingConstants.RIGHT);

        // Aksen warna berupa lingkaran kecil (Dot) bukan garis kaku
        JPanel dotPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(accentColor);
                g2.fillOval(0, 0, 8, 8);
                g2.dispose();
            }
        };
        dotPanel.setPreferredSize(new Dimension(8, 8));
        dotPanel.setOpaque(false);

        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setOpaque(false);
        topPanel.add(lblTitle, BorderLayout.WEST);
        topPanel.add(dotPanel, BorderLayout.EAST);

        card.add(topPanel, BorderLayout.NORTH);
        card.add(lblValue, BorderLayout.CENTER);
        card.add(lblAi, BorderLayout.SOUTH);

        return card;
    }

    private JPanel createApprovalTaskPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);
        panel.putClientProperty(FlatClientProperties.STYLE, "arc: 24;");
        panel.setBorder(new EmptyBorder(25, 25, 25, 25));

        JLabel title = new JLabel("Daftar Persetujuan");
        title.setFont(new Font("Segoe UI Variable Display", Font.BOLD, 18));
        title.setForeground(new Color(15, 23, 42));
        title.setBorder(new EmptyBorder(0, 0, 20, 0));
        panel.add(title, BorderLayout.NORTH);

        String[] cols = { "ID", "Nama Barang", "Qty", "Pemohon", "Total", "Vendor", "Alasan" };
        DefaultTableModel model = new DefaultTableModel(cols, 0) {
            @Override
            public boolean isCellEditable(int r, int c) {
                return false;
            }
        };

        JTable table = new JTable(model);
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(SwingConstants.CENTER);

        // ID
        table.getColumnModel()
                .getColumn(0)
                .setCellRenderer(centerRenderer);

        table.getColumnModel()
                .getColumn(2)
                .setCellRenderer(centerRenderer);

        table.getColumnModel()
                .getColumn(5)
                .setCellRenderer(centerRenderer);

        table.getTableHeader().setFont(new Font("Segoe UI Semibold", Font.PLAIN, 12));
        table.getTableHeader().setBackground(Color.WHITE);
        table.setRowHeight(45);
        table.setShowGrid(false);
        table.setIntercellSpacing(new Dimension(0, 0));

        table.putClientProperty(FlatClientProperties.STYLE,
                // BENAR
                "selectionBackground: #F1F5F9; " +
                        "selectionForeground: #0F172A; " +
                        "showHorizontalLines: true; " +
                        "gridColor: #F1F5F9;"); 

        table.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected,
                    boolean hasFocus, int row, int col) {
                JLabel c = (JLabel) super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, col);
                c.setBorder(new EmptyBorder(0, 15, 0, 15));
                if (!isSelected)
                    c.setForeground(new Color(71, 85, 105));
                return c;
            }
        });

        table.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2 && table.getSelectedRow() != -1) {
                    int row = table.getSelectedRow();
                    int id = Integer.parseInt(table.getValueAt(row, 0).toString().replace("#", ""));
                    showApprovalAction(id, table.getValueAt(row, 1).toString());
                }
            }
        });

        loadDashboardData(model);
        JScrollPane scroll = new JScrollPane(table);
        scroll.setBorder(BorderFactory.createEmptyBorder());
        scroll.getViewport().setBackground(Color.WHITE);
        panel.add(scroll, BorderLayout.CENTER);

        return panel;
    }

    private void loadDashboardData(DefaultTableModel model) {
        try {
            List<Requisition> myTasks = reqController.getMyPendingTasks(user);
            for (Requisition r : myTasks) {
                model.addRow(new Object[] { "#" + r.getId(), r.getName(), r.getQuantity(), r.getRequesterName(),
                        "Rp " + String.format("%,d", r.getTotalPrice()), r.getVendor(), r.getJustification() });
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void showApprovalAction(int id, String itemName) {
        if (user.isStaff())
            return;
        String[] options = { "Setujui", "Tolak", "Batal" };
        int choice = JOptionPane.showOptionDialog(this, "Tentukan keputusan untuk " + itemName, "Konfirmasi Action",
                0, JOptionPane.PLAIN_MESSAGE, null, options, options[2]);

        try {
            if (choice == 0)
                executeRoleAction(id, true);
            else if (choice == 1)
                executeRoleAction(id, false);
            if (choice == 0 || choice == 1)
                initContent();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Gagal: " + ex.getMessage());
        }
    }

    private void executeRoleAction(int id, boolean approve) throws Exception {
        if (approve) {
            if (user.isAsman())
                reqController.approveAsman(user, id);
            else if (user.isManager())
                reqController.approveManager(user, id);
            else if (user.isBudget())
                reqController.approveBudget(user, id);
            else if (user.isAdmin())
                reqController.approveAdmin(user, id);
        } else {
            if (user.isAsman())
                reqController.rejectAsman(user, id);
            else if (user.isManager())
                reqController.rejectManager(user, id);
            else if (user.isBudget())
                reqController.rejectBudget(user, id);
            else if (user.isAdmin())
                reqController.rejectAdmin(user, id);
        }
    }

    private long countPendingTasks(List<Requisition> all) {
        RequisitionStatus target = user.isAsman() ? RequisitionStatus.PENDING_ASMAN
                : user.isManager() ? RequisitionStatus.PENDING_MANAGER
                        : user.isBudget() ? RequisitionStatus.PENDING_BUDGET
                                : user.isAdmin() ? RequisitionStatus.PENDING_ADMIN : null;
        return all.stream().filter(r -> r.getStatus() == target).count();
    }

    private String getTaskSummaryText() {
        return user.isStaff() ? "Pantau status pengajuan barang Anda."
                : "Ada beberapa permintaan yang butuh approval Anda.";
    }
}