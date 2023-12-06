package tn.esprit.com.foyer.dto;

import lombok.*;
import tn.esprit.com.foyer.entities.Foyer;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class blocDTO {
    private Long idBloc;
    private String nomBloc;
    private Long capaciteBloc;
    private Foyer foyer;
}

