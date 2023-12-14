package tn.esprit.com.foyer.services;

import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.stereotype.Service;
import tn.esprit.com.foyer.entities.*;

import java.util.Properties;
import java.util.Set;

@Service
@Slf4j
public class EmailReservationService implements iEmailService {

    public JavaMailSender getJavaMailSender() {
        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
        mailSender.setHost("smtp.gmail.com");
        mailSender.setPort(587);
        String em = "gamgamitelgou@gmail.com";
        String pass = "ybvj yvbn uirt zvgi";
        mailSender.setUsername(em);
        mailSender.setPassword(pass);
        Properties props = mailSender.getJavaMailProperties();
        props.put("mail.transport.protocol", "smtp");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.debug", "true");
        return mailSender;
    }

    private final JavaMailSender javaMailSender = getJavaMailSender();

    @Override
    public void sendReservationConfirmationEmail(Etudiant etudiant, Reservation reservation) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(etudiant.getEmail());
        message.setSubject("Reservation Registered");
        message.setText("Dear " + etudiant.getNomEt() + " " + etudiant.getPrenomEt() + ", ID : " + etudiant.getCin() + ",\n\nYour reservation has been registered.\nReservation details : \n \n"
                + "Id : " + reservation.getIdReservation() + "\n"
                + "Room : " + reservation.getChambre().getIdChambre() + "\n"
                + "Bloc : " + reservation.getChambre().getBloc().getIdBloc() + "\n"
                + "Dorm : " + reservation.getChambre().getBloc().getFoyer().getNomFoyer() + "\n"
                + "For the year : " + (1900 + reservation.getAnneeReservation().getYear()) + "\n"
                + "Please click on the link below to further validate the reservation : " + "\n"
                + "http://localhost:4200/user/bloc/" + reservation.getChambre().getBloc().getIdBloc() + "/chambre/reservations/validate/" + reservation.getIdReservation() + "/" + etudiant.getIdEtudiant() + "/" + etudiant.getCode()
        );

        // Sending the email
        try {
            log.info("" + javaMailSender.createMimeMessage().getSession().getDebug());
            javaMailSender.send(message);
        } catch (MailException e) {
            log.error("Error sending email", e);
        }
    }

    @Override
    public void sendemailtomatch(Etudiant tocontact, Etudiant student, Reservation reservation, double[] matchingscores, EtudiantServices etudiantServices) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(tocontact.getEmail());
        message.setSubject("Someone matched with you");
        StringBuilder text = new StringBuilder("Dear " + tocontact.getNomEt() + " " + tocontact.getPrenomEt() + ",\nYou have been matched with another student. \n \n");
        if (matchingscores[0] == 1.0) {
            text.append("You are part of the same university \n");
        }
        if (matchingscores[1] >= 0.66) {
            Set<String> set1 = etudiantServices.splitInterests(student.getInterests());
            Set<String> set2 = etudiantServices.splitInterests(tocontact.getInterests());
            Set<String> similarinterests = etudiantServices.similarinterests(set1, set2);
            text.append("You share many interests, including : \n");
            for (String interest : similarinterests) {
                text.append(interest).append("\n");
            }
        }
        if (matchingscores[2] >= 0.75) {
            text.append("Your academic performance is similar \n");
        }
        text.append("\n")
                .append("Reservation Id : ").append(reservation.getIdReservation()).append("\n")
                .append("Room : ").append(reservation.getChambre().getIdChambre()).append("\n")
                .append("Bloc : " + reservation.getChambre().getBloc().getIdBloc() + "\n")
                .append("Dorm : " + reservation.getChambre().getBloc().getFoyer().getNomFoyer() + "\n")
                .append("For the year : ").append(1900 + reservation.getAnneeReservation().getYear()).append("\n")
                .append("Please click on the link below to make a reservation with him : ").append("\n")
                .append("http://localhost:4200/user/bloc/" + reservation.getChambre().getBloc().getIdBloc() + "/chambre/reservations/reserve/" + reservation.getIdReservation() + "/" + tocontact.getIdEtudiant());

        message.setText(text.toString());
        // Sending the email
        try {
            log.info("" + javaMailSender.createMimeMessage().getSession().getDebug());
            javaMailSender.send(message);
        } catch (MailException e) {
            log.error("Error sending email", e);
        }
    }
}
