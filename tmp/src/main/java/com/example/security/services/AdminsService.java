package com.example.security.services;

import com.example.security.objects.Admin;
import com.example.security.repositories.AdminsRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class AdminsService {
    private final AdminsRepository adminsRepository;
    private List<Admin> admins = new ArrayList<>();

    public AdminsService(AdminsRepository adminsRepository) {
        this.adminsRepository = adminsRepository;
    }

    public List<Admin> getAdminsByParams(Map<String, Object> params) {
        UUID id = null;
        String firstname = (String) params.get("firstname");
        String lastname = (String) params.get("lastname");
        String email = (String) params.get("email");
        String username = (String) params.get("username");
        String office = (String) params.get("office");
        String department = (String) params.get("department");

        if (params.containsKey("id") && (!params.get("id").equals(""))) {
            id = (UUID) params.get("id");
        }

        return adminsRepository.findAdminsByParams(id, firstname, lastname, email, username, office, department);
    }

    public void updateAdmins() {
        admins = adminsRepository.findAll();
    }

    public void saveAdmin(Admin admin) {
        adminsRepository.save(admin);
    }

    @Transactional
    public void updateAdmin(UUID id, Admin admin) {
        adminsRepository.updateAdmin(id, admin.getFirstname(), admin.getLastname(), admin.getEmail(), admin.getUsername(), admin.getOffice(), admin.getDepartment());
    }

//    @Transactional
//    public void addAdmins(String name, String surname) {
//        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
//        Date date = new Date();
//        Date createdAt = new Date(formatter.format(date));
//        Date updatedAt = new Date(formatter.format(date));
//        Admin admin = new Admin(name, surname, createdAt, updatedAt);
//        admins.add(admin);
//        adminsRepository.save(admin);
//    }
//
//    @Transactional
//    public void updateAdmins(Admin admin1, String newName, String newSurname) {
//        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
//        adminsRepository.findById(admin1.getId()).ifPresent(a1 -> {
//            a1.setName(newName);
//            a1.setSurname(newSurname);
//            a1.setUpdatedAt(new Date(formatter.format(new Date())));
//            adminRepository.save(a1);
//        });
//        adminsRepository.delete(admin1);
//    }
//
//    @Transactional
//    public void deleteAdmins(Admin admin1) {
//        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
//        adminsRepository.findById(admin1.getId()).ifPresent(a1 -> {
//            a1.setDeleted(true);
//            a1.setUpdatedAt(new Date(formatter.format(new Date())));
//            adminsRepository.save(a1);
//        });
//        adminsRepository.delete(admin1);
//    }
//
//    @Transactional
//    public List<Admin> getAdminByName(String name) {
//        return adminsRepository.findByName(name);
//    }
//
//    @Transactional
//    public List<Admin> getAdminBySurname(String surname) {
//        return adminsRepository.findBySurname(surname);
//    }
//
//    @Transactional
//    public List<Admin> getAdminByNameAndSurname(String name, String surname) {
//        return adminsRepository.findByNameAndSurname(name, surname);
//    }
}
