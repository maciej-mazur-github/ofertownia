package pl.ofertownia.api.weather.exception;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@ControllerAdvice
public class ErrorControllerAdvice {

    @ExceptionHandler(CityNotFoundException.class)
    public String handleCityNotFoundException(CityNotFoundException e,
                                                    RedirectAttributes attributes) {

        attributes.addFlashAttribute("cityNotFoundException", e);
        return "redirect:/pogoda";
    }

    @ExceptionHandler(ServerConnectionException.class)
    public String handleServerConnectionException(ServerConnectionException e,
                                                  RedirectAttributes attributes) {

        attributes.addFlashAttribute("serverConnectionException", e);
        return "redirect:/pogoda";
    }
}
