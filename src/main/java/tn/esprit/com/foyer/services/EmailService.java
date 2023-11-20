package tn.esprit.com.foyer.services;

import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.stereotype.Service;
import tn.esprit.com.foyer.entities.*;

import java.util.Properties;

@Service
@Slf4j
public class EmailService implements iEmailService {
    public JavaMailSender getJavaMailSender() {
        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
        mailSender.setHost("smtp.gmail.com");
        mailSender.setPort(587);
        String em = "placeholder";
        String pass = "placeholder";
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
        message.setSubject("Reservation Confirmation");
        message.setText("Dear " + etudiant.getNomEt() + " " + etudiant.getPrenomEt() + ", ID : " + etudiant.getCin() + ",\n\nYour reservation has been confirmed.\nReservation details : \n \n"
                + "Id : " + reservation.getIdReservation() + "\n"
                + "Room : " + reservation.getChambre().getIdChambre() + "\n"
                + "For the year : " + (1900+reservation.getAnneeReservation().getYear()) + "\n"
        );

        // Sending the email
        try {
            log.info("" + javaMailSender.createMimeMessage().getSession().getDebug());
            javaMailSender.send(message);
        } catch (MailException e) {
            log.error("Error sending email", e);
        }
    }


}