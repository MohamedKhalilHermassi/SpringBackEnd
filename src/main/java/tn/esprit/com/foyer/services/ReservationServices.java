package tn.esprit.com.foyer.services;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tn.esprit.com.foyer.entities.Etudiant;
import tn.esprit.com.foyer.entities.Reservation;
import tn.esprit.com.foyer.repositories.EtudiantRepository;
import tn.esprit.com.foyer.repositories.ReservationRepository;

import java.util.List;
import java.util.Set;

@Service
@AllArgsConstructor
public class ReservationServices implements IReservationService {
    ReservationRepository reservationRepository;
    EtudiantRepository etudiantRepository;
    ChambreServices chambreService;
    EmailService emailService;

    @Override
    public List<Reservation> retrieveAllReservation() {
        return reservationRepository.findAll();
    }

    @Override
    public Reservation addReservation(Reservation reservation, Long idEtudiant, Long idChambre) {
        Etudiant etudiant = etudiantRepository.findById(idEtudiant).orElse(null);
        if (etudiant != null) {
            Set<Reservation> newReservations = etudiant.getReservations();
            newReservations.add(reservation);
            etudiant.setReservations(newReservations);
            etudiantRepository.save(etudiant);

            Reservation savedReservation = reservationRepository.save(reservation);
            chambreService.affecterReservationAChambre(idChambre,reservation.getIdReservation());
            // Send email after successfully saving the reservation
            emailService.sendReservationConfirmationEmail(etudiant, savedReservation);
            return savedReservation;
        } else {
            return null;
        }
    }

    @Override
    public Reservation updateReservation(Reservation r) {
        return reservationRepository.save(r);
    }

    @Override
    public Reservation retrieveReservation(Long idReservation) {
        return reservationRepository.findById(idReservation).get();
    }

    @Override
    public void removeReservation(Long idReservation) {
        reservationRepository.deleteById(idReservation);
    }
}