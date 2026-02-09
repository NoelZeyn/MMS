package com.inventory.repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import com.inventory.model.UserActivity;
import com.inventory.util.DatabaseUtil;

public class UserActivityRepository {

    public List<UserActivity> getAllUserActivities() {
        String sql = "SELECT * FROM user_activity_log";

        try (
                Connection conn = DatabaseUtil.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {
            ResultSet rs = ps.executeQuery();
            List<UserActivity> activities = new ArrayList<>();
            while (rs.next()) {
                UserActivity activity = new UserActivity(
                        rs.getInt("id"),
                        rs.getInt("user_id"),
                        rs.getString("activity"),
                        rs.getString("target_table"),
                        rs.getInt("target_id"),
                        rs.getString("description"),
                        rs.getString("timestamp"));
                activities.add(activity);
            }
            return activities;
        } catch (Exception e) {
            throw new RuntimeException("Gagal mengambil log aktivitas pengguna", e);
        }
    }

    public void insertLogActivity(UserActivity activity) {
        String sql = """
                    INSERT INTO user_activity_log (user_id, activity, target_table, target_id, description, timestamp)
                    VALUES (?, ?, ?, ?, ?, ?)
                """;

        try (
                Connection conn = DatabaseUtil.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, activity.getUserId());
            ps.setString(2, activity.getActivity());
            ps.setString(3, activity.getTargetTable());
            ps.setInt(4, activity.getTargetId());
            ps.setString(5, activity.getDescription());
            ps.setString(6, activity.getTimestamp());
            ps.executeUpdate();
        } catch (Exception e) {
            throw new RuntimeException("Gagal mencatat aktivitas pengguna", e);
        }
    }

    public void updateLogActivity(UserActivity activity) {
        String sql = """
                    UPDATE user_activity_log
                    SET user_id = ?, activity = ?, target_table = ?, target_id = ?, description = ?, timestamp = ?
                    WHERE id = ?
                """;

        try (
                Connection conn = DatabaseUtil.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, activity.getUserId());
            ps.setString(2, activity.getActivity());
            ps.setString(3, activity.getTargetTable());
            ps.setInt(4, activity.getTargetId());
            ps.setString(5, activity.getDescription());
            ps.setString(6, activity.getTimestamp());
            ps.setInt(7, activity.getId());
            ps.executeUpdate();
        } catch (Exception e) {
            throw new RuntimeException("Gagal memperbarui log aktivitas pengguna", e);
        }
    }

    public void getLogsByUserId(int userId) {
        String sql = "SELECT * FROM user_activity_log WHERE user_id = ?";

        try (
                Connection conn = DatabaseUtil.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, userId);
            ps.executeQuery();
        } catch (Exception e) {
            throw new RuntimeException("Gagal mengambil log aktivitas pengguna", e);
        }
    }

    public void deleteLogsByTarget(String targetTable, int targetId) {
        String sql = "DELETE FROM user_activity_log WHERE target_table = ? AND target_id = ?";

        try (
                Connection conn = DatabaseUtil.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, targetTable);
            ps.setInt(2, targetId);
            ps.executeUpdate();
        } catch (Exception e) {
            throw new RuntimeException("Gagal menghapus log aktivitas pengguna", e);
        }
    }
}
