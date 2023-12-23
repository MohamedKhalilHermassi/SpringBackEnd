package tn.esprit.com.foyer.services;

import tn.esprit.com.foyer.entities.Etudiant;
import tn.esprit.com.foyer.entities.Reservation;

import java.util.List;
import java.util.Map;

public interface IEtudiantService {
    List<Etudiant> retrieveAllEtudiants();

    Etudiant addEtudiant(Etudiant e);

    Etudiant updateEtudiant(Etudiant e);

    Etudiant retrieveEtudiant(Long idEtudiant);

    void removeEtudiant(Long idEtudiant);

    List<Etudiant> addEtudiants(List<Etudiant> etudiants);

    public Map<String, Object> RoommateMatcher(long idEtudiant);

}
