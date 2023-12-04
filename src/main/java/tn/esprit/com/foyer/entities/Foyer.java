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
@Table( name = "Foyer")
public class Foyer implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idFoyer")
    private Long idFoyer; // Clé primaire
    private String nomFoyer;
    private Long capaciteFoyer;

    @Column(columnDefinition = "boolean default false")
    private boolean archived;
    @OneToMany(cascade = CascadeType.ALL, mappedBy="foyer", fetch = FetchType.EAGER)
    private Set<Bloc> bloc;
}
