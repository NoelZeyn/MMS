package main.java.com.inventory.model;

public class Bidang {
    private int id;
    private String nama;

    public Bidang(int id, String nama) {
        this.id = id;
        this.nama = nama;
    }
    public Bidang (String nama) {
        this.nama = nama;
    }
    public int getId() {
        return id;
    }
    public String getName() {
        return nama;
    }
    public void setNama(String nama) {
        this.nama = nama;
    }

}
