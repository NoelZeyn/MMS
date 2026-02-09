package com.inventory.repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import com.inventory.model.User;
import com.inventory.util.DatabaseUtil;

public class UserManagementRepository {

    public List<User> getAllUsers() {
        String sql = """
                SELECT u.id, u.username, u.NID, u.password_hash, u.role, u.status, sb.name as name
                FROM users u
                LEFT JOIN sub_bidang sb ON u.subBidangId = sb.id
                """;

        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            
            List<User> users = new ArrayList<>();
            while (rs.next()) {
                // Menggunakan Constructor 1 (dengan String Name untuk Table UI)
                User user = new User(
                        rs.getInt("id"),
                        rs.getString("username"),
                        rs.getString("NID"),
                        rs.getString("password_hash"),
                        rs.getString("role"),
                        rs.getString("status"),
                        rs.getString("name")
                );
                users.add(user);
            }
            return users;
        } catch (Exception e) {
            throw new RuntimeException("Gagal mengambil daftar pengguna", e);
        }
    }

    public User getUserById(int userId) {
        String sql = "SELECT * FROM users WHERE id = ?";

        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, userId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    // Menggunakan Constructor 2 (dengan int ID untuk keperluan logika/update)
                    return new User(
                            rs.getInt("id"),
                            rs.getString("username"),
                            rs.getString("NID"),
                            rs.getString("password_hash"),
                            rs.getString("role"),
                            rs.getString("status"),
                            rs.getInt("subBidangId") // Pastikan nama kolom di DB adalah subBidangId
                    );
                }
            }
        } catch (Exception e) {
            throw new RuntimeException("Gagal mencari pengguna ID: " + userId, e);
        }
        return null;
    }

    public void insertUser(User user) {
        String sql = "INSERT INTO users (username, NID, password_hash, role, status, subBidangId) VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, user.getUsername());
            ps.setString(2, user.getNID());
            ps.setString(3, user.getPasswordHash());
            ps.setString(4, user.getRole());
            ps.setString(5, user.getStatus());
            ps.setInt(6, user.getSubBidangId());
            ps.executeUpdate();
        } catch (Exception e) {
            throw new RuntimeException("Gagal simpan user baru", e);
        }
    }

    public void updateUser(User user) {
        String sql = "UPDATE users SET username=?, NID=?, password_hash=?, role=?, status=?, subBidangId=? WHERE id=?";

        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, user.getUsername());
            ps.setString(2, user.getNID());
            ps.setString(3, user.getPasswordHash());
            ps.setString(4, user.getRole());
            ps.setString(5, user.getStatus());
            ps.setInt(6, user.getSubBidangId());
            ps.setInt(7, user.getId());
            ps.executeUpdate();
        } catch (Exception e) {
            throw new RuntimeException("Gagal update user ID: " + user.getId(), e);
        }
    }

    public void deleteUserById(int userId) {
        String sql = "DELETE FROM users WHERE id = ?";
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, userId);
            ps.executeUpdate();
        } catch (Exception e) {
            throw new RuntimeException("Gagal hapus user", e);
        }
    }

    public void suspendUserById(int userId) {
        String sql = "UPDATE users SET status = 'SUSPENDED' WHERE id = ?";
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, userId);
            ps.executeUpdate();
        } catch (Exception e) {
            throw new RuntimeException("Gagal suspend user", e);
        }
    }
}