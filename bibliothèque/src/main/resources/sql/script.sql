INSERT INTO admin (email, mot_de_passe, nom, prenom)
VALUES (
  'admin@biblio.mg',
  'admin',
  'Rakoto',
  'Jean'
);

INSERT INTO adherent (nom, prenom, email, mot_de_passe, date_naissance, statut, date_deblocage, date_inscription) VALUES (
    'aaa', 'bbb', 'aaa@bbb.mg', '123', '2008-10-10', 'actif', NULL, CURDATE()
);
-- adherent 16 ans
INSERT INTO adherent (nom, prenom, email, mot_de_passe, date_naissance, statut, date_deblocage, date_inscription) VALUES (
    'ccc', 'ddd', 'ccc@ddd.mg', '123', '2009-07-03', 'actif', NULL, CURDATE()
);


INSERT INTO livre (titre, auteur, editeur, annee_parution, isbn, statut, restriction_age, nb_exemplaires) VALUES
('Le Petit Prince', 'Antoine de Saint-Exupéry', 'Gallimard', 1943, '9782070612758', 'disponible', 0, 5),
('1984', 'George Orwell', 'Secker & Warburg', 1949, '9780451524935', 'emprunte', 16, 3),
("Harry Potter à l'école des sorciers", 'J.K. Rowling', 'Bloomsbury', 1997, '9780747532699', 'reserve', 10, 10),
('Le Comte de Monte-Cristo', 'Alexandre Dumas', 'Penguin Classics', 1844, '9780140449266', 'disponible', 12, 2),
('La Métamorphose', 'Franz Kafka', 'Kurt Wolff Verlag', 1915, '9782070360024', 'lecture_sur_place', 0, 1),
('Le Seigneur des Anneaux', 'J.R.R. Tolkien', 'Allen & Unwin', 1954, '9780618640157', 'disponible', 14, 4),
('Game of Thrones', 'George R.R. Martin', 'Bantam Spectra', 1996, '9780553103540', 'emprunte', 18, 2),
("Le Journal d'Anne Frank", 'Anne Frank', 'Contact Publishing', 1947, '9780553296983', 'disponible', 12, 3),
('Les Misérables', 'Victor Hugo', 'A. Lacroix, Verboeckhoven & Cie.', 1862, '9780451419439', 'disponible', 0, 6),
('La Peste', 'Albert Camus', 'Gallimard', 1947, '9782070360420', 'reserve', 16, 3);
