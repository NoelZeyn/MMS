package main.java.com.inventory.service;

import java.util.List;

import main.java.com.inventory.exception.AuthException;
import main.java.com.inventory.exception.ValidationException;
import main.java.com.inventory.model.Requisition;
import main.java.com.inventory.model.User;
import main.java.com.inventory.repository.RequisitionRepository;

public class RequisitionService {

    private final RequisitionRepository repository = new RequisitionRepository();

    private void authorize(User user) {
        if (!user.isAdmin() && !user.isManager() && !user.isStaff() && !user.isUser()) {
            throw new AuthException("Akses ditolak: role tidak memiliki izin");
        }
    }

public void insertRequisition(User user, Requisition requisition) {
    authorize(user);
    
    // Pastikan userId diisi dari user yang sedang login
    requisition.setUserId(user.getId());
    
    // Validasi data (termasuk hitung ulang TotalPrice)
    validate(requisition);
    
    try {
        repository.insert(requisition);
    } catch (Exception e) {
        // Log error asli ke console untuk debug
        e.printStackTrace(); 
        throw new ValidationException("Gagal simpan ke Database: " + e.getMessage());
    }
}

private void validate(Requisition req) {
    if (req.getUserId() <= 0) throw new ValidationException("Sesi User tidak valid.");
    if (req.getName() == null || req.getName().isBlank()) throw new ValidationException("Nama barang wajib diisi.");
    if (req.getQuantity() <= 0) throw new ValidationException("Quantity harus lebih dari 0.");
    if (req.getUnitPrice() <= 0) throw new ValidationException("Unit price harus lebih dari 0.");

    // PAKSA HITUNG TOTAL LAGI (Safety First)
    req.setTotalPrice(req.getQuantity() * req.getUnitPrice());
}

    public void updateRequisition(User user, Requisition requisition) {
        authorize(user);
        requisition.setUserId(user.getId());
        validate(requisition);
        try {
            repository.update(requisition);
        } catch (Exception e) {
            throw new ValidationException("Gagal memperbarui requisition");
        }
    }

    public void deleteRequisitionById(User user, int id) {
        authorize(user);
        try {
            repository.delete(id);
        } catch (Exception e) {
            throw new ValidationException("Gagal menghapus requisition");
        }
    }

    /* ================= READ ================= */

    public List<Requisition> getAllRequisitions(User user) {
        authorize(user);
        try {
            return repository.getAllRequisitions();
        } catch (Exception e) {
            throw new ValidationException("Gagal mengambil data requisition");
        }
    }

    public Requisition getRequisitionById(User user, int id) {
        authorize(user);
        try {
            Requisition req = repository.getRequisitionById(id);
            if (req == null) {
                throw new ValidationException("Requisition tidak ditemukan");
            }
            return req;
        } catch (Exception e) {
            throw new ValidationException("Gagal mengambil requisition");
        }
    }

    // public List<Requisition> getByUser(User user) {
    // return repository.getByUserId(user.getId());
    // }

}
