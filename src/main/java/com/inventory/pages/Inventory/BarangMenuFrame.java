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
        loadData(); // Langsung load data saat aplikasi dibuka
    }

    private void initContent() {
        setLayout(new BorderLayout());
        setBackground(UIConfig.BG_LIGHT);

        // --- 1. HEADER & TOOLBAR (LOKASI BUTTON) ---
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(Color.WHITE);
        headerPanel.setPreferredSize(new Dimension(0, 70));
        headerPanel.setBorder(new MatteBorder(0, 0, 1, 0, new Color(226, 232, 240)));
        headerPanel.setBorder(BorderFactory.createCompoundBorder(
                headerPanel.getBorder(), new EmptyBorder(10, 20, 10, 20)));

        // Title Section
        JPanel titleSection = new JPanel(new GridLayout(2, 1));
        titleSection.setOpaque(false);
        JLabel title = new JLabel("Stock Management");
        title.setFont(new Font("Segoe UI", Font.BOLD, 18));
        title.setForeground(UIConfig.TEXT_DARK);
        JLabel subTitle = new JLabel("Monitor and execute administrative inventory operations");
        subTitle.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        subTitle.setForeground(new Color(100, 116, 139));
        titleSection.add(title);
        titleSection.add(subTitle);

        // Action Buttons (Kanan Atas)
        JPanel actionSection = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 5));
        actionSection.setOpaque(false);

        JButton btnAdd = createActionButton("ADD RECORD", UIConfig.PRIMARY);
        JButton btnManage = createActionButton("MANAGE BY ID", new Color(51, 65, 85));
        JButton btnRefresh = createActionButton("REFRESH", new Color(148, 163, 184));

        actionSection.add(btnRefresh);
        actionSection.add(btnManage);
        actionSection.add(btnAdd);

        headerPanel.add(titleSection, BorderLayout.WEST);
        headerPanel.add(actionSection, BorderLayout.EAST);

        // --- 2. TABLE AREA (LANGSUNG TAMPIL) ---
        String[] columns = { "ID", "ITEM DESCRIPTION", "STOK QTY", "LOCATION" };
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int r, int c) {
                return c != 0;
            }
        };

        table = new JTable(tableModel);
        // Style Tabel
        table.putClientProperty(FlatClientProperties.STYLE,
                "rowHeight: 35; showHorizontalLines: true; selectionBackground: #f1f5f9; intercellSpacing: 0,1;");

        // Render tengah untuk ID dan Stok
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(SwingConstants.CENTER);
        table.getColumnModel().getColumn(0).setCellRenderer(centerRenderer);
        table.getColumnModel().getColumn(2).setCellRenderer(centerRenderer);

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(new EmptyBorder(20, 20, 10, 20));
        scrollPane.setBackground(UIConfig.BG_LIGHT);
        scrollPane.getViewport().setBackground(Color.WHITE);

        // Hint di bawah tabel
        JLabel hint = new JLabel(" 💡 Double-click any cell to live-edit data.");
        hint.setFont(new Font("Segoe UI", Font.ITALIC, 11));
        hint.setForeground(new Color(148, 163, 184));
        hint.setBorder(new EmptyBorder(0, 25, 10, 0));

        add(headerPanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
        add(hint, BorderLayout.SOUTH);

        // --- 3. EVENTS ---
        btnAdd.addActionListener(e -> insertBarang());
        btnManage.addActionListener(e -> manageDetailBarang());
        btnRefresh.addActionListener(e -> loadData());

        // Listener untuk Live Edit
        tableModel.addTableModelListener(e -> handleLiveEdit(e));
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

        // --- LOGIKA ENTER KEY (FOKUS PINDAH) ---
        nameF.addActionListener(e -> stockF.requestFocus()); // Enter di Name -> pindah ke Stock
        stockF.addActionListener(e -> locF.requestFocus()); // Enter di Stock -> pindah ke Location

        // Logika khusus di Location: Tekan Enter langsung klik "OK" pada Dialog
        locF.addActionListener(e -> {
            Window window = SwingUtilities.getWindowAncestor(locF);
            if (window instanceof JDialog dialog) {
                // Cari tombol OK secara otomatis
                JButton okButton = findOKButton(dialog);
                if (okButton != null)
                    okButton.doClick();
            }
        });

        Object[] message = {
                "Item Name:", nameF,
                "Initial Stock:", stockF,
                "Location:", locF
        };

        // Tampilkan Dialog
        int option = JOptionPane.showConfirmDialog(this, message, "Register New Item (Fast Entry)",
                JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

        if (option == JOptionPane.OK_OPTION) {
            try {
                // Validasi sederhana
                if (nameF.getText().isEmpty())
                    throw new Exception("Name cannot be empty");

                Barang b = new Barang(nameF.getText(), Integer.parseInt(stockF.getText()), locF.getText());
                controller.insertBarang(user, b);

                // Simpan log
                activityController.insertLogActivity(user, "CREATE", "BARANG", 0, "Added new item: " + b.getName());

                loadData(); // Refresh tabel

                // --- LOOPING OTOMATIS ---
                // Setelah klik OK, panggil lagi dirinya sendiri agar dialog muncul kembali
                SwingUtilities.invokeLater(this::insertBarang);

            } catch (NumberFormatException ex) {
                showEnterpriseError("Stock must be a number!");
            } catch (Exception ex) {
                showEnterpriseError("Input Error: " + ex.getMessage());
            }
        }
    }

    // Helper untuk mencari tombol OK di dalam JOptionPane
    private JButton findOKButton(Container container) {
        for (Component c : container.getComponents()) {
            switch (c) {
                case JButton b -> {
                    if (b.getText().equalsIgnoreCase("OK"))
                        return b;
                }
                case Container container1 -> {
                    JButton b = findOKButton(container1);
                    if (b != null)
                        return b;
                }
                default -> {
                }
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
            } catch (HeadlessException | NumberFormatException e) {
                showEnterpriseError("Format ID salah.");
            }
        }
    }

    private void showEnterpriseError(String message) {
        JOptionPane.showMessageDialog(this, message, "System Error", JOptionPane.ERROR_MESSAGE);
    }
}