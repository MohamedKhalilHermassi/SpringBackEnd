package tn.esprit.com.foyer.services;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.apache.commons.lang3.RandomStringUtils;
import tn.esprit.com.foyer.entities.Chambre;
import tn.esprit.com.foyer.entities.Etudiant;
import tn.esprit.com.foyer.entities.Reservation;
import tn.esprit.com.foyer.repositories.ChambreRepository;
import tn.esprit.com.foyer.repositories.EtudiantRepository;
import tn.esprit.com.foyer.repositories.ReservationRepository;

import java.time.Year;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@AllArgsConstructor
@Slf4j
public class ReservationServices implements IReservationService {
    ReservationRepository reservationRepository;
    EtudiantRepository etudiantRepository;
    ChambreRepository chambreRepository;
    ChambreServices chambreService;
    EtudiantServices etudiantServices;
    EmailService emailService;

    @Override
    public List<Reservation> retrieveAllReservation() {
        return reservationRepository.findAll();
    }

    @Override
    public Reservation addReservation(Reservation reservation, Long idEtudiant, Long idChambre, boolean send, Long idmatched, double[] matchingscores) {
        Etudiant etudiant = etudiantRepository.findById(idEtudiant).orElse(null);
        Chambre roomtoreserve = chambreRepository.findById(idChambre).orElse(null);
        if (etudiant != null && roomtoreserve != null && nbPlacesDisponibleParChambreAnneeEnCours(roomtoreserve, reservation.getAnneeReservation()) > 0) {
            if (!(etudiant.getReservations().stream().
                    anyMatch(existingReservation -> existingReservation.getAnneeReservation().getYear() + 1900 == reservation.getAnneeReservation().getYear() + 1900))) {
                etudiant.setCode(RandomStringUtils.randomAlphanumeric(6));
                reservation.setChambre(roomtoreserve);
                Set<Etudiant> newStudents = reservation.getEtudiants();
                if (newStudents == null) {
                    newStudents = new HashSet<>();
                }
                newStudents.add(etudiant);
                reservation.setEtudiants(newStudents);
                Reservation savedReservation = reservationRepository.save(reservation);
                // Sending email after successfully saving the reservation
                emailService.sendReservationConfirmationEmail(etudiant, savedReservation);
                // Sending email to matched student if send
                if (send) sendemailtomatch(idmatched, etudiant, reservation, matchingscores);

                return savedReservation;
            } else throw new RuntimeException("You already reserved a room for the same year");
        } else throw new RuntimeException("No places left or invalid student/room information");
    }

    @Override
    public long nbPlacesDisponibleParChambreAnneeEnCours(Chambre chambre, Date year) {
        int currentYear = year.getYear() + 1900;
        long currentYearReservationsCount = reservationRepository.findReservationsByYear(chambre, currentYear);
        long placesleft = switch (chambre.getTypeC()) {
            case SIMPLE -> 1 - currentYearReservationsCount;
            case DOUBLE -> 2 - currentYearReservationsCount;
            case TRIPLE -> 3 - currentYearReservationsCount;
            default -> 0;
        };
        log.info(currentYearReservationsCount + "  " + placesleft + "  " + currentYear);
        return placesleft;
    }

    @Override
    public void validatereservation(long idreservation, long idstudent, String code) {
        Reservation reservation = reservationRepository.findById(idreservation).get();
        if (code.equals(etudiantRepository.findById(idstudent).get().getCode())) {
            reservation.setEstValide(true);
            reservationRepository.save(reservation);
        }
    }

    @Override
    public void sendemailtomatch(long idmatched, Etudiant student, Reservation reservation, double[] matchingscores) {
        Etudiant tocontact = etudiantRepository.findById(idmatched).get();
        // Sending email after successfully saving the reservation
        emailService.sendemailtomatch(tocontact, student, reservation, matchingscores, etudiantServices);
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