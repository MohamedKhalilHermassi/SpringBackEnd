package tn.esprit.com.foyer.services;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tn.esprit.com.foyer.entities.Etudiant;
import tn.esprit.com.foyer.repositories.EtudiantRepository;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@AllArgsConstructor
@Slf4j
public class EtudiantServices implements IEtudiantService {

    EtudiantRepository etudiantRepository;

    @Override
    public List<Etudiant> retrieveAllEtudiants() {
        return etudiantRepository.findAll();
    }

    @Override
    public Etudiant addEtudiant(Etudiant e) {
        return etudiantRepository.save(e);
    }

    @Override
    public Etudiant updateEtudiant(Etudiant e) {
        return etudiantRepository.save(e);
    }

    @Override
    public Etudiant retrieveEtudiant(Long idEtudiant) {
        return etudiantRepository.findById(idEtudiant).get();

    }

    @Override
    public void removeEtudiant(Long idEtudiant) {
        etudiantRepository.deleteById(idEtudiant);
    }

    @Override
    public List<Etudiant> addEtudiants(List<Etudiant> etudiants) {
        return etudiantRepository.saveAll(etudiants);
    }


    @Override
    public Etudiant RoommateMatcher(long idEtudiant) {
        Etudiant student = retrieveEtudiant(idEtudiant);
        List<Etudiant> students = retrieveAllEtudiants();
        students.removeIf(etudiant -> etudiant.getIdEtudiant() == idEtudiant);

        double x = 0.0;
        Etudiant beststudent = null;
        for (Etudiant student2 : students) {
            double y = student.getRoommateMatchingScore(student, student2);
            if (y > x) {
                x = y;
                beststudent = student2;
            }
        }
        return (x > 0) ? beststudent : null;
    }

}
