package pl.ofertownia.api.weather.dtofield;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ViewSys {
    private LocalDateTime sunrise;
    private LocalDateTime sunset;
}