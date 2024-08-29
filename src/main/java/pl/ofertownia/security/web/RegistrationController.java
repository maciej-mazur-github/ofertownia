package pl.ofertownia.security.web;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import pl.ofertownia.security.user.UserService;
import pl.ofertownia.security.user.dto.UserRegistrationDto;

@Controller
@RequestMapping("/rejestracja")
public class RegistrationController {
    private final UserService userService;

    public RegistrationController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    String registerPage(Model model) {
        UserRegistrationDto userRegistrationDto = new UserRegistrationDto();
        model.addAttribute("userRegistrationDto", userRegistrationDto);
        return "registration-form";
    }

    @PostMapping
    String register(UserRegistrationDto userRegistrationDto,
                    RedirectAttributes attributes) {
        if (userService.register(userRegistrationDto)) {
            return "redirect:/rejestracja/potwierdzenie";
        } else {
            attributes.addAttribute("userEmail", userRegistrationDto.getEmail());
            return "redirect:/rejestracja/blad";
        }
    }

    @GetMapping("/potwierdzenie")
    String registrationConfirmation() {
        return "registration-confirmation";
    }

    @GetMapping("/blad")
    String registrationFailed(@ModelAttribute("userEmail") String userEmail,
                              Model model) {
        model.addAttribute("userEmail", userEmail);
        return "registration-failed";
    }
}
