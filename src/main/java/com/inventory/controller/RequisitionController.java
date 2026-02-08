package main.java.com.inventory.controller;

import java.util.List;

import main.java.com.inventory.model.Requisition;
import main.java.com.inventory.model.User;
import main.java.com.inventory.service.RequisitionService;

public class RequisitionController {

    private final RequisitionService service;

    public RequisitionController(RequisitionService service) {
        this.service = service;
    }

    public void insertRequisition(User user, Requisition requisition) {
        service.insertRequisition(user, requisition);
    }

    public List<Requisition> getAllRequisitions(User user) {
        return service.getAllRequisitions(user);
    }

    public Requisition getRequisitionById(User user, int id) {
        return service.getRequisitionById(user, id);
    }

    public void updateRequistion(User user, Requisition requisition) {
        service.updateRequisition(user, requisition);
    }

    public void deleteRequisitionById(User user, int id) {
        service.deleteRequisitionById(user, id);
    }
}
