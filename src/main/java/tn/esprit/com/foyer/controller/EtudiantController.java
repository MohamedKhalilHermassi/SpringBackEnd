package tn.esprit.com.foyer.controller;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import tn.esprit.com.foyer.entities.Etudiant;
import tn.esprit.com.foyer.services.EtudiantServices;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@AllArgsConstructor
@RequestMapping("/etudiant")
@PreAuthorize("hasAnyRole('ADMIN','ETUDIANT')")
public class EtudiantController {
    EtudiantServices etudiantService;

    @GetMapping("/retrieve-all-etudiants")
    public List<Etudiant> getAllEtudiants() {
        return etudiantService.retrieveAllEtudiants();
    }

    @GetMapping("/retrieve-etudiant/{etudiant-id}")
    public Etudiant retrieveEtudiant(@PathVariable("etudiant-id") Long etudiantId) {
        return etudiantService.retrieveEtudiant(etudiantId);
    }

    @PostMapping("/update")
    public Etudiant updateEtudiant(@RequestBody Etudiant etudiant) {
        return etudiantService.updateEtudiant(etudiant);
    }

    @GetMapping("/match/{etudiant-id}")
    public ResponseEntity<Map<String, Object>> roommatematcher(@PathVariable("etudiant-id") Long etudiantId) {
        Map<String, Object> resultMap = etudiantService.RoommateMatcher(etudiantId);
        if (resultMap != null) {
            return new ResponseEntity<>(resultMap, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
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
