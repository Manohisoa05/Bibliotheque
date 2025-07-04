CREATE TABLE admin (
    id INT AUTO_INCREMENT PRIMARY KEY,
    email VARCHAR(100) UNIQUE,
    mot_de_passe VARCHAR(255),
    nom VARCHAR(100),
    prenom VARCHAR(100)
);

CREATE TABLE adherent (
    id INT AUTO_INCREMENT PRIMARY KEY,
    nom VARCHAR(100),
    prenom VARCHAR(100),
    email VARCHAR(100) UNIQUE,
    mot_de_passe VARCHAR(255),
    date_naissance DATE,
    statut ENUM('actif', 'bloque') DEFAULT 'actif',
    date_deblocage DATE,  -- utilisé si l’adhérent est bloqué pour retard
    date_inscription DATE
);

CREATE TABLE livre (
    id INT AUTO_INCREMENT PRIMARY KEY,
    titre VARCHAR(200),
    auteur VARCHAR(150),
    editeur VARCHAR(150),
    annee_parution INT,
    isbn VARCHAR(20),
    statut ENUM('disponible', 'emprunte', 'reserve', 'lecture_sur_place') DEFAULT 'disponible',
    restriction_age INT DEFAULT 0,  -- exemple : 18 si interdit aux mineurs
    nb_exemplaires INT DEFAULT 1
);

-- CREATE TABLE categorie (
--     id INT AUTO_INCREMENT PRIMARY KEY,
--     nom VARCHAR(100)
-- );

-- CREATE TABLE livre_categorie (
--     id_livre INT,
--     id_categorie INT,
--     PRIMARY KEY (id_livre, id_categorie),
--     FOREIGN KEY (id_livre) REFERENCES livre(id),
--     FOREIGN KEY (id_categorie) REFERENCES categorie(id)
-- );

CREATE TABLE emprunt (
    id INT AUTO_INCREMENT PRIMARY KEY,
    id_adherent INT,
    id_livre INT,
    date_emprunt DATE,
    date_retour_prevue DATE,
    date_retour_effective DATE,
    prolonge BOOLEAN DEFAULT FALSE,
    FOREIGN KEY (id_adherent) REFERENCES adherent(id),
    FOREIGN KEY (id_livre) REFERENCES livre(id)
);

CREATE TABLE reservation (
    id INT AUTO_INCREMENT PRIMARY KEY,
    id_adherent INT,
    id_livre INT,
    date_reservation DATE,
    date_expiration DATE,
    est_active BOOLEAN DEFAULT TRUE,
    FOREIGN KEY (id_adherent) REFERENCES adherent(id),
    FOREIGN KEY (id_livre) REFERENCES livre(id)
);

CREATE TABLE lecture_sur_place (
    id INT AUTO_INCREMENT PRIMARY KEY,
    id_adherent INT,
    id_livre INT,
    date_lecture DATE,
    FOREIGN KEY (id_adherent) REFERENCES adherent(id),
    FOREIGN KEY (id_livre) REFERENCES livre(id)
);

CREATE TABLE historique_blocage (
    id INT AUTO_INCREMENT PRIMARY KEY,
    id_adherent INT,
    date_debut DATE,
    date_fin DATE,
    raison VARCHAR(255),  -- ex: "retard retour livre"
    FOREIGN KEY (id_adherent) REFERENCES adherent(id)
);

CREATE TABLE demande_emprunt (
    id INT AUTO_INCREMENT PRIMARY KEY,
    id_adherent INT,
    id_livre INT,
    type_action ENUM('EMPRUNT', 'RESERVATION', 'LECTURE_SUR_PLACE'),
    statut ENUM('EN_ATTENTE', 'ACCEPTEE', 'REFUSEE') DEFAULT 'EN_ATTENTE',
    date_demande DATE,
    date_traitement DATE,
    commentaire_admin TEXT,
    FOREIGN KEY (id_adherent) REFERENCES adherent(id),
    FOREIGN KEY (id_livre) REFERENCES livre(id)
);