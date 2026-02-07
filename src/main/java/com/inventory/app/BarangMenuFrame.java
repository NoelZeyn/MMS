// BarangMenuFrame.java
package main.java.com.inventory.app;

import main.java.com.inventory.controller.BarangController;
import main.java.com.inventory.model.Barang;
import main.java.com.inventory.model.User;
import main.java.com.inventory.service.BarangService;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class BarangMenuFrame extends JFrame {
    private User user;
    private BarangController controller;

    public BarangMenuFrame(User user) {
        this.user = user;
        this.controller = new BarangController(new BarangService());
        setupUI();
    }

    private void setupUI() {
        setTitle("Manajemen Inventaris");
        setSize(500, 450);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(new EmptyBorder(25, 25, 25, 25));
        panel.setBackground(new Color(245, 250, 255));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.gridx = 0;

        // Header
        JLabel header = new JLabel("Pilih Aksi:", SwingConstants.CENTER);
        header.setFont(new Font("Segoe UI", Font.BOLD, 20));
        header.setForeground(new Color(40, 60, 90));
        gbc.gridy = 0; gbc.gridwidth = 2;
        panel.add(header, gbc);

        gbc.gridwidth = 1;
        gbc.gridy = 1;

        // Buttons
        JButton addButton = createButton("➕ Tambah Barang Baru", new Color(52, 152, 219));
        JButton viewButton = createButton("🔍 Lihat Daftar Barang", new Color(41, 128, 185));
        JButton detailButton = createButton("⚙️ Kelola Detail (Edit/Hapus)", new Color(46, 204, 113));
        JButton backButton = createButton("⬅️ Kembali ke Menu Utama", new Color(231, 76, 60));

        gbc.gridy = 1; panel.add(addButton, gbc);
        gbc.gridy = 2; panel.add(viewButton, gbc);
        gbc.gridy = 3; panel.add(detailButton, gbc);
        gbc.gridy = 4; panel.add(backButton, gbc);

        add(panel);

        // Action listeners
        addButton.addActionListener(e -> tambahBarang());
        viewButton.addActionListener(e -> lihatBarang());
        detailButton.addActionListener(e -> manageDetailBarang());
        backButton.addActionListener(e -> {
            new MainMenuFrame(user).setVisible(true);
            this.dispose();
        });
    }

    private JButton createButton(String text, Color bg) {
        JButton btn = new JButton(text);
        btn.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        btn.setBackground(bg);
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setOpaque(true);
        btn.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));
        btn.setBorderPainted(false);

        // Hover effect
        btn.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) { btn.setBackground(bg.darker()); }
            public void mouseExited(java.awt.event.MouseEvent evt) { btn.setBackground(bg); }
        });

        return btn;
    }

    private void tambahBarang() {
        JTextField nameF = new JTextField();
        JTextField stockF = new JTextField();
        JTextField locF = new JTextField();
        Object[] message = {
            "Nama Barang:", nameF,
            "Stok:", stockF,
            "Lokasi:", locF
        };

        int option = JOptionPane.showConfirmDialog(this, message, "Input Barang Baru", JOptionPane.OK_CANCEL_OPTION);
        if(option == JOptionPane.OK_OPTION) {
            try {
                controller.insertBarang(user, new Barang(nameF.getText(), Integer.parseInt(stockF.getText()), locF.getText()));
                JOptionPane.showMessageDialog(this, "Barang berhasil disimpan!");
            } catch(Exception ex) {
                JOptionPane.showMessageDialog(this, "Gagal: " + ex.getMessage());
            }
        }
    }

    private void lihatBarang() {
        List<Barang> list = controller.getAllBarang();
        String[] columns = {"ID", "Nama", "Stok", "Lokasi"};
        DefaultTableModel model = new DefaultTableModel(columns, 0);
        for(Barang b : list) model.addRow(new Object[]{b.getId(), b.getNama(), b.getStok(), b.getLokasi()});

        JTable table = new JTable(model);
        JScrollPane scroll = new JScrollPane(table);
        scroll.setPreferredSize(new Dimension(450, 300));

        JOptionPane.showMessageDialog(this, scroll, "Daftar Stok Inventaris", JOptionPane.PLAIN_MESSAGE);
    }

    private void manageDetailBarang() {
        String inputId = JOptionPane.showInputDialog(this, "Masukkan ID Barang yang ingin dikelola:");
        if(inputId == null || inputId.isEmpty()) return;

        int id = Integer.parseInt(inputId);
        Barang barang = controller.getBarangById(id);
        if(barang == null) { JOptionPane.showMessageDialog(this, "Barang tidak ditemukan."); return; }

        String[] options = {"Kurangi Stok", "Update Data", "Hapus Barang", "Batal"};
        int choice = JOptionPane.showOptionDialog(this,
                "Detail Barang:\nID: "+barang.getId()+"\nNama: "+barang.getNama()+
                        "\nStok: "+barang.getStok()+"\nLokasi: "+barang.getLokasi(),
                "Opsi Pengelolaan", JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[0]);

        switch(choice) {
            case 0:
                String q = JOptionPane.showInputDialog("Jumlah yang dikurangi:");
                if(q != null) controller.kurangiStok(id, Integer.parseInt(q));
                break;
            case 1:
                String n = JOptionPane.showInputDialog("Nama baru:", barang.getNama());
                int s = Integer.parseInt(JOptionPane.showInputDialog("Stok baru:", barang.getStok()));
                String l = JOptionPane.showInputDialog("Lokasi baru:", barang.getLokasi());
                barang.setNama(n); barang.setStok(s); barang.setLokasi(l);
                controller.updateBarang(user, barang);
                break;
            case 2:
                int confirm = JOptionPane.showConfirmDialog(this, "Yakin hapus barang ini?", "Konfirmasi", JOptionPane.YES_NO_OPTION);
                if(confirm == JOptionPane.YES_OPTION) controller.deleteBarangById(id);
                break;
        }
    }
}
