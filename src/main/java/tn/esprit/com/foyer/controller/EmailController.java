package tn.esprit.com.foyer.controller;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.thymeleaf.context.Context;
import tn.esprit.com.foyer.dto.MailRequest;
import tn.esprit.com.foyer.services.EmailReservationService;
import tn.esprit.com.foyer.services.EmailService;

@RestController
@AllArgsConstructor
@RequestMapping("/mail")
public class EmailController {

    private final EmailService emailService;

    @PostMapping("/send-email")
    public String sendHtmlEmail(@RequestBody MailRequest emailRequest) {
        Context context = new Context();
        context.setVariable("userName", emailRequest.getUserName());
        context.setVariable("code", emailRequest.getCode());

        emailService.sendEmailWithHtmlTemplate(emailRequest.getTo(), emailRequest.getSubject(), "email-template", context);
        return "HTML email sent successfully!";
    }
}
