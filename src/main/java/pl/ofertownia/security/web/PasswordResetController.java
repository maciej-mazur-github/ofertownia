package pl.ofertownia.security.web;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;
import pl.ofertownia.security.user.UserService;

@Controller
@RequestMapping("/reset-hasla")
public class PasswordResetController {
    private final UserService userService;

    public PasswordResetController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    String passwordResetRequestPage() {
        return "password-reset-request";
    }

    @PostMapping
    RedirectView sendResetPasswordEmail(@RequestParam("username") String username,
                                        RedirectAttributes attributes) {
        attributes.addAttribute("username", username);
        if (userService.sendResetEmail(username)) {
            return new RedirectView("/reset-hasla/wyslano-email");
        } else {
            return new RedirectView("/reset-hasla/mail-blad");
        }
    }

    @GetMapping("/wyslano-email")
    String showResetEmailSentConfirmation(@ModelAttribute("username") String username,
                                          Model model) {
        model.addAttribute("username", username);
        return "reset-email-sent-confirmation";
    }

    @GetMapping("/mail-blad")
    String nonExistentUserPasswordResetAttempt(@ModelAttribute("username") String username,
                                               Model model) {
        return "wrong-email-password-reset-failed";
    }

    @GetMapping("/{resetCode}")
    String resetPasswordPage(@PathVariable String resetCode,
                             Model model) {
        model.addAttribute("resetCode", resetCode);
        return "reset-password-form";
    }

    @PostMapping("/send-request")
    RedirectView sendPasswordChangeRequest(@RequestParam("newPassword") String newPassword,
                                           @RequestParam("resetCode") String resetCode,
                                           RedirectAttributes attributes) {
        return userService.changeResetPassword(newPassword, resetCode)
                .map(email -> {
                    attributes.addAttribute("email", email);
                    return new RedirectView("/reset-hasla/ok");
                })
                .orElse(new RedirectView("/reset-hasla/blad-resetu"));
    }

    @GetMapping("/ok")
    String passwordResetSuccess(@ModelAttribute("email") String email,
                                Model model) {
        model.addAttribute("userEmail", email);
        return "password-reset-success";
    }

    @GetMapping("/blad-resetu")
    String passwordResetFailed() {
        return "password-reset-failed";
    }
}
