package pl.ofertownia.api.weather;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
public class GeolocationDto implements Serializable {
    private String name;
    private BigDecimal lat;
    private BigDecimal lon;
    private String country;
    private String state;
}
