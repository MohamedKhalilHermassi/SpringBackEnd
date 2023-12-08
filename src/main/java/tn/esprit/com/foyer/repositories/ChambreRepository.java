package tn.esprit.com.foyer.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import tn.esprit.com.foyer.entities.Chambre;

import java.util.List;

@Repository
public interface ChambreRepository extends JpaRepository<Chambre,Long> {

    int countByBloc_IdBloc(Long blocId);


    List<Chambre> getChambresByBloc_IdBloc(Long blocId);

    @Query("SELECT c FROM Chambre c LEFT JOIN FETCH c.bloc WHERE c.idChambre = :id")
    Chambre findByIdWithBloc(@Param("id") Long id);

    public Chambre findByNumeroChambre(Long numchambre);

    @Query("SELECT c.bloc.nomBloc, c.typeC, COUNT(c) FROM Chambre c GROUP BY c.bloc.nomBloc, c.typeC")
    List<Object[]> countChambresByTypeAndBloc();



}
