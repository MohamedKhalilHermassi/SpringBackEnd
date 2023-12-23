package tn.esprit.com.foyer.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import tn.esprit.com.foyer.entities.Reservation;
import tn.esprit.com.foyer.services.ReservationServices;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@AllArgsConstructor
@RequestMapping("/reservation")
@PreAuthorize("hasAnyRole('ADMIN','ETUDIANT')")
public class ReservationController {
    ReservationServices reservationServices;

    @GetMapping("/retrieve-all-reservation")
    public List<Reservation> retrieveAllReservation() {
        return reservationServices.retrieveAllReservation();
    }

    @GetMapping("/retrieve-reservation/{reservation-id}")
    public Reservation retrieveReservation(@PathVariable("reservation-id") Long reservationId) {
        return reservationServices.retrieveReservation(reservationId);
    }

    @GetMapping("/reservationsbystudent/{student-id}")
    public List<Reservation> retrieveReservationforstudent(@PathVariable("student-id") long studentid) {
        return reservationServices.retrieveReservationbystudent(studentid);
    }

    // PUT
    @PutMapping("/newreserv/{idetud}/{idchambre}/{send}/{matched}")
    public ResponseEntity<?> addReservation(@RequestBody Map<String, Object> requestBody, @PathVariable(name = "idetud") Long idEtudiant, @PathVariable(name = "idchambre") Long idChambre,
                                            @PathVariable(name = "send") boolean send, @PathVariable(name = "matched") Long idmatch) {
        try {
            List<Object> rawMatchingScores = (List<Object>) requestBody.get("matchingScores");
            List<Double> matchingScores = rawMatchingScores.stream()
                    .map(score -> {
                        if (score instanceof Integer) {
                            return ((Integer) score).doubleValue();
                        } else if (score instanceof Double) {
                            return (Double) score;
                        } else {
                            // Handle other types or throw an exception if needed
                            throw new IllegalArgumentException("Invalid score type: " + score.getClass().getSimpleName());
                        }
                    })
                    .collect(Collectors.toList());

            ObjectMapper objectMapper = new ObjectMapper();
            Reservation reservation = objectMapper.convertValue(requestBody.get("reservation"), Reservation.class);
            reservation.setEstValide(false);
            return new ResponseEntity<>(reservationServices.addReservation(reservation, idEtudiant, idChambre, send, idmatch,matchingScores), HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("/update")
    public Reservation updateReservation(@RequestBody Reservation reservation) {
        return reservationServices.updateReservation(reservation);
    }

    @PatchMapping("/validate/{idreserv}/{idstud}/{code}")
    public void validatereservation(@PathVariable(name = "idreserv") Long idreserv, @PathVariable(name = "idstud") Long idstud, @PathVariable(name = "code") String code) {
        reservationServices.validatereservation(idreserv, idstud, code);
    }

    // DELETE
    @DeleteMapping("/delete/{reservation-id}")
    public void deletestudentreserv(@PathVariable("reservation-id") Long reservationId) {
        reservationServices.removeReservation(reservationId);
    }

    @DeleteMapping("/delete-reservation/{reservation-id}")
    @PreAuthorize("hasAuthority('admin:delete')")
    public void deleteReservation(@PathVariable("reservation-id") Long reservationId) {
        reservationServices.removeReservation(reservationId);
    }
}
