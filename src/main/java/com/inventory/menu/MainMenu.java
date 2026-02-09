package com.inventory.menu;

import java.util.Scanner;

import com.inventory.model.User;

public class MainMenu {

    private final Scanner scanner;
    private final User user;

    public MainMenu(Scanner scanner, User user) {
        this.scanner = scanner;
        this.user = user;
    }

    public void start() {
        while (true) {
            System.out.println("=== Inventory Management ===");
            System.out.println("1. Manajemen Barang");
            System.out.println("2. Keluar");
            System.out.print("Pilih opsi: ");

            String choice = scanner.nextLine();

            switch (choice) {
                case "1" -> new BarangMenu(scanner, user).start();
                case "2" -> {
                    System.out.println("Keluar dari aplikasi.");
                    return;
                }
                default -> System.out.println("Opsi tidak valid.");
            }
        }
    }
}
