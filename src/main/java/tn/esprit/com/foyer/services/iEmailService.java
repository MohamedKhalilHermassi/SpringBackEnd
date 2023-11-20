package tn.esprit.com.foyer.services;

import tn.esprit.com.foyer.entities.*;

public interface iEmailService {
    void sendReservationConfirmationEmail(Etudiant etudiant, Reservation reservation);
}
