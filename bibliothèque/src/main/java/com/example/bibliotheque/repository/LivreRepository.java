package com.example.bibliotheque.repository;

import com.example.bibliotheque.model.Livre;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LivreRepository extends JpaRepository<Livre, Integer> {
}
