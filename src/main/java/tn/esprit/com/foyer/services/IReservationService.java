package tn.esprit.com.foyer.services;

import tn.esprit.com.foyer.entities.Chambre;
import tn.esprit.com.foyer.entities.Etudiant;
import tn.esprit.com.foyer.entities.Reservation;

import java.util.Date;
import java.util.List;

public interface IReservationService {
    List<Reservation> retrieveAllReservation();

    Reservation addReservation(Reservation reservation, Long idEtudiant, Long idChambre, boolean send, Long idmatched, List<Double> matchingscores);

    long nbPlacesDisponibleParChambreAnneeEnCours(Chambre chambre, Date year);

    Reservation updateReservation(Reservation r);

    Reservation retrieveReservation(Long idReservation);

    List<Reservation> retrieveReservationbystudent(Long idstudent);

    void removeReservation(Long idReservation);

    void validatereservation(long idreservation, long idstudent, String code);

    void sendemailtomatch(long idmatched, Etudiant student, Reservation reservation, List<Double> matchingscores);

}
