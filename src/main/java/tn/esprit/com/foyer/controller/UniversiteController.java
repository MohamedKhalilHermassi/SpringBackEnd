package tn.esprit.com.foyer.controller;

import lombok.AllArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import tn.esprit.com.foyer.entities.Foyer;
import tn.esprit.com.foyer.entities.Reservation;
import tn.esprit.com.foyer.entities.Universite;
import tn.esprit.com.foyer.services.UniversiteServices;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/universite")
@PreAuthorize("hasAnyRole('ADMIN','ETUDIANT')")
public class UniversiteController {
    UniversiteServices universiteServices;

    @PutMapping("/update-universite/{id}")
    @PreAuthorize("hasAuthority('admin:update')")
    public Universite updateFoyer(@RequestBody Universite u, @PathVariable("id") Long id){

        return universiteServices.updateUniversite(u,id);
    }
    @GetMapping("/retrieve-all-universite")
    public List<Universite> retrieveAllUniversite(){
        return universiteServices.retrieveAllUniversities();
    }

    @GetMapping("get-universite-by-id/{id}")
    public Universite getUniversiteById(@PathVariable("id") Long id){
        return universiteServices.findUniversiteById(id);
    }
    @GetMapping("/retrieve-universite/{universite-id}")
    public Universite retrieveUniversite(@PathVariable("universite-id") Long universiteId){
        return universiteServices.retrieveUniversity(universiteId);
    }

    @PostMapping("/add-universite")
    @PreAuthorize("hasAuthority('admin:create')")
    public Universite addUniversite(@RequestBody Universite u){
        return universiteServices.addUniversity(u);
    }

    @DeleteMapping("/delete-universite/{universite-id}")
    @PreAuthorize("hasAuthority('admin:delete')")
    public void deleteUniversite(@PathVariable("universite-id") Long universiteId){
        universiteServices.removeUniversity(universiteId);
    }
    @PutMapping("/affecterFoyer/{universite-nom}/{foyer-id}")
    @PreAuthorize("hasAuthority('admin:update')")
    public Universite affecterFoyer(@PathVariable("universite-nom") String nomUniversite, @PathVariable("foyer-id") Long idFoyer){
       Universite u =  universiteServices.affecterFoyerUniversite(idFoyer,nomUniversite);
        return u;
    }
    @PutMapping("/desaffecterFoyer/{universite-nom}")
    @PreAuthorize("hasAuthority('admin:update')")
    public Universite desaffecterFoyer(@PathVariable("universite-nom") String nomUniversite){
        Universite u =  universiteServices.desaffeecterFoyerUniversite(nomUniversite);
        return u;
    }
}
