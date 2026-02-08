package main.java.com.inventory.model;

public class Barang {

    private int id;
    private String name;
    private int stok;
    private String lokasi;

    public Barang(String name, int stok, String lokasi) {
        // this.id = id;
        this.name = name;
        this.stok = stok;
        this.lokasi = lokasi;
    }
    // Constructor untuk READ dari DB (pakai id)
    public Barang(int id, String name, int stok, String lokasi) {
        this.id = id;
        this.name = name;
        this.stok = stok;
        this.lokasi = lokasi;
    }
    
    public int getId() {
        return id;
    }
    

    public String getName() {
        return name;
    }

    public int getStok() {
        return stok;
    }

    public String getLokasi() {
        return lokasi;
    }
        public void kurangiStok(int jumlah) {
        this.stok -= jumlah;
    }

    public void tambahStok(int jumlah) {
        this.stok += jumlah;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setStok(int stok) {
        this.stok = stok;
    }

    public void setLokasi(String lokasi) {
        this.lokasi = lokasi;
    }
}
