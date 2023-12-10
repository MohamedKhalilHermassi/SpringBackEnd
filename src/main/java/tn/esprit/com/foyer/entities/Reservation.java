package tn.esprit.com.foyer.entities;

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
@Table( name = "Reservation")
public class Reservation implements Serializable {
    @Id
    @Column(name = "idReservation", length = 50)
    private String idReservation; // Clé primaire
    private Date anneeReservation;
    private boolean estValide;
    @ManyToOne
    private Chambre chambre ;
    @ManyToMany(mappedBy="reservations", cascade = CascadeType.ALL)
    private Set<User> users;
}
