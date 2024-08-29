package pl.ofertownia.api.weather.dtofield;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ResponseWeather {
    private int id;
    private String main;
    private String description;
    private String icon;
}