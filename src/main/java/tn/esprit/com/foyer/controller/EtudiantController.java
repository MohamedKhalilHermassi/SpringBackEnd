package tn.esprit.com.foyer.controller;

import lombok.AllArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import tn.esprit.com.foyer.entities.Etudiant;
import tn.esprit.com.foyer.services.EtudiantServices;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/etudiant")
@PreAuthorize("hasAnyRole('ADMIN','ETUDIANT')")
public class EtudiantController {
    EtudiantServices etudiantService;
    @GetMapping("/retrieve-all-etudiants")
    public List<Etudiant> getAllEtudiants(){
        return etudiantService.retrieveAllEtudiants();
    }

    @GetMapping("/retrieve-etudiant/{etudiant-id}")
    public Etudiant retrieveEtudiant(@PathVariable("etudiant-id") Long etudiantId) {
        return etudiantService.retrieveEtudiant(etudiantId);
    }

    @GetMapping("/match/{etudiant-id}")
    public Etudiant roommatematcher(@PathVariable("etudiant-id") Long etudiantId) {
        return etudiantService.RoommateMatcher(etudiantId);
    }

    @PostMapping("/add-etudiant")
    @PreAuthorize("hasAuthority('admin:create')")
    public Etudiant addEtudiant(@RequestBody Etudiant e) {
        return etudiantService.addEtudiant(e);
    }
    @DeleteMapping("/remove-etudiant/{etudiant-id}")
    @PreAuthorize("hasAuthority('admin:delete')")
    public void removeEtudiant(@PathVariable("etudiant-id") Long etudiantId) {
        etudiantService.removeEtudiant(etudiantId);
    }
}
