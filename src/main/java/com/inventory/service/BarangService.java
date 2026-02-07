package main.java.com.inventory.service;

import main.java.com.inventory.model.Barang;
import main.java.com.inventory.model.User;
import main.java.com.inventory.repository.BarangRepository;
import main.java.com.inventory.exception.AuthException;
import main.java.com.inventory.exception.ValidationException;

import java.util.List;

public class BarangService {

    private final BarangRepository repository = new BarangRepository();

    private void authorize(User user) {
        if (!user.isAdmin() && !user.isManager()) {
            throw new AuthException("Akses ditolak: role tidak memiliki izin");
        }
    }

    private void validate(Barang barang) {
        if (barang.getNama() == null || barang.getNama().isEmpty()) {
            throw new ValidationException("Nama barang wajib diisi");
        }
        if (barang.getStok() < 0) {
            throw new ValidationException("Stok tidak boleh negatif");
        }
    }

    public void insertBarang(User user, Barang barang) {
        authorize(user);
        validate(barang);
        try {
            repository.insert(barang);
        } catch (Exception e) {
            throw new ValidationException("Gagal menyimpan barang");
        }
    }

    public void updateBarang(User user, Barang barang) {
        authorize(user);
        validate(barang);
        try {
            repository.update(barang);
        } catch (Exception e) {
            throw new ValidationException("Gagal memperbarui barang");
        }
    }

    public void delete(int id) {
        try {
            repository.delete(id);
        } catch (Exception e) {
            throw new ValidationException("Gagal menghapus barang");
        }
    }

    public void kurangiStok(int id, int jumlah) {
        try {
            Barang barang = repository.getBarangById(id);
            if (barang == null) {
                throw new ValidationException("Barang tidak ditemukan");
            }
            if (barang.getStok() < jumlah) {
                throw new ValidationException("Stok tidak mencukupi");
            }
            barang.setStok(barang.getStok() - jumlah);
            repository.update(barang);
        } catch (Exception e) {
            throw new ValidationException("Gagal mengurangi stok barang");
        }
    }

    public void deleteByLocation(String lokasi) {
        try {
            List<Barang> semuaBarang = repository.getAllBarang();
            for (Barang barang : semuaBarang) {
                if (barang.getLokasi().equalsIgnoreCase(lokasi)) {
                    repository.delete(barang.getId());
                }
            }
        } catch (Exception e) {
            throw new ValidationException("Gagal menghapus barang berdasarkan lokasi");
        }
    }

    public List<Barang> getAllBarang() {
        try {
            return repository.getAllBarang();
        } catch (Exception e) {
            throw new ValidationException("Gagal mengambil data barang");
        }
    }

    public Barang getBarangById(int id) {
        try {
            Barang barang = repository.getBarangById(id);
            if (barang == null) {
                throw new ValidationException("Barang tidak ditemukan");
            }
            return barang;
        } catch (Exception e) {
            throw new ValidationException("Error saat mengambil barang");
        }
    }
}