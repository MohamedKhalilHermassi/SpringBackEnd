package tn.esprit.com.foyer.services;

import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.springframework.stereotype.Service;
import tn.esprit.com.foyer.dto.blocDTO;
import tn.esprit.com.foyer.dto.chambreDTO;
import tn.esprit.com.foyer.entities.Bloc;
import tn.esprit.com.foyer.entities.Chambre;
import tn.esprit.com.foyer.repositories.BlocRepository;
import tn.esprit.com.foyer.repositories.ChambreRepository;
import tn.esprit.com.foyer.repositories.ReservationRepository;


import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

import static tn.esprit.com.foyer.entities.TypeChambre.*;

@Service
@AllArgsConstructor
public class ChambreServices implements IChambreService {
    ChambreRepository chambreRepository;
    BlocRepository blocRepository;
    ReservationRepository reservationRepository;
    private EmailService emailService;


    public Map<String, Map<String, Long>> getStatistiquesTypesChambresParBloc() {
        List<Object[]> result = chambreRepository.countChambresByTypeAndBloc();

        return result.stream()
                .collect(Collectors.groupingBy(
                        array -> (String) array[0],
                        Collectors.toMap(
                                array -> ((Enum) array[1]).toString(),
                                array -> (Long) array[2]
                        )
                ));
    }

    public Chambre affecterBlocToChambre(Long numeroChambre, Long idBloc) {


        int affectedChambresCount = chambreRepository.countByBloc_IdBloc(idBloc);
        Chambre chambre = chambreRepository.findByNumeroChambre(numeroChambre);
        Bloc bloc = blocRepository.findById(idBloc)
                .orElseThrow(() -> new EntityNotFoundException("Bloc not found"));

        if (affectedChambresCount < bloc.getCapaciteBloc()) {

            if (chambre == null) {
                throw new EntityNotFoundException("Chambre not found");
            }

            chambre.setBloc(bloc);
            return chambreRepository.save(chambre);
        } else {
            emailService.sendEmail("figawew503@newcupon.com",
                    "Cannot assign more chambres to " + bloc.getNomBloc() + ", capaciteBloc exceeded",
                    "choisir une autre bloc pour ce numeroChambre");


            chambreRepository.delete(chambre);
            throw new IllegalStateException("Cannot assign more chambres, capaciteBloc exceeded");

        }


    }



    public int actualchambrenumbersnow (Long idBloc)
    {




        return chambreRepository.countByBloc_IdBloc(idBloc);
    }




    public List<chambreDTO> getChambresBy_BlocId(Long blocId) {
        List <chambreDTO> chambreDTOS =  new ArrayList<>() ;
        List <Chambre> chambres  = new ArrayList<>() ;
        chambres  = chambreRepository.getChambresByBloc_IdBloc(blocId);
        chambreDTO chambreDTO= new chambreDTO( ) ;
        blocDTO blocDTO = new blocDTO() ;
        for (Chambre C:chambres
        ) {

            blocDTO.setIdBloc(C.getBloc().getIdBloc());
            blocDTO.setCapaciteBloc(C.getBloc().getCapaciteBloc());
            blocDTO.setNomBloc(C.getBloc().getNomBloc());
            blocDTO.setFoyer(C.getBloc().getFoyer());

            chambreDTO.setNumeroChambre(C.getNumeroChambre());
            chambreDTO.setIdChambre(C.getIdChambre());
            chambreDTO.setTypeC(C.getTypeC());
            chambreDTO.setBloc(blocDTO);
            chambreDTO.setReservations(C.getReservations());
            chambreDTOS.add(chambreDTO);
        }

        return  chambreDTOS ;

    }


    @Override
    public List<Chambre> retrieveAllChambre() {

        return  chambreRepository.findAll() ;
    }
    public boolean isNumeroChambreExists(Long numeroChambre) {
        return chambreRepository.existsByNumeroChambre(numeroChambre);
    }
    @Override
    public Chambre addChambre(Chambre c) {
        Long numeroChambre = c.getNumeroChambre();
        if (isNumeroChambreExists(numeroChambre)) {
            throw new IllegalArgumentException("Chambre with numeroChambre " + numeroChambre + " already exists");
        }
        return chambreRepository.save(c);
    }

    @Override
    public chambreDTO updateChambre(Long idChambre , Chambre c) {


        Chambre existingChambre = chambreRepository.findById(idChambre).get();

        existingChambre.setTypeC(c.getTypeC());
        existingChambre.setNumeroChambre(c.getNumeroChambre());

        //DTO

        chambreDTO chambreDTO= new chambreDTO( ) ;
        blocDTO blocDTO = new blocDTO() ;
        blocDTO.setIdBloc(existingChambre.getBloc().getIdBloc());
        blocDTO.setCapaciteBloc(existingChambre.getBloc().getCapaciteBloc());
        blocDTO.setNomBloc(existingChambre.getBloc().getNomBloc());
        blocDTO.setFoyer(existingChambre.getBloc().getFoyer());

        chambreDTO.setNumeroChambre(existingChambre.getNumeroChambre());
        chambreDTO.setIdChambre(existingChambre.getIdChambre());
        chambreDTO.setTypeC(existingChambre.getTypeC());
        chambreDTO.setBloc(blocDTO);
        chambreDTO.setReservations(existingChambre.getReservations());
        chambreRepository.save(existingChambre);
        return chambreDTO;

    }

    @Override
    public chambreDTO retrieveChambre(Long idChambre) {
        Chambre C=chambreRepository.findById(idChambre).get();
        chambreDTO chambreDTO= new chambreDTO( ) ;
        blocDTO blocDTO = new blocDTO() ;
        blocDTO.setIdBloc(C.getBloc().getIdBloc());
        blocDTO.setCapaciteBloc(C.getBloc().getCapaciteBloc());
        blocDTO.setNomBloc(C.getBloc().getNomBloc());
        blocDTO.setFoyer(C.getBloc().getFoyer());

        chambreDTO.setNumeroChambre(C.getNumeroChambre());
        chambreDTO.setIdChambre(C.getIdChambre());
        chambreDTO.setTypeC(C.getTypeC());
        chambreDTO.setBloc(blocDTO);
        chambreDTO.setReservations(C.getReservations());

        return chambreDTO;

    }

    @Override
    public void removeChambre(Long idChambre) {
        chambreRepository.deleteById(idChambre);
    }

    public Bloc affecterChambresABloc(List<Long> numChambre, String nomBloc)
    {
        Bloc b = blocRepository.findByNomBloc(nomBloc);
        for(int i=0;i<numChambre.size();i++)
        {
            long k=  numChambre.get(i);
            System.out.println(k);
            Chambre ch = chambreRepository.findById(k).get();
            ch.setBloc(b);
            chambreRepository.save(ch);
        }
        return b;
    }

    public void generateOverviewPDFReport(List<Bloc> blocs, ByteArrayOutputStream baos) {
        try (PDDocument document = new PDDocument()) {
            for (Bloc bloc : blocs) {
                PDPage page = new PDPage(PDRectangle.A4);
                document.addPage(page);
                PDPageContentStream contentStream = new PDPageContentStream(document, page, PDPageContentStream.AppendMode.APPEND, true);

                contentStream.setFont(PDType1Font.HELVETICA_BOLD, 14);
                contentStream.beginText();
                contentStream.newLineAtOffset(50, 750);
                contentStream.showText("Bloc Information:");
                contentStream.newLineAtOffset(0, -20);
                contentStream.setFont(PDType1Font.HELVETICA, 12);

                contentStream.showText("Name:");
                contentStream.newLine();
                contentStream.newLineAtOffset(0, -20);
                contentStream.showText(bloc.getNomBloc());
                contentStream.newLineAtOffset(0, -40);
                contentStream.newLine();

                contentStream.newLineAtOffset(0, -20);
                contentStream.showText("Capacity:");
                contentStream.newLineAtOffset(0, -20);
                contentStream.showText(String.valueOf(bloc.getCapaciteBloc()));
                contentStream.newLineAtOffset(0, -20);
                contentStream.newLine();
                contentStream.newLine();
                contentStream.endText();

                contentStream.setFont(PDType1Font.HELVETICA_BOLD, 12);
                contentStream.beginText();
                contentStream.newLineAtOffset(50, 620);
                contentStream.newLineAtOffset(0, -20);
                contentStream.showText("Chambres Information:");
                contentStream.newLineAtOffset(0, -20);
                contentStream.setFont(PDType1Font.HELVETICA, 10);


                Set<Chambre> chambres = bloc.getChambres();
                boolean chambreExists = false;
                int yOffset = 0;
                for (Chambre chambre : chambres) {
                    contentStream.newLineAtOffset(0, -25);
                    contentStream.showText("Chambre Number:");
                    contentStream.newLineAtOffset(0, -25);
                    contentStream.showText(String.valueOf(chambre.getNumeroChambre()));
                    contentStream.newLineAtOffset(0, -25);
                    contentStream.newLine();
                    contentStream.showText("Type:");
                    contentStream.newLineAtOffset(0, -25);
                    contentStream.showText(String.valueOf(chambre.getTypeC()));
                    contentStream.newLineAtOffset(0, -25);
                    yOffset += 30;
                    if (yOffset >= 650) {
                        page = new PDPage(PDRectangle.A4);
                        document.addPage(page);
                        contentStream.close();
                        contentStream = new PDPageContentStream(document, page, PDPageContentStream.AppendMode.APPEND, true);
                        contentStream.setFont(PDType1Font.HELVETICA_BOLD, 12);
                        contentStream.beginText();
                        contentStream.newLineAtOffset(50, 750);
                        contentStream.showText("Chambres Information (Continued):");
                        contentStream.newLineAtOffset(0, -20);
                        contentStream.setFont(PDType1Font.HELVETICA, 10);
                        yOffset = 0;
                    }
                    chambreExists = true;
                }
                if (!chambreExists) {
                    contentStream.newLine();
                    contentStream.showText("No chambres affected to this bloc.");
                    contentStream.newLine();
                }
                contentStream.endText();
                contentStream.close();
            }
            document.save(baos);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }





}
