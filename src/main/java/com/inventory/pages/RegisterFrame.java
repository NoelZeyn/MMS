package main.java.com.inventory.pages;

import com.formdev.flatlaf.FlatClientProperties;

import main.java.com.inventory.service.AuthService;

import javax.swing.*;
import java.awt.*;

public class RegisterFrame extends JFrame {
    // Dipisah agar data tidak tertimpa
    private JTextField nidField; 
    private JTextField userField;
    private JPasswordField passField;
    private JButton registerButton;
    private final AuthService authService;

    public RegisterFrame() {
        this.authService = new AuthService();
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

        // Card Container
        JPanel card = new JPanel(new GridBagLayout());
        card.setBackground(Color.WHITE);
        card.setPreferredSize(new Dimension(360, 500));
        card.putClientProperty(FlatClientProperties.STYLE, "arc: 40");

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 0;

        // --- 0. Header ---
        JLabel title = new JLabel("Buat Akun Baru", SwingConstants.CENTER);
        title.setFont(new Font("Segoe UI", Font.BOLD, 22));
        gbc.gridy = 0;
        gbc.insets = new Insets(20, 30, 20, 30);
        card.add(title, gbc);

        // --- 1. NID ---
        nidField = createStyledField("NID (Nomor Induk)");
        gbc.gridy = 1;
        gbc.insets = new Insets(5, 30, 5, 30);
        card.add(nidField, gbc);

        // --- 2. Username ---
        userField = createStyledField("Username");
        gbc.gridy = 2;
        card.add(userField, gbc);

        // --- 3. Password ---
        passField = createStyledPassField("Password");
        gbc.gridy = 3;
        card.add(passField, gbc);

        // --- 4. Register Button ---
        registerButton = new JButton("Daftar Sekarang");
        registerButton.setBackground(UIConfig.PRIMARY);
        registerButton.setForeground(Color.WHITE);
        registerButton.setFont(new Font("Segoe UI", Font.BOLD, 14));
        registerButton.setPreferredSize(new Dimension(0, 45));
        registerButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        registerButton.putClientProperty(FlatClientProperties.STYLE, "borderWidth: 0; focusWidth: 0");
        
        gbc.gridy = 4;
        gbc.insets = new Insets(25, 30, 10, 30);
        card.add(registerButton, gbc);

        // --- 5. Footer (Pake UIComponents) ---
        JPanel footer = new JPanel(new FlowLayout(FlowLayout.CENTER));
        footer.setOpaque(false);
        footer.add(new JLabel("Sudah punya akun?"));

        JButton loginLinkBtn = UIComponents.createLinkButton("Login Sekarang");
        footer.add(loginLinkBtn);

        gbc.gridy = 5;
        gbc.insets = new Insets(10, 30, 20, 30);
        card.add(footer, gbc);

        contentPane.add(card);

        // --- Logic ---
        registerButton.addActionListener(e -> handleRegister());
        loginLinkBtn.addActionListener(e -> {
            new LoginFrame().setVisible(true);
            this.dispose();
        });
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

    private void handleRegister() {
        String nid = nidField.getText();
        String user = userField.getText();
        String pass = new String(passField.getPassword());

        if (nid.isEmpty() || user.isEmpty() || pass.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Harap lengkapi semua data!", "Peringatan", JOptionPane.WARNING_MESSAGE);
            return;
        }

        // Contoh integrasi service
        try {
            // authService.register(nid, user, pass); 
            JOptionPane.showMessageDialog(this, "Akun " + user + " berhasil didaftarkan!");
            new LoginFrame().setVisible(true);
            this.dispose();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Gagal daftar: " + e.getMessage());
        }
    }
}