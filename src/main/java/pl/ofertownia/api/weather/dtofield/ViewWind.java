package pl.ofertownia.api.weather.dtofield;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ViewWind {
    private BigDecimal speed;
    private BigDecimal deg;
    private BigDecimal gust;
}