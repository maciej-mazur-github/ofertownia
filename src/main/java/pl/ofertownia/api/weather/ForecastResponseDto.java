package pl.ofertownia.api.weather;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ForecastResponseDto {
    private CurrentWeatherResponseDto[] list;
    private City city;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class City {
        private Long sunrise;
        private Long sunset;
    }
}
