package tn.esprit.com.foyer.controller;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import tn.esprit.com.foyer.dto.chambreDTO;
import tn.esprit.com.foyer.entities.Bloc;
import tn.esprit.com.foyer.entities.Chambre;
import tn.esprit.com.foyer.services.BlocServices;
import tn.esprit.com.foyer.services.ChambreServices;

import java.io.ByteArrayOutputStream;
import java.util.List;
import java.util.Map;

@RestController
@AllArgsConstructor
@RequestMapping("/chambre")
@PreAuthorize("hasAnyRole('ADMIN','ETUDIANT')")
public class ChambreController {

    ChambreServices chambreServices;
    BlocServices    blocServices;

    @GetMapping("/nombrechambresparbloc/{blocId}")
    public ResponseEntity<Integer> getActualChambreNumbers(@PathVariable Long blocId) {
        int actualChambreNumbers = chambreServices.actualchambrenumbersnow( blocId);
        return ResponseEntity.ok(actualChambreNumbers);
    }
    @GetMapping("/get-all-chambres")

    public List<Chambre> retrieveChambres(){
        return chambreServices.retrieveAllChambre();
    }
    @GetMapping("/retrieve-chambre/{chambre-id}")
    public chambreDTO retrieveChambre(@PathVariable("chambre-id") Long chambreId){
        return chambreServices.retrieveChambre(chambreId);
    }
    @PostMapping("/add-chambre")
    public Chambre addChambre(@RequestBody Chambre ch){
        return chambreServices.addChambre(ch);
    }

    @DeleteMapping("/delete-chambre/{chambre-id}")
    public void deleteChambre(@PathVariable("chambre-id") Long chambreId){
        chambreServices.removeChambre(chambreId);
    }
    @PutMapping("/affecterListeChambre/{nomBloc}")
    public void  affecterChambreBloc(@PathVariable("nomBloc") String nomBloc,@RequestBody List<Long> ch){
        chambreServices.affecterChambresABloc(ch,nomBloc);

    }
    @GetMapping("/byBlocId/{blocId}")
    public List<chambreDTO> getChambresByBlocId(@PathVariable Long blocId) {
        return chambreServices.getChambresBy_BlocId(blocId);

    }

    @PutMapping("/affecter-bloc-to-chambre/{numeroChambre}/{bloc-id}")
    public Chambre affecterBlocToChambre(@PathVariable("numeroChambre") Long numeroChambre, @PathVariable("bloc-id") Long idBloc) {
        return chambreServices.affecterBlocToChambre(numeroChambre, idBloc);
    }

    @PutMapping("/updateChambre/{idChambre}")
    public chambreDTO UpdateChambre(@PathVariable("idChambre") Long idChambre, @RequestBody Chambre chambre)
    {

        return chambreServices.updateChambre(idChambre,chambre);
    }

    @GetMapping("/typesChambresParBloc")
    public ResponseEntity<Map<String, Map<String, Long>>> getStatistiquesTypesChambresParBloc() {
        Map<String, Map<String, Long>> statistiques = chambreServices.getStatistiquesTypesChambresParBloc();
        return ResponseEntity.ok(statistiques);

    }

    @GetMapping(value = "/generate-pdf", produces = MediaType.APPLICATION_PDF_VALUE)
    public ResponseEntity<byte[]> generateOverviewPDF() {
        List<Bloc> blocs = blocServices.retrieveAllBlocs();

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        chambreServices.generateOverviewPDFReport(blocs, baos);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.parseMediaType("application/pdf"));
        headers.setContentDispositionFormData("attachment", "Overview_Report.pdf");
        headers.setCacheControl("must-revalidate, no-store");
        byte[] pdfBytes = baos.toByteArray();

        return ResponseEntity.ok()
                .headers(headers)
                .contentLength(pdfBytes.length)
                .body(pdfBytes);
    }



}
