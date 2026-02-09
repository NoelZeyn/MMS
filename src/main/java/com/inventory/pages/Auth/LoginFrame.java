package com.inventory.pages.Auth;

import com.formdev.flatlaf.FlatClientProperties;

import com.inventory.app.MainMenuFrame;
import com.inventory.model.User;
import com.inventory.pages.UI.UIComponents;
import com.inventory.pages.UI.UIConfig;
import com.inventory.service.AuthService;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class LoginFrame extends JFrame {
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JButton loginButton;
    private final AuthService authService;

    public LoginFrame() {
        this.authService = new AuthService();
        setupFrame();
        initContent();
    }

    private void setupFrame() {
        setTitle("System MMS - Login");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(420, 550);
        setLocationRelativeTo(null);
        setResizable(false);
    }

    private void initContent() {
        JPanel contentPane = new JPanel(new GridBagLayout());
        contentPane.setBackground(UIConfig.BG_LIGHT);
        setContentPane(contentPane);

        JPanel card = createCardPanel();
        contentPane.add(card);
    }

    private JPanel createCardPanel() {
        JPanel card = new JPanel(new GridBagLayout());
        card.setBackground(Color.WHITE);
        card.setPreferredSize(new Dimension(360, 460));
        card.putClientProperty(FlatClientProperties.STYLE, "arc: 40");

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 0;

        // Header Section
        gbc.gridy = 0; gbc.insets = new Insets(20, 35, 5, 35);
        JLabel icon = new JLabel("📦", SwingConstants.CENTER);
        icon.setFont(new Font("Segoe UI", Font.PLAIN, 52));
        card.add(icon, gbc);

        gbc.gridy = 1; gbc.insets = new Insets(0, 35, 30, 35);
        JLabel title = new JLabel("System MMS", SwingConstants.CENTER);
        title.setFont(new Font("Segoe UI", Font.BOLD, 24));
        title.setForeground(UIConfig.TEXT_DARK);
        card.add(title, gbc);

        // Input Section
        usernameField = new JTextField();
        usernameField.setPreferredSize(new Dimension(0, 45));
        usernameField.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, "Username");
        gbc.gridy = 2; gbc.insets = new Insets(5, 35, 12, 35);
        card.add(usernameField, gbc);

        passwordField = new JPasswordField();
        passwordField.setPreferredSize(new Dimension(0, 45));
        passwordField.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, "Password");
        passwordField.putClientProperty(FlatClientProperties.STYLE, "showRevealButton: true");
        gbc.gridy = 3; gbc.insets = new Insets(5, 35, 5, 35);
        card.add(passwordField, gbc);

        // Button Section
        loginButton = new JButton("Masuk ke Sistem");
        loginButton.setBackground(UIConfig.PRIMARY);
        loginButton.setForeground(Color.WHITE);
        loginButton.setFont(new Font("Segoe UI", Font.BOLD, 14));
        loginButton.setPreferredSize(new Dimension(0, 48));
        loginButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        loginButton.addActionListener(e -> handleLogin());
        gbc.gridy = 4; gbc.insets = new Insets(25, 35, 10, 35);
        card.add(loginButton, gbc);

        // Footer Section
        JPanel footer = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 0));
        footer.setOpaque(false);
        footer.add(new JLabel("Belum punya akun?"));
        
        JButton regBtn = UIComponents.createLinkButton("Daftar Sekarang");
        regBtn.addActionListener(e -> {
            new RegisterFrame().setVisible(true);
            this.dispose();
        });
        footer.add(regBtn);

        gbc.gridy = 5; gbc.insets = new Insets(10, 35, 25, 35);
        card.add(footer, gbc);

        setupKeyEvents();
        return card;
    }

    private void setupKeyEvents() {
        KeyAdapter resetOutline = new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                usernameField.putClientProperty(FlatClientProperties.OUTLINE, null);
                passwordField.putClientProperty(FlatClientProperties.OUTLINE, null);
            }
        };
        usernameField.addKeyListener(resetOutline);
        passwordField.addKeyListener(resetOutline);
    }

    private void handleLogin() {
        String userStr = usernameField.getText();
        String passStr = new String(passwordField.getPassword());

        if (userStr.isEmpty() || passStr.isEmpty()) {
            showFieldError("Harap isi semua bidang!");
            return;
        }

        try {
            User user = authService.login(userStr, passStr);
            JOptionPane.showMessageDialog(this, "Selamat Datang, " + userStr + "!", "Sukses", JOptionPane.INFORMATION_MESSAGE);
            new MainMenuFrame(user).setVisible(true);
            this.dispose();
        } catch (Exception e) {
            showFieldError(e.getMessage());
        }
    }

    private void showFieldError(String message) {
        usernameField.putClientProperty(FlatClientProperties.OUTLINE, "error");
        passwordField.putClientProperty(FlatClientProperties.OUTLINE, "error");
        JOptionPane.showMessageDialog(this, message, "Gagal", JOptionPane.ERROR_MESSAGE);
    }
}