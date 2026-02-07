package main.java.com.inventory.menu;

import main.java.com.inventory.controller.BarangController;
import main.java.com.inventory.model.Barang;
import main.java.com.inventory.model.User;
import main.java.com.inventory.security.InputSanitizer;
import main.java.com.inventory.service.BarangService;

import java.util.Scanner;

public class BarangMenu {

    private final Scanner scanner;
    private final User user;
    private final BarangController controller;

    public BarangMenu(Scanner scanner, User user) {
        this.scanner = scanner;
        this.user = user;
        this.controller = new BarangController(new BarangService());
    }

    public void start() {
        while (true) {
            System.out.println("=== Manajemen Barang ===");
            System.out.println("1. Tambah Barang");
            System.out.println("2. Lihat Semua Barang");
            System.out.println("3. Manajemen Detail Barang");
            System.out.println("4. Kembali");
            System.out.print("Pilih opsi: ");

            String choice = scanner.nextLine();

            switch (choice) {
                case "1":
                    insertBarang();
                    break;
                case "2":
                    getAllBarang();
                    break;
                case "3":
                    manageDetailBarang();
                    break;
                case "4":
                    return;
                default:
                    System.out.println("Opsi tidak valid.");
            }
        }
    }

    private void insertBarang() {
        try {
            System.out.print("Nama Barang: ");
            String nama = InputSanitizer.sanitize(scanner.nextLine());

            System.out.print("Stok Barang: ");
            int stok = Integer.parseInt(scanner.nextLine());

            System.out.print("Lokasi Barang: ");
            String lokasi = InputSanitizer.sanitize(scanner.nextLine());

            controller.insertBarang(user, new Barang(nama, stok, lokasi));
            System.out.println("Barang berhasil ditambahkan.");
        } catch (Exception e) {
            System.out.println("Terjadi Error: " + e.getMessage());
        }
    }

    private void getAllBarang() {
        try {
            System.out.println("=== Daftar Semua Barang ===");
            controller.getAllBarang().forEach(b -> {
                System.out.println("ID    : " + b.getId());
                System.out.println("Nama  : " + b.getNama());
                System.out.println("Stok  : " + b.getStok());
                System.out.println("Lokasi: " + b.getLokasi());
                System.out.println("-----------------------");
            });
        } catch (Exception e) {
            System.out.println("Terjadi Error: " + e.getMessage());
        }
    }

    private void manageDetailBarang() {
        try {
            System.out.print("Masukkan ID Barang: ");
            int id = Integer.parseInt(scanner.nextLine());

            Barang barang = controller.getBarangById(id);

            while (true) {
                System.out.println("=== Manajemen Detail Barang ===");
                System.out.println("ID    : " + barang.getId());
                System.out.println("Nama  : " + barang.getNama());
                System.out.println("Stok  : " + barang.getStok());
                System.out.println("Lokasi: " + barang.getLokasi());
                System.out.println("1. Kurangi Stok");
                System.out.println("2. Perbarui Barang");
                System.out.println("3. Hapus Barang");
                System.out.println("4. Kembali");
                System.out.print("Pilih opsi: ");

                String choice = scanner.nextLine();

                switch (choice) {
                    case "1":
                        kurangiStok(barang.getId());
                        barang = controller.getBarangById(barang.getId());
                        break;
                    case "2":
                        perbaruiBarang(barang);
                        break;
                    case "3":
                        controller.deleteBarangById(barang.getId());
                        System.out.println("Barang berhasil dihapus.");
                        return;
                    case "4":
                        return;
                    default:
                        System.out.println("Opsi tidak valid.");
                }
            }
        } catch (Exception e) {
            System.out.println("Terjadi Error: " + e.getMessage());
        }
    }

    private void kurangiStok(int id) {
        try {
            System.out.print("Jumlah pengurangan stok: ");
            int jumlah = Integer.parseInt(scanner.nextLine());
            controller.kurangiStok(id, jumlah);
            System.out.println("Stok berhasil dikurangi.");
        } catch (Exception e) {
            System.out.println("Terjadi Error: " + e.getMessage());
        }
    }

    private void perbaruiBarang(Barang barang) {
        try {
            System.out.print("Nama baru: ");
            barang.setNama(InputSanitizer.sanitize(scanner.nextLine()));

            System.out.print("Stok baru: ");
            barang.setStok(Integer.parseInt(scanner.nextLine()));

            System.out.print("Lokasi baru: ");
            barang.setLokasi(InputSanitizer.sanitize(scanner.nextLine()));

            controller.updateBarang(user, barang);
            System.out.println("Barang berhasil diperbarui.");
        } catch (Exception e) {
            System.out.println("Terjadi Error: " + e.getMessage());
        }
    }
}
