package com.example.bibliotheque.controller;

import com.example.bibliotheque.model.Livre;
import com.example.bibliotheque.service.LivreService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
@RequiredArgsConstructor
public class PublicLivreController {

    private final LivreService livreService;

 @GetMapping("/livre/{id}")
public String voirDetailsLivre(@PathVariable Integer id, Model model) {
    try {
        Livre livre = livreService.getLivreById(id);
        model.addAttribute("livre", livre);
        return "livre-details";
    } catch (RuntimeException e) {
        e.printStackTrace(); // Ajoute ça pour voir l'erreur complète dans la console
        return "redirect:/error"; // page erreur personnalisée
    }
}




}
