package main.java.com.inventory.model;

public class Requisition {
    private int id;
    private String name;
    private String specification;
    private int quantity;
    private int unitPrice;
    private int totalPrice;
    private String vendor;
    private String justification;

    public Requisition(int id, String name, String specification, int quantity, int unitPrice, int totalPrice,
            String vendor, String justification) {
        this.id = id;
        this.name = name;
        this.specification = specification;
        this.quantity = quantity;
        this.unitPrice = unitPrice;
        this.totalPrice = totalPrice;
        this.vendor = vendor;
        this.justification = justification;
    }

    public Requisition(String name, String specification, int quantity, int unitPrice, int totalPrice, String vendor,
            String justification) {
        // this.id = id;
        this.name = name;
        this.specification = specification;
        this.quantity = quantity;
        this.unitPrice = unitPrice;
        this.totalPrice = totalPrice;
        this.vendor = vendor;
        this.justification = justification;
    }

    public int getId() {
        return this.id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return this.name;
    }

    public void setSpecification(String specification) {
        this.specification = specification;
    }

    public String getSpecification() {
        return this.specification;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public int getQuantity() {
        return this.quantity;
    }

    public void setUnitPrice(int unitPrice) {
        this.unitPrice = unitPrice;
    }

    public int getUnitPrice() {
        return this.unitPrice;
    }

    public void setTotalPrice(int totalPrice) {
        this.totalPrice = totalPrice;
    }

    public int getTotalPrice() {
        return this.totalPrice;
    }

    public void setVendor(String vendor){
        this.vendor = vendor;
    }

    public String getVendor(){
        return this.vendor;
    }

        public void setJustification(String justification){
        this.justification = justification;
    }

    public String getJustification(){
        return this.justification;
    }

}
