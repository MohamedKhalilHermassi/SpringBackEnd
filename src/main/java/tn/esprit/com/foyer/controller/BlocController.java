package tn.esprit.com.foyer.controller;

import lombok.AllArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import tn.esprit.com.foyer.dto.blocDTO;
import tn.esprit.com.foyer.entities.Bloc;
import tn.esprit.com.foyer.services.BlocServices;

import java.util.List;
import java.util.Map;

@RestController
@AllArgsConstructor
@RequestMapping("/bloc")
@PreAuthorize("hasAnyRole('ADMIN','ETUDIANT')")
public class BlocController {

    BlocServices blocServices;
    @GetMapping("/retrieve-all-bloc")

    public List<Bloc> retrieveAllBloc(){
        return blocServices.retrieveAllBlocs();
    }
    @GetMapping("/retrieve-bloc/{bloc-id}")
    public blocDTO retrieveBloc(@PathVariable("bloc-id") Long blocId){
        return blocServices.retrieveBloc(blocId);
    }

    @PostMapping("/add-bloc")
    public Bloc addBloc(@RequestBody Bloc bloc){
        return blocServices.addBloc(bloc);
    }

    @DeleteMapping("/delete-bloc/{bloc-id}")
    public void deleteBloc(@PathVariable("bloc-id") Long blocId){
        blocServices.removeBloc(blocId);
    }

    @PutMapping("/affecter-bloc-to-foyer/{nomBloc}/{foyer-id}")
    public Bloc affecterBlocToFoyer(@PathVariable("nomBloc") String nomBloc, @PathVariable("foyer-id") Long idFoyer) {
        return blocServices.affecterFoyerToBloc(nomBloc, idFoyer);
    }
    @PutMapping("/updateBloc/{idBloc}")
    public blocDTO UpdateBloc(@PathVariable("idBloc") Long idBloc, @RequestBody Bloc b)
    {

        return blocServices.updateBloc(idBloc,b);
    }
    @GetMapping("/blocStat")
    public Map<String,Long> retrieveAllBlocNamesCapcite(){
        return blocServices.getListStat();
    }


}
