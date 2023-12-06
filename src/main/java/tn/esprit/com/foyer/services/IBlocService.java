package tn.esprit.com.foyer.services;

import tn.esprit.com.foyer.dto.blocDTO;
import tn.esprit.com.foyer.entities.Bloc;
import tn.esprit.com.foyer.entities.Etudiant;

import java.util.List;

public interface IBlocService {
    List<Bloc> retrieveAllBlocs();

    Bloc addBloc(Bloc b);

    blocDTO updateBloc(Long idBloc, Bloc b);

    blocDTO retrieveBloc(Long idBloc);

    void removeBloc(Long idBloc);
}
