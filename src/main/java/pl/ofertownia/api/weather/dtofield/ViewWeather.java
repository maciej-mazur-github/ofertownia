package pl.ofertownia.api.weather.dtofield;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ViewWeather {
    private String description;
    private String iconUrl;
}