package pl.ofertownia.security.web;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import pl.ofertownia.security.user.UserService;
import pl.ofertownia.security.user.dto.UserEditingDto;

@Controller
@RequestMapping("/edycja")
public class EditionController {
    private final UserService userService;

    public EditionController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    String editingPage(Model model) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        UserEditingDto userEditingDto = userService.findEditingDetailsByEmail(username)
                .orElseThrow();
        model.addAttribute("userEditingDto", userEditingDto);
        return "user-editing-form";
    }

    @PostMapping
    String saveChanges(UserEditingDto userEditingDto,
                       RedirectAttributes attributes) {
        attributes.addAttribute("username", userEditingDto.getEmail());
        if (userService.saveEditingChanges(userEditingDto)) {
            return "redirect:/edycja/potwierdzenie";
        } else {
            return "redirect:/edycja/blad";
        }
    }

    @GetMapping("/potwierdzenie")
    String editingSuccessfulConfirmation(@ModelAttribute("username") String username,
                                         Model model) {
        model.addAttribute("username", username);
        return "editing-successful-confirmation";
    }

    @GetMapping("/blad")
    String editingFailedPage(@ModelAttribute("username") String username,
                             Model model) {
        model.addAttribute("username", username);
        return "editing-failed";
    }
}
