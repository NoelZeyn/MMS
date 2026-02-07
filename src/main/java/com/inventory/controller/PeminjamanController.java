package main.java.com.inventory.controller;


import main.java.com.inventory.model.Barang;
import main.java.com.inventory.model.User;
import main.java.com.inventory.service.PeminjamanService;

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
