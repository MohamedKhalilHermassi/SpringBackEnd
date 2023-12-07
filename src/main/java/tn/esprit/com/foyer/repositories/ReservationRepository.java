package tn.esprit.com.foyer.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import tn.esprit.com.foyer.entities.Reservation;

import java.util.Date;
import java.util.List;

@Repository
public interface ReservationRepository extends JpaRepository<Reservation,Long> {

    @Query("SELECT r FROM Reservation r WHERE r.idReservation= :id  ")
    Reservation findReservationByIdReservation(@Param("id") String id);

    @Query("SELECT r FROM Reservation r JOIN r.etudiants e WHERE e.idEtudiant = :id")
    List<Reservation> findReservationByEtudiant(@Param("id") long id);


}
