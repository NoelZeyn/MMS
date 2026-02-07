// MainMenuFrame.java
package main.java.com.inventory.app;

import main.java.com.inventory.model.User;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class MainMenuFrame extends JFrame {
    private User user;

    public MainMenuFrame(User user) {
        this.user = user;
        setupUI();
    }

    private void setupUI() {
        setTitle("Inventory Dashboard");
        setSize(420, 380);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        JPanel panel = new JPanel();
        panel.setBackground(new Color(245, 250, 255));
        panel.setBorder(new EmptyBorder(30, 30, 30, 30));
        panel.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(15, 15, 15, 15);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 0; gbc.gridy = 0;

        // Welcome Label
        JLabel welcomeLabel = new JLabel("Halo, " + user.getUsername(), SwingConstants.CENTER);
        welcomeLabel.setFont(new Font("Segoe UI", Font.BOLD, 22));
        welcomeLabel.setForeground(new Color(40, 60, 90));
        gbc.gridwidth = 2;
        panel.add(welcomeLabel, gbc);

        gbc.gridwidth = 1; gbc.gridy = 1;

        // Buttons
        JButton btnManage = createStyledButton("Manajemen Barang", new Color(46, 204, 113));
        JButton btnExit = createStyledButton("Keluar", new Color(231, 76, 60));

        gbc.gridy = 2;
        panel.add(btnManage, gbc);
        gbc.gridy = 3;
        panel.add(btnExit, gbc);

        add(panel);

        btnManage.addActionListener(e -> {
            new BarangMenuFrame(user).setVisible(true);
            this.dispose();
        });

        btnExit.addActionListener(e -> System.exit(0));
    }

    private JButton createStyledButton(String text, Color bg) {
        JButton btn = new JButton(text);
        btn.setBackground(bg);
        btn.setForeground(Color.WHITE);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 16));
        btn.setFocusPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        btn.setOpaque(true);
        btn.setBorderPainted(false);

        // Hover effect
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
}
