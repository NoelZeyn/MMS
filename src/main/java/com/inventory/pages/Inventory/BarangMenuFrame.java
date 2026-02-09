package com.inventory.pages.Inventory;

import java.awt.*;
import java.util.List;
import javax.swing.*;
import javax.swing.border.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.*;

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
    private JTextField txtSearch;

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
        headerPanel.setPreferredSize(new Dimension(0, 100));
        headerPanel.setBorder(new MatteBorder(0, 0, 1, 0, new Color(226, 232, 240)));
        headerPanel.setBorder(BorderFactory.createCompoundBorder(
                headerPanel.getBorder(), new EmptyBorder(10, 25, 10, 25)));

        // Title Section
        JPanel titleSection = new JPanel(new GridLayout(2, 1));
        titleSection.setOpaque(false);
        JLabel title = new JLabel("Stock Management");
        title.setFont(new Font("Segoe UI", Font.BOLD, 22));
        title.setForeground(UIConfig.TEXT_DARK);
        JLabel subTitle = new JLabel("Monitor and execute administrative inventory operations");
        subTitle.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        subTitle.setForeground(new Color(100, 116, 139));
        titleSection.add(title);
        titleSection.add(subTitle);

        // Action & Search Section
        JPanel actionSection = new JPanel(new FlowLayout(FlowLayout.RIGHT, 12, 15));
        actionSection.setOpaque(false);

        // Modern Search Bar
        txtSearch = new JTextField();
        txtSearch.setPreferredSize(new Dimension(280, 38));
        txtSearch.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, "Search item name or location...");
        txtSearch.putClientProperty(FlatClientProperties.TEXT_FIELD_SHOW_CLEAR_BUTTON, true);
        txtSearch.putClientProperty(FlatClientProperties.STYLE, "arc: 12; focusWidth: 1; outlineColor: #cbd5e1;");
        
        // Real-time Search Listener
        txtSearch.getDocument().addDocumentListener(new DocumentListener() {
            public void insertUpdate(DocumentEvent e) { performSearch(); }
            public void removeUpdate(DocumentEvent e) { performSearch(); }
            public void changedUpdate(DocumentEvent e) { performSearch(); }
        });

        JButton btnAdd = createActionButton("ADD RECORD", UIConfig.PRIMARY);
        JButton btnRefresh = createActionButton("REFRESH", new Color(148, 163, 184));

        actionSection.add(new JLabel("🔍"));
        actionSection.add(txtSearch);
        actionSection.add(btnRefresh);
        actionSection.add(btnAdd);

        headerPanel.add(titleSection, BorderLayout.WEST);
        headerPanel.add(actionSection, BorderLayout.EAST);

        // --- 2. TABLE AREA ---
        String[] columns = { "ID", "ITEM DESCRIPTION", "STOK QTY", "LOCATION" };
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int r, int c) { return c != 0; }
        };

        table = new JTable(tableModel);
        styleTable();

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(new EmptyBorder(25, 25, 10, 25));
        scrollPane.setBackground(UIConfig.BG_LIGHT);
        scrollPane.getViewport().setBackground(Color.WHITE);

        JLabel hint = new JLabel(" 💡 Double-click any cell to live-edit data. Use the search bar for quick filtering.");
        hint.setFont(new Font("Segoe UI", Font.ITALIC, 11));
        hint.setForeground(new Color(148, 163, 184));
        hint.setBorder(new EmptyBorder(0, 30, 15, 0));

        add(headerPanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
        add(hint, BorderLayout.SOUTH);

        // --- 3. EVENTS ---
        btnAdd.addActionListener(e -> insertBarang());
        btnRefresh.addActionListener(e -> {
            txtSearch.setText("");
            loadData();
        });
        
        // Listener untuk Live Edit
        tableModel.addTableModelListener(this::handleLiveEdit);

        // Click event untuk mengelola item via ID (opsional jika ingin double click row)
        table.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                if (evt.getClickCount() == 2 && SwingUtilities.isRightMouseButton(evt)) {
                    manageDetailBarang();
                }
            }
        });
    }

    private void styleTable() {
        table.putClientProperty(FlatClientProperties.STYLE,
                "rowHeight: 40; " +
                "showHorizontalLines: true; " +
                "gridColor: #f1f5f9; " +
                "selectionBackground: #e2e8f0; " +
                "selectionForeground: #0f172a; " +
                "intercellSpacing: 0,0;");

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

                if (column == 0 || column == 2) {
                    setHorizontalAlignment(SwingConstants.CENTER);
                } else {
                    setHorizontalAlignment(SwingConstants.LEFT);
                }
                return this;
            }
        };

        for (int i = 0; i < table.getColumnCount(); i++) {
            table.getColumnModel().getColumn(i).setCellRenderer(customRenderer);
        }
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
                    b.getId(), b.getName(), b.getStok(), b.getLokasi().toUpperCase()
                });
            }
        } catch (Exception e) {
            showEnterpriseError("Gagal load data: " + e.getMessage());
        }
    }

    private void performSearch() {
        String query = txtSearch.getText().trim();
        // Gunakan SwingWorker jika data sangat besar agar UI tidak freeze
        try {
            List<Barang> list = controller.searchBarang(query);
            tableModel.setRowCount(0);
            for (Barang b : list) {
                tableModel.addRow(new Object[] {
                    b.getId(), b.getName(), b.getStok(), b.getLokasi().toUpperCase()
                });
            }
        } catch (Exception e) {
            System.err.println("Search error: " + e.getMessage());
        }
    }

    private void handleLiveEdit(javax.swing.event.TableModelEvent e) {
        int row = e.getFirstRow();
        int col = e.getColumn();
        if (col > 0 && row != -1) { // Jangan edit ID
            try {
                int id = (int) tableModel.getValueAt(row, 0);
                String name = tableModel.getValueAt(row, 1).toString();
                int stok = Integer.parseInt(tableModel.getValueAt(row, 2).toString());
                String lokasi = tableModel.getValueAt(row, 3).toString();

                Barang updated = new Barang(id, name, stok, lokasi);
                controller.updateBarang(user, updated);
                activityController.insertLogActivity(user, "UPDATE", "BARANG", id, "Live edit: " + name);
            } catch (Exception ex) {
                showEnterpriseError("Gagal update: " + ex.getMessage());
                loadData(); // Revert tampilan
            }
        }
    }

    private void insertBarang() {
        JTextField nameF = new JTextField();
        JTextField stockF = new JTextField();
        JTextField locF = new JTextField();

        Object[] message = { "Item Name:", nameF, "Initial Stock:", stockF, "Location:", locF };

        int option = JOptionPane.showConfirmDialog(this, message, "Register New Item",
                JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

        if (option == JOptionPane.OK_OPTION) {
            try {
                Barang b = new Barang(nameF.getText(), Integer.parseInt(stockF.getText()), locF.getText());
                controller.insertBarang(user, b);
                activityController.insertLogActivity(user, "CREATE", "BARANG", 0, "Added: " + b.getName());
                loadData();
            } catch (Exception ex) {
                showEnterpriseError("Input Error: " + ex.getMessage());
            }
        }
    }

    private void manageDetailBarang() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow == -1) return;

        int id = (int) tableModel.getValueAt(selectedRow, 0);
        String name = tableModel.getValueAt(selectedRow, 1).toString();

        String[] options = { "ADJUST STOCK", "DELETE", "CANCEL" };
        int choice = JOptionPane.showOptionDialog(this, "Manage " + name, "Administration",
                JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE, null, options, options[0]);

        if (choice == 1) { // Delete
            int confirm = JOptionPane.showConfirmDialog(this, "Delete " + name + "?", "Confirm", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                controller.deleteBarangById(id);
                activityController.insertLogActivity(user, "DELETE", "BARANG", id, "Deleted item: " + name);
                loadData();
            }
        } else if (choice == 0) { // Adjust
            String sub = JOptionPane.showInputDialog("Amount to subtract:");
            if (sub != null) {
                controller.kurangiStok(id, Integer.parseInt(sub));
                activityController.insertLogActivity(user, "UPDATE", "BARANG", id, "Reduced stock by " + sub);
                loadData();
            }
        }
    }

    private void showEnterpriseError(String message) {
        JOptionPane.showMessageDialog(this, message, "System Error", JOptionPane.ERROR_MESSAGE);
    }
}