package com.inventory.pages.Requisition;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Window;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.border.EmptyBorder;
import javax.swing.border.MatteBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

import com.formdev.flatlaf.FlatClientProperties;
import com.inventory.controller.RequisitionController;
import com.inventory.controller.UserActivityController;
import com.inventory.model.Requisition;
import com.inventory.model.RequisitionStatus;
import com.inventory.model.User;
import com.inventory.pages.UI.UIConfig;
import com.inventory.service.RequisitionService;
import com.inventory.service.UserActivityService;

public class RequisitionFrame extends JPanel {
    private final User user;
    private final RequisitionController controller;
    private final UserActivityController activityController = new UserActivityController(new UserActivityService());

    private JTable table;
    private DefaultTableModel tableModel;

    public RequisitionFrame(User user) {
        this.user = user;
        this.controller = new RequisitionController(new RequisitionService());
        initContent();
        loadData();
    }

    private void initContent() {
        setLayout(new BorderLayout());
        setBackground(UIConfig.BG_LIGHT);

        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(Color.WHITE);
        headerPanel.setPreferredSize(new Dimension(0, 75));
        headerPanel.setBorder(new MatteBorder(0, 0, 1, 0, new Color(226, 232, 240)));
        headerPanel.setBorder(BorderFactory.createCompoundBorder(
                headerPanel.getBorder(), new EmptyBorder(10, 25, 10, 25)));

        JPanel titleSection = new JPanel(new GridLayout(2, 1));
        titleSection.setOpaque(false);
        JLabel title = new JLabel("Purchase Requisition");
        title.setFont(new Font("Segoe UI", Font.BOLD, 20));
        JLabel subTitle = new JLabel("Status: Logged in as " + user.getRole());
        subTitle.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        subTitle.setForeground(new Color(100, 116, 139));
        titleSection.add(title);
        titleSection.add(subTitle);

        JPanel actionSection = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 10));
        actionSection.setOpaque(false);

        JButton btnRefresh = createActionButton("REFRESH", new Color(148, 163, 184));
        JButton btnManage = createActionButton("ACTION BY ID", new Color(51, 65, 85));
        JButton btnCreate = createActionButton("CREATE NEW", UIConfig.PRIMARY);

        actionSection.add(btnRefresh);
        actionSection.add(btnManage);
        actionSection.add(btnCreate);

        headerPanel.add(titleSection, BorderLayout.WEST);
        headerPanel.add(actionSection, BorderLayout.EAST);

        String[] columns = { "ID", "REQUESTER", "ITEM NAME", "QTY", "TOTAL PRICE", "VENDOR", "STATUS", "JUSTIFICATION" };
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int r, int c) {
                return false;
            }
        };

        table = new JTable(tableModel);
        table.putClientProperty(FlatClientProperties.STYLE,
                "rowHeight: 40; showHorizontalLines: true; selectionBackground: #e2e8f0; selectionForeground: #0f172a; gridColor: #f1f5f9;");

        DefaultTableCellRenderer baseRenderer = new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable t, Object v, boolean isS, boolean hasF, int r, int c) {
                super.getTableCellRendererComponent(t, v, isS, hasF, r, c);
                setBorder(new EmptyBorder(0, 15, 0, 15));
                if (isS) {
                    setBackground(new Color(226, 232, 240));
                    setForeground(new Color(15, 23, 42));
                } else {
                    setBackground(Color.WHITE);
                    setForeground(new Color(51, 65, 85));
                }
                
                if (c == 0 || c == 3 || c == 4 || c == 5) {
                    setHorizontalAlignment(SwingConstants.CENTER);
                } else {
                    setHorizontalAlignment(SwingConstants.LEFT);
                }
                return this;
            }
        };

        DefaultTableCellRenderer statusRenderer = new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable t, Object v, boolean isS, boolean hasF, int r, int c) {
                JLabel l = (JLabel) super.getTableCellRendererComponent(t, v, isS, hasF, r, c);
                l.setFont(new Font("Segoe UI", Font.BOLD, 11));
                l.setHorizontalAlignment(SwingConstants.CENTER);
                l.setBorder(new EmptyBorder(0, 15, 0, 15));

                if (isS) {
                    l.setBackground(new Color(226, 232, 240));
                } else {
                    l.setBackground(Color.WHITE);
                }

                String status = v.toString();
                if (status.contains("APPROVED")) l.setForeground(new Color(22, 163, 74));
                else if (status.contains("REJECTED")) l.setForeground(new Color(220, 38, 38));
                else l.setForeground(new Color(217, 119, 6));
                
                return l;
            }
        };

        for (int i = 0; i < table.getColumnCount(); i++) {
            if (i == 6) table.getColumnModel().getColumn(i).setCellRenderer(statusRenderer);
            else table.getColumnModel().getColumn(i).setCellRenderer(baseRenderer);
        }

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(new EmptyBorder(25, 25, 25, 25));
        scrollPane.getViewport().setBackground(Color.WHITE);
        scrollPane.setBackground(UIConfig.BG_LIGHT);

        add(headerPanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);

        btnCreate.addActionListener(e -> insertRequisition());
        btnManage.addActionListener(e -> manageRequisition());
        btnRefresh.addActionListener(e -> loadData());
    }

    private JButton createActionButton(String text, Color bg) {
        JButton btn = new JButton(text);
        btn.setPreferredSize(new Dimension(130, 38));
        btn.setBackground(bg);
        btn.setForeground(Color.WHITE);
        btn.setFont(new Font("Segoe UI Semibold", Font.PLAIN, 11));
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.putClientProperty(FlatClientProperties.STYLE, "arc: 8; borderWidth: 0; focusWidth: 0;");
        return btn;
    }

    private void loadData() {
        tableModel.setRowCount(0);
        try {
            List<Requisition> list = controller.getAllRequisitions(user);
            for (Requisition r : list) {
                tableModel.addRow(new Object[] {
                        r.getId(),
                        r.getRequesterName(),
                        r.getName(),
                        r.getQuantity(),
                        "Rp " + String.format("%,d", r.getTotalPrice()),
                        r.getVendor(),
                        r.getStatus(),
                        r.getJustification()
                });
            }
        } catch (Exception e) {
            showEnterpriseError("Load Failed: " + e.getMessage());
        }
    }

    private void manageRequisition() {
        String idStr = JOptionPane.showInputDialog(this, "Masukkan ID Requisition:");
        if (idStr == null || idStr.isEmpty()) return;

        try {
            int id = Integer.parseInt(idStr);
            Requisition req = controller.getRequisitionById(user, id);

            if (req == null) {
                showEnterpriseError("Data tidak ditemukan.");
                return;
            }

            if (user.isAsman()) {
                handleApprovalAction(id, "Assistant Manager", () -> controller.approveAsman(user, id), () -> controller.rejectAsman(user, id));
            } else if (user.isManager()) {
                handleApprovalAction(id, "Manager", () -> controller.approveManager(user, id), () -> controller.rejectManager(user, id));
            } else if (user.isBudget()) {
                handleApprovalAction(id, "Budget Office", () -> controller.approveBudget(user, id), () -> controller.rejectBudget(user, id));
            } else if (user.isAdmin()) {
                handleApprovalAction(id, "General Admin", () -> controller.approveAdmin(user, id), () -> controller.rejectAdmin(user, id));
            } else {
                if (req.getStatus() == RequisitionStatus.PENDING_ASMAN) {
                    int conf = JOptionPane.showConfirmDialog(this, "Hapus permohonan ini?", "Delete", JOptionPane.YES_NO_OPTION);
                    if (conf == JOptionPane.YES_OPTION) {
                        controller.deleteRequisitionById(user, id);
                        loadData();
                    }
                } else {
                    JOptionPane.showMessageDialog(this, "Data sudah diproses, tidak bisa dibatalkan.");
                }
            }
        } catch (Exception e) {
            showEnterpriseError(e.getMessage());
        }
    }

    private void handleApprovalAction(int id, String roleName, ApprovalTask onApprove, ApprovalTask onReject) {
        String[] options = { "APPROVE", "REJECT", "CANCEL" };
        int choice = JOptionPane.showOptionDialog(this, "Reviewing ID: " + id + "\nDecision for " + roleName + ":", "Enterprise Approval System",
                JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[2]);

        try {
            if (choice == 0) {
                onApprove.execute();
                JOptionPane.showMessageDialog(this, "Status: APPROVED by " + roleName);
                activityController.insertLogActivity(user, "APPROVE", "REQUISITION", id, "Approved by " + roleName);
            } else if (choice == 1) {
                onReject.execute();
                JOptionPane.showMessageDialog(this, "Status: REJECTED by " + roleName, "Warning", JOptionPane.WARNING_MESSAGE);
                activityController.insertLogActivity(user, "REJECT", "REQUISITION", id, "Rejected by " + roleName);
            }
            loadData();
        } catch (Exception ex) {
            showEnterpriseError("Gagal memproses approval: " + ex.getMessage());
        }
    }

    @FunctionalInterface
    interface ApprovalTask {
        void execute() throws Exception;
    }

    private void insertRequisition() {
        JTextField nameF = new JTextField();
        JTextField specF = new JTextField();
        JTextField qtyF = new JTextField();
        JTextField priceF = new JTextField();
        JTextField vendorF = new JTextField();
        JTextArea justifA = new JTextArea(3, 20);
        justifA.setLineWrap(true);
        justifA.setWrapStyleWord(true);

        nameF.addActionListener(e -> specF.requestFocus());
        specF.addActionListener(e -> qtyF.requestFocus());
        qtyF.addActionListener(e -> priceF.requestFocus());
        priceF.addActionListener(e -> vendorF.requestFocus());
        vendorF.addActionListener(e -> {
            Window window = SwingUtilities.getWindowAncestor(vendorF);
            if (window != null) {
                JButton okButton = findOKButton(window);
                if (okButton != null) okButton.doClick();
            }
        });

        Object[] message = { "Item Name:", nameF, "Specification:", specF, "Quantity:", qtyF, "Unit Price:", priceF, "Vendor:", vendorF, "Justification:", new JScrollPane(justifA) };
        SwingUtilities.invokeLater(nameF::requestFocusInWindow);

        int option = JOptionPane.showConfirmDialog(this, message, "Fast Entry - New Purchase Request", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

        if (option == JOptionPane.OK_OPTION) {
            try {
                if (nameF.getText().isBlank()) throw new Exception("Item name cannot be empty");
                int qty = Integer.parseInt(qtyF.getText().trim());
                int uPrice = Integer.parseInt(priceF.getText().trim());
                Requisition req = new Requisition(user.getId(), nameF.getText().trim(), specF.getText().trim(), qty, uPrice, qty * uPrice, RequisitionStatus.PENDING_ASMAN, vendorF.getText().trim(), justifA.getText().trim());
                controller.insertRequisition(user, req);
                activityController.insertLogActivity(user, "CREATE", "REQUISITION", 0, "Fast Entry: " + req.getName());
                loadData();
                SwingUtilities.invokeLater(this::insertRequisition);
            } catch (Exception ex) {
                showEnterpriseError("Error: " + ex.getMessage());
                SwingUtilities.invokeLater(this::insertRequisition);
            }
        }
    }

    private JButton findOKButton(Container container) {
        if (container == null) return null;
        for (Component c : container.getComponents()) {
            if (c instanceof JButton && ((JButton) c).getText().equalsIgnoreCase("OK")) return (JButton) c;
            else if (c instanceof Container) {
                JButton b = findOKButton((Container) c);
                if (b != null) return b;
            }
        }
        return null;
    }

    private void showEnterpriseError(String message) {
        JOptionPane.showMessageDialog(this, message, "System Error", JOptionPane.ERROR_MESSAGE);
    }
}