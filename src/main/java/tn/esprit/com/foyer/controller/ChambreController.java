package tn.esprit.com.foyer.controller;

import lombok.AllArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import tn.esprit.com.foyer.entities.Bloc;
import tn.esprit.com.foyer.entities.Chambre;
import tn.esprit.com.foyer.services.BlocServices;
import tn.esprit.com.foyer.services.ChambreServices;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/chambre")
@PreAuthorize("hasAnyRole('ADMIN','ETUDIANT')")
public class ChambreController {
    ChambreServices chambreServices;
    BlocServices    blocServices;
    @GetMapping("/get-all-chambres")
    public List<Chambre> retrieveChambres(){
        return chambreServices.retrieveAllChambre();
    }
    @GetMapping("/retrieve-chambre/{chambre-id}")
    public Chambre retrieveChambre(@PathVariable("chambre-id") Long chambreId){
        return chambreServices.retrieveChambre(chambreId);
    }
    @PostMapping("/add-chambre")
    @PreAuthorize("hasAuthority('admin:create')")
    public Chambre addChambre(@RequestBody Chambre ch){
        return chambreServices.addChambre(ch);
    }

    @DeleteMapping("/delete-chambre/{chambre-id}")
    @PreAuthorize("hasAuthority('admin:delete')")
    public void deleteChambre(@PathVariable("chambre-id") Long chambreId){
        chambreServices.removeChambre(chambreId);
    }
    @PutMapping("/affecterListeChambre/{nomBloc}")
    @PreAuthorize("hasAuthority('admin:update')")
    public void  affecterChambreBloc(@PathVariable("nomBloc") String nomBloc,@RequestBody List<Long> ch){
          chambreServices.affecterChambresABloc(ch,nomBloc);

    }
}
