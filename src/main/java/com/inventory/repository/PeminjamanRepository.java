package main.java.com.inventory.repository;


import main.java.com.inventory.model.Peminjaman;
import java.util.ArrayList;
import java.util.List;

public class PeminjamanRepository {
    private List<Peminjaman> dataPeminjaman = new ArrayList<>();

    public void save(Peminjaman peminjaman) {
        dataPeminjaman.add(peminjaman);
    }

    public List<Peminjaman> getAllPeminjaman() {
        return dataPeminjaman;
    }
}
