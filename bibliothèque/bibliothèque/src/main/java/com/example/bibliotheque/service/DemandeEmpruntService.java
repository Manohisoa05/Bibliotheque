package com.example.bibliotheque.service;

import java.time.LocalDate;
import java.util.List;

import org.springframework.stereotype.Service;

import com.example.bibliotheque.model.Adherent;
import com.example.bibliotheque.model.DemandeEmprunt;
import com.example.bibliotheque.model.Emprunt;
import com.example.bibliotheque.model.Livre;
import com.example.bibliotheque.repository.DemandeEmpruntRepository;
import com.example.bibliotheque.repository.EmpruntRepository;
import com.example.bibliotheque.repository.LivreRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class DemandeEmpruntService {

    private final DemandeEmpruntRepository repository;
    private final EmpruntRepository empruntRepository;
    private final LivreRepository livreRepository;

   public void creerDemande(Adherent adherent, Livre livre, DemandeEmprunt.TypeAction typeAction, LocalDate dateEmprunt) {
    if (adherent.getDateDeblocage() != null && adherent.getDateDeblocage().isAfter(LocalDate.now())) {
        throw new RuntimeException("Vous êtes bloqué jusqu'au " + adherent.getDateDeblocage() + ".");
    }

    int age = LocalDate.now().getYear() - adherent.getDateNaissance().getYear();
    if (livre.getRestrictionAge() > age) {
        throw new RuntimeException("Ce livre est réservé aux " + livre.getRestrictionAge() + "+ ans.");
    }

    DemandeEmprunt demandeExistante = repository.findByAdherentIdAndLivreIdAndStatut(
        adherent.getId(), livre.getId(), DemandeEmprunt.StatutDemande.EN_ATTENTE);

    if (demandeExistante != null) {
        throw new RuntimeException("Une demande est déjà en cours pour ce livre.");
    }

    DemandeEmprunt demande = DemandeEmprunt.builder()
        .adherent(adherent)
        .livre(livre)
        .typeAction(typeAction)
        .dateDemande(dateEmprunt != null ? dateEmprunt : LocalDate.now()) // ✅ Ici on prend la date choisie
        .build();

    repository.save(demande);
}



    public List<DemandeEmprunt> getDemandesEnAttente() {
        return repository.findByStatut(DemandeEmprunt.StatutDemande.EN_ATTENTE);
    }

    public List<DemandeEmprunt> getDemandesParAdherent(Integer adherentId) {
        return repository.findByAdherentId(adherentId);
    }

   public void traiterDemande(Integer demandeId, DemandeEmprunt.StatutDemande nouveauStatut, String commentaire) {
    DemandeEmprunt demande = repository.findById(demandeId)
        .orElseThrow(() -> new RuntimeException("Demande non trouvée"));

    Livre livre = demande.getLivre();

    // Vérifier la disponibilité avant traitement
    if (nouveauStatut == DemandeEmprunt.StatutDemande.ACCEPTEE &&
        (demande.getTypeAction() == DemandeEmprunt.TypeAction.EMPRUNT ||
         demande.getTypeAction() == DemandeEmprunt.TypeAction.RESERVATION)) {

        if (livre.getNbExemplaires() <= 0) {
            throw new RuntimeException("Aucun exemplaire disponible pour ce livre.");
        }

        // Décrémenter le nombre d'exemplaires
        livre.setNbExemplaires(livre.getNbExemplaires() - 1);

        // Si plus aucun exemplaire, on met à jour le statut du livre
        if (livre.getNbExemplaires() == 0) {
            livre.setStatut(Livre.Statut.EMPRUNTE);
        }

        livreRepository.save(livre);
    }

    // Créer l’emprunt si c'est un emprunt accepté
    if (nouveauStatut == DemandeEmprunt.StatutDemande.ACCEPTEE &&
    demande.getTypeAction() == DemandeEmprunt.TypeAction.EMPRUNT) {

    Emprunt emprunt = Emprunt.builder()
        .adherent(demande.getAdherent())
        .livre(demande.getLivre())
        .dateEmprunt(demande.getDateDemande())  // ✅ utilise la date saisie par l’adhérent
        .dateRetourPrevue(demande.getDateDemande().plusDays(7))
        .prolonge(false)
        .rendu(false)
        .build();

    empruntRepository.save(emprunt);
}


    demande.setStatut(nouveauStatut);
    demande.setDateTraitement(LocalDate.now());
    demande.setCommentaireAdmin(commentaire);
    repository.save(demande);
}

 private final DemandeEmpruntRepository demandeRepository;

public void creerDemandeProlongement(Adherent adherent, Integer empruntId) {
    Emprunt emprunt = empruntRepository.findById(empruntId)
        .orElseThrow(() -> new RuntimeException("Emprunt introuvable"));

    if (!emprunt.getAdherent().getId().equals(adherent.getId())) {
        throw new RuntimeException("Cet emprunt ne vous appartient pas.");
    }

    DemandeEmprunt demande = new DemandeEmprunt();
    demande.setAdherent(adherent);
    demande.setLivre(emprunt.getLivre());
    demande.setTypeAction(DemandeEmprunt.TypeAction.PROLONGEMENT);
    demande.setDateDemande(LocalDate.now());

    demandeRepository.save(demande);
}

}