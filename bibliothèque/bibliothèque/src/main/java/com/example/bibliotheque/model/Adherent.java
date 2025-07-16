package com.example.bibliotheque.model;

import java.time.LocalDate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Adherent {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String nom;
    private String prenom;

    @Column(unique = true)
    private String email;

    private String motDePasse;
    private LocalDate dateNaissance;

    @Builder.Default
    @Enumerated(EnumType.STRING)
    private Statut statut = Statut.actif;


    private LocalDate dateInscription;

    public enum Statut {
        actif, bloque
    }

    private LocalDate bloqueJusqua;

    @Column(name = "date_deblocage")
    private LocalDate dateDeblocage;

    public boolean estBloque() {
        return dateDeblocage != null && dateDeblocage.isAfter(LocalDate.now());
    }


    
}