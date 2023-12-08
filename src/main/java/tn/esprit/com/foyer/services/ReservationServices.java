package tn.esprit.com.foyer.services;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tn.esprit.com.foyer.entities.Etudiant;
import tn.esprit.com.foyer.entities.Reservation;
import tn.esprit.com.foyer.repositories.ChambreRepository;
import tn.esprit.com.foyer.repositories.EtudiantRepository;
import tn.esprit.com.foyer.repositories.ReservationRepository;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@AllArgsConstructor
public class ReservationServices implements IReservationService {
    ReservationRepository reservationRepository;
    EtudiantRepository etudiantRepository;
    ChambreRepository chambreRepository;
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
            reservation.setChambre(chambreRepository.findById(idChambre).get());
            Set<Etudiant> newStudents = reservation.getEtudiants();
            if (newStudents == null) {
                newStudents = new HashSet<>();
            }
            newStudents.add(etudiant);
            reservation.setEtudiants(newStudents);
            Reservation savedReservation = reservationRepository.save(reservation);
            //chambreService.affecterReservationAChambre(idChambre,reservation.getIdReservation());
            // Sending email after successfully saving the reservation
            //emailService.sendReservationConfirmationEmail(etudiant, savedReservation);
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
    public List<Reservation> retrieveReservationbystudent(Long idstudent) {
        return reservationRepository.findReservationByEtudiant(idstudent);
    }

    @Override
    public void removeReservation(Long idReservation) {
        reservationRepository.deleteById(idReservation);
    }
}