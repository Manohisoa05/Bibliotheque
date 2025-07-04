package com.example.bibliotheque.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.bibliotheque.model.Admin;
import com.example.bibliotheque.repository.AdminRepository;

@Service
public class AdminService {

    @Autowired
    private AdminRepository adminRepository;

    public Optional<Admin> verifierConnexion(String email, String motDePasse) {
        return adminRepository.findByEmailAndMotDePasse(email, motDePasse);
    }
}
