package com.example.bibliotheque.repository;

import com.example.bibliotheque.model.DemandeEmprunt;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import java.util.List;

public interface DemandeEmpruntRepository extends JpaRepository<DemandeEmprunt, Integer> {
    
    List<DemandeEmprunt> findByStatut(DemandeEmprunt.StatutDemande statut);
    
    List<DemandeEmprunt> findByAdherentId(Integer adherentId);
    
    @Query("SELECT d FROM DemandeEmprunt d WHERE d.adherent.id = :adherentId AND d.livre.id = :livreId AND d.statut = :statut")
    DemandeEmprunt findByAdherentIdAndLivreIdAndStatut(Integer adherentId, Integer livreId, DemandeEmprunt.StatutDemande statut);
}