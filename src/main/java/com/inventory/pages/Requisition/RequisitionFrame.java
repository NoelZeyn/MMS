package main.java.com.inventory.pages.Requisition;

import com.formdev.flatlaf.FlatClientProperties;
import main.java.com.inventory.controller.RequisitionController;
import main.java.com.inventory.controller.UserActivityController;
import main.java.com.inventory.model.Requisition;
import main.java.com.inventory.model.RequisitionStatus;
import main.java.com.inventory.model.User;
import main.java.com.inventory.pages.UI.UIConfig;
import main.java.com.inventory.service.RequisitionService;
import main.java.com.inventory.service.UserActivityService;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.MatteBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

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

        // --- 1. HEADER ---
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

        // --- 2. TABLE AREA ---
        // Menambahkan kolom STATUS (Penting untuk workflow)
        String[] columns = { "ID", "ITEM NAME", "QTY", "TOTAL PRICE", "VENDOR", "STATUS", "JUSTIFICATION" };
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int r, int c) {
                return false;
            }
        };

        table = new JTable(tableModel);
        table.putClientProperty(FlatClientProperties.STYLE,
                "rowHeight: 40; showHorizontalLines: true; selectionBackground: #f1f5f9;");

        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(SwingConstants.CENTER);
        // Custom Renderer untuk Status agar berwarna
        table.getColumnModel().getColumn(0).setCellRenderer(centerRenderer);
        table.getColumnModel().getColumn(2).setCellRenderer(centerRenderer);
        table.getColumnModel().getColumn(3).setCellRenderer(centerRenderer);
        table.getColumnModel().getColumn(4).setCellRenderer(centerRenderer);
        table.getColumnModel().getColumn(5).setCellRenderer(new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable t, Object v, boolean isS, boolean hasF, int r,
                    int c) {
                JLabel l = (JLabel) super.getTableCellRendererComponent(t, v, isS, hasF, r, c);
                l.setFont(new Font("Segoe UI", Font.BOLD, 11));
                String status = v.toString();
                if (status.contains("APPROVED"))
                    l.setForeground(new Color(22, 163, 74));
                else if (status.contains("REJECTED"))
                    l.setForeground(new Color(220, 38, 38));
                else
                    l.setForeground(new Color(217, 119, 6));
                l.setHorizontalAlignment(SwingConstants.CENTER);
                return l;
            }
        });

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(new EmptyBorder(20, 25, 20, 25));
        scrollPane.getViewport().setBackground(Color.WHITE);

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
        btn.setFont(new Font("Segoe UI", Font.BOLD, 11));
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
        String idStr = JOptionPane.showInputDialog(this, "Enter Requisition ID:");
        if (idStr == null || idStr.isEmpty())
            return;

        try {
            int id = Integer.parseInt(idStr);
            Requisition req = controller.getRequisitionById(user, id);

            // LOGIKA DINAMIS BERDASARKAN ROLE
            if (user.isAsman()) {
                handleApprovalAction(id, "Asman", () -> {
                    try {
                        controller.approveAsman(user, id);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }, () -> {
                    try {
                        controller.rejectAsman(user, id);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                });
            } else if (user.isManager()) {
                handleApprovalAction(id, "Manager", () -> {
                    try {
                        controller.approveManager(user, id);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }, () -> {
                    try {
                        controller.rejectManager(user, id);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                });
            } else if (user.isBudget()) {
                handleApprovalAction(id, "Budget", () -> {
                    try {
                        controller.approveBudget(user, id);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }, () -> {
                    try {
                        controller.rejectBudget(user, id);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                });
            } else if (user.isAdmin()) {
                handleApprovalAction(id, "General Admin", () -> {
                    try {
                        controller.approveAdmin(user, id);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }, () -> {
                    try {
                        controller.rejectAdmin(user, id);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                });
            } else {
                // User Biasa hanya bisa Delete jika status masih awal
                int conf = JOptionPane.showConfirmDialog(this, "Cancel this requisition?", "Delete",
                        JOptionPane.YES_NO_OPTION);
                if (conf == JOptionPane.YES_OPTION) {
                    controller.deleteRequisitionById(user, id);
                    loadData();
                }
            }
        } catch (Exception e) {
            showEnterpriseError(e.getMessage());
        }
    }

    // Helper untuk memudahkan Reusable Approval Dialog
    private void handleApprovalAction(int id, String roleName, Runnable onApprove, Runnable onReject) {
        String[] options = { "APPROVE", "REJECT", "CANCEL" };
        int choice = JOptionPane.showOptionDialog(this,
                "Reviewing ID: " + id + "\nDecision for " + roleName + ":",
                "Approval System",
                JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[2]);

        try {
            if (choice == 0) {
                onApprove.run();
                JOptionPane.showMessageDialog(this, "Request Approved.");
            } else if (choice == 1) {
                onReject.run();
                JOptionPane.showMessageDialog(this, "Request Rejected.");
            }
            loadData();
        } catch (Exception ex) {
            showEnterpriseError(ex.getMessage());
        }
    }

    private void insertRequisition() {
        JTextField nameF = new JTextField();
        JTextField specF = new JTextField();
        JTextField qtyF = new JTextField();
        JTextField priceF = new JTextField();
        JTextField vendorF = new JTextField();
        JTextArea justifA = new JTextArea(3, 20);

        nameF.addActionListener(e -> specF.requestFocus());
        specF.addActionListener(e -> qtyF.requestFocus());
        qtyF.addActionListener(e -> priceF.requestFocus());
        priceF.addActionListener(e -> vendorF.requestFocus());
        vendorF.addActionListener(e -> justifA.requestFocus());

        Object[] message = {
                "Item Name:", nameF, "Specification:", specF, "Quantity:", qtyF,
                "Unit Price:", priceF, "Vendor:", vendorF, "Justification:", new JScrollPane(justifA)
        };

        int option = JOptionPane.showConfirmDialog(this, message, "New Purchase Request",
                JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

        if (option == JOptionPane.OK_OPTION) {
            try {
                Requisition req = new Requisition(
                        user.getId(), nameF.getText().trim(), specF.getText().trim(),
                        Integer.parseInt(qtyF.getText().trim()),
                        Integer.parseInt(priceF.getText().trim()),
                        0, RequisitionStatus.SUBMITTED, vendorF.getText().trim(), justifA.getText().trim());

                controller.insertRequisition(user, req);
                loadData();
                // Jika ingin input beruntun, uncomment baris bawah:
                // SwingUtilities.invokeLater(this::insertRequisition);
            } catch (Exception ex) {
                showEnterpriseError("Input Error: " + ex.getMessage());
            }
        }
    }

    private JButton findOKButton(Container container) {
        if (container == null)
            return null;
        for (Component c : container.getComponents()) {
            if (c instanceof JButton && ((JButton) c).getText().equalsIgnoreCase("OK"))
                return (JButton) c;
            else if (c instanceof Container) {
                JButton b = findOKButton((Container) c);
                if (b != null)
                    return b;
            }
        }
        return null;
    }

    private void showEnterpriseError(String message) {
        JOptionPane.showMessageDialog(this, message, "System Error", JOptionPane.ERROR_MESSAGE);
    }
}