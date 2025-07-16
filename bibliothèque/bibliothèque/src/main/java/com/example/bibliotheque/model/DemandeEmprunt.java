package com.example.bibliotheque.model;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DemandeEmprunt {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "id_adherent")
    private Adherent adherent;

    @ManyToOne
    @JoinColumn(name = "id_livre")
    private Livre livre;

    @Builder.Default
    @Enumerated(EnumType.STRING)
    private TypeAction typeAction = TypeAction.EMPRUNT;

    @Builder.Default
    @Enumerated(EnumType.STRING)
    private StatutDemande statut = StatutDemande.EN_ATTENTE;

    private LocalDate dateDemande;
    private LocalDate dateTraitement;
    private String commentaireAdmin;
    private LocalDate dateRetourPrevue;

    public enum TypeAction {
        EMPRUNT, 
        RESERVATION, 
        LECTURE_SUR_PLACE, 
        PROLONGEMENT // ✅ AJOUTÉ !
    }

    public enum StatutDemande {
        EN_ATTENTE, ACCEPTEE, REFUSEE
    }
}
