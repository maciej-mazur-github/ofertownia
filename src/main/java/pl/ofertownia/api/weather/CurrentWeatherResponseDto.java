package pl.ofertownia.api.weather;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import pl.ofertownia.api.weather.dtofield.ResponseMain;
import pl.ofertownia.api.weather.dtofield.ResponseSys;
import pl.ofertownia.api.weather.dtofield.ResponseWeather;
import pl.ofertownia.api.weather.dtofield.ResponseWind;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CurrentWeatherResponseDto {
    private ResponseWeather[] weather;
    private ResponseMain main;
    private BigDecimal visibility;
    private ResponseWind wind;
    private Long dt;
    private ResponseSys sys;
    private String name;
}
