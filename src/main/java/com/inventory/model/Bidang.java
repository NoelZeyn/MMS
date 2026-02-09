package com.inventory.model;

public class Bidang {
    private int id;
    private String name;

    public Bidang(int id, String name) {
        this.id = id;
        this.name = name;
    }
    public Bidang (String name) {
        this.name = name;
    }
    public int getId() {
        return id;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

}
