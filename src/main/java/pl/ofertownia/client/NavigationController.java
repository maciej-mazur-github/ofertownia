package pl.ofertownia.client;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

@Controller
public class NavigationController {

    @GetMapping("/")
    String homePage(Model model) {
        model.addAttribute("pathToOffer",
                ServletUriComponentsBuilder.fromCurrentRequest().path("/oferta").toUriString());
        return "index";
    }

    @GetMapping("/dodaj-oferte")
    String addOfferPage() {
        return "add";
    }

    @GetMapping("/kategorie")
    String categoriesPage() {
        return "categories";
    }

    @GetMapping("/szukaj")
    String searchPage(Model model,
                      HttpServletRequest request) {
        model.addAttribute("pathToOffer",
                ServletUriComponentsBuilder.fromContextPath(request).path("/oferta").toUriString());
        return "search";
    }

    @GetMapping("/oferta")
    String offerDetailsPage(Model model) {
        model.addAttribute(
                "pathToImage",
                ServletUriComponentsBuilder.fromCurrentContextPath().path("/img/jumbo-back.jpg").toUriString());
        return "offer";
    }

    @GetMapping("/odmowa-dostepu")
    String accessDenied(Model model) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        model.addAttribute("username", username);
        return "access-denied";
    }
}
