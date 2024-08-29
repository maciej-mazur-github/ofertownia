package pl.ofertownia.api.weather;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ForecastViewDto {
    private List<CurrentWeatherViewDto> list;
    private City city;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class City {
        private LocalDateTime sunrise;
        private LocalDateTime sunset;
    }
}
