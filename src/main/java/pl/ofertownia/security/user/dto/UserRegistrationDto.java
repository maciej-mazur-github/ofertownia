package pl.ofertownia.security.user.dto;

import jakarta.validation.constraints.*;
import pl.ofertownia.security.user.constraint.MinOneLowerCaseChar;
import pl.ofertownia.security.user.constraint.MinOneSpecialChar;
import pl.ofertownia.security.user.constraint.MinOneUpperCaseChar;
import pl.ofertownia.security.user.constraint.PostalCode;

public class UserRegistrationDto {
    @NotBlank
    @Size(min = 3, message = "Pole musi mieć co najmniej 3 znaki")
    private String firstName;
    @NotBlank
    @Size(min = 3, message = "Pole musi mieć co najmniej 3 znaki")
    private String lastName;
    @NotBlank
    private String address;
    @NotBlank
    @PostalCode  // adnotacja własna
    private String postalCode;
    @NotBlank
    private String city;
    @NotBlank
    @Email(message = "Musi być poprawnie sformatowanym adresem email")
    private String email;
    @NotBlank
    @Size(min = 8, message = "Hasło musi mieć co najmniej 8 znaków")
    @MinOneLowerCaseChar
    @MinOneUpperCaseChar
    @MinOneSpecialChar
    private String password;
    @AssertTrue(message = "Akceptacja regulaminu jest wymagana")
    private Boolean termsAgreement;

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Boolean getTermsAgreement() {
        return termsAgreement;
    }

    public void setTermsAgreement(Boolean termsAgreement) {
        this.termsAgreement = termsAgreement;
    }
}
