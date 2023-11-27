package tn.esprit.com.foyer.controller;

import lombok.AllArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import tn.esprit.com.foyer.entities.Reservation;
import tn.esprit.com.foyer.services.ReservationServices;

import java.util.Date;
import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/reservation")
@PreAuthorize("hasAnyRole('ADMIN','ETUDIANT')")
public class ReservationController {
    ReservationServices reservationServices;

    @GetMapping("/retrieve-all-reservation")
    public List<Reservation> retrieveAllReservation(){
        return reservationServices.retrieveAllReservation();
    }

    @GetMapping("/retrieve-reservation/{reservation-id}")
    public Reservation retrieveReservation(@PathVariable("reservation-id") Long reservationId){
        return reservationServices.retrieveReservation(reservationId);
    }

    @GetMapping("/reservationsbystudent/{student-id}")
    public List<Reservation> retrieveReservationforstudent(@PathVariable("student-id") long studentid){
        return reservationServices.retrieveReservationbystudent(studentid);
    }

    // PUT
    @PutMapping("/newreserv/{idetud}/{idchambre}")
    public Reservation addReservation(@RequestBody Reservation reservation, @PathVariable(name = "idetud") Long idEtudiant , @PathVariable(name = "idchambre") Long idChambre) {
        return reservationServices.addReservation(reservation, idEtudiant,idChambre);
    }

    @PutMapping("/update")
    public Reservation updateReservation(@RequestBody Reservation reservation) {
        return reservationServices.updateReservation(reservation);
    }


    // DELETE
    @DeleteMapping("/delete-reservation/{reservation-id}")
    @PreAuthorize("hasAuthority('admin:delete')")
    public void deleteReservation(@PathVariable("reservation-id") Long reservationId){
        reservationServices.removeReservation(reservationId);
    }
}
