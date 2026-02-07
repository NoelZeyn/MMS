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
    public Peminjaman(Barang barang, User peminjam, int jumlah) {
        this.barang = barang;
        this.peminjam = peminjam;
        this.jumlah = jumlah;
        this.tanggalPinjam = LocalDate.now();
    }

    public int getId() {
        return id;
    }

    public User getPeminjam() {
        return peminjam;
    }

    public void setPeminjam(User peminjam) {
        this.peminjam = peminjam;
    }

    public LocalDate getTanggalPinjam() {
        return tanggalPinjam;
    }

    public Barang getBarang() {
        return barang;
    }

    public int getJumlah() {
        return jumlah;
    }
}
