package main.java.com.inventory.pages;

import com.formdev.flatlaf.FlatClientProperties;

import main.java.com.inventory.controller.SubBidangController;
import main.java.com.inventory.controller.UserActivityController;
import main.java.com.inventory.controller.UserManagementController;
import main.java.com.inventory.model.SubBidang;
import main.java.com.inventory.model.User;
import main.java.com.inventory.security.PasswordHasher;
import main.java.com.inventory.service.SubBidangService;
import main.java.com.inventory.service.UserActivityService;
import main.java.com.inventory.service.UserManagementService;

import javax.swing.*;
import javax.swing.border.MatteBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

// INGAT WOI ini JPanel bukan JFrame
public class UserManagementFrame extends JPanel {
    private final User user;
    private final UserManagementController controller;
    private final UserActivityController activityController = new UserActivityController(new UserActivityService());
    private final SubBidangController subBidangController = new SubBidangController(new SubBidangService());
    private static final String[] ROLE_OPTIONS = {
            "ADMIN", "MANAGER", "USER"
    };

    private static final String[] STATUS_OPTIONS = {
            "ACTIVE", "INACTIVE", "SUSPENDED", "PENDING"
    };

    public UserManagementFrame(User user) {
        if (!user.isAdmin() && !user.isManager()) {
            throw new SecurityException("Unauthorized Access");
        }
        this.user = user;
        this.controller = new UserManagementController(new UserManagementService());
        initContent();
    }

    private void initContent() {
        setLayout(new BorderLayout());
        setBackground(UIConfig.BG_LIGHT);

        // --- 1. Header (Mirip Top Bar) ---
        JPanel topBar = new JPanel(new BorderLayout());
        topBar.setBackground(Color.WHITE);
        topBar.setPreferredSize(new Dimension(0, 60));
        topBar.setBorder(new MatteBorder(0, 0, 1, 0, new Color(220, 225, 230)));

        JLabel navTitle = new JLabel("  USER / MANAGEMENT CONTROL");
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
        JLabel mainTitle = new JLabel("USER MANAGEMENT", SwingConstants.LEFT);
        mainTitle.setFont(new Font("Segoe UI", Font.BOLD, 18));
        mainTitle.setForeground(UIConfig.TEXT_DARK);
        gbc.gridy = 0;
        gbc.insets = new Insets(25, 45, 5, 45);
        card.add(mainTitle, gbc);

        JLabel subTitle = new JLabel("Execute administrative user operations", SwingConstants.LEFT);
        subTitle.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        subTitle.setForeground(new Color(148, 163, 184));
        gbc.gridy = 1;
        gbc.insets = new Insets(0, 45, 30, 45);
        card.add(subTitle, gbc);

        // --- 3. Action Buttons ---
        gbc.insets = new Insets(8, 45, 8, 45);

        JButton addButton = createEnterpriseButton("ADD NEW USER", UIConfig.PRIMARY);
        gbc.gridy = 2;
        card.add(addButton, gbc);

        JButton viewButton = createEnterpriseButton("VIEW USER", new Color(51, 65, 85));
        gbc.gridy = 3;
        card.add(viewButton, gbc);

        JButton detailButton = createEnterpriseButton("MANAGE DETAIL / DELETE", new Color(51, 65, 85));
        gbc.gridy = 4;
        card.add(detailButton, gbc);

        centerPanel.add(card);
        add(centerPanel, BorderLayout.CENTER);

        // Events
        addButton.addActionListener(e -> insertUser());
        viewButton.addActionListener(e -> lihatUser());
        detailButton.addActionListener(e -> manageDetailUser());
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

    private void insertUser() {
        JTextField nameF = new JTextField();
        JPasswordField passwordF = new JPasswordField();
        JTextField NIDF = new JTextField();
        JComboBox<String> roleF = new JComboBox<>(new String[] { "ADMIN", "MANAGER", "USER" });
        JComboBox<String> statusF = new JComboBox<>(new String[] { "ACTIVE", "INACTIVE", "SUSPENDED", "PENDING" });
        JComboBox<SubBidang> subBidangComboBox = new JComboBox<>();
        try {
            List<SubBidang> listSub = subBidangController.getAllSubBidangs();
            for (SubBidang sb : listSub) {
                subBidangComboBox.addItem(sb);
            }
        } catch (Exception e) {
            System.err.println("Gagal memuat subbidang: " + e.getMessage());
        }

        nameF.putClientProperty(FlatClientProperties.STYLE, "arc: 5");
        passwordF.putClientProperty(FlatClientProperties.STYLE, "arc: 5");
        NIDF.putClientProperty(FlatClientProperties.STYLE, "arc: 5");
        roleF.putClientProperty(FlatClientProperties.STYLE, "arc: 5");
        statusF.putClientProperty(FlatClientProperties.STYLE, "arc: 5");
        subBidangComboBox.putClientProperty(FlatClientProperties.STYLE, "arc: 5");

        Object[] message = {
                "User Name:", nameF,
                "Password:", passwordF,
                "NID:", NIDF,
                "Role:", roleF,
                "Status:", statusF,
                "Sub Bidang:", subBidangComboBox,
        };

        int option = JOptionPane.showConfirmDialog(this, message, "Register New User", JOptionPane.OK_CANCEL_OPTION,
                JOptionPane.PLAIN_MESSAGE);

        if (option == JOptionPane.OK_OPTION) {
            try {
                // Ambil input
                String name = nameF.getText().trim();
                String password = new String(passwordF.getPassword()).trim();
                String nid = NIDF.getText().trim();
                String role = roleF.getSelectedItem().toString().toUpperCase();
                String status = statusF.getSelectedItem().toString().toUpperCase();
                SubBidang selectedSub = (SubBidang) subBidangComboBox.getSelectedItem();

                // --- VALIDASI INPUT ---
                if (name.isEmpty())
                    throw new IllegalArgumentException("User name tidak boleh kosong");
                if (password.isEmpty() || password.length() < 8)
                    throw new IllegalArgumentException("Password minimal 8 karakter");
                if (nid.isEmpty())
                    throw new IllegalArgumentException("NID tidak boleh kosong");
                if (!(role.equals("ADMIN") || role.equals("MANAGER") || role.equals("USER")))
                    throw new IllegalArgumentException("Role tidak valid");
                if (!(status.equals("ACTIVE") || status.equals("INACTIVE") || status.equals("SUSPENDED")
                        || status.equals("PENDING")))
                    throw new IllegalArgumentException("Status tidak valid");
                if (selectedSub == null) {
                    throw new IllegalArgumentException("Sub Bidang wajib dipilih");
                }

                String hashedPassword = PasswordHasher.hash(password);
                selectedSub = (SubBidang) subBidangComboBox.getSelectedItem();
                int selectedId = (selectedSub != null) ? selectedSub.getId() : 0;
                User newUser = new User(name, nid, hashedPassword, role, status, selectedId);

                controller.insertUser(user, newUser);

                List<User> all = controller.getAllUsers(user);
                int latestId = all.isEmpty() ? 0 : all.get(all.size() - 1).getId();

                // Simpan log aktivitas
                activityController.insertLogActivity(user, "CREATE", "USER", latestId,
                        "New user added: " + newUser.getUsername() + " with role " + newUser.getRole());

                JOptionPane.showMessageDialog(this, "Record successfully created.");

            } catch (IllegalArgumentException ex) {
                showEnterpriseError("Validation Error: " + ex.getMessage());
            } catch (Exception ex) {
                ex.printStackTrace();
                showEnterpriseError("Data Error: " + ex.getMessage());
            }
        }
    }

    private void lihatUser() {

        List<User> list = controller.getAllUsers(user);
        String[] columns = { "ID", "USER NAME", "NID", "ROLE", "STATUS", "SUB BIDANG ID" };

        DefaultTableModel model = new DefaultTableModel(columns, 0) {

            @Override
            public boolean isCellEditable(int r, int c) {
                return c != 0 && c != 5; // ID & SubBidangID TIDAK editable
            }

            @Override
            public Class<?> getColumnClass(int columnIndex) {
                if (columnIndex == 0 || columnIndex == 5)
                    return Integer.class;
                return String.class;
            }
        };

        for (User u : list) {
            model.addRow(new Object[] {
                    u.getId(),
                    u.getUsername(),
                    u.getNID(),
                    u.getRole(),
                    u.getStatus(),
                    u.getSubBidangId()
            });
        }

        JTable table = new JTable(model);

        /*
         * ===============================
         * ROLE → COMBOBOX
         * ===============================
         */
        JComboBox<String> roleCombo = new JComboBox<>(ROLE_OPTIONS);
        table.getColumnModel()
                .getColumn(3)
                .setCellEditor(new DefaultCellEditor(roleCombo));
        JComboBox<String> statusCombo = new JComboBox<>(STATUS_OPTIONS);
        table.getColumnModel()
                .getColumn(4)
                .setCellEditor(new DefaultCellEditor(statusCombo));

        /*
         * ===============================
         * LIVE UPDATE LISTENER
         * ===============================
         */
        table.getModel().addTableModelListener(e -> {

            int row = e.getFirstRow();
            int col = e.getColumn();

            if (row < 0 || col < 0)
                return;

            try {
                int id = (int) model.getValueAt(row, 0);
                int subBidangId = (int) model.getValueAt(row, 5);

                User oldUser = controller.getUserById(id);

                String newUsername = model.getValueAt(row, 1).toString();
                String newNID = model.getValueAt(row, 2).toString();
                String newRole = model.getValueAt(row, 3).toString();
                String newStatus = model.getValueAt(row, 4).toString();

                User updatedUser = new User(
                        id,
                        newUsername,
                        newNID,
                        oldUser.getPasswordHash(),
                        newRole,
                        newStatus,
                        subBidangId);

                controller.updateUser(user, updatedUser);

                StringBuilder changes = new StringBuilder();

                if (!oldUser.getUsername().equals(newUsername))
                    changes.append("Username: ")
                            .append(oldUser.getUsername())
                            .append(" → ")
                            .append(newUsername)
                            .append("; ");

                if (!oldUser.getNID().equals(newNID))
                    changes.append("NID: ")
                            .append(oldUser.getNID())
                            .append(" → ")
                            .append(newNID)
                            .append("; ");

                if (!oldUser.getRole().equals(newRole))
                    changes.append("Role: ")
                            .append(oldUser.getRole())
                            .append(" → ")
                            .append(newRole)
                            .append("; ");

                if (!oldUser.getStatus().equals(newStatus))
                    changes.append("Status: ")
                            .append(oldUser.getStatus())
                            .append(" → ")
                            .append(newStatus)
                            .append("; ");

                activityController.insertLogActivity(
                        user,
                        "UPDATE",
                        "USER",
                        id,
                        changes.length() == 0
                                ? "No changes made via live edit."
                                : "Live edit updated fields: " + changes);

                System.out.println("Sync Success: User ID " + id + " updated.");

            } catch (Exception ex) {
                showEnterpriseError("Update failed: Invalid data format.");
                SwingUtilities.invokeLater(this::lihatUser);
            }
        });

        // 3. EXXCEL BRO
        table.putClientProperty(FlatClientProperties.STYLE,
                "showHorizontalLines: true; " +
                        "showVerticalLines: true; " +
                        "rowHeight: 32; " +
                        "selectionBackground: #f1f5f9; " +
                        "selectionForeground: #0f172a; " +
                        "intercellSpacing: 1,1"); // Jarak antar cell

        table.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 11));
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        table.getTableHeader().setReorderingAllowed(false);

        // Pengaturan lebar kolom
        table.getColumnModel().getColumn(0).setPreferredWidth(50);
        table.getColumnModel().getColumn(1).setPreferredWidth(150);
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

    private void manageDetailUser() {
        JTextField idField = new JTextField();
        idField.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, "Enter USER ID...");
        Object[] msg = { "SEARCH USER BY ID:", idField };

        int res = JOptionPane.showConfirmDialog(this, msg, "Data Retrieval", JOptionPane.OK_CANCEL_OPTION,
                JOptionPane.PLAIN_MESSAGE);

        if (res != JOptionPane.OK_OPTION || idField.getText().isEmpty())
            return;

        try {
            int id = Integer.parseInt(idField.getText());
            User user = controller.getUserById(id);

            if (user == null) {
                showEnterpriseError("Record ID [" + id + "] not found in the global database.");
                return;
            }

            // Tampilan Detail yang lebih rapi
            JPanel infoPanel = new JPanel(new GridLayout(0, 1, 2, 2));
            infoPanel.add(new JLabel("USERNAME: " + user.getUsername().toUpperCase()));
            infoPanel.add(new JLabel("NID: " + user.getNID().toUpperCase()));
            infoPanel.add(new JLabel("ROLE: " + user.getRole().toUpperCase()));
            infoPanel.add(new JLabel("STATUS: " + user.getStatus().toUpperCase()));
            infoPanel.add(new JLabel("SUB BIDANG ID: " + user.getSubBidangId()));
            infoPanel.setBorder(BorderFactory.createCompoundBorder(
                    new MatteBorder(0, 4, 0, 0, UIConfig.PRIMARY),
                    BorderFactory.createEmptyBorder(5, 15, 5, 0)));

            String[] options = { "MODIFY DATA", "DELETE", "CLOSE" };
            int choice = JOptionPane.showOptionDialog(this,
                    infoPanel, "Record Administration - " + id,
                    JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE, null, options, options[0]);

            handleManageChoice(choice, user);
        } catch (NumberFormatException e) {
            showEnterpriseError("Search rejected. ID must be a numeric value.");
        }
    }

    private void handleManageChoice(int choice, User userToEdit) { // Ubah nama parameter agar tidak bentrok dengan
                                                                   // 'user' session
        switch (choice) {
            case 0 -> {
                // Simpan data lama untuk log aktivitas
                String oldUsername = userToEdit.getUsername();
                String oldNID = userToEdit.getNID();
                String oldRole = userToEdit.getRole();
                String oldStatus = userToEdit.getStatus();

                // Input fields
                JTextField u = new JTextField(oldUsername);
                JTextField n = new JTextField(oldNID);
                JComboBox<String> r = new JComboBox<>(new String[] { "ADMIN", "MANAGER", "USER" });
                JComboBox<String> s = new JComboBox<>(new String[] { "ACTIVE", "INACTIVE", "SUSPENDED", "PENDING" });

                r.setSelectedItem(oldRole);
                s.setSelectedItem(oldStatus);

                // Styling
                u.putClientProperty(FlatClientProperties.STYLE, "arc: 5");
                n.putClientProperty(FlatClientProperties.STYLE, "arc: 5");
                r.putClientProperty(FlatClientProperties.STYLE, "arc: 5");
                s.putClientProperty(FlatClientProperties.STYLE, "arc: 5");

                // TAMPILAN: SUB BIDANG ID dibuat Read-Only (Hanya Label)
                Object[] updateMsg = {
                        "USERNAME:", u,
                        "NID:", n,
                        "ROLE:", r,
                        "STATUS:", s,
                        "SUB BIDANG ID (Read-only):", new JLabel(String.valueOf(userToEdit.getSubBidangId()))
                };

                int option = JOptionPane.showConfirmDialog(this, updateMsg, "User Data Modification",
                        JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

                if (option == JOptionPane.OK_OPTION) {
                    try {
                        // Update data objek user (ID Sub Bidang tidak disentuh/diubah)
                        userToEdit.setUsername(u.getText());
                        userToEdit.setNID(n.getText());
                        userToEdit.setRole((String) r.getSelectedItem());
                        userToEdit.setStatus((String) s.getSelectedItem());

                        controller.updateUser(user, userToEdit); // 'user' adalah admin yang sedang login, 'userToEdit'
                                                                 // adalah objek yang diubah

                        // Log Perubahan
                        StringBuilder changes = new StringBuilder();
                        if (!oldUsername.equals(u.getText()))
                            changes.append("Username; ");
                        if (!oldNID.equals(n.getText()))
                            changes.append("NID; ");
                        if (!oldRole.equals(r.getSelectedItem()))
                            changes.append("Role; ");
                        if (!oldStatus.equals(s.getSelectedItem()))
                            changes.append("Status; ");

                        activityController.insertLogActivity(user, "UPDATE", "USER", userToEdit.getId(),
                                changes.toString().isEmpty() ? "No changes made." : "Updated: " + changes.toString());

                        JOptionPane.showMessageDialog(this, "Master Record Synchronized.");
                    } catch (Exception e) {
                        showEnterpriseError("Update rejected by server.");
                    }
                }
            }
            case 2 -> { // DELETE
                int confirm = JOptionPane.showConfirmDialog(this,
                        "Are you sure you want to PERMANENTLY DELETE\n" + userToEdit.getUsername().toUpperCase()
                                + "?\nThis action cannot be undone.",
                        "Security Confirmation", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);

                if (confirm == JOptionPane.YES_OPTION) {
                    controller.deleteUserById(user, userToEdit.getId());
                    activityController.insertLogActivity(user, "DELETE", "USER", userToEdit.getId(), "User deleted.");
                    JOptionPane.showMessageDialog(this, "Resource successfully purged.");
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