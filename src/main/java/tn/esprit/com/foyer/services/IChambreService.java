package tn.esprit.com.foyer.services;

import tn.esprit.com.foyer.dto.chambreDTO;
import tn.esprit.com.foyer.entities.Chambre;

import java.util.List;

public interface IChambreService {
    List<Chambre> retrieveAllChambre();

    Chambre addChambre(Chambre c);

    chambreDTO updateChambre(Long id ,Chambre c);

    chambreDTO retrieveChambre(Long idChambre);

    void removeChambre(Long idChambre);

    Chambre affecterReservationAChambre(Long id, long idreserv);

}
