package com.inventory.pages.Inventory;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.HeadlessException;
import java.awt.Window;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.border.EmptyBorder;
import javax.swing.border.MatteBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

import com.formdev.flatlaf.FlatClientProperties;
import com.inventory.controller.BarangController;
import com.inventory.controller.UserActivityController;
import com.inventory.model.Barang;
import com.inventory.model.User;
import com.inventory.pages.UI.UIConfig;
import com.inventory.service.BarangService;
import com.inventory.service.UserActivityService;

public class BarangMenuFrame extends JPanel {
    private final User user;
    private final BarangController controller;
    private final UserActivityController activityController = new UserActivityController(new UserActivityService());

    private JTable table;
    private DefaultTableModel tableModel;

    public BarangMenuFrame(User user) {
        this.user = user;
        this.controller = new BarangController(new BarangService());
        initContent();
        loadData(); 
    }

    private void initContent() {
        setLayout(new BorderLayout());
        setBackground(UIConfig.BG_LIGHT);

        // --- 1. HEADER SECTION ---
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(Color.WHITE);
        headerPanel.setPreferredSize(new Dimension(0, 75));
        headerPanel.setBorder(new MatteBorder(0, 0, 1, 0, new Color(226, 232, 240)));
        headerPanel.setBorder(BorderFactory.createCompoundBorder(
                headerPanel.getBorder(), new EmptyBorder(10, 25, 10, 25)));

        JPanel titleSection = new JPanel(new GridLayout(2, 1));
        titleSection.setOpaque(false);
        JLabel title = new JLabel("Stock Management");
        title.setFont(new Font("Segoe UI", Font.BOLD, 20));
        title.setForeground(UIConfig.TEXT_DARK);
        JLabel subTitle = new JLabel("Monitor and execute administrative inventory operations");
        subTitle.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        subTitle.setForeground(new Color(100, 116, 139));
        titleSection.add(title);
        titleSection.add(subTitle);

        JPanel actionSection = new JPanel(new FlowLayout(FlowLayout.RIGHT, 12, 8));
        actionSection.setOpaque(false);

        JButton btnAdd = createActionButton("ADD RECORD", UIConfig.PRIMARY);
        JButton btnManage = createActionButton("MANAGE BY ID", new Color(51, 65, 85));
        JButton btnRefresh = createActionButton("REFRESH", new Color(148, 163, 184));

        actionSection.add(btnRefresh);
        actionSection.add(btnManage);
        actionSection.add(btnAdd);

        headerPanel.add(titleSection, BorderLayout.WEST);
        headerPanel.add(actionSection, BorderLayout.EAST);

        // --- 2. TABLE AREA ---
        String[] columns = { "ID", "ITEM DESCRIPTION", "STOK QTY", "LOCATION" };
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int r, int c) {
                return c != 0;
            }
        };

        table = new JTable(tableModel);
        
        // Style Tabel yang diperbaiki kontrasnya
        table.putClientProperty(FlatClientProperties.STYLE,
                "rowHeight: 40; " +
                "showHorizontalLines: true; " +
                "gridColor: #f1f5f9; " +
                "selectionBackground: #e2e8f0; " + // Slate 200 (lebih terlihat)
                "selectionForeground: #0f172a; " + // Teks Biru Gelap (kontras tinggi)
                "intercellSpacing: 0,0;");

        // Custom Renderer untuk menangani pewarnaan saat di-klik
        DefaultTableCellRenderer customRenderer = new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value,
                    boolean isSelected, boolean hasFocus, int row, int column) {
                
                super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                setBorder(new EmptyBorder(0, 15, 0, 15));

                if (isSelected) {
                    setBackground(new Color(226, 232, 240));
                    setForeground(new Color(15, 23, 42));
                } else {
                    setBackground(Color.WHITE);
                    setForeground(new Color(51, 65, 85));
                }

                // Alignment berdasarkan kolom
                if (column == 0 || column == 2) {
                    setHorizontalAlignment(SwingConstants.CENTER);
                } else {
                    setHorizontalAlignment(SwingConstants.LEFT);
                }
                
                return this;
            }
        };

        // Terapkan ke semua kolom
        for (int i = 0; i < table.getColumnCount(); i++) {
            table.getColumnModel().getColumn(i).setCellRenderer(customRenderer);
        }

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(new EmptyBorder(25, 25, 10, 25));
        scrollPane.setBackground(UIConfig.BG_LIGHT);
        scrollPane.getViewport().setBackground(Color.WHITE);

        JLabel hint = new JLabel(" 💡 Double-click any cell to live-edit data.");
        hint.setFont(new Font("Segoe UI", Font.ITALIC, 11));
        hint.setForeground(new Color(148, 163, 184));
        hint.setBorder(new EmptyBorder(0, 30, 15, 0));

        add(headerPanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
        add(hint, BorderLayout.SOUTH);

        // --- 3. EVENTS ---
        btnAdd.addActionListener(e -> insertBarang());
        btnManage.addActionListener(e -> manageDetailBarang());
        btnRefresh.addActionListener(e -> loadData());
        tableModel.addTableModelListener(this::handleLiveEdit);
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
            List<Barang> list = controller.getAllBarang();
            for (Barang b : list) {
                tableModel.addRow(new Object[] {
                        b.getId(),
                        b.getName(),
                        b.getStok(),
                        b.getLokasi().toUpperCase()
                });
            }
        } catch (Exception e) {
            showEnterpriseError("Gagal load data: " + e.getMessage());
        }
    }

    private void handleLiveEdit(javax.swing.event.TableModelEvent e) {
        int row = e.getFirstRow();
        int col = e.getColumn();
        if (col != -1) {
            try {
                int id = (int) tableModel.getValueAt(row, 0);
                String name = tableModel.getValueAt(row, 1).toString();
                int stok = Integer.parseInt(tableModel.getValueAt(row, 2).toString());
                String lokasi = tableModel.getValueAt(row, 3).toString();

                Barang updated = new Barang(id, name, stok, lokasi);
                controller.updateBarang(user, updated);
                activityController.insertLogActivity(user, "UPDATE", "BARANG", id, "Live edit on " + name);
            } catch (NumberFormatException ex) {
                showEnterpriseError("Gagal update: Format data salah.");
                loadData();
            }
        }
    }

    private void insertBarang() {
        JTextField nameF = new JTextField();
        JTextField stockF = new JTextField();
        JTextField locF = new JTextField();

        nameF.addActionListener(e -> stockF.requestFocus());
        stockF.addActionListener(e -> locF.requestFocus());

        locF.addActionListener(e -> {
            Window window = SwingUtilities.getWindowAncestor(locF);
            if (window instanceof JDialog dialog) {
                JButton okButton = findOKButton(dialog);
                if (okButton != null) okButton.doClick();
            }
        });

        Object[] message = {
                "Item Name:", nameF,
                "Initial Stock:", stockF,
                "Location:", locF
        };

        int option = JOptionPane.showConfirmDialog(this, message, "Register New Item",
                JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

        if (option == JOptionPane.OK_OPTION) {
            try {
                if (nameF.getText().isEmpty()) throw new Exception("Name cannot be empty");
                Barang b = new Barang(nameF.getText(), Integer.parseInt(stockF.getText()), locF.getText());
                controller.insertBarang(user, b);
                activityController.insertLogActivity(user, "CREATE", "BARANG", 0, "Added new item: " + b.getName());
                loadData();
                SwingUtilities.invokeLater(this::insertBarang);
            } catch (Exception ex) {
                showEnterpriseError("Input Error: " + ex.getMessage());
            }
        }
    }

    private JButton findOKButton(Container container) {
        for (Component c : container.getComponents()) {
            if (c instanceof JButton b && b.getText().equalsIgnoreCase("OK")) return b;
            if (c instanceof Container c1) {
                JButton b = findOKButton(c1);
                if (b != null) return b;
            }
        }
        return null;
    }

    private void manageDetailBarang() {
        String idStr = JOptionPane.showInputDialog(this, "Enter Item ID to Manage:");
        if (idStr != null && !idStr.isEmpty()) {
            try {
                int id = Integer.parseInt(idStr);
                Barang b = controller.getBarangById(id);
                if (b == null) {
                    showEnterpriseError("ID tidak ditemukan.");
                    return;
                }

                String[] options = { "ADJUST STOCK", "DELETE", "CANCEL" };
                int choice = JOptionPane.showOptionDialog(this, "Manage " + b.getName(), "Administration",
                        JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE, null, options, options[0]);

                if (choice == 1) { // Delete
                    controller.deleteBarangById(id);
                    loadData();
                } else if (choice == 0) { // Adjust Stock
                    String sub = JOptionPane.showInputDialog("Amount to subtract:");
                    if (sub != null) {
                        controller.kurangiStok(id, Integer.parseInt(sub));
                        loadData();
                    }
                }
            } catch (Exception e) {
                showEnterpriseError("Format ID atau Data salah.");
            }
        }
    }

    private void showEnterpriseError(String message) {
        JOptionPane.showMessageDialog(this, message, "System Error", JOptionPane.ERROR_MESSAGE);
    }
}