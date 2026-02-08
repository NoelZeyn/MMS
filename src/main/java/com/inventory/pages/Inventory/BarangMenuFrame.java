package main.java.com.inventory.pages.Inventory;

import com.formdev.flatlaf.FlatClientProperties;
import main.java.com.inventory.controller.BarangController;
import main.java.com.inventory.controller.UserActivityController;
import main.java.com.inventory.model.Barang;
import main.java.com.inventory.model.User;
import main.java.com.inventory.pages.UI.UIConfig;
import main.java.com.inventory.service.BarangService;
import main.java.com.inventory.service.UserActivityService;

import javax.swing.*;
import javax.swing.border.MatteBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

// INGAT WOI ini JPanel bukan JFrame
public class BarangMenuFrame extends JPanel {
    private final User user;
    private final BarangController controller;
    private final UserActivityController activityController = new UserActivityController(new UserActivityService());

    public BarangMenuFrame(User user) {
        this.user = user;
        this.controller = new BarangController(new BarangService());
        initContent();
    }

    private void initContent() {
        // Layout utama
        setLayout(new BorderLayout());
        setBackground(UIConfig.BG_LIGHT);

        // --- 1. Header (Mirip Top Bar) ---
        JPanel topBar = new JPanel(new BorderLayout());
        topBar.setBackground(Color.WHITE);
        topBar.setPreferredSize(new Dimension(0, 60));
        topBar.setBorder(new MatteBorder(0, 0, 1, 0, new Color(220, 225, 230)));

        JLabel navTitle = new JLabel("  INVENTORY / STOCK CONTROL");
        navTitle.setFont(new Font("Segoe UI", Font.BOLD, 14));
        navTitle.setForeground(UIConfig.TEXT_DARK);
        topBar.add(navTitle, BorderLayout.WEST);
        add(topBar, BorderLayout.NORTH);

        // --- 2. Central Card Area ---
        JPanel centerPanel = new JPanel(new GridBagLayout());
        centerPanel.setOpaque(false);

        JPanel card = new JPanel(new GridBagLayout());
        card.setBackground(Color.WHITE);
        card.setPreferredSize(new Dimension(460, 500));
        card.putClientProperty(FlatClientProperties.STYLE, "arc: 12");

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 0;

        // Header Text
        JLabel mainTitle = new JLabel("STOCK MANAGEMENT", SwingConstants.LEFT);
        mainTitle.setFont(new Font("Segoe UI", Font.BOLD, 18));
        mainTitle.setForeground(UIConfig.TEXT_DARK);
        gbc.gridy = 0;
        gbc.insets = new Insets(25, 45, 5, 45);
        card.add(mainTitle, gbc);

        JLabel subTitle = new JLabel("Execute administrative inventory operations", SwingConstants.LEFT);
        subTitle.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        subTitle.setForeground(new Color(148, 163, 184));
        gbc.gridy = 1;
        gbc.insets = new Insets(0, 45, 30, 45);
        card.add(subTitle, gbc);

        // --- 3. Action Buttons ---
        gbc.insets = new Insets(8, 45, 8, 45);

        JButton addButton = createEnterpriseButton("ADD NEW RECORD", UIConfig.PRIMARY);
        gbc.gridy = 2;
        card.add(addButton, gbc);

        JButton viewButton = createEnterpriseButton("VIEW STOCK LEDGER", new Color(51, 65, 85));
        gbc.gridy = 3;
        card.add(viewButton, gbc);

        JButton detailButton = createEnterpriseButton("MANAGE DETAIL / DELETE", new Color(51, 65, 85));
        gbc.gridy = 4;
        card.add(detailButton, gbc);

        centerPanel.add(card);
        add(centerPanel, BorderLayout.CENTER);

        // Events
        addButton.addActionListener(e -> insertBarang());
        viewButton.addActionListener(e -> lihatBarang());
        detailButton.addActionListener(e -> manageDetailBarang());
    }

    private JButton createEnterpriseButton(String text, Color bg) {
        JButton btn = new JButton(text);
        btn.setPreferredSize(new Dimension(0, 48));
        btn.setBackground(bg);
        btn.setForeground(Color.WHITE);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setFocusPainted(false);
        btn.putClientProperty(FlatClientProperties.STYLE, "borderWidth: 0; focusWidth: 0; arc: 8");
        btn.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btn.setBackground(bg.darker());
            }

            public void mouseExited(java.awt.event.MouseEvent evt) {
                btn.setBackground(bg);
            }
        });
        return btn;
    }

    private void insertBarang() {
        JTextField nameF = new JTextField();
        JTextField stockF = new JTextField();
        JTextField locF = new JTextField();

        // Style inputs
        nameF.putClientProperty(FlatClientProperties.STYLE, "arc: 5");
        stockF.putClientProperty(FlatClientProperties.STYLE, "arc: 5");

        Object[] message = {
                "Item Name:", nameF,
                "Quantity In Stock:", stockF,
                "Warehouse Location:", locF
        };

        int option = JOptionPane.showConfirmDialog(this, message, "Register New Item", JOptionPane.OK_CANCEL_OPTION,
                JOptionPane.PLAIN_MESSAGE);
        if (option == JOptionPane.OK_OPTION) {
            try {
                Barang newBarang = new Barang(nameF.getText(), Integer.parseInt(stockF.getText()), locF.getText());
                controller.insertBarang(user, newBarang);
                List<Barang> all = controller.getAllBarang();
                int latestId = all.isEmpty() ? 0 : all.get(all.size() - 1).getId();

                activityController.insertLogActivity(user, "CREATE", "BARANG", latestId,
                        "New item added: " + newBarang.getName());

                JOptionPane.showMessageDialog(this, "Record successfully created.");
            } catch (Exception ex) {
                showEnterpriseError("Data Error: " + ex.getMessage());
            }
        }
    }

    private void lihatBarang() {
        List<Barang> list = controller.getAllBarang();
        String[] columns = { "ID", "ITEM DESCRIPTION", "QTY", "LOCATION" };

        // 1. Izinkan Cell Editable (Kecuali ID)
        DefaultTableModel model = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int r, int c) {
                return c != 0; // ID (kolom 0) tidak boleh diubah
            }

            @Override
            public Class<?> getColumnClass(int columnIndex) {
                if (columnIndex == 2)
                    return Integer.class; // Agar sorting & input angka benar
                return String.class;
            }
        };

        for (Barang b : list) {
            model.addRow(new Object[] { b.getId(), b.getName(), b.getStok(), b.getLokasi().toUpperCase() });
        }

        JTable table = new JTable(model);

        table.getModel().addTableModelListener(e -> {
            int row = e.getFirstRow();
            int col = e.getColumn();

            if (col != -1) {
                try {
                    int id = (int) model.getValueAt(row, 0);

                    Barang oldBarang = controller.getBarangById(id);
                    String oldNama = oldBarang.getName();
                    int oldStok = oldBarang.getStok();
                    String oldLokasi = oldBarang.getLokasi();

                    String newNama = model.getValueAt(row, 1).toString();
                    int newStok = Integer.parseInt(model.getValueAt(row, 2).toString());
                    String newLokasi = model.getValueAt(row, 3).toString();

                    Barang updatedBarang = new Barang(id, newNama, newStok, newLokasi);
                    controller.updateBarang(user, updatedBarang);

                    StringBuilder changes = new StringBuilder();
                    if (!oldNama.equals(newNama))
                        changes.append("Nama: ").append(oldNama).append(" → ").append(newNama).append("; ");
                    if (oldStok != newStok)
                        changes.append("Stok: ").append(oldStok).append(" → ").append(newStok).append("; ");
                    if (!oldLokasi.equals(newLokasi))
                        changes.append("Lokasi: ").append(oldLokasi).append(" → ").append(newLokasi).append("; ");
                    activityController.insertLogActivity(user, "UPDATE", "BARANG", id,
                            changes.toString().isEmpty() ? "No changes made via live edit."
                                    : "Live edit updated fields: " + changes.toString());

                    System.out.println("Sync Success: Record ID " + id + " updated.");
                } catch (Exception ex) {
                    showEnterpriseError("Update failed: Invalid data format.");
                    SwingUtilities.invokeLater(this::lihatBarang);
                }
            }
        });

        // 3. EXXCEL BRO
        table.putClientProperty(FlatClientProperties.STYLE,
                "showHorizontalLines: true; " +
                        "showVerticalLines: true; " + // Garis vertikal agar seperti Excel
                        "rowHeight: 32; " +
                        "selectionBackground: #f1f5f9; " +
                        "selectionForeground: #0f172a; " +
                        "intercellSpacing: 1,1"); // Jarak antar cell

        table.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 11));
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        table.getTableHeader().setReorderingAllowed(false);

        // Pengaturan lebar kolom
        table.getColumnModel().getColumn(0).setPreferredWidth(50);
        table.getColumnModel().getColumn(1).setPreferredWidth(250);
        table.getColumnModel().getColumn(2).setPreferredWidth(80);
        table.getColumnModel().getColumn(3).setPreferredWidth(150);

        JScrollPane scroll = new JScrollPane(table);
        scroll.setPreferredSize(new Dimension(650, 400));
        scroll.setBorder(new MatteBorder(1, 1, 1, 1, new Color(226, 232, 240)));

        // Tampilkan instruksi singkat di atas tabel
        JPanel container = new JPanel(new BorderLayout(0, 10));
        JLabel hint = new JLabel("💡 Double-click any cell to edit. Press ENTER to sync with database.");
        hint.setFont(new Font("Segoe UI", Font.ITALIC, 11));
        hint.setForeground(new Color(100, 116, 139));

        container.add(hint, BorderLayout.NORTH);
        container.add(scroll, BorderLayout.CENTER);

        JOptionPane.showMessageDialog(this, container, "Master Stock Ledger (Live Edit)", JOptionPane.PLAIN_MESSAGE);
    }

    private void manageDetailBarang() {
        // Dialog input ID yang lebih gaya enterprise
        JTextField idField = new JTextField();
        idField.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, "Enter System ID...");
        Object[] msg = { "SEARCH RECORD BY ID:", idField };

        int res = JOptionPane.showConfirmDialog(this, msg, "Data Retrieval", JOptionPane.OK_CANCEL_OPTION,
                JOptionPane.PLAIN_MESSAGE);

        if (res != JOptionPane.OK_OPTION || idField.getText().isEmpty())
            return;

        try {
            int id = Integer.parseInt(idField.getText());
            Barang barang = controller.getBarangById(id);

            if (barang == null) {
                showEnterpriseError("Record ID [" + id + "] not found in the global database.");
                return;
            }

            // Tampilan Detail yang lebih rapi
            JPanel infoPanel = new JPanel(new GridLayout(0, 1, 2, 2));
            infoPanel.add(new JLabel("ITEM IDENTITY: " + barang.getName().toUpperCase()));
            infoPanel.add(new JLabel("CURRENT STOCK: " + barang.getStok() + " UNITS"));
            infoPanel.add(new JLabel("WAREHOUSE: " + barang.getLokasi().toUpperCase()));
            infoPanel.setBorder(BorderFactory.createCompoundBorder(
                    new MatteBorder(0, 4, 0, 0, UIConfig.PRIMARY),
                    BorderFactory.createEmptyBorder(5, 15, 5, 0)));

            String[] options = { "ADJUST STOCK", "MODIFY DATA", "DELETE", "CLOSE" };
            int choice = JOptionPane.showOptionDialog(this,
                    infoPanel, "Record Administration - " + id,
                    JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE, null, options, options[0]);

            handleManageChoice(choice, barang);
        } catch (NumberFormatException e) {
            showEnterpriseError("Search rejected. ID must be a numeric value.");
        }
    }

    private void handleManageChoice(int choice, Barang barang) {
        switch (choice) {
            case 0 -> { // ADJUST STOCK (UI Terintegrasi)
                JTextField qField = new JTextField();
                qField.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, "Amount to subtract...");
                Object[] msg = { "SUBTRACT QUANTITY FROM " + barang.getName().toUpperCase(), qField };

                if (JOptionPane.showConfirmDialog(this, msg, "Stock Adjustment", JOptionPane.OK_CANCEL_OPTION,
                        JOptionPane.PLAIN_MESSAGE) == JOptionPane.OK_OPTION) {
                    try {
                        int amount = Integer.parseInt(qField.getText());
                        controller.kurangiStok(barang.getId(), amount);
                        activityController.insertLogActivity(user, "UPDATE", "BARANG", barang.getId(),
                                "Stock adjusted (-) by " + amount + " units for item: " + barang.getName());
                        JOptionPane.showMessageDialog(this, "Inventory Ledger Updated Successfully.");
                    } catch (Exception e) {
                        showEnterpriseError("Adjustment failed. Check input values.");
                    }
                }
            }
            case 1 -> { // MODIFY DATA (Form Terstruktur)
                // Simpan nilai lama untuk log
                String oldNama = barang.getName();
                int oldStok = barang.getStok();
                String oldLokasi = barang.getLokasi();

                JTextField n = new JTextField(oldNama);
                JTextField s = new JTextField(String.valueOf(oldStok));
                JTextField l = new JTextField(oldLokasi);

                n.putClientProperty(FlatClientProperties.STYLE, "arc: 5");
                s.putClientProperty(FlatClientProperties.STYLE, "arc: 5");
                l.putClientProperty(FlatClientProperties.STYLE, "arc: 5");

                Object[] updateMsg = {
                        "SYSTEM DESCRIPTION:", n,
                        "QUANTITY ON HAND:", s,
                        "STORAGE LOCATION:", l
                };

                int option = JOptionPane.showConfirmDialog(this, updateMsg, "System Data Modification",
                        JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

                if (option == JOptionPane.OK_OPTION) {
                    try {
                        String newNama = n.getText();
                        int newStok = Integer.parseInt(s.getText());
                        String newLokasi = l.getText();
                        barang.setName(newNama);
                        barang.setStok(newStok);
                        barang.setLokasi(newLokasi);
                        controller.updateBarang(user, barang);

                        StringBuilder changes = new StringBuilder();
                        if (!oldNama.equals(newNama))
                            changes.append("Nama: ").append(oldNama).append(" → ").append(newNama).append("; ");
                        if (oldStok != newStok)
                            changes.append("Stok: ").append(oldStok).append(" → ").append(newStok).append("; ");
                        if (!oldLokasi.equals(newLokasi))
                            changes.append("Lokasi: ").append(oldLokasi).append(" → ").append(newLokasi).append("; ");

                        // Catat aktivitas user
                        activityController.insertLogActivity(user, "UPDATE", "BARANG", barang.getId(),
                                changes.toString().isEmpty() ? "No changes made."
                                        : "Updated fields: " + changes.toString());

                        JOptionPane.showMessageDialog(this, "Master Record Synchronized.");
                    } catch (Exception e) {
                        showEnterpriseError("Update rejected by server.");
                    }
                }
            }

            case 2 -> { // DELETE (Warna Peringatan)
                int confirm = JOptionPane.showConfirmDialog(this,
                        "Are you sure you want to PERMANENTLY DELETE\n" + barang.getName().toUpperCase()
                                + "?\nThis action cannot be undone.",
                        "Security Confirmation", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);

                if (confirm == JOptionPane.YES_OPTION) {
                    controller.deleteBarangById(barang.getId());
                    activityController.insertLogActivity(user, "DELETE", "BARANG", barang.getId(),
                            "Item deleted: " + barang.getName());
                    JOptionPane.showMessageDialog(this, "Resource successfully purged from database.");
                }
            }
        }
    }

    // Helper untuk Error Enterprise
    private void showEnterpriseError(String message) {
        JLabel errLabel = new JLabel(message);
        errLabel.setForeground(new Color(220, 38, 38)); // Merah tajam
        errLabel.setFont(new Font("Segoe UI Semibold", Font.PLAIN, 12));
        JOptionPane.showMessageDialog(this, errLabel, "System Alert", JOptionPane.ERROR_MESSAGE);
    }
}