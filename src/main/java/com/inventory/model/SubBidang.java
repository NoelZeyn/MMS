package main.java.com.inventory.model;

public class SubBidang {
    private int id;
    private String name;
    private int bidangId;

    // Constructor untuk existing record (dengan id)
    public SubBidang(int id, String name, int bidangId) {
        this.id = id;
        this.name = name;
        this.bidangId = bidangId;
    }

    // Constructor untuk new record (tanpa id)
    public SubBidang(String name, int bidangId) {
        this.name = name;
        this.bidangId = bidangId;
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

    public int getBidangId() {
        return bidangId;
    }

    public void setBidangId(int bidangId) {
        this.bidangId = bidangId;
    }

    @Override
    public String toString() {
        return name; // tampil NAMA, bukan alamat object
    }

}
