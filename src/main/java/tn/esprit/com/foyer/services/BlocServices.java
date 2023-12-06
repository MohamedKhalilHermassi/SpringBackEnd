package tn.esprit.com.foyer.services;

import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import tn.esprit.com.foyer.dto.blocDTO;
import tn.esprit.com.foyer.entities.Bloc;
import tn.esprit.com.foyer.entities.Chambre;
import tn.esprit.com.foyer.entities.Foyer;
import tn.esprit.com.foyer.repositories.BlocRepository;
import tn.esprit.com.foyer.repositories.FoyerRepository;

import java.util.*;

@Service
@AllArgsConstructor
@Slf4j

public class BlocServices implements IBlocService{
    BlocRepository blocRepository;
    FoyerRepository foyerRepository ;
    public Bloc affecterFoyerToBloc(String nomBloc, Long idFoyer) {
        Bloc bloc = blocRepository.findByNomBloc(nomBloc); // Assuming there's a method to find by name
        if (bloc == null) {
            throw new EntityNotFoundException("Bloc not found");
        }

        Foyer foyer = foyerRepository.findById(idFoyer).get();

        bloc.setFoyer(foyer);
        return blocRepository.save(bloc);
    }

    public Map<String,Long> getListStat ()
    {
        Map <String,Long> map =  new HashMap<>() ;
        List<Bloc> blocs = blocRepository.findAll() ;
        for (int i=0 ;i<blocs.size();i++) {

            map.put(blocs.get(i).getNomBloc(),blocs.get(i).getCapaciteBloc()) ;
        }


        return  map;

    }


    @Override
    public List<Bloc> retrieveAllBlocs() {


        return blocRepository.findAll();


    }

    @Override
    public Bloc addBloc(Bloc b) {
        return blocRepository.save(b);
    }

    @Override
    public blocDTO updateBloc(Long idBloc, Bloc b) {

        Bloc existingBloc = blocRepository.findById(idBloc).get();
        existingBloc.setNomBloc(b.getNomBloc());
        existingBloc.setCapaciteBloc(b.getCapaciteBloc());

        //dto
        blocDTO blocDTO= new blocDTO()  ;
        blocDTO.setIdBloc(existingBloc.getIdBloc());
        blocDTO.setNomBloc(existingBloc.getNomBloc());
        blocDTO.setCapaciteBloc(existingBloc.getCapaciteBloc());
        blocDTO.setFoyer(existingBloc.getFoyer());
        blocRepository.save(existingBloc) ;
        return  blocDTO ;
    }

    @Override
    public blocDTO retrieveBloc(Long idBloc) {
        Bloc B = blocRepository.findById(idBloc).get();
        blocDTO blocDTO = new blocDTO() ;
        blocDTO.setIdBloc(B.getIdBloc());
        blocDTO.setNomBloc(B.getNomBloc());
        blocDTO.setCapaciteBloc(B.getCapaciteBloc());
        blocDTO.setFoyer(B.getFoyer());

        return  blocDTO ;
    }

    @Override
    public void removeBloc(Long idBloc) {
        blocRepository.deleteById(idBloc);
    }

    @Scheduled(fixedRate = 10000)

    public void performTask() {
        log.info("start");
        List<Bloc> blocs = blocRepository.findAll();
        log.info("size : "+blocs.size());
        for (int i=0;i<blocs.size();i++)
        {
            log.info("La capacité du bloc "+blocs.get(i).getNomBloc()+ "est : " + blocs.get(i).getCapaciteBloc());
            Set<Chambre> chambres = blocs.get(i).getChambres();
            chambres.stream().forEach(
                    chambre -> {
                        log.info("le numero de la chambre est : "+ chambre.getNumeroChambre() + " et de type : " + chambre.getTypeC());
                    }
            );




        }



    }
}
