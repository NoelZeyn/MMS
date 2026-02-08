package main.java.com.inventory.repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.List;
import main.java.com.inventory.model.Barang;
import main.java.com.inventory.util.DatabaseUtil;
import java.sql.ResultSet;

public class BarangRepository {

    public void insert(Barang barang) {
        String sql = """
                    INSERT INTO barang (name, stok, lokasi)
                    VALUES (?, ?, ?)
                """;

        try (
                Connection conn = DatabaseUtil.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, barang.getName());
            ps.setInt(2, barang.getStok());
            ps.setString(3, barang.getLokasi());
            ps.executeUpdate();
        } catch (Exception e) {
            throw new RuntimeException("Gagal menyimpan barang", e);
        }
    }

    public void update(Barang barang) {
        String sql = """
                    UPDATE barang
                    SET name = ?, stok = ?, lokasi = ?
                    WHERE id = ?
                """;

        try (
                Connection conn = DatabaseUtil.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, barang.getName());
            ps.setInt(2, barang.getStok());
            ps.setString(3, barang.getLokasi());
            ps.setInt(4, barang.getId());
            ps.executeUpdate();
        } catch (Exception e) {
            throw new RuntimeException("Gagal memperbarui barang", e);
        }
    }

    public void delete(int id) {
        String sql = "DELETE FROM barang WHERE id = ?";

        try (
                Connection conn = DatabaseUtil.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.executeUpdate();
        } catch (Exception e) {
            throw new RuntimeException("Gagal menghapus barang", e);
        }
    }

    public void kurangiStok(int id, int jumlah) {
        String sql = "UPDATE barang SET stok = stok - ? WHERE id = ?";

        try (
                Connection conn = DatabaseUtil.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, jumlah);
            ps.setInt(2, id);
            ps.executeUpdate();
        } catch (Exception e) {
            throw new RuntimeException("Gagal mengurangi stok barang, barang dengan ID " + id + " tidak ditemukan atau stok tidak mencukupi", e);
        }
    }

    public void deleteByLocation(String lokasi) {
        String sql = "DELETE FROM barang WHERE lokasi = ?";

        try (
                Connection conn = DatabaseUtil.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, lokasi);
            ps.executeUpdate();
        } catch (Exception e) {
            throw new RuntimeException("Gagal menghapus barang berdasarkan lokasi", e);
        }
    }

    public List<Barang> getAllBarang() throws Exception {
        String sql = "SELECT id, name, stok, lokasi FROM barang";
        List<Barang> barangList = new ArrayList<>();

        try (Connection conn = DatabaseUtil.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql);
                ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                barangList.add(new Barang(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getInt("stok"),
                        rs.getString("lokasi")));
            }
        }
        return barangList;
    }

    public Barang getBarangById(int id) throws Exception {
        String sql = "SELECT id, name, stok, lokasi FROM barang WHERE id = ?";

        try (Connection conn = DatabaseUtil.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new Barang(
                            rs.getInt("id"),
                            rs.getString("name"),
                            rs.getInt("stok"),
                            rs.getString("lokasi"));
                }
            }
        }
        return null;
    }
}