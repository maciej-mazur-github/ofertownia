package pl.ofertownia.api.weather.dtofield;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ResponseWind {
    private BigDecimal speed;
    private BigDecimal deg;
    private BigDecimal gust;
}