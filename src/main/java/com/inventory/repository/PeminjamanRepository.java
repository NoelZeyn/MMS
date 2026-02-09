package com.inventory.repository;


import java.util.ArrayList;
import java.util.List;

import com.inventory.model.Peminjaman;

public class PeminjamanRepository {
    private List<Peminjaman> dataPeminjaman = new ArrayList<>();

    public void save(Peminjaman peminjaman) {
        dataPeminjaman.add(peminjaman);
    }

    public List<Peminjaman> getAllPeminjaman() {
        return dataPeminjaman;
    }
}
