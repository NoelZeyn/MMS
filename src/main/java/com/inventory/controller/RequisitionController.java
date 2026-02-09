package com.inventory.controller;

import java.util.List;

import com.inventory.model.Requisition;
import com.inventory.model.User;
import com.inventory.service.RequisitionService;
public class RequisitionController {
    private final RequisitionService service;

    public RequisitionController(RequisitionService service) {
        this.service = service;
    }

    // --- Tambahkan Metode Ini ---
    public List<Requisition> getMyPendingTasks(User user) throws Exception {
        return service.getMyPendingTasks(user);
    }

    public void insertRequisition(User user, Requisition requisition) {
        service.insertRequisition(user, requisition);
    }

    public List<Requisition> getAllRequisitions(User user) throws Exception {
        return service.getAllRequisitions(user);
    }

    public Requisition getRequisitionById(User user, int id) throws Exception {
        return service.getRequisitionById(user, id);
    }

    public void updateRequisition(User user, Requisition requisition) throws Exception {
        service.updateRequisition(user, requisition);
    }

    public void deleteRequisitionById(User user, int id) {
        service.deleteRequisitionById(user, id);
    }

    // --- Approval Methods ---
    public void approveAsman(User user, int id) throws Exception { service.approveAsman(user, id); }
    public void rejectAsman(User user, int id) throws Exception { service.rejectAsman(user, id); }
    public void approveManager(User user, int id) throws Exception { service.approveManager(user, id); }
    public void rejectManager(User user, int id) throws Exception { service.rejectManager(user, id); }
    public void approveBudget(User user, int id) throws Exception { service.approveBudget(user, id); }
    public void rejectBudget(User user, int id) throws Exception { service.rejectBudget(user, id); }
    public void approveAdmin(User user, int id) throws Exception { service.approveAdmin(user, id); }
    public void rejectAdmin(User user, int id) throws Exception { service.rejectAdmin(user, id); }
}