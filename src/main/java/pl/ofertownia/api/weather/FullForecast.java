package pl.ofertownia.api.weather;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
@AllArgsConstructor
public class FullForecast {
    private List<CurrentWeatherViewDto.WeatherTempIconUrlTime> forecastFor24Hours;
    private Map<String, List<CurrentWeatherViewDto.WeatherTempIconUrlTime>> forecastFor5Days;
}
