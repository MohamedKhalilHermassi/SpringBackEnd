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
        if (e.getInterests()=="") e.setInterests(null);
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


    public double getRoommateMatchingScore(Etudiant student1, Etudiant student2) {
        double jaccardSimilarity = calculateJaccardSimilarity(
                student1.getInterests(),
                student2.getInterests()
        );

        log.info("Jaccard Similarity: " + jaccardSimilarity);

        double weightSchool = 0.45;
        double weightPerformance = 0.15;
        double weightInterests = 0.4;

        double schoolMatch = (student1.getEcole().equals(student2.getEcole())) ? 1.0 : 0.0;
        double performanceMatch = 1.0 - Math.abs(student1.getSchoolperformance() - student2.getSchoolperformance());

        return (weightSchool * schoolMatch) + (weightPerformance * performanceMatch)
                + (weightInterests * jaccardSimilarity);
    }
    public double calculateJaccardSimilarity(String interests1, String interests2) {
        Set<String> set1 = splitInterests(interests1);
        Set<String> set2 = splitInterests(interests2);

        Set<String> intersection = new HashSet<>(set1);
        intersection.retainAll(set2);

        Set<String> union = new HashSet<>(set1);
        union.addAll(set2);

        if (union.isEmpty()) {
            return 0.0;
        }

        return (double) intersection.size() / union.size();
    }
    private Set<String> splitInterests(String interests) {
        return new HashSet<>(Arrays.asList(interests.split(",")));
    }
    @Override
    public Etudiant RoommateMatcher(long idEtudiant) {
        Etudiant student = retrieveEtudiant(idEtudiant);
        List<Etudiant> students = retrieveAllEtudiants();
        students.removeIf(etudiant -> etudiant.getIdEtudiant() == idEtudiant);

        double x = 0.0;
        Etudiant beststudent = null;
        for (Etudiant student2 : students) {
            double y = getRoommateMatchingScore(student, student2);
            if (y > x) {
                x = y;
                beststudent = student2;
            }
        }
        return (x > 0) ? beststudent : null;
    }

}
