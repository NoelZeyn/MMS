package main.java.com.inventory.app;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class InventoryPanel extends JPanel {
    private JTable dataTable;
    private DefaultTableModel tableModel;

    public InventoryPanel(main.java.com.inventory.model.User user) {
        setLayout(new BorderLayout());
        setBorder(new EmptyBorder(25, 25, 25, 25));
        setBackground(Color.WHITE);

        // --- HEADER SECTION ---
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(Color.WHITE);
        
        JLabel title = new JLabel("Manajemen Inventaris");
        title.setFont(new Font("Segoe UI", Font.BOLD, 24));
        header.add(title, BorderLayout.WEST);

        // Action Buttons
        JPanel actions = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        actions.setBackground(Color.WHITE);
        JButton btnAdd = new JButton("+ Tambah Barang");
        btnAdd.setBackground(new Color(13, 110, 253));
        btnAdd.setForeground(Color.WHITE);
        actions.add(new JTextField(20)); // Search bar
        actions.add(btnAdd);
        header.add(actions, BorderLayout.EAST);

        add(header, BorderLayout.NORTH);

        // --- TABLE SECTION ---
        String[] cols = {"SKU", "Nama Barang", "Kategori", "Stok", "Lokasi", "Status"};
        tableModel = new DefaultTableModel(cols, 0);
        dataTable = new JTable(tableModel);
        
        // UX Detail: Baris yang tinggi dan font bersih
        dataTable.setRowHeight(40);
        dataTable.getTableHeader().setPreferredSize(new Dimension(0, 40));
        dataTable.getTableHeader().setBackground(new Color(248, 249, 250));
        dataTable.setSelectionBackground(new Color(232, 240, 254));
        
        JScrollPane scroll = new JScrollPane(dataTable);
        scroll.setBorder(BorderFactory.createLineBorder(new Color(230, 230, 230)));
        add(scroll, BorderLayout.CENTER);
        
        // Mock Data
        tableModel.addRow(new Object[]{"BRG-001", "MacBook Pro M3", "Elektronik", "12", "Gudang A", "In Stock"});
        tableModel.addRow(new Object[]{"BRG-002", "Logitech MX Master", "Aksesoris", "3", "Gudang B", "Low Stock"});
    }
}