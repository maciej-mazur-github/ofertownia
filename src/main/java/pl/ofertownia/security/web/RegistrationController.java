package pl.ofertownia.security.web;

import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
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

    @ModelAttribute("userRegistrationDto")
    public UserRegistrationDto newUserDto() {
        return new UserRegistrationDto();
    }

    @ModelAttribute("existingEmailFromDb")
    public String existingEmailFromDb() {
        return new String("");
    }

    @GetMapping
    String registerPage() {
        return "registration-form";
    }

    @PostMapping
    String register(@Valid @ModelAttribute("userRegistrationDto") UserRegistrationDto userRegistrationDto,
                    BindingResult bindingResult,
                    RedirectAttributes attributes) {

        String userEmail = userRegistrationDto.getEmail();
        boolean emailExists = userService.checkIfExistsByEmail(userEmail);
        if (bindingResult.hasErrors() || emailExists) {
            attributes.addFlashAttribute("userRegistrationDto", userRegistrationDto);
            attributes.addFlashAttribute("org.springframework.validation.BindingResult.userRegistrationDto",
                    bindingResult);
            if (emailExists) {
                attributes.addFlashAttribute("existingEmailFromDb", userEmail);
            }
            return "redirect:/rejestracja";
        }

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

    @GetMapping("/regulamin")
    String registrationTerms() {
        return "registration-terms";
    }
}
