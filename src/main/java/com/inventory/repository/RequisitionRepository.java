package main.java.com.inventory.repository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import main.java.com.inventory.exception.ValidationException;
import main.java.com.inventory.model.Requisition;
import main.java.com.inventory.util.DatabaseUtil;

public class RequisitionRepository {

    public void insert(Requisition requisition) {
        String sql = """
                    INSERT INTO requisitions (userId, name, specification, quantity, unitPrice, totalPrice, vendor, justification)
                    VALUES (?, ?, ?, ?, ?, ?, ?, ?)
                """;
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, requisition.getUserId());
            ps.setString(2, requisition.getName());
            ps.setString(3, requisition.getSpecification());
            ps.setInt(4, requisition.getQuantity());
            ps.setInt(5, requisition.getUnitPrice());
            ps.setInt(6, requisition.getTotalPrice());
            ps.setString(7, requisition.getVendor());
            ps.setString(8, requisition.getJustification());
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace(); // Penting untuk melihat detail error di terminal
            throw new ValidationException("Gagal menyimpan requisition: " + e.getMessage());
        } catch (Exception e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }
    }

    public void update(Requisition requisition) {
        String sql = """
                    UPDATE requisitions
                    SET userId = ?, name = ?, specification = ?, quantity = ?, unitPrice = ?, totalPrice = ?, vendor = ?, justification = ?
                    WHERE id = ?
                """;
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, requisition.getUserId());
            ps.setString(2, requisition.getName());
            ps.setString(3, requisition.getSpecification());
            ps.setInt(4, requisition.getQuantity());
            ps.setInt(5, requisition.getUnitPrice());
            ps.setInt(6, requisition.getTotalPrice());
            ps.setString(7, requisition.getVendor());
            ps.setString(8, requisition.getJustification());
            ps.setInt(9, requisition.getId());
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new ValidationException("Gagal memperbarui requisition: " + e.getMessage());
        } catch (Exception e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }
    }

    public void delete(int id) {
        String sql = "DELETE FROM requisitions WHERE id = ?";
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new ValidationException("Gagal menghapus barang");
        } catch (Exception e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }
    }

    public List<Requisition> getAllRequisitions() throws Exception {
        // PERBAIKAN: Tambahkan userId ke dalam SELECT
        String sql = "SELECT id, userId, name, specification, quantity, unitPrice, totalPrice, vendor, justification FROM requisitions";
        List<Requisition> requisitionList = new ArrayList<>();

        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                requisitionList.add(new Requisition(
                        rs.getInt("id"),
                        rs.getInt("userId"), // Sekarang kolom ini sudah ada di SELECT
                        rs.getString("name"),
                        rs.getString("specification"),
                        rs.getInt("quantity"),
                        rs.getInt("unitPrice"),
                        rs.getInt("totalPrice"),
                        rs.getString("vendor"),
                        rs.getString("justification")));
            }
        }
        return requisitionList;
    }

    public Requisition getRequisitionById(int id) throws Exception {
        String sql = "SELECT id, userId, name, specification, quantity, unitPrice, totalPrice, vendor, justification FROM requisitions WHERE id = ?";
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new Requisition(
                            rs.getInt("id"),
                            rs.getInt("userId"),
                            rs.getString("name"),
                            rs.getString("specification"),
                            rs.getInt("quantity"),
                            rs.getInt("unitPrice"),
                            rs.getInt("totalPrice"),
                            rs.getString("vendor"),
                            rs.getString("justification")
                    );
                }
            }
        }
        return null;
    }
}