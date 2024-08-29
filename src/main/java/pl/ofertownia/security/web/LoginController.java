package pl.ofertownia.security.web;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class LoginController {

    @GetMapping("/zaloguj")
    String loginForm(@RequestParam(required = false) String error,
                     Model model) {
        if (error != null) {
            model.addAttribute("error", error);
        }
        return "login-form";
    }
}
