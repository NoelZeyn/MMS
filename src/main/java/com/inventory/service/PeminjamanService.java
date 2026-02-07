package main.java.com.inventory.service;


import main.java.com.inventory.exception.ValidationException;
import main.java.com.inventory.model.Barang;
import main.java.com.inventory.model.Peminjaman;
import main.java.com.inventory.model.User;
import main.java.com.inventory.repository.PeminjamanRepository;

public class PeminjamanService {
    private PeminjamanRepository repository;

    public PeminjamanService(PeminjamanRepository repository) {
        this.repository = repository;
    }

    public void pinjamBarang(int id, Barang barang, User user, int jumlah) {
        if (barang.getStok() < jumlah) {
            throw new ValidationException("Stok tidak mencukupi");
        }

        barang.kurangiStok(jumlah);
        Peminjaman peminjaman = new Peminjaman(id, barang, user, jumlah);
        repository.save(peminjaman);
    }
}
