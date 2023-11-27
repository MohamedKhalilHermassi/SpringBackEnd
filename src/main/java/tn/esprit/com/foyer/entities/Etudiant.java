package tn.esprit.com.foyer.entities;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

import java.io.Serializable;
import java.util.*;

@Slf4j
@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Table(name = "Etudiant")
public class Etudiant implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idEtudiant")
    private Long idEtudiant; // Clé primaire
    private String nomEt;
    private String prenomEt;
    private Long cin;
    private String email;
    private String ecole;
    @Temporal(TemporalType.DATE)
    private Date dateNaissance;

    private double schoolperformance;
    private String interests;


    @ManyToMany(cascade = CascadeType.ALL)
    private Set<Reservation> reservations;

    public double getRoommateMatchingScore(Etudiant student1, Etudiant student2) {
        /*if (student1 == null || students == null) {
            // Handle invalid IDs or missing data
            return 0.0;
        }*/
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
            return 0.0; // Avoid division by zero
        }

        return (double) intersection.size() / union.size();
    }

    private Set<String> splitInterests(String interests) {
        return new HashSet<>(Arrays.asList(interests.split(",")));
    }
}
