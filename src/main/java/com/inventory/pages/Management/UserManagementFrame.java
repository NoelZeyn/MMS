package com.inventory.pages.Management;

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
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.DefaultCellEditor;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
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
import com.inventory.controller.SubBidangController;
import com.inventory.controller.UserActivityController;
import com.inventory.controller.UserManagementController;
import com.inventory.model.SubBidang;
import com.inventory.model.User;
import com.inventory.pages.UI.UIConfig;
import com.inventory.security.PasswordHasher;
import com.inventory.service.SubBidangService;
import com.inventory.service.UserActivityService;
import com.inventory.service.UserManagementService;

public class UserManagementFrame extends JPanel {
    private final User currentUser;
    private final UserManagementController controller;
    private final UserActivityController activityController = new UserActivityController(new UserActivityService());
    private final SubBidangController subBidangController = new SubBidangController(new SubBidangService());

    private JTable table;
    private DefaultTableModel tableModel;

    private static final String[] ROLE_OPTIONS = { "USER", "ADMIN", "MANAGER", "ASMAN", "SUPERADMIN", "BUDGETER" };
    private static final String[] STATUS_OPTIONS = { "ACTIVE", "INACTIVE", "SUSPENDED", "PENDING" };

    public UserManagementFrame(User user) {
        if (!user.isAdmin() && !user.isManager()) {
            throw new SecurityException("Unauthorized Access");
        }
        this.currentUser = user;
        this.controller = new UserManagementController(new UserManagementService());
        initContent();
        loadUserData();
    }

    private void initContent() {
        setLayout(new BorderLayout());
        setBackground(UIConfig.BG_LIGHT);

        // --- 1. HEADER & TOOLBAR ---
        JPanel toolbar = new JPanel(new BorderLayout());
        toolbar.setBackground(Color.WHITE);
        toolbar.setPreferredSize(new Dimension(0, 75));
        toolbar.setBorder(BorderFactory.createCompoundBorder(
                new MatteBorder(0, 0, 1, 0, new Color(226, 232, 240)),
                new EmptyBorder(12, 25, 12, 25)));

        JPanel titlePanel = new JPanel(new GridLayout(2, 1));
        titlePanel.setOpaque(false);
        JLabel title = new JLabel("Identity & Access Management");
        title.setFont(new Font("Segoe UI", Font.BOLD, 20));
        title.setForeground(UIConfig.TEXT_DARK);
        JLabel sub = new JLabel("Administrative control for user credentials and system permissions");
        sub.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        sub.setForeground(new Color(100, 116, 139));
        titlePanel.add(title);
        titlePanel.add(sub);

        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 8));
        btnPanel.setOpaque(false);

        JButton btnAdd = createStyledButton("NEW USER", UIConfig.PRIMARY);
        JButton btnManage = createStyledButton("MANAGE ID", new Color(51, 65, 85));
        JButton btnRefresh = createStyledButton("REFRESH", new Color(148, 163, 184));

        btnPanel.add(btnRefresh);
        btnPanel.add(btnManage);
        btnPanel.add(btnAdd);

        toolbar.add(titlePanel, BorderLayout.WEST);
        toolbar.add(btnPanel, BorderLayout.EAST);

        // --- 2. TABLE AREA ---
        String[] columns = { "ID", "USER NAME", "NID", "ROLE", "STATUS", "SUB-BIDANG ID" };
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int r, int c) {
                return c != 0 && c != 5; // ID dan SubBidang ID read-only
            }

            @Override
            public Class<?> getColumnClass(int columnIndex) {
                return (columnIndex == 0 || columnIndex == 5) ? Integer.class : String.class;
            }
        };

        table = new JTable(tableModel);
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(SwingConstants.CENTER);
        table.getColumnModel().getColumn(0).setCellRenderer(centerRenderer);
        table.getColumnModel().getColumn(3).setCellRenderer(centerRenderer);
        table.getColumnModel().getColumn(4).setCellRenderer(centerRenderer);
        table.getColumnModel().getColumn(5).setCellRenderer(centerRenderer);
        setupTableStyle();

        JScrollPane scroll = new JScrollPane(table);
        scroll.setBorder(new EmptyBorder(20, 25, 10, 25));
        scroll.getViewport().setBackground(Color.WHITE);
        scroll.setBackground(UIConfig.BG_LIGHT);

        JLabel hint = new JLabel(" 💡 Double-click Username, NID, Role, or Status to quick-edit.");
        hint.setFont(new Font("Segoe UI", Font.ITALIC, 11));
        hint.setForeground(new Color(148, 163, 184));
        hint.setBorder(new EmptyBorder(0, 30, 15, 0));

        add(toolbar, BorderLayout.NORTH);
        add(scroll, BorderLayout.CENTER);
        add(hint, BorderLayout.SOUTH);

        // --- 3. LISTENERS ---
        btnAdd.addActionListener(e -> insertUser());
        btnManage.addActionListener(e -> manageDetailUser());
        btnRefresh.addActionListener(e -> loadUserData());
        tableModel.addTableModelListener(this::handleLiveUpdate);
    }

    private void setupTableStyle() {
        // Pengaturan Style FlatLaf
        table.putClientProperty(FlatClientProperties.STYLE,
                "rowHeight: 35; " +
                        "showHorizontalLines: true; " +
                        "intercellSpacing: 0,1; " +
                        "selectionBackground: #E2E8F0; " + // Warna biru keabuan muda saat dipilih
                        "selectionForeground: #0F172A; " + // Warna teks tetap gelap saat dipilih (PENTING)
                        "selectionInactiveBackground: #F1F5F9;" // Warna saat tabel kehilangan fokus
        );

        // Renderer untuk meratakan teks ke tengah (Opsional)
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(SwingConstants.CENTER);

        // Pastikan renderer juga menghormati warna seleksi
        table.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value,
                    boolean isSelected, boolean hasFocus, int row, int column) {

                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

                if (isSelected) {
                    c.setBackground(new Color(226, 232, 240)); // Latar saat diklik (abu muda)
                    c.setForeground(Color.BLACK); // Teks saat diklik (hitam)
                } else {
                    c.setBackground(Color.WHITE);
                    c.setForeground(UIConfig.TEXT_DARK);
                }
                return c;
            }
        });

        // Pasang editor dropdown untuk Role dan Status
        table.getColumnModel().getColumn(3).setCellEditor(new DefaultCellEditor(new JComboBox<>(ROLE_OPTIONS)));
        table.getColumnModel().getColumn(4).setCellEditor(new DefaultCellEditor(new JComboBox<>(STATUS_OPTIONS)));

        table.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 12));
    }

    private JButton createStyledButton(String text, Color bg) {
        JButton btn = new JButton(text);
        btn.setPreferredSize(new Dimension(125, 38));
        btn.setBackground(bg);
        btn.setForeground(Color.WHITE);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 11));
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.putClientProperty(FlatClientProperties.STYLE, "arc: 8; borderWidth: 0; focusWidth: 0;");
        return btn;
    }

    private void loadUserData() {
        tableModel.setRowCount(0);
        try {
            List<User> list = controller.getAllUsers(currentUser);
            for (User u : list) {
                tableModel.addRow(new Object[] {
                        u.getId(), u.getUsername(), u.getNID(), u.getRole(), u.getStatus(), u.getSubBidangId()
                });
            }
        } catch (Exception e) {
            showEnterpriseError("Sync Failed: " + e.getMessage());
        }
    }

    private void handleLiveUpdate(javax.swing.event.TableModelEvent e) {
        int row = e.getFirstRow();
        int col = e.getColumn();
        if (row < 0 || col < 0)
            return;

        try {
            int id = (int) tableModel.getValueAt(row, 0);
            User oldData = controller.getUserById(id);

            String newName = tableModel.getValueAt(row, 1).toString();
            String newNID = tableModel.getValueAt(row, 2).toString();
            String newRole = tableModel.getValueAt(row, 3).toString();
            String newStatus = tableModel.getValueAt(row, 4).toString();

            User updated = new User(id, newName, newNID, oldData.getPasswordHash(), newRole, newStatus,
                    oldData.getSubBidangId());
            controller.updateUser(currentUser, updated);

            activityController.insertLogActivity(currentUser, "UPDATE", "USER", id,
                    "Live edit modified record: " + newName);
        } catch (Exception ex) {
            showEnterpriseError("Update Rejected: " + ex.getMessage());
            SwingUtilities.invokeLater(this::loadUserData);
        }
    }

    private void insertUser() {
        JTextField nameF = new JTextField();
        JPasswordField passF = new JPasswordField();
        JTextField nidF = new JTextField();
        JComboBox<String> roleF = new JComboBox<>(ROLE_OPTIONS);
        JComboBox<SubBidang> subF = new JComboBox<>();

        // --- WORKFLOW ENTER ---
        nameF.addActionListener(e -> passF.requestFocus());
        passF.addActionListener(e -> nidF.requestFocus());
        nidF.addActionListener(e -> roleF.requestFocus());

        // Saat di JComboBox, tekan Enter untuk pindah ke ComboBox berikutnya atau
        // Simpan
        roleF.addActionListener(e -> subF.requestFocus());

        // Di field terakhir (subF), tekan Enter langsung Simpan
        subF.addActionListener(e -> {
            JButton ok = findOKButton((Container) SwingUtilities.getWindowAncestor(subF));
            if (ok != null)
                ok.doClick();
        });

        try {
            subBidangController.getAllSubBidangs().forEach(subF::addItem);
        } catch (Exception e) {
            showEnterpriseError("Failed to load sub-departments.");
        }

        Object[] fields = { "Username:", nameF, "Password:", passF, "NID:", nidF, "Role:", roleF, "Department:", subF };

        int opt = JOptionPane.showConfirmDialog(this, fields, "Register User (Fast Entry)",
                JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

        if (opt == JOptionPane.OK_OPTION) {
            try {
                String pass = new String(passF.getPassword());
                if (pass.length() < 8)
                    throw new Exception("Password min 8 chars.");

                SubBidang sb = (SubBidang) subF.getSelectedItem();
                User newUser = new User(nameF.getText(), nidF.getText(), PasswordHasher.hash(pass),
                        roleF.getSelectedItem().toString(), "ACTIVE", sb.getId());

                controller.insertUser(currentUser, newUser);
                loadUserData();
                // Looping otomatis agar bisa input banyak user sekaligus
                SwingUtilities.invokeLater(this::insertUser);
            } catch (Exception ex) {
                showEnterpriseError("Registration Failed: " + ex.getMessage());
            }
        }
    }

    private JButton findOKButton(Container container) {
        if (container == null)
            return null;
        for (Component c : container.getComponents()) {
            if (c instanceof JButton && ((JButton) c).getText().equalsIgnoreCase("OK")) {
                return (JButton) c;
            } else if (c instanceof Container container1) {
                JButton b = findOKButton(container1);
                if (b != null)
                    return b;
            }
        }
        return null;
    }

    private void manageDetailUser() {
        String input = JOptionPane.showInputDialog(this, "Enter Target User ID:");
        if (input == null || input.isEmpty())
            return;

        try {
            int id = Integer.parseInt(input);
            User target = controller.getUserById(id);
            if (target == null) {
                showEnterpriseError("User not found.");
                return;
            }

            JPanel info = new JPanel(new GridLayout(0, 1));
            info.add(new JLabel("Target: " + target.getUsername()));
            info.add(new JLabel("Current Role: " + target.getRole()));

            String[] opts = { "DELETE ACCOUNT", "RESET PASSWORD", "CANCEL" };
            int choice = JOptionPane.showOptionDialog(this, info, "Account Management - ID " + id,
                    JOptionPane.DEFAULT_OPTION, JOptionPane.WARNING_MESSAGE, null, opts, opts[2]);

            if (choice == 0) { // Delete
                int confirm = JOptionPane.showConfirmDialog(this, "Permanently delete " + target.getUsername() + "?",
                        "Security", JOptionPane.YES_NO_OPTION);
                if (confirm == JOptionPane.YES_OPTION) {
                    controller.deleteUserById(currentUser, id);
                    loadUserData();
                }
            } else if (choice == 1) { // Reset Password logic
                String newPass = JOptionPane.showInputDialog("Enter New Password:");
                if (newPass != null && newPass.length() >= 8) {
                    target.setPasswordHash(PasswordHasher.hash(newPass));
                    controller.updateUser(currentUser, target);
                    JOptionPane.showMessageDialog(this, "Password updated.");
                }
            }
        } catch (HeadlessException | NumberFormatException e) {
            showEnterpriseError("Invalid input ID.");
        }
    }

    private void showEnterpriseError(String message) {
        JOptionPane.showMessageDialog(this, message, "System Alert", JOptionPane.ERROR_MESSAGE);
    }
}