// package com.inventory.app;

// import com.inventory.controller.BarangController;
// import com.inventory.controller.PeminjamanController;
// import com.inventory.model.*;
// import com.inventory.repository.*;
// import com.inventory.service.*;

// public class InventoryApplication {

//     public static void main(String[] args) {

//         BarangRepository barangRepo = new BarangRepository();
//         PeminjamanRepository pinjamRepo = new PeminjamanRepository();

//         BarangService barangService = new BarangService(barangRepo);
//         PeminjamanService peminjamanService = new PeminjamanService(pinjamRepo);

//         BarangController barangController = new BarangController(barangService);
//         PeminjamanController peminjamanController = new PeminjamanController(peminjamanService);

//         Kategori elektronik = new Kategori(1, "Elektronik");
//         Barang laptop = new Barang(1, "Laptop Lenovo", elektronik, 10, "Gudang A");

//         barangController.insertBarang(laptop);
//         barangController.tampilkanBarang();

//         User user = new User(1, "Ahmad", "Mahasiswa");

//         peminjamanController.pinjam(1, laptop, user, 2);

//         barangController.tampilkanBarang();
//     }
// }
