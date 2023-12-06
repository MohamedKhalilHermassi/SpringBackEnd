package tn.esprit.com.foyer.controller;

import lombok.AllArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import tn.esprit.com.foyer.entities.Foyer;
import tn.esprit.com.foyer.repositories.FoyerRepository;
import tn.esprit.com.foyer.services.FoyerServices;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@AllArgsConstructor
@RequestMapping("/foyer")
@PreAuthorize("hasAnyRole('ADMIN','ETUDIANT')")
public class FoyerController {
    FoyerServices foyerServices;
    FoyerRepository foyerRepository ;
    @GetMapping("/ids")
    public List<String> getAllFoyerIds() {
        List<Foyer> foyers = foyerRepository.findAll();
        return foyers.stream().map(Foyer::getNomFoyer).collect(Collectors.toList());
    }
    @GetMapping("/retrieve-libre-foyer")
    public List<Foyer> retrieveLibreFoyer(){
        return foyerServices.retrieveAllFoyers();
    }

    @GetMapping("/get-foyer-by-capacite")
    public Map<String,Long> getFoyerByCapacite(){
        return foyerServices.getCapaciteParFoyer();
    }

    @GetMapping("/retrieve-all-foyer")
    public List<Foyer> retrieveAllFoyer(){
        return foyerServices.retrieveAllFoyers();
    }

    @GetMapping("/retrieve-foyer/{foyer-id}")
    public Foyer retrieveFoyer(@PathVariable("foyer-id") Long foyerId){
        return foyerServices.retrieveFoyer(foyerId);
    }

    @PutMapping("/update-foyer/{id}")
    @PreAuthorize("hasAuthority('admin:update')")
    public Foyer updateFoyer(@RequestBody Foyer f, @PathVariable("id") Long id){

        return foyerServices.updateFoyer(f,id);
    }

    @PostMapping("/add-foyer")
    @PreAuthorize("hasAuthority('admin:create')")
    public Foyer addFoyer(@RequestBody Foyer f){
        return foyerServices.addFoyer(f);
    }
    @DeleteMapping("/delete-foyer/{foyer-id}")
    @PreAuthorize("hasAuthority('admin:delete')")
    public void deleteFoyer(@PathVariable("foyer-id") Long foyerId){
        foyerServices.deleteFoyer(foyerId);
    }

    @PostMapping("/archiver-foyer/{foyer-id}")
    @PreAuthorize("hasAuthority('admin:create')")
    public void archiverFoyer(@PathVariable("foyer-id") Long foyerId){
        foyerServices.archiverFoyer(foyerId);
    }
}
