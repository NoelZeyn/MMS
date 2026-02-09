package com.inventory.repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import com.inventory.model.SubBidang;
import com.inventory.util.DatabaseUtil;

public class SubBidangRepository {

    public List<SubBidang> getAllSubBidangs() {
        String sql = "SELECT * FROM sub_bidang";

        try (
                Connection conn = DatabaseUtil.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {
            ResultSet rs = ps.executeQuery();
            java.util.List<SubBidang> activities = new ArrayList<>();
            while (rs.next()) {
                SubBidang activity = new SubBidang(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getInt("bidangId")
                    );
                activities.add(activity);
            }
            return activities;
        } catch (Exception e) {
            throw new RuntimeException("Gagal mengambil log aktivitas pengguna", e);
        }
    }

    public void insertSubBidang(SubBidang subBidang) {
        String sql = "INSERT INTO sub_bidang (name, bidangId) VALUES (?, ?)";

        try (
                Connection conn = DatabaseUtil.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, subBidang.getName());
            ps.setInt(2, subBidang.getBidangId());
            ps.executeUpdate();
        } catch (Exception e) {
            throw new RuntimeException("Gagal menambahkan sub bidang", e);
        }
    }

    public void updateSubBidang(SubBidang subBidang) {
        String sql = "UPDATE sub_bidang SET name = ?, bidangId = ? WHERE id = ?";

        try (
                Connection conn = DatabaseUtil.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, subBidang.getName());
            ps.setInt(2, subBidang.getBidangId());
            ps.setInt(3, subBidang.getId());
            ps.executeUpdate();
        } catch (Exception e) {
            throw new RuntimeException("Gagal memperbarui sub bidang", e);
        }
    }

    public SubBidang getSubBidangById(int id) {
        String sql = "SELECT * FROM sub_bidang WHERE id = ?";

        try (
                Connection conn = DatabaseUtil.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return new SubBidang(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getInt("bidangId")
                    );
            }
        } catch (Exception e) {
            throw new RuntimeException("Gagal mengambil sub bidang", e);
        }
        return null;
    }

    public void deleteSubBidangById(int id) {
        String sql = "DELETE FROM sub_bidang WHERE id = ?";

        try (
                Connection conn = DatabaseUtil.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.executeUpdate();
        } catch (Exception e) {
            throw new RuntimeException("Gagal menghapus sub bidang", e);
        }
    }
}
