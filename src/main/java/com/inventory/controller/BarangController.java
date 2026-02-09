package com.inventory.controller;


import java.util.List;

import com.inventory.model.Barang;
import com.inventory.model.User;
import com.inventory.service.BarangService;

public class BarangController {

    private final BarangService service;

    public BarangController(BarangService service) {
        this.service = service;
    }

    public void insertBarang(User user, Barang barang) {
        service.insertBarang(user, barang);
    }

    public List<Barang> getAllBarang() {
        return service.getAllBarang();
    }

    public Barang getBarangById(int id) {
        return service.getBarangById(id);
    }

    public void updateBarang(User user, Barang barang) {
        service.updateBarang(user, barang);
    }

    public void deleteBarangById(int id) {
        service.delete(id);
    }

    public void kurangiStok(int id, int jumlah) {
        service.kurangiStok(id, jumlah);
    }
}
