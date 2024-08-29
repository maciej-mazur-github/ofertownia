package pl.ofertownia.security.user;

import java.util.ArrayList;
import java.util.List;

public enum RoleEnum {
    SUPERADMIN("Super Administrator"),
    ADMIN("Administrator"),
    USER("Użytkownik");

    private String translation;

    RoleEnum(String translation) {
        this.translation = translation;
    }

    public String getTranslation() {
        return translation;
    }

    public static List<RoleEnum> getRolesWithoutSuperAdmin() {
        List<RoleEnum> roles = new ArrayList<>();
        for (RoleEnum value : values()) {
            if (value != SUPERADMIN) {
                roles.add(value);
            }
        }
        return roles;
    }
}
