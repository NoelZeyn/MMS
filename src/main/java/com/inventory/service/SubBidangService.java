package com.inventory.service;

import java.util.List;

import com.inventory.exception.AuthException;
import com.inventory.exception.ValidationException;
import com.inventory.model.SubBidang;
import com.inventory.model.User;
import com.inventory.repository.SubBidangRepository;

public class SubBidangService {
    private final SubBidangRepository repository = new SubBidangRepository();

    private void authorize(User user) {
        if (user == null || (!user.isAdmin() && !user.isManager() && !user.isSuperAdmin())) {
            throw new AuthException("Akses ditolak: role tidak memiliki izin");
        }
    }

    public List<SubBidang> getAllSubBidangs() {
        try {
            return repository.getAllSubBidangs();
        } catch (Exception e) {
            throw new ValidationException("Gagal mengambil semua data subbidang : ");
        }
    }

    public void insertSubBidang(User user, String name, int bidangId) {
        authorize(user);
        try {
            SubBidang subBidang = new SubBidang(name, bidangId);
            repository.insertSubBidang(subBidang);
        } catch (Exception e) {
            System.err.println("CRITICAL: Gagal mencatat ");
        }
    }

    public void updateSubBidang(User user, int id, String name, int bidangId) {
        authorize(user);
        try {
            SubBidang subBidang = new SubBidang(id, name, bidangId);
            repository.updateSubBidang(subBidang);
        } catch (Exception e) {
            throw new ValidationException("Gagal memperbarui data subbidang pengguna");
        }
    }

    public void getSubBidangById(User user, int id) {
        authorize(user);
        try {
            repository.getSubBidangById(id);
        } catch (Exception e) {
            throw new ValidationException("Gagal mengambil subbidang");
        }
    }

    public void deleteSubBidangById(User user, int id) {
        authorize(user);
        try {
            repository.deleteSubBidangById(id);
        } catch (Exception e) {
            throw new ValidationException("Gagal menghapus subbidang");
        }
    }
}
