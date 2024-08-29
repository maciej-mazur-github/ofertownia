package pl.ofertownia.security.web;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import pl.ofertownia.security.user.RoleEnum;
import pl.ofertownia.security.user.UserService;
import pl.ofertownia.security.user.dto.UserDetailsDto;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/admin")
public class AdminController {
    private final UserService userService;
    private final static String ROLE_PREFIX = "ROLE_";

    public AdminController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    String adminPanel(Model model) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        UserDetailsDto currentAdminDetails = userService.findUserDetailsByEmail(username).orElseThrow();
        String superAdminRoleName =  ROLE_PREFIX + RoleEnum.SUPERADMIN.name();
        Collection<? extends GrantedAuthority> authorities = SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getAuthorities();
        List<UserDetailsDto> allAdminsAndUsers = userService.findAllAdminsAndUsers();

        model.addAttribute("currentAdminDetails", currentAdminDetails);
        model.addAttribute("welcomeMessage", welcomeMessage(currentAdminDetails));
        model.addAttribute("roles", RoleEnum.getRolesWithoutSuperAdmin());
        model.addAttribute("serverPath",
                ServletUriComponentsBuilder.fromCurrentContextPath().toUriString());

        if (authorities.contains(new SimpleGrantedAuthority(superAdminRoleName))) {
            List<UserDetailsDto> otherSuperAdmins = userService.findEditingDetailsByRole(RoleEnum.SUPERADMIN)
                    .stream()
                    .filter(user -> !user.getEmail().equals(username))
                    .collect(Collectors.toList());
            model.addAttribute("otherSuperAdmins", otherSuperAdmins);

        } else {
            allAdminsAndUsers = allAdminsAndUsers.stream()
                    .filter(user -> !user.getEmail().equals(currentAdminDetails.getEmail()))
                    .collect(Collectors.toList());
        }
        model.addAttribute("allAdminsAndUsers", allAdminsAndUsers);
        return "admin-form";
    }

    private String welcomeMessage(UserDetailsDto currentAdminDetails) {
        return "Witaj %sAdministratorze %s %s (%s)"
                .formatted(
                        currentAdminDetails.getRoles().contains(RoleEnum.SUPERADMIN) ? "Super " : "",
                        currentAdminDetails.getFirstName(),
                        currentAdminDetails.getLastName(),
                        currentAdminDetails.getEmail()
                    );
    }
}
