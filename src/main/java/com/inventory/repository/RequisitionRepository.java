package com.inventory.repository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import com.inventory.exception.ValidationException;
import com.inventory.model.Requisition;
import com.inventory.model.RequisitionStatus;
import com.inventory.util.DatabaseUtil;

public class RequisitionRepository {

    public void insert(Requisition requisition) {
        String sql = """
                    INSERT INTO requisitions (userId, name, specification, quantity, unitPrice, totalPrice, status, vendor, justification)
                    VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)
                """;
        try (Connection conn = DatabaseUtil.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, requisition.getUserId());
            ps.setString(2, requisition.getName());
            ps.setString(3, requisition.getSpecification());
            ps.setInt(4, requisition.getQuantity());
            ps.setInt(5, requisition.getUnitPrice());
            ps.setInt(6, requisition.getTotalPrice());
            ps.setString(7, requisition.getStatus().name());
            ps.setString(8, requisition.getVendor());
            ps.setString(9, requisition.getJustification());
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace(); // Penting untuk melihat detail error di terminal
            throw new ValidationException("Gagal menyimpan requisition: " + e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            throw new ValidationException("Gagal menyimpan requisition: " + e.getMessage());
        }
    }

    public void update(Requisition requisition) {
        String sql = """
                    UPDATE requisitions
                    SET userId = ?, name = ?, specification = ?, quantity = ?, unitPrice = ?, totalPrice = ?, status = ?, vendor = ?, justification = ?
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
            ps.setString(7, requisition.getStatus().name());
            ps.setString(8, requisition.getVendor());
            ps.setString(9, requisition.getJustification());
            ps.setInt(10, requisition.getId());
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new ValidationException("Gagal memperbarui requisition: " + e.getMessage());
        } catch (Exception e1) {
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
            e1.printStackTrace();
        }
    }

public List<Requisition> getAllRequisitions() throws Exception {
    // Gunakan JOIN untuk mengambil kolom 'name' dari tabel users
    String sql = """
            SELECT r.*, u.username as requesterName 
            FROM requisitions r 
            INNER JOIN users u ON r.userId = u.id
            """;
    List<Requisition> requisitionList = new ArrayList<>();

    try (Connection conn = DatabaseUtil.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery()) {

        while (rs.next()) {
            Requisition req = new Requisition(
                    rs.getInt("id"),
                    rs.getInt("userId"),
                    rs.getString("name"),
                    rs.getString("specification"),
                    rs.getInt("quantity"),
                    rs.getInt("unitPrice"),
                    rs.getInt("totalPrice"),
                    RequisitionStatus.valueOf(rs.getString("status")),
                    rs.getString("vendor"),
                    rs.getString("justification"));
            
            // Masukkan nama requester ke field tambahan di model jika ada
            req.setRequesterName(rs.getString("requesterName")); 
            
            requisitionList.add(req);
        }
    }
    return requisitionList;
}
    public Requisition getRequisitionById(int id) throws Exception {
        String sql = "SELECT id, userId, name, specification, quantity, unitPrice, totalPrice, status, vendor, justification FROM requisitions WHERE id = ?";
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
                            RequisitionStatus.valueOf(rs.getString("status")),
                            rs.getString("vendor"),
                            rs.getString("justification"));
                }
            }
        }
        return null;
    }

    public List<Requisition> getRequisitionsByStatus(RequisitionStatus status) throws Exception {
    String sql = """
            SELECT r.*, u.username as requesterName 
            FROM requisitions r 
            INNER JOIN users u ON r.userId = u.id
            WHERE r.status = ?
            """;
    List<Requisition> list = new ArrayList<>();

    try (Connection conn = DatabaseUtil.getConnection();
         PreparedStatement ps = conn.prepareStatement(sql)) {
        
        ps.setString(1, status.name());
        try (ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                Requisition req = new Requisition(
                        rs.getInt("id"),
                        rs.getInt("userId"),
                        rs.getString("name"),
                        rs.getString("specification"),
                        rs.getInt("quantity"),
                        rs.getInt("unitPrice"),
                        rs.getInt("totalPrice"),
                        RequisitionStatus.valueOf(rs.getString("status")),
                        rs.getString("vendor"),
                        rs.getString("justification"));
                req.setRequesterName(rs.getString("requesterName"));
                list.add(req);
            }
        }
    }
    return list;
}
}