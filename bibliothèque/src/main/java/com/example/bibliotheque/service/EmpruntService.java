package com.example.bibliotheque.service;

import java.time.LocalDate;
import java.util.List;

import org.springframework.stereotype.Service;

import com.example.bibliotheque.model.Adherent;
import com.example.bibliotheque.model.Emprunt;
import com.example.bibliotheque.repository.AdherentRepository;
import com.example.bibliotheque.repository.EmpruntRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class EmpruntService {
    private final EmpruntRepository empruntRepository;
    private final AdherentRepository adherentRepository;

    public List<Emprunt> getEmpruntsEnCours(Adherent adherent) {
        return empruntRepository.findByAdherent_IdAndRenduFalse(adherent.getId());
    }

  public void rendreLivre(Integer empruntId, Adherent adherent, LocalDate dateRetourEffective) {
    Emprunt emprunt = empruntRepository.findById(empruntId)
        .orElseThrow(() -> new RuntimeException("Emprunt introuvable"));

    if (!emprunt.getAdherent().getId().equals(adherent.getId())) {
        throw new RuntimeException("Cet emprunt ne vous appartient pas.");
    }

    emprunt.setRendu(true);
    emprunt.setDateRetourEffective(dateRetourEffective);

    if (emprunt.getDateRetourPrevue().isBefore(dateRetourEffective)) {
        adherent.setStatut(Adherent.Statut.bloque);
        adherent.setDateDeblocage(dateRetourEffective.plusDays(10));
        adherentRepository.save(adherent);
    }

    empruntRepository.save(emprunt);
}


}
