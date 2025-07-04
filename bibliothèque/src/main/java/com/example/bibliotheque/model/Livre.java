package com.example.bibliotheque.model;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "livre")
public class Livre {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(length = 200)
    private String titre;

    @Column(length = 150)
    private String auteur;

    @Column(length = 150)
    private String editeur;

    private Integer anneeParution;

    @Column(length = 20)
    private String isbn;

    @Convert(converter = StatutConverter.class)
    @Column(columnDefinition = "ENUM('disponible', 'emprunte', 'reserve', 'lecture_sur_place') DEFAULT 'disponible'")
    private Statut statut = Statut.DISPONIBLE;

    private Integer restrictionAge = 0;

    private Integer nbExemplaires = 1;

    public enum Statut {
        DISPONIBLE, EMPRUNTE, RESERVE, LECTURE_SUR_PLACE
    }

    @Converter(autoApply = true)
    public static class StatutConverter implements AttributeConverter<Statut, String> {

        @Override
        public String convertToDatabaseColumn(Statut statut) {
            if (statut == null) return null;
            return statut.name().toLowerCase();  // Enum Java -> base en minuscules
        }

        @Override
        public Statut convertToEntityAttribute(String dbData) {
            if (dbData == null) return null;
            return Statut.valueOf(dbData.toUpperCase()); // base en minuscules -> Enum Java
        }
    }
}
