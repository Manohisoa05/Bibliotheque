package com.example.bibliotheque.controller;

import com.example.bibliotheque.model.Adherent;
import com.example.bibliotheque.service.AdherentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
@RequiredArgsConstructor
public class PublicAdherentController {

    private final AdherentService adherentService;

    @GetMapping("/adherent/{id}")
    public String voirDetailsAdherent(@PathVariable Integer id, Model model) {
        try {
            Adherent adherent = adherentService.getAdherentById(id);
            model.addAttribute("adherent", adherent);
            return "adherent-details";
        } catch (RuntimeException e) {
            e.printStackTrace();
            return "redirect:/error"; // page erreur personnalisée
        }
    }
}
