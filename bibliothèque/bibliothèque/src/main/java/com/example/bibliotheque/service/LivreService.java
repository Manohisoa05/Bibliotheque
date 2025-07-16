package com.example.bibliotheque.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.bibliotheque.model.Livre;
import com.example.bibliotheque.repository.LivreRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class LivreService {

    private final LivreRepository repository;

    // Lire tous les livres
    public List<Livre> getAllLivres() {
        return repository.findAll();
    }

    // Lire un livre par son ID
    public Livre getLivreById(Integer id) {
        return repository.findById(id)
            .orElseThrow(() -> new RuntimeException("Livre non trouvé"));
    }

    // Créer ou modifier un livre
    public Livre saveLivre(Livre livre) {
        return repository.save(livre);
    }

    // Supprimer un livre
    public void deleteLivre(Integer id) {
        repository.deleteById(id);
    }
    
}
