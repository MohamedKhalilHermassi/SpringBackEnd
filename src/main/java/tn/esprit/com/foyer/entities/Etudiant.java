package tn.esprit.com.foyer.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

import java.io.Serializable;
import java.util.*;

@Slf4j
@Entity
@Builder
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
    private Long idEtudiant;
    private String nomEt;
    private String prenomEt;
    private Long cin;
    private String email;
    private String ecole;
    @Temporal(TemporalType.DATE)
    private Date dateNaissance;
    private double schoolperformance;
    private String interests;
    private String code = null;


    @ManyToMany(cascade = CascadeType.ALL, mappedBy = "etudiants", fetch = FetchType.EAGER)
    @JsonIgnore
    private Set<Reservation> reservations;

}
