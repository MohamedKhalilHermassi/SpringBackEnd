package tn.esprit.com.foyer.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MailRequest {
    private String to;
    private String subject;
    private String userName;
    private String code;
}
