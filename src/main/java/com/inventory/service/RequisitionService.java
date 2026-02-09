package com.inventory.service;

import java.util.List;

import com.inventory.exception.AuthException;
import com.inventory.exception.ValidationException;
import com.inventory.model.Requisition;
import com.inventory.model.RequisitionStatus;
import com.inventory.model.User;
import com.inventory.repository.RequisitionRepository;

public class RequisitionService {

    private final RequisitionRepository repository = new RequisitionRepository();

    private void authorize(User user) {
        // Memastikan user memiliki role yang valid untuk mengakses sistem requisition
        if (user == null) throw new AuthException("Sesi tidak valid");
    }

    /* ================= CREATE & UPDATE ================= */

    public void insertRequisition(User user, Requisition requisition) {
        authorize(user);
        requisition.setUserId(user.getId());
        requisition.setStatus(RequisitionStatus.PENDING_ASMAN);
        validate(requisition);

        try {
            repository.insert(requisition);
        } catch (Exception e) {
            throw new ValidationException("Gagal simpan ke Database: " + e.getMessage());
        }
    }

    public void updateRequisition(User user, Requisition requisition) throws Exception {
        authorize(user);
        Requisition existing = getRequisitionById(user, requisition.getId());

        // Hanya bisa edit jika masih di tahap awal (PENDING_ASMAN)
        if (existing.getStatus() != RequisitionStatus.PENDING_ASMAN && 
            existing.getStatus() != RequisitionStatus.SUBMITTED) {
            throw new ValidationException("Data sudah diproses dan tidak bisa diubah");
        }

        if (existing.getUserId() != user.getId() && !user.isAdmin()) {
            throw new AuthException("Hanya pemilik atau Admin yang boleh mengubah data");
        }

        requisition.setUserId(existing.getUserId());
        requisition.setStatus(existing.getStatus());
        validate(requisition);

        repository.update(requisition);
    }

    private void validate(Requisition req) {
        if (req.getUserId() <= 0) throw new ValidationException("User ID tidak valid.");
        if (req.getName() == null || req.getName().isBlank()) throw new ValidationException("Nama barang wajib diisi.");
        if (req.getQuantity() <= 0) throw new ValidationException("Quantity harus lebih dari 0.");
        if (req.getUnitPrice() <= 0) throw new ValidationException("Harga satuan harus lebih dari 0.");

        req.setTotalPrice(req.getQuantity() * req.getUnitPrice());
    }

    /* ================= APPROVAL WORKFLOW ================= */

    // --- TAHAP 1: ASMAN ---
    public void approveAsman(User user, int id) throws Exception {
        if (!user.isAsman()) throw new AuthException("Otoritas Asisten Manager diperlukan");
        
        Requisition req = findAndValidate(id, RequisitionStatus.PENDING_ASMAN);
        // Set status ke disetujui asman, lalu lompat ke PENDING tahap berikutnya
        req.setStatus(RequisitionStatus.PENDING_MANAGER); 
        repository.update(req);
    }

    public void rejectAsman(User user, int id) throws Exception {
        if (!user.isAsman()) throw new AuthException("Otoritas Asisten Manager diperlukan");
        
        Requisition req = findAndValidate(id, RequisitionStatus.PENDING_ASMAN);
        req.setStatus(RequisitionStatus.REJECTED_ASMAN);
        repository.update(req);
    }

    // --- TAHAP 2: MANAGER ---
    public void approveManager(User user, int id) throws Exception {
        if (!user.isManager()) throw new AuthException("Otoritas Manager diperlukan");

        Requisition req = findAndValidate(id, RequisitionStatus.PENDING_MANAGER);
        req.setStatus(RequisitionStatus.PENDING_BUDGET);
        repository.update(req);
    }

    public void rejectManager(User user, int id) throws Exception {
        if (!user.isManager()) throw new AuthException("Otoritas Manager diperlukan");

        Requisition req = findAndValidate(id, RequisitionStatus.PENDING_MANAGER);
        req.setStatus(RequisitionStatus.REJECTED_MANAGER);
        repository.update(req);
    }

    // --- TAHAP 3: BUDGET (ANGGARAN) ---
    public void approveBudget(User user, int id) throws Exception {
        // Perbaikan: Gunakan method check role yang benar
        if (!user.isBudget()) throw new AuthException("Otoritas Bagian Anggaran diperlukan");

        Requisition req = findAndValidate(id, RequisitionStatus.PENDING_BUDGET);
        req.setStatus(RequisitionStatus.PENDING_ADMIN);
        repository.update(req);
    }

    public void rejectBudget(User user, int id) throws Exception {
        if (!user.isBudget()) throw new AuthException("Otoritas Bagian Anggaran diperlukan");

        Requisition req = findAndValidate(id, RequisitionStatus.PENDING_BUDGET);
        req.setStatus(RequisitionStatus.REJECTED_BUDGET);
        repository.update(req);
    }

    // --- TAHAP 4: ADMIN (FINAL) ---
    public void approveAdmin(User user, int id) throws Exception {
        if (!user.isAdmin()) throw new AuthException("Otoritas Admin Umum diperlukan");

        Requisition req = findAndValidate(id, RequisitionStatus.PENDING_ADMIN);
        req.setStatus(RequisitionStatus.APPROVED_ADMIN); // Final Approval
        repository.update(req);
    }

    public void rejectAdmin(User user, int id) throws Exception {
        if (!user.isAdmin()) throw new AuthException("Otoritas Admin Umum diperlukan");

        Requisition req = findAndValidate(id, RequisitionStatus.PENDING_ADMIN);
        req.setStatus(RequisitionStatus.REJECTED_ADMIN);
        repository.update(req);
    }

    /* ================= HELPER & READ ================= */

    private Requisition findAndValidate(int id, RequisitionStatus expectedStatus) throws Exception {
        Requisition req = repository.getRequisitionById(id);
        if (req == null) throw new ValidationException("Data Requisition tidak ditemukan");
        if (req.getStatus() != expectedStatus) 
            throw new ValidationException("Status saat ini (" + req.getStatus() + ") tidak sesuai untuk aksi ini.");
        return req;
    }

    public List<Requisition> getAllRequisitions(User user) throws Exception {
        authorize(user);
        return repository.getAllRequisitions();
    }

    public Requisition getRequisitionById(User user, int id) throws Exception {
        authorize(user);
        Requisition req = repository.getRequisitionById(id);
        if (req == null) throw new ValidationException("Requisition tidak ditemukan");
        return req;
    }

    public void deleteRequisitionById(User user, int id) {
        authorize(user);
        // Opsional: Batasi hapus hanya jika status masih awal
        repository.delete(id);
    }
}