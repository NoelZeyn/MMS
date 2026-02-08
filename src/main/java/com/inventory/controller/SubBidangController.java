package main.java.com.inventory.controller;

import main.java.com.inventory.model.SubBidang;
import main.java.com.inventory.model.User;
import main.java.com.inventory.service.SubBidangService;

import java.util.List;

public class SubBidangController {
    private final SubBidangService service;

    public SubBidangController(SubBidangService service) {
        this.service = service;
    }

    public List<SubBidang> getAllSubBidangs() {
        return service.getAllSubBidangs();
    }

    public void insertSubBidang(User user, String name, int bidangId) {
        service.insertSubBidang(user, name, bidangId);
    }

    public void updateSubBidang(User user, int id, String name, int bidangId) {
        service.updateSubBidang(user, id, name, bidangId);
    }

    public void getSubBidangById(User user, int targetUserId) {
        service.getSubBidangById(user, targetUserId);
    }

    public void deleteSubBidang(User user, int id) {
        service.deleteSubBidang(user, id);
    }
}
