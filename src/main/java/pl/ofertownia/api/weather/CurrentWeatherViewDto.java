package pl.ofertownia.api.weather;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import pl.ofertownia.api.weather.dtofield.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CurrentWeatherViewDto {
    private ViewWeather weather;
    private ViewMain main;
    private BigDecimal visibility;
    private ViewWind wind;
    private LocalDateTime dt;
    private DayOfWeek dayOfWeek;
    private ViewSys sys;
    private String name;

    record WeatherTempIconUrlTime(BigDecimal temp, String iconUrl, LocalDateTime time, DayOfWeek dayOfWeek) {}
}
