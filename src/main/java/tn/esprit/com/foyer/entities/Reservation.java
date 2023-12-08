package tn.esprit.com.foyer.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.io.Serializable;
import java.util.Date;
import java.util.Set;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Table(name = "Reservation")
public class Reservation implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idReservation", length = 50)
    private long idReservation;
    @Temporal(TemporalType.DATE)
    private Date anneeReservation;
    private boolean estValide;
    @ManyToOne(cascade = CascadeType.ALL)
    private Chambre chambre;
    @ManyToMany(cascade = CascadeType.ALL)
    private Set<Etudiant> etudiants;

}
