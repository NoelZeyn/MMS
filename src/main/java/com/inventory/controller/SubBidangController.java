package com.inventory.controller;

import java.util.List;

import com.inventory.model.SubBidang;
import com.inventory.model.User;
import com.inventory.service.SubBidangService;

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

    public void deleteSubBidangById(User user, int id) {
        service.deleteSubBidangById(user, id);
    }
}
