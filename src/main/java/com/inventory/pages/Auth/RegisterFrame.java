package main.java.com.inventory.pages.Auth;

import com.formdev.flatlaf.FlatClientProperties;
import main.java.com.inventory.controller.SubBidangController;
import main.java.com.inventory.model.SubBidang;
import main.java.com.inventory.pages.UI.UIComponents;
import main.java.com.inventory.pages.UI.UIConfig;
import main.java.com.inventory.service.AuthService;
import main.java.com.inventory.service.SubBidangService;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class RegisterFrame extends JFrame {

    private JTextField nidField;
    private JTextField userField;
    private JPasswordField passField;
    private JComboBox<String> roleComboBox;
    private JComboBox<SubBidang> subBidangComboBox;
    private JButton registerButton;

    private final AuthService authService;
    private final SubBidangController subBidangController;

    public RegisterFrame() {
        this.authService = new AuthService();
        this.subBidangController = new SubBidangController(new SubBidangService());
        setupFrame();
        initContent();
    }

    private void setupFrame() {
        setTitle("System MMS - Create Account");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(450, 600);
        setLocationRelativeTo(null);
        setResizable(false);
    }

    private void initContent() {
        JPanel contentPane = new JPanel(new GridBagLayout());
        contentPane.setBackground(UIConfig.BG_LIGHT);
        setContentPane(contentPane);

        JPanel card = new JPanel(new GridBagLayout());
        card.setBackground(Color.WHITE);
        card.setPreferredSize(new Dimension(360, 500));
        card.putClientProperty(FlatClientProperties.STYLE, "arc: 40");

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 0;

        JLabel title = new JLabel("Buat Akun Baru", SwingConstants.CENTER);
        title.setFont(new Font("Segoe UI", Font.BOLD, 22));
        gbc.gridy = 0;
        gbc.insets = new Insets(20, 30, 20, 30);
        card.add(title, gbc);

        nidField = createStyledField("NID (Nomor Induk)");
        gbc.gridy = 1;
        gbc.insets = new Insets(5, 30, 5, 30);
        card.add(nidField, gbc);

        userField = createStyledField("Username");
        gbc.gridy = 2;
        card.add(userField, gbc);

        passField = createStyledPassField("Password");
        gbc.gridy = 3;
        card.add(passField, gbc);

        roleComboBox = new JComboBox<>(new String[] { "USER", "ADMIN", "MANAGER" });
        roleComboBox.setPreferredSize(new Dimension(0, 40));
        gbc.gridy = 4;
        card.add(roleComboBox, gbc);

        subBidangComboBox = new JComboBox<>();
        loadSubBidang();
        subBidangComboBox.setPreferredSize(new Dimension(0, 40));
        gbc.gridy = 5;
        card.add(subBidangComboBox, gbc);

        registerButton = new JButton("Daftar Sekarang");
        registerButton.setBackground(UIConfig.PRIMARY);
        registerButton.setForeground(Color.WHITE);
        registerButton.setFont(new Font("Segoe UI", Font.BOLD, 14));
        registerButton.setPreferredSize(new Dimension(0, 45));
        registerButton.putClientProperty(
                FlatClientProperties.STYLE,
                "borderWidth: 0; focusWidth: 0");

        gbc.gridy = 6;
        gbc.insets = new Insets(25, 30, 10, 30);
        card.add(registerButton, gbc);
        // Footer Section (Referensi dari LoginFrame)
        JPanel footer = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 0));
        footer.setOpaque(false);

        footer.add(new JLabel("Sudah punya akun?"));

        JButton loginBtn = UIComponents.createLinkButton("Masuk");
        loginBtn.addActionListener(e -> {
            new LoginFrame().setVisible(true);
            this.dispose();
        });

        footer.add(loginBtn);

        gbc.gridy = 7;
        gbc.insets = new Insets(10, 30, 20, 30);
        card.add(footer, gbc);

        contentPane.add(card);

        registerButton.addActionListener(e -> handleRegister());
    }

    private void loadSubBidang() {
        try {
            List<SubBidang> list = subBidangController.getAllSubBidangs();
            for (SubBidang sb : list) {
                subBidangComboBox.addItem(sb);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                    "Gagal memuat Sub Bidang",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private void handleRegister() {
        String nid = nidField.getText().trim();
        String user = userField.getText().trim();
        String pass = new String(passField.getPassword());
        String role = (String) roleComboBox.getSelectedItem();
        SubBidang subBidang = (SubBidang) subBidangComboBox.getSelectedItem();

        if (nid.isEmpty() || user.isEmpty() || pass.isEmpty() || subBidang == null) {
            JOptionPane.showMessageDialog(this,
                    "Semua field wajib diisi!",
                    "Validasi",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        int subBidangId = subBidang.getId(); // INT, sesuai DB

        try {
            authService.register(nid, user, pass, role, subBidangId);
            JOptionPane.showMessageDialog(this,
                    "Akun berhasil dibuat");
            new LoginFrame().setVisible(true);
            dispose();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                    e.getMessage(),
                    "Registrasi gagal",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private JTextField createStyledField(String hint) {
        JTextField f = new JTextField();
        f.setPreferredSize(new Dimension(0, 40));
        f.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, hint);
        return f;
    }

    private JPasswordField createStyledPassField(String hint) {
        JPasswordField f = new JPasswordField();
        f.setPreferredSize(new Dimension(0, 40));
        f.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, hint);
        f.putClientProperty(FlatClientProperties.STYLE, "showRevealButton: true");
        return f;
    }
}
