package com.example.bibliotheque.controller;


import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.bibliotheque.model.Livre;
import com.example.bibliotheque.service.LivreService;

import lombok.RequiredArgsConstructor;

@Controller
@RequestMapping("/admin/livres")
@RequiredArgsConstructor
public class LivreController {

    private final LivreService livreService;

    @GetMapping("/ajouter")
    public String afficherFormulaireAjout(Model model) {
        model.addAttribute("livre", new Livre());
        model.addAttribute("statuts", Livre.Statut.values());
        return "admin/ajouter-livre";
    }

    @PostMapping("/ajouter")
    public String ajouterLivre(@ModelAttribute Livre livre) {
        livreService.saveLivre(livre);
        return "redirect:/admin/dashboard";
    }

    @GetMapping("/modifier/{id}")
    public String afficherFormulaireModification(@PathVariable Integer id, Model model) {
        Livre livre = livreService.getLivreById(id);
        model.addAttribute("livre", livre);
        model.addAttribute("statuts", Livre.Statut.values());
        return "admin/ajouter-livre"; // réutilise le même template que pour l'ajout
    }

    @PostMapping("/modifier/{id}")
    public String modifierLivre(@PathVariable Integer id, @ModelAttribute Livre livre) {
        livre.setId(id);
        livreService.saveLivre(livre);
        return "redirect:/admin/dashboard";
    }

    @PostMapping("/supprimer/{id}")
    public String supprimerLivre(@PathVariable Integer id) {
        livreService.deleteLivre(id);
        return "redirect:/admin/dashboard";
    }

  


    
}
