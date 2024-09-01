package pl.ofertownia.mail;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties("ofertownia")
@Getter
@Setter
public class MailProperties {
    private String host;
    private int port;
    private String userName;
    private String password;
    private String protocol;
    private boolean smtpAuth;
    private boolean starttlsEnabled;
    private boolean mailDebugEnabled;
    private String from;
}
