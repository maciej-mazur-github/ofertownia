package pl.ofertownia.api.weather.exception;

public class CityNotFoundException extends RuntimeException {
    public CityNotFoundException(String msg) {
        super(msg);
    }
}
