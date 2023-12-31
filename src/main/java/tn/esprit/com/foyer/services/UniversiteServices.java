package tn.esprit.com.foyer.services;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tn.esprit.com.foyer.entities.Foyer;
import tn.esprit.com.foyer.entities.Universite;
import tn.esprit.com.foyer.repositories.FoyerRepository;
import tn.esprit.com.foyer.repositories.UniversteRepository;

import java.util.List;

@Service
@AllArgsConstructor
public class UniversiteServices implements IUniversiteService{
    UniversteRepository universteRepository;
    FoyerRepository foyerRepository;
    public Universite updateUniversite(Universite u, Long id)
    {
        Universite universite = universteRepository.findById(id).get();
        universite.setNomUniversite(u.getNomUniversite());
        universite.setAdresse(u.getAdresse());

        return universteRepository.save(universite);
    }
    @Override
    public List<Universite> retrieveAllUniversities() {
        return universteRepository.findAll();
    }

    @Override
    public Universite addUniversity(Universite u) {
        return universteRepository.save(u);
    }

    @Override
    public Universite updateUniversity(Universite u) {
        return universteRepository.save(u);
    }

    @Override
    public Universite retrieveUniversity(long idUniversite) {
        return universteRepository.findById(idUniversite).get();
    }

    @Override
    public void removeUniversity(long idUniversite) {
        universteRepository.deleteById(idUniversite);
    }

    public Universite affecterFoyerUniversite(long idFoyer,String nomUniversite)
    {
        Foyer f = foyerRepository.findById(idFoyer).get();
        Universite u = universteRepository.findByNomUniversite(nomUniversite);

        u.setFoyer(f);
        universteRepository.save(u);
        return u;
    }
    public Universite desaffeecterFoyerUniversite( String nomUniversite)
    {

        Universite u = universteRepository.findByNomUniversite(nomUniversite);

        u.setFoyer(null);
        universteRepository.save(u);
        return u;

    }

    public Universite findUniversiteById(Long id)
    {
        return universteRepository.findById(id).get();

    }
}
