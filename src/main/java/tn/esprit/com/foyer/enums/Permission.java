package tn.esprit.com.foyer.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum Permission {
    ADMIN_READ("admin:read"),
    ADMIN_UPDATE("admin:update"),
    ADMIN_CREATE("admin:create"),
    ADMIN_DELETE("admin:delete"),
    ETUDIANT_READ("etudiant:read"),
    ETUDIANT_UPDATE("etudiant:update"),
    ETUDIANT_CREATE("etudiant:create"),
    ETUDIANT_DELETE("etudiant:delete")
    ;


    @Getter
    private final String permission;
}
