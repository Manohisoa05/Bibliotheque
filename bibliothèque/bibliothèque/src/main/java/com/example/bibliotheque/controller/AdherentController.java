package com.example.bibliotheque.controller;

import com.example.bibliotheque.model.Adherent;
import com.example.bibliotheque.model.DemandeEmprunt;
import com.example.bibliotheque.model.Livre;
import com.example.bibliotheque.service.AdherentService;
import com.example.bibliotheque.service.DemandeEmpruntService;
import com.example.bibliotheque.service.EmpruntService;
import com.example.bibliotheque.service.LivreService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDate;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class AdherentController {

    private final AdherentService adherentService;
    private final LivreService livreService;
    private final EmpruntService empruntService;
    private final DemandeEmpruntService demandeEmpruntService;

    // ✅ PAGE LOGIN
    @GetMapping("/login-adherent")
    public String showLogin() {
        return "adherent/login";
    }

    // ✅ ACTION LOGIN
    @PostMapping("/login-adherent")
    public String login(@RequestParam String email,
                        @RequestParam String motDePasse,
                        HttpSession session,
                        Model model) {
        Adherent adherent = adherentService.authentifier(email, motDePasse);
        if (adherent != null) {
            session.setAttribute("adherent", adherent);
            return "redirect:/dashboard-adherent";
        } else {
            model.addAttribute("error", "Email ou mot de passe invalide.");
            return "adherent/login";
        }
    }

    // ✅ PAGE INSCRIPTION
    @GetMapping("/register-adherent")
    public String showRegister(Model model) {
        model.addAttribute("adherent", new Adherent());
        return "adherent/register";
    }

    // ✅ ACTION INSCRIPTION
    @PostMapping("/register-adherent")
    public String register(@ModelAttribute Adherent adherent) {
        adherentService.inscrire(adherent);
        return "redirect:/login-adherent";
    }

    // ✅ DASHBOARD ADHERENT
    @GetMapping("/dashboard-adherent")
    public String dashboard(HttpSession session, Model model) {
        Adherent adherent = (Adherent) session.getAttribute("adherent");
        if (adherent == null) return "redirect:/login-adherent";

        model.addAttribute("emprunts", empruntService.getEmpruntsEnCours(adherent));
        return "adherent/dashboard-adherent";
    }

    // ✅ CATALOGUE PUBLIC
    @GetMapping("/catalogue")
    public String afficherLivres(Model model) {
        model.addAttribute("livres", livreService.getAllLivres());
        return "adherent/catalogue";
    }

    // ✅ DECONNEXION
    @GetMapping("/adherent/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/";
    }

    // ✅ CREER UNE DEMANDE (emprunt, lecture, réservation)
    @PostMapping("/demande-action")
    public String creerDemande(@RequestParam Integer livreId,
                               @RequestParam String typeAction,
                               @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dateEmprunt,
                               HttpSession session,
                               RedirectAttributes redirectAttributes) {

        Adherent adherent = (Adherent) session.getAttribute("adherent");
        if (adherent == null) return "redirect:/login-adherent";

        if (adherent.getDateDeblocage() != null && adherent.getDateDeblocage().isAfter(LocalDate.now())) {
            redirectAttributes.addFlashAttribute("error", "Vous êtes bloqué jusqu'au " + adherent.getDateDeblocage());
            return "redirect:/catalogue";
        }

        try {
            Livre livre = livreService.getLivreById(livreId);
            DemandeEmprunt.TypeAction action = DemandeEmprunt.TypeAction.valueOf(typeAction);

            demandeEmpruntService.creerDemande(adherent, livre, action, dateEmprunt);
            redirectAttributes.addFlashAttribute("success", "Demande envoyée avec succès !");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }

        return "redirect:/catalogue";
    }

    // ✅ MES DEMANDES
    @GetMapping("/mes-demandes")
    public String mesDemandes(HttpSession session, Model model) {
        Adherent adherent = (Adherent) session.getAttribute("adherent");
        if (adherent == null) return "redirect:/login-adherent";

        List<DemandeEmprunt> demandes = demandeEmpruntService.getDemandesParAdherent(adherent.getId());
        model.addAttribute("demandes", demandes);
        return "adherent/mes-demandes";
    }

    // ✅ RENDRE LIVRE
    @PostMapping("/adherent/rendre-livre/{id}")
    public String rendreLivre(@PathVariable Integer id,
                              @RequestParam("dateRetourEffective") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dateRetourEffective,
                              HttpSession session) {
        Adherent adherent = (Adherent) session.getAttribute("adherent");
        if (adherent == null) return "redirect:/login-adherent";

        empruntService.rendreLivre(id, adherent, dateRetourEffective);
        return "redirect:/dashboard-adherent";
    }

    // ✅ DEMANDER PROLONGEMENT
    @PostMapping("/adherent/demande-prolongement/{id}")
    public String demanderProlongement(@PathVariable Integer id,
                                       HttpSession session,
                                       RedirectAttributes redirectAttributes) {
        Adherent adherent = (Adherent) session.getAttribute("adherent");
        if (adherent == null) return "redirect:/login-adherent";

        try {
            demandeEmpruntService.creerDemandeProlongement(adherent, id);
            redirectAttributes.addFlashAttribute("success", "Demande de prolongement envoyée !");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }

        return "redirect:/dashboard-adherent";
    }

    // ✅ BLOCAGE/DEBLOCAGE (admin)
    @PostMapping("/admin/blocage")
    public String blocageAdherent(@RequestParam Integer adherentId,
                                  @RequestParam String action,
                                  RedirectAttributes redirectAttributes) {
        Adherent adherent = adherentService.getAdherentById(adherentId);

        if ("bloquer".equals(action)) {
            adherent.setStatut(Adherent.Statut.bloque);
            adherent.setDateDeblocage(LocalDate.now().plusDays(10));
        } else if ("debloquer".equals(action)) {
            adherent.setStatut(Adherent.Statut.actif);
            adherent.setDateDeblocage(null);
        }

        adherentService.save(adherent);
        redirectAttributes.addFlashAttribute("success", "Action effectuée avec succès.");
        return "redirect:/admin/dashboard";
    }
}
