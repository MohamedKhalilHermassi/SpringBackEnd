package tn.esprit.com.foyer.services;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import tn.esprit.com.foyer.entities.Foyer;
import tn.esprit.com.foyer.repositories.FoyerRepository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@AllArgsConstructor
@Slf4j
public class FoyerServices implements IFoyerService{
    FoyerRepository foyerRepository;
    ServiceSMS serviceSMS;


    public Map<String,Long> getCapaciteParFoyer()
    {
        Map<String,Long> map = new HashMap<>();
        List<Foyer> foyers = foyerRepository.findAll();
        for (int i=0;i<foyers.size();i++)
        {
            map.put(foyers.get(i).getNomFoyer(),foyers.get(i).getCapaciteFoyer());
        }
        return map;

    }
    @Override
    public List<Foyer> retrieveAllFoyers() {
        return foyerRepository.findAll();
    }

    @Override
    public Foyer addFoyer(Foyer f) {
        return foyerRepository.save(f);
    }

    @Override
    public Foyer updateFoyer(Foyer f, Long id) {
        Foyer foyer = foyerRepository.findById(id).get();
        foyer.setCapaciteFoyer(f.getCapaciteFoyer());
        foyer.setNomFoyer(f.getNomFoyer());
        return foyerRepository.save(foyer);
    }

    @Override
    public Foyer retrieveFoyer(long idFoyer) {
        return foyerRepository.findById(idFoyer).get();
    }

    @Override
    public void archiverFoyer(long idFoyer) {
        Foyer f = foyerRepository.findById(idFoyer).get();
        f.setArchived(true);
        foyerRepository.save(f);
    }

    @Override
    public void deleteFoyer(long idFoyer) {
        foyerRepository.deleteById(idFoyer);
    }

    @Scheduled(fixedRate = 3000)
    public void alerterCapaciteMaximal()
    {
        foyerRepository.findAll().forEach(
                foyer ->{
                    long x = foyer.getBloc().size();
                    long y = foyer.getCapaciteFoyer();
                    if((y-x)<10)
                    {
                       log.info("le foyer "+foyer.getNomFoyer()+" est presque saturé");
                        //serviceSMS.sendSms("+21629188594","le foyer "+foyer.getNomFoyer()+" est presque saturé");
                    }

                }
        );
    }

}
