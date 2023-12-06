package tn.esprit.com.foyer.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import tn.esprit.com.foyer.entities.Bloc;
import tn.esprit.com.foyer.entities.Reservation;
import tn.esprit.com.foyer.entities.TypeChambre;

import java.util.Set;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class chambreDTO {

    private Long idChambre;
    private Long numeroChambre;
    private TypeChambre typeC;
    private tn.esprit.com.foyer.dto.blocDTO bloc;
    private Set<Reservation> reservations;

}
