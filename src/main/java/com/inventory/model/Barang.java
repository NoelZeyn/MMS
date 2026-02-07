package main.java.com.inventory.model;

public class Barang {

    private int id;
    private String nama;
    private int stok;
    private String lokasi;

    public Barang(String nama, int stok, String lokasi) {
        // this.id = id;
        this.nama = nama;
        this.stok = stok;
        this.lokasi = lokasi;
    }
    // Constructor untuk READ dari DB (pakai id)
    public Barang(int id, String nama, int stok, String lokasi) {
        this.id = id;
        this.nama = nama;
        this.stok = stok;
        this.lokasi = lokasi;
    }
    
    public int getId() {
        return id;
    }
    

    public String getNama() {
        return nama;
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

    public void setNama(String nama) {
        this.nama = nama;
    }

    public void setStok(int stok) {
        this.stok = stok;
    }

    public void setLokasi(String lokasi) {
        this.lokasi = lokasi;
    }
}
