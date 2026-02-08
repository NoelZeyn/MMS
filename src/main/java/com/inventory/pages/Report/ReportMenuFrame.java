package main.java.com.inventory.pages.Report;

import com.formdev.flatlaf.FlatClientProperties;
import main.java.com.inventory.controller.BarangController;
import main.java.com.inventory.controller.UserActivityController;
import main.java.com.inventory.model.Barang;
import main.java.com.inventory.model.User;
import main.java.com.inventory.model.UserActivity;
import main.java.com.inventory.pages.UI.UIConfig;
import main.java.com.inventory.service.BarangService;
import main.java.com.inventory.service.UserActivityService;

import javax.swing.*;
import javax.swing.border.MatteBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

// INGAT WOI ini JPanel bukan JFrame
public class ReportMenuFrame extends JPanel {
    private final User user;
    private final BarangController controller;
    private final UserActivityController userActivityController;

    public ReportMenuFrame(User user) {
        this.user = user;
        this.controller = new BarangController(new BarangService());
        this.userActivityController = new UserActivityController(new UserActivityService());
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

        JLabel navTitle = new JLabel("  REPORTS / LOG CONTROL");
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
        JLabel mainTitle = new JLabel("REPORT MANAGEMENT", SwingConstants.LEFT);
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

        JButton viewButton = createEnterpriseButton("VIEW STOCK LEDGER", new Color(51, 65, 85));
        gbc.gridy = 2;
        card.add(viewButton, gbc);

        JButton userActivityButton = createEnterpriseButton("VIEW USER ACTIVITY LOG", new Color(51, 65, 85));
        gbc.gridy = 3;
        card.add(userActivityButton, gbc);
        centerPanel.add(card);
        add(centerPanel, BorderLayout.CENTER);
        viewButton.addActionListener(e -> lihatBarang());
        userActivityButton.addActionListener(e -> lihatUserActivity());
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

    private void lihatBarang() {
        List<Barang> list = controller.getAllBarang();
        String[] columns = { "ID", "ITEM DESCRIPTION", "QTY", "LOCATION" };
        DefaultTableModel model = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int r, int c) {
                return false;
            }
        };

        for (Barang b : list) {
            model.addRow(new Object[] { b.getId(), b.getName(), b.getStok(), b.getLokasi().toUpperCase() });
        }

        JTable table = new JTable(model);
        
        table.putClientProperty(FlatClientProperties.STYLE,
                "showHorizontalLines: true; showVerticalLines: false; rowHeight: 32; selectionBackground: #f1f5f9; selectionForeground: #0f172a");

        JScrollPane scroll = new JScrollPane(table);
        scroll.setPreferredSize(new Dimension(600, 350));

        // --- TAMBAHAN: PANEL BUTTON EXPORT ---
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton btnExport = new JButton("DOWNLOAD EXCEL (.CSV)");
        btnExport.setBackground(new Color(34, 197, 94)); // Warna Hijau Excel
        btnExport.setForeground(Color.WHITE);
        btnExport.setFont(new Font("Segoe UI", Font.BOLD, 11));
        btnExport.setCursor(new Cursor(Cursor.HAND_CURSOR));

        btnExport.addActionListener(e -> exportToCSV(table));
        bottomPanel.add(btnExport);

        // Container untuk menyatukan tabel dan tombol
        JPanel mainContent = new JPanel(new BorderLayout());
        mainContent.add(scroll, BorderLayout.CENTER);
        mainContent.add(bottomPanel, BorderLayout.SOUTH);

        JOptionPane.showMessageDialog(this, mainContent, "Master Stock Ledger", JOptionPane.PLAIN_MESSAGE);
    }

    private void lihatUserActivity() {
        List<UserActivity> list = userActivityController.getAllUserActivities(user);
        String[] columns = { "ID", "USER", "ACTION", "TARGET", "RECORD ID", "DESCRIPTION", "TIMESTAMP" };

        DefaultTableModel model = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int col) {
                return false; // Semua cell read-only
            }
        };

        for (UserActivity ua : list) {
            model.addRow(new Object[] {
                    ua.getId(),
                    ua.getUserId(),
                    ua.getActivity(),
                    ua.getTargetTable(),
                    ua.getTargetId(),
                    ua.getDescription(),
                    ua.getTimestamp()
            });
        }

        JTable table = new JTable(model);

        // Styling tabel agar lebih enterprise / excel-like
        table.putClientProperty(FlatClientProperties.STYLE,
                "showHorizontalLines: true; showVerticalLines: false; rowHeight: 32; " +
                        "selectionBackground: #f1f5f9; selectionForeground: #0f172a");
        table.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 11));
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        table.getTableHeader().setReorderingAllowed(false);

        // Scroll pane
        JScrollPane scroll = new JScrollPane(table);
        scroll.setPreferredSize(new Dimension(700, 350));
        scroll.setBorder(new MatteBorder(1, 1, 1, 1, new Color(226, 232, 240)));

        // --- Tombol export CSV ---
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton btnExport = new JButton("DOWNLOAD EXCEL (.CSV)");
        btnExport.setBackground(new Color(34, 197, 94));
        btnExport.setForeground(Color.WHITE);
        btnExport.setFont(new Font("Segoe UI", Font.BOLD, 11));
        btnExport.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnExport.addActionListener(e -> exportToCSV(table));
        bottomPanel.add(btnExport);

        // Container untuk scroll + tombol
        JPanel mainContent = new JPanel(new BorderLayout());
        mainContent.add(scroll, BorderLayout.CENTER);
        mainContent.add(bottomPanel, BorderLayout.SOUTH);

        // Tampilkan dialog
        JOptionPane.showMessageDialog(this, mainContent, "User Activity Log", JOptionPane.PLAIN_MESSAGE);
    }

    private void exportToCSV(JTable table) {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Specify a file to save");

        // Nama file default
        fileChooser.setSelectedFile(new java.io.File("Report.csv"));

        int userSelection = fileChooser.showSaveDialog(this);

        if (userSelection == JFileChooser.APPROVE_OPTION) {
            java.io.File fileToSave = fileChooser.getSelectedFile();

            try (java.io.FileWriter fw = new java.io.FileWriter(fileToSave)) {
                DefaultTableModel model = (DefaultTableModel) table.getModel();

                // 1. Tulis Header
                for (int i = 0; i < model.getColumnCount(); i++) {
                    fw.write(model.getColumnName(i) + (i == model.getColumnCount() - 1 ? "" : ","));
                }
                fw.write("\n");

                // 2. Tulis Data
                for (int i = 0; i < model.getRowCount(); i++) {
                    for (int j = 0; j < model.getColumnCount(); j++) {
                        fw.write(model.getValueAt(i, j).toString() + (j == model.getColumnCount() - 1 ? "" : ","));
                    }
                    fw.write("\n");
                }

                JOptionPane.showMessageDialog(this, "Excel report generated successfully!", "Success",
                        JOptionPane.INFORMATION_MESSAGE);

            } catch (java.io.IOException ex) {
                JOptionPane.showMessageDialog(this, "Error writing to file: " + ex.getMessage(), "Export Error",
                        JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}