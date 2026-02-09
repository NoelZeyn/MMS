package com.inventory.controller;


import com.inventory.model.Barang;
import com.inventory.model.User;
import com.inventory.service.PeminjamanService;

public class PeminjamanController {
    private PeminjamanService service;

    public PeminjamanController(PeminjamanService service) {
        this.service = service;
    }

    public void pinjam(int id, Barang barang, User user, int jumlah) {
        service.pinjamBarang(id, barang, user, jumlah);
        System.out.println("Peminjaman berhasil");
    }
}
