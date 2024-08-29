package pl.ofertownia.security.user;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "application_user")
public class User {
    private static final String AT_LEAST_ONE_LOWER_CASE_CHAR_MESSAGE = "Hasło musi zawierać min. 1 znak mały";
    private static final String AT_LEAST_ONE_UPPER_CASE_CHAR_MESSAGE = "Hasło musi zawierać min. 1 znak duży";
    private static final String AT_LEAST_ONE_SPECIAL_CHAR_MESSAGE = "Hasło musi zawierać min. 1 znak specjalny";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotBlank
    @Size(min = 3, message = "Pole musi mieć co najmniej 3 znaki")
    private String firstName;
    @NotBlank
    @Size(min = 3, message = "Pole musi mieć co najmniej 3 znaki")
    private String lastName;
    @NotBlank
    private String address;
    @NotBlank
    @Pattern(regexp = "[0-9]{2}-[0-9]{3}")
    private String postalCode;
    @NotBlank
    private String city;
    @Column(unique = true)
    @NotBlank
    @Email
    private String email;
    @NotBlank
    @Size(min = 8, message = "Hasło musi mieć co najmniej 8 znaków")
    @Pattern.List({
            @Pattern(regexp = ".*[a-z].*", message = AT_LEAST_ONE_LOWER_CASE_CHAR_MESSAGE),
            @Pattern(regexp = ".*[A-Z].*", message = AT_LEAST_ONE_UPPER_CASE_CHAR_MESSAGE),
            @Pattern(regexp = ".*[~!@#$%^&*()_+={\\[}\\]|\"'<,>.?/:-].*", message = AT_LEAST_ONE_SPECIAL_CHAR_MESSAGE)
    })
    private String password;
    private String passwordResetCode;
    @AssertTrue(message = "Akceptacja regulaminu jest wymagana")
    private Boolean termsAgreement;
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "user_roles",
            joinColumns = @JoinColumn(name = "user_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "role_id", referencedColumnName = "id")
    )
    private Set<UserRole> roles = new HashSet<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

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

    public String getPasswordResetCode() {
        return passwordResetCode;
    }

    public void setPasswordResetCode(String passwordResetCode) {
        this.passwordResetCode = passwordResetCode;
    }

    public Set<UserRole> getRoles() {
        return roles;
    }

    public void setRoles(Set<UserRole> roles) {
        this.roles = roles;
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

    public Boolean getTermsAgreement() {
        return termsAgreement;
    }

    public void setTermsAgreement(Boolean termsAgreement) {
        this.termsAgreement = termsAgreement;
    }
}
