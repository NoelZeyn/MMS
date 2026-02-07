package main.java.com.inventory.repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
// INI PAKAI USER
import main.java.com.inventory.model.User;
import main.java.com.inventory.util.DatabaseUtil;

public class UserManagementRepository {
    public List<User> getAllUsers() {
        String sql = "SELECT * FROM users";
        try (
                Connection conn = DatabaseUtil.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {
            ResultSet rs = ps.executeQuery();
            java.util.List<User> users = new ArrayList<>();
            while (rs.next()) {
                User user = new User(
                        rs.getInt("id"),
                        rs.getString("username"),
                        rs.getString("NID"),
                        rs.getString("password_hash"),
                        rs.getString("role"),
                        rs.getString("status"));
                users.add(user);
            }
            return users;
        } catch (Exception e) {
            throw new RuntimeException("Gagal mengambil semua pengguna", e);
        }
    }

    public User getUserById(int userId) {
        String sql = "SELECT * FROM users WHERE id = ?";

        try (
                Connection conn = DatabaseUtil.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, userId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                User user = new User(
                        rs.getInt("id"),
                        rs.getString("username"),
                        rs.getString("NID"),
                        rs.getString("password_hash"),
                        rs.getString("role"),
                        rs.getString("status"));
                return user;
            }
        } catch (Exception e) {
            throw new RuntimeException("Gagal mengambil pengguna berdasarkan ID", e);
        }
        return null;
    }

    public void insertUser(User user) {
        String sql = """
                    INSERT INTO users (username, NID, password_hash, role, status)
                    VALUES (?, ?, ?, ?, ?)
                """;

        try (
                Connection conn = DatabaseUtil.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, user.getUsername());
            ps.setString(2, user.getNID());
            ps.setString(3, user.getPasswordHash());
            ps.setString(4, user.getRole());
            ps.setString(5, user.getStatus());
            ps.executeUpdate();
        } catch (Exception e) {
            throw new RuntimeException("Gagal memasukkan pengguna baru", e);
        }
    }

    public void updateUser(User user) {
        String sql = """
                    UPDATE users
                    SET username = ?, NID = ?, password_hash = ?, role = ?, status = ?
                    WHERE id = ?
                """;

        try (
                Connection conn = DatabaseUtil.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, user.getUsername());
            ps.setString(2, user.getNID());
            ps.setString(3, user.getPasswordHash());
            ps.setString(4, user.getRole());
            ps.setString(5, user.getStatus());
            ps.setInt(6, user.getId());
            ps.executeUpdate();
        } catch (Exception e) {
            throw new RuntimeException("Gagal memperbarui pengguna", e);
        }
    }

    public void deleteUserById(int userId) {
        String sql = "DELETE FROM users WHERE id = ?";

        try (
                Connection conn = DatabaseUtil.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, userId);
            ps.executeUpdate();
        } catch (Exception e) {
            throw new RuntimeException("Gagal menghapus pengguna", e);
        }
    }

    public void suspendUserById(int userId) {
        String sql = "UPDATE users SET status = 'SUSPEND' WHERE id = ?";

        try (
                Connection conn = DatabaseUtil.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, userId);
            ps.executeUpdate();
        } catch (Exception e) {
            throw new RuntimeException("Gagal memblokir pengguna berdasarkan ID", e);
        }
    }

}
