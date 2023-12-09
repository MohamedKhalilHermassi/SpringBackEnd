package tn.esprit.com.foyer.services;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tn.esprit.com.foyer.entities.Etudiant;
import tn.esprit.com.foyer.entities.Reservation;
import tn.esprit.com.foyer.repositories.EtudiantRepository;

import java.util.*;

@Service
@AllArgsConstructor
@Slf4j
public class EtudiantServices implements IEtudiantService {

    EtudiantRepository etudiantRepository;
    EmailService emailService;

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
        if (e.getInterests().trim() == "") e.setInterests(null);
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


    public double getRoommateMatchingScore(double schoolMatch, double performanceMatch, double InterestsjaccardSimilarity) {
        double weightSchool = 0.45;
        double weightPerformance = 0.15;
        double weightInterests = 0.4;

        return (weightSchool * schoolMatch) + (weightPerformance * performanceMatch)
                + (weightInterests * InterestsjaccardSimilarity);
    }

    public double calculateJaccardSimilarity(String interests1, String interests2) {
        Set<String> set1 = splitInterests(interests1);
        Set<String> set2 = splitInterests(interests2);
        Set<String> intersection = similarinterests(set1, set2);
        Set<String> union = new HashSet<>(set1);
        union.addAll(set2);

        if (union.isEmpty()) {
            return 0.0;
        }

        return (double) intersection.size() / union.size();
    }

    public Set<String> similarinterests(Set<String> set1, Set<String> set2) {
        Set<String> intersection = new HashSet<>(set1);
        intersection.retainAll(set2);
        return intersection;
    }

    public Set<String> splitInterests(String interests) {
        return new HashSet<>(Arrays.asList(interests.split(",")));
    }

    @Override
    public Map<String, Object> RoommateMatcher(long idEtudiant) {
        Etudiant student = retrieveEtudiant(idEtudiant);
        List<Etudiant> students = retrieveAllEtudiants();
        students.removeIf(etudiant -> etudiant == student);

        double[] beststudentscores = {0.0, 0.0, 0.0};
        double x = 0.0;
        Etudiant beststudent = null;
        for (Etudiant student2 : students) {
            double InterestsjaccardSimilarity = calculateJaccardSimilarity(
                    student.getInterests(),
                    student2.getInterests()
            );
            log.info("Interests Jaccard Similarity : " + InterestsjaccardSimilarity);

            double schoolMatch = (student.getEcole().equals(student2.getEcole())) ? 1.0 : 0.0;
            double performanceMatch = 1.0 - Math.abs(student.getSchoolperformance() - student2.getSchoolperformance()) / 20;
            double y = getRoommateMatchingScore(schoolMatch, performanceMatch, InterestsjaccardSimilarity);
            if (y > x) {
                x = y;
                beststudent = student2;
                beststudentscores[0] = schoolMatch;
                beststudentscores[1] = InterestsjaccardSimilarity;
                beststudentscores[2] = performanceMatch;
            }
        }
        log.info("Best Matching score was : " + x);

        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put("etudiant", beststudent);
        resultMap.put("matchingScores", beststudentscores);
        return (x > 0) ? resultMap : null;
    }
}