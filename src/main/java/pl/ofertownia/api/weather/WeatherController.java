package pl.ofertownia.api.weather;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class WeatherController {
    private final WeatherService weatherService;

    public WeatherController(WeatherService weatherService) {
        this.weatherService = weatherService;
    }

    @GetMapping("/pogoda")
    String weatherForm(@RequestParam(name = "miasto", required = false) String cityName,
                       Model model) {
        if (StringUtils.isEmpty(cityName)) {
            return "weather-form";
        }

        FullForecast forecast = weatherService.get24HoursAnd5DaysForecast(cityName);

        model.addAttribute("currentWeatherViewDto", weatherService.getCurrentWeather(cityName));
        model.addAttribute("forecastForCurrent24h", forecast.getForecastFor24Hours());
        model.addAttribute("forecastFor5Days", forecast.getForecastFor5Days());
        return "weather-forecast";
    }
}
