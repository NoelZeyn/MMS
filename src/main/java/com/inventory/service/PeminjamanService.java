package com.inventory.service;


import com.inventory.exception.ValidationException;
import com.inventory.model.Barang;
import com.inventory.model.Peminjaman;
import com.inventory.model.User;
import com.inventory.repository.PeminjamanRepository;

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
