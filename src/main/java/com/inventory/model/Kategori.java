package com.inventory.model;
public class Kategori {
    private int id;
    private String name;

    public Kategori(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}
