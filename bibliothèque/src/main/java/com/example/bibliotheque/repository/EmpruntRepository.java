package com.example.bibliotheque.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.bibliotheque.model.Emprunt;

public interface EmpruntRepository extends JpaRepository<Emprunt, Integer> {
    List<Emprunt> findByAdherent_IdAndRenduFalse(Integer adherentId);
}
