package tn.esprit.com.foyer.services;

import tn.esprit.com.foyer.entities.*;

import java.util.List;

public interface iEmailService {
    void sendReservationConfirmationEmail(Etudiant etudiant, Reservation reservation);

    public void sendemailtomatch(Etudiant tocontact, Etudiant etudiant, Reservation reservation, List<Double> matchingscores, EtudiantServices etudiantServices);
}
