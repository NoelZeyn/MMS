package main.java.com.inventory.model;

import java.time.LocalDate;

public class Peminjaman {
    private int id;
    private Barang barang;
    private User peminjam;
    private int jumlah;
    private LocalDate tanggalPinjam;

    public Peminjaman(int id, Barang barang, User peminjam, int jumlah) {
        this.id = id;
        this.barang = barang;
        this.peminjam = peminjam;
        this.jumlah = jumlah;
        this.tanggalPinjam = LocalDate.now();
    }

    public Barang getBarang() {
        return barang;
    }

    public int getJumlah() {
        return jumlah;
    }
}
