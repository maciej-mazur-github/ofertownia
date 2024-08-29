package pl.ofertownia.api.weather.exception;

public class ServerConnectionException extends RuntimeException{
    public ServerConnectionException(String message) {
        super(message);
    }
}
