package main.java.com.inventory.pages.Requisition;

import com.formdev.flatlaf.FlatClientProperties;
import main.java.com.inventory.controller.RequisitionController;
import main.java.com.inventory.controller.UserActivityController;
import main.java.com.inventory.model.Requisition;
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
        loadData(); // Load data saat panel pertama kali dibuka
    }

    private void initContent() {
        setLayout(new BorderLayout());
        setBackground(UIConfig.BG_LIGHT);

        // --- 1. HEADER & TOOLBAR ---
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(Color.WHITE);
        headerPanel.setPreferredSize(new Dimension(0, 70));
        headerPanel.setBorder(new MatteBorder(0, 0, 1, 0, new Color(226, 232, 240)));
        headerPanel.setBorder(BorderFactory.createCompoundBorder(
                headerPanel.getBorder(), new EmptyBorder(10, 20, 10, 20)));

        // Title Section
        JPanel titleSection = new JPanel(new GridLayout(2, 1));
        titleSection.setOpaque(false);
        JLabel title = new JLabel("Purchase Requisition");
        title.setFont(new Font("Segoe UI", Font.BOLD, 18));
        title.setForeground(UIConfig.TEXT_DARK);
        JLabel subTitle = new JLabel("Manage and track all procurement requests");
        subTitle.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        subTitle.setForeground(new Color(100, 116, 139));
        titleSection.add(title);
        titleSection.add(subTitle);

        // Action Buttons Section (LOKASI BUTTON: Pojok Kanan Atas)
        JPanel actionSection = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 5));
        actionSection.setOpaque(false);

        JButton btnCreate = createActionButton("CREATE NEW", UIConfig.PRIMARY);
        JButton btnManage = createActionButton("MANAGE ID", new Color(51, 65, 85));
        JButton btnRefresh = createActionButton("REFRESH", new Color(148, 163, 184));

        actionSection.add(btnRefresh);
        actionSection.add(btnManage);
        actionSection.add(btnCreate);

        headerPanel.add(titleSection, BorderLayout.WEST);
        headerPanel.add(actionSection, BorderLayout.EAST);

        // --- 2. TABLE AREA (LANGSUNG TAMPIL) ---
        String[] columns = { "ID", "ITEM NAME", "QTY", "TOTAL PRICE", "VENDOR", "SPECIFICATION", "REASON" };
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int r, int c) {
                return false;
            }
        };

        table = new JTable(tableModel);
        table.putClientProperty(FlatClientProperties.STYLE,
                "rowHeight: 35; showHorizontalLines: true; selectionBackground: #f1f5f9; intercellSpacing: 0,1;");
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(SwingConstants.CENTER);
        table.getColumnModel().getColumn(0).setCellRenderer(centerRenderer);
        table.getColumnModel().getColumn(2).setCellRenderer(centerRenderer);
        table.getColumnModel().getColumn(4).setCellRenderer(centerRenderer);
        table.getTableHeader().setFont(new Font("Segoe UI Semibold", Font.PLAIN, 12));
        table.getTableHeader().setBackground(new Color(248, 250, 252));

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(new EmptyBorder(20, 20, 20, 20));
        scrollPane.setBackground(UIConfig.BG_LIGHT);
        scrollPane.getViewport().setBackground(Color.WHITE);

        add(headerPanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);

        // Events
        btnCreate.addActionListener(e -> insertRequisition());
        btnManage.addActionListener(e -> manageRequisition());
        btnRefresh.addActionListener(e -> loadData());
    }

    private JButton createActionButton(String text, Color bg) {
        JButton btn = new JButton(text);
        btn.setPreferredSize(new Dimension(120, 36));
        btn.setBackground(bg);
        btn.setForeground(Color.WHITE);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 11));
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.putClientProperty(FlatClientProperties.STYLE, "arc: 6; borderWidth: 0; focusWidth: 0;");
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
                        "Rp " + String.format("%, d", r.getTotalPrice()),
                        r.getVendor(),
                        r.getSpecification(),
                        r.getJustification()
                });
            }
        } catch (Exception e) {
            showEnterpriseError("Gagal memuat data: " + e.getMessage());
        }
    }

    private void insertRequisition() {
        JTextField nameF = new JTextField();
        JTextField specF = new JTextField();
        JTextField qtyF = new JTextField();
        JTextField priceF = new JTextField();
        JTextField vendorF = new JTextField();
        JTextArea justifA = new JTextArea(3, 20);

        // --- WORKFLOW ENTER ---
        nameF.addActionListener(e -> specF.requestFocus());
        specF.addActionListener(e -> qtyF.requestFocus());
        qtyF.addActionListener(e -> priceF.requestFocus());
        priceF.addActionListener(e -> vendorF.requestFocus());
        vendorF.addActionListener(e -> justifA.requestFocus());

        // Khusus JTextArea, Enter biasanya buat baris baru.
        // Jika ingin Enter di Vendor langsung klik OK:
        vendorF.addActionListener(e -> {
            JButton ok = findOKButton((Container) SwingUtilities.getWindowAncestor(vendorF));
            if (ok != null)
                ok.doClick();
        });

        Object[] message = {
                "Item Name:", nameF, "Specification:", specF, "Quantity:", qtyF,
                "Unit Price:", priceF, "Vendor:", vendorF, "Justification:", new JScrollPane(justifA)
        };

        int option = JOptionPane.showConfirmDialog(this, message, "New Purchase Request",
                JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

        if (option == JOptionPane.OK_OPTION) {
            try {
                int qty = Integer.parseInt(qtyF.getText().trim());
                int uPrice = Integer.parseInt(priceF.getText().trim());
                int tPrice = qty * uPrice;

                Requisition req = new Requisition(
                        user.getId(), nameF.getText().trim(), specF.getText().trim(),
                        qty, uPrice, tPrice, vendorF.getText().trim(), justifA.getText().trim());

                controller.insertRequisition(user, req);
                activityController.insertLogActivity(user, "CREATE", "REQUISITION", 0, "Requested: " + req.getName());
                loadData();
                SwingUtilities.invokeLater(this::insertRequisition);
            } catch (Exception ex) {
                showEnterpriseError("Input Error: Check numbers and fields.");
            }
        }
    }

    private JButton findOKButton(Container container) {
        if (container == null)
            return null;
        for (Component c : container.getComponents()) {
            if (c instanceof JButton && ((JButton) c).getText().equalsIgnoreCase("OK")) {
                return (JButton) c;
            } else if (c instanceof Container) {
                JButton b = findOKButton((Container) c);
                if (b != null)
                    return b;
            }
        }
        return null;
    }

    private void manageRequisition() {
        String idStr = JOptionPane.showInputDialog(this, "Enter Requisition ID to manage:");
        if (idStr != null && !idStr.isEmpty()) {
            try {
                int id = Integer.parseInt(idStr);
                Requisition req = controller.getRequisitionById(user, id);

                if (req == null) {
                    showEnterpriseError("ID [" + id + "] tidak ditemukan.");
                    return;
                }

                String[] options = { "DELETE", "CANCEL" };
                int choice = JOptionPane.showOptionDialog(this,
                        "Manage " + req.getName() + " (Total: Rp " + req.getTotalPrice() + ")?",
                        "Management", JOptionPane.DEFAULT_OPTION, JOptionPane.WARNING_MESSAGE, null, options,
                        options[1]);

                if (choice == 0) {
                    controller.deleteRequisitionById(user, id);
                    activityController.insertLogActivity(user, "DELETE", "REQUISITION", id,
                            "Cancelled: " + req.getName());
                    loadData(); // REFRESH TABEL
                }
            } catch (Exception e) {
                showEnterpriseError("ID harus berupa angka.");
            }
        }
    }

    private void showEnterpriseError(String message) {
        JOptionPane.showMessageDialog(this, message, "System Error", JOptionPane.ERROR_MESSAGE);
    }
}