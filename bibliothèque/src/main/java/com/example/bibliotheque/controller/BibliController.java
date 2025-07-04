package com.example.bibliotheque.controller;

import com.example.bibliotheque.model.Admin;
import com.example.bibliotheque.model.DemandeEmprunt;
import com.example.bibliotheque.service.AdherentService;
import com.example.bibliotheque.service.AdminService;
import com.example.bibliotheque.service.DemandeEmpruntService;
import com.example.bibliotheque.service.LivreService;

import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@Controller
public class BibliController {

    @Autowired
    private AdminService adminService;

    // Page d’accueil : / => index.html
    @GetMapping("/")
    public String home() {
        return "index";
    }

    // Affiche formulaire de login admin : /login-admin => admin/login-admin.html
    @GetMapping("/login-admin")
    public String loginPage() {
        return "admin/login-admin";
    }

    // Traite le login admin
    @PostMapping("/login-admin")
    public String login(@RequestParam String email,
                        @RequestParam String motdepasse,
                        HttpSession session,
                        Model model) {

        Optional<Admin> adminOpt = adminService.verifierConnexion(email, motdepasse);

        if (adminOpt.isPresent()) {
            session.setAttribute("adminConnecte", adminOpt.get());
            return "redirect:/admin/dashboard";
        } else {
            model.addAttribute("erreur", "Email ou mot de passe incorrect");
            return "admin/login-admin";
        }
    }

    @Autowired
    private DemandeEmpruntService demandeService;

    @Autowired 
    private LivreService livreService;

    @Autowired
    private AdherentService adherentService;
    // Dashboard admin : /admin/dashboard => admin/dashboard-admin.html
    @GetMapping("/admin/dashboard")
    public String dashboard(HttpSession session, Model model) {
        Admin admin = (Admin) session.getAttribute("adminConnecte");
        if (admin == null) {
            return "redirect:/login-admin";
        }
        
        // Ajouter les données nécessaires au dashboard
        model.addAttribute("demandes", demandeService.getDemandesEnAttente());
        model.addAttribute("livres", livreService.getAllLivres());
        model.addAttribute("adherents", adherentService.getAllAdherents());
        
        return "admin/dashboard-admin";
    }

    @PostMapping("/admin/traiter-demande")
    public String traiterDemande(@RequestParam Integer demandeId,
                                @RequestParam String statut,
                                @RequestParam(required = false) String commentaire,
                                HttpSession session) {
        if (session.getAttribute("adminConnecte") == null) {
            return "redirect:/login-admin";
        }
        
        DemandeEmprunt.StatutDemande nouveauStatut = DemandeEmprunt.StatutDemande.valueOf(statut);
        demandeService.traiterDemande(demandeId, nouveauStatut, commentaire);
        
        return "redirect:/admin/dashboard";
    }

    // Déconnexion : supprime session et redirige vers /
    @GetMapping("/admin/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/";
    }
    
}
