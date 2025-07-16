package com.example.bibliotheque.service;

import com.example.bibliotheque.model.Adherent;
import com.example.bibliotheque.repository.AdherentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AdherentService {
    private final AdherentRepository repository;

    public void inscrire(Adherent adherent) {
        adherent.setDateInscription(LocalDate.now());
        repository.save(adherent);
    }

    public Adherent authentifier(String email, String motDePasse) {
        return repository.findByEmail(email)
            .filter(a -> a.getMotDePasse().equals(motDePasse))
            .orElse(null);
    }
    public Adherent getAdherentById(Integer id) {
    return repository.findById(id)
        .orElseThrow(() -> new RuntimeException("Adhérent introuvable"));
}


    public List<Adherent> getAllAdherents() {
        return repository.findAll();
    }
    public void save(Adherent adherent) {
    repository.save(adherent);
}


}

