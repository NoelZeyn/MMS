package com.inventory.model;

public class Requisition {
    private int id;
    private int userId;
    private String name;
    private String specification;
    private int quantity;
    private int unitPrice;
    private int totalPrice;
    private RequisitionStatus status;
    private String vendor;
    private String justification;

    // Constructor untuk INSERT (Tanpa ID)
    public Requisition(int userId, String name, String specification, int quantity, int unitPrice, int totalPrice,
            RequisitionStatus status, String vendor, String justification) {
        this.userId = userId;
        this.name = name;
        this.specification = specification;
        this.quantity = quantity;
        this.unitPrice = unitPrice;
        this.totalPrice = totalPrice;
        this.status = status;
        this.vendor = vendor;
        this.justification = justification;
    }

    // Constructor untuk SELECT/UPDATE (Dengan ID)
    public Requisition(int id, int userId, String name, String specification, int quantity, int unitPrice,
            int totalPrice, RequisitionStatus status, String vendor, String justification) {
        this.id = id;
        this.userId = userId;
        this.name = name;
        this.specification = specification;
        this.quantity = quantity;
        this.unitPrice = unitPrice;
        this.totalPrice = totalPrice;
        this.status = status;
        this.vendor = vendor;
        this.justification = justification;
    }

    public int getId() {
        return id;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getUserId() {
        return userId;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setSpecification(String specification) {
        this.specification = specification;
    }

    public String getSpecification() {
        return specification;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setUnitPrice(int unitPrice) {
        this.unitPrice = unitPrice;
    }

    public int getUnitPrice() {
        return unitPrice;
    }

    public void setTotalPrice(int totalPrice) {
        this.totalPrice = totalPrice;
    }

    public RequisitionStatus getStatus() {
        return status;
    }

    public void setStatus(RequisitionStatus status) {
        this.status = status;
    }

    public int getTotalPrice() {
        return totalPrice;
    }

    public void setVendor(String vendor) {
        this.vendor = vendor;
    }

    public String getVendor() {
        return vendor;
    }

    public void setJustification(String justification) {
        this.justification = justification;
    }

    public String getJustification() {
        return justification;
    }

}
