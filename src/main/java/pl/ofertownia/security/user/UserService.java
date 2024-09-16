package pl.ofertownia.security.user;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import pl.ofertownia.mail.MailProperties;
import pl.ofertownia.security.user.dto.UserCredentialsDto;
import pl.ofertownia.security.user.dto.UserDetailsDto;
import pl.ofertownia.security.user.dto.UserEditingDto;
import pl.ofertownia.security.user.dto.UserRegistrationDto;
import pl.ofertownia.security.user.mapper.Mapper;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class UserService {
    private static final String RESET_EMAIL_SUBJECT = "Reset hasła konta";

    private final UserRepository userRepository;
    private final UserRoleRepository userRoleRepository;
    private final MailProperties mailProperties;

    private final Mapper mapper;
    private final JavaMailSender mailSender;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository,
                       UserRoleRepository userRoleRepository, MailProperties mailProperties,
                       Mapper mapper,
                       JavaMailSender mailSender,
                       PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.userRoleRepository = userRoleRepository;
        this.mailProperties = mailProperties;
        this.mapper = mapper;
        this.mailSender = mailSender;
        this.passwordEncoder = passwordEncoder;
    }

    public Optional<UserCredentialsDto> findCredentialsByEmail(String userName) {
        return userRepository.findByEmail(userName)
                .map(mapper::userToUserCredentialsDto);
    }

    public boolean sendResetEmail(String username) {
        return userRepository.findByEmail(username)
                .map(user -> {
                    sendEmail(user);
                    return true;
                })
                .orElse(Boolean.FALSE);
    }

    private void sendEmail(User user) {
        String userEmail = user.getEmail();
        SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
        simpleMailMessage.setFrom(mailProperties.getFrom());
        simpleMailMessage.setTo(userEmail);

        String resetCode = UUID.randomUUID().toString();
        addResetCodeToDb(userEmail, resetCode);

        simpleMailMessage.setText(generateResetEmailContent(userEmail, resetCode));
        simpleMailMessage.setSubject(RESET_EMAIL_SUBJECT);
        mailSender.send(simpleMailMessage);
    }

    private void addResetCodeToDb(String userEmail, String resetCode) {
        User user = userRepository.findByEmail(userEmail).orElseThrow();
        user.setPasswordResetCode(resetCode);
        userRepository.save(user);
    }

    private String generateResetEmailContent(String userEmail, String resetCode) {
        ServletUriComponentsBuilder builder = ServletUriComponentsBuilder.fromCurrentRequest();
        String link = builder.path("/" + resetCode).toUriString();
        String content = """
                Witaj %s
                
                Do naszego serwisu wpłynęło żądanie resetu hasła Twojego konta.
                Jeśli żądanie to faktycznie pochodzi od Ciebie, kliknij w poniższy link, aby zresetować hasło.
                
                %s
                
                Jeśli to nie byłeś Ty, zignoruj tego maila.
                
                Z poważaniem,
                Spring Security Team
                """.formatted(userEmail, link);
        return content;
    }

    public Optional<String> changeResetPassword(String newPassword, String resetCode) {
        return userRepository.findByPasswordResetCode(resetCode)
                .map(user -> {
                    changePasswordInDb(user, passwordEncoder.encode(newPassword));
                    return Optional.of(user.getEmail());
                })
                .orElse(Optional.empty());
    }

    private void changePasswordInDb(User user, String newPassword) {
        user.setPassword(newPassword);
        user.setPasswordResetCode(null);
        userRepository.save(user);
    }

    public boolean register(UserRegistrationDto userRegistrationDto) {
        try {
            User user = mapper.userRegistrationDtoToUser(userRegistrationDto);
            userRepository.save(user);
            return true;
        } catch (ConstraintViolationException cve) {
            Set<ConstraintViolation<?>> errors = cve.getConstraintViolations();
            System.out.println("Rejestracja nie powiodła się");
            errors.stream()
                    .map(err -> err.getPropertyPath() + " " + err.getInvalidValue() + " " + err.getMessage())
                    .forEach(System.out::println);
        } catch (DataIntegrityViolationException e) {
            System.out.println("Rejestracja nie powiodła się");
            System.out.printf("Użytkownik o adresie %s już istnieje w bazie.", userRegistrationDto.getEmail());
        }

        return false;
    }

    public Optional<UserEditingDto> findEditingDetailsByEmail(String username) {
        return userRepository.findByEmail(username)
                .map(mapper::userToUserEditingDto);
    }

    @Transactional
    public boolean saveEditingChanges(UserEditingDto userEditingDto) {
        User user = userRepository.findByEmail(userEditingDto.getEmail()).orElseThrow();
        if (!passwordEncoder.matches(userEditingDto.getOldPassword(), user.getPassword())) {
            return false;
        }
        user.setFirstName(userEditingDto.getFirstName());
        user.setLastName(userEditingDto.getLastName());
        if (!"".equals(userEditingDto.getNewPassword())) {
            user.setPassword(passwordEncoder.encode(userEditingDto.getNewPassword()));
        }
        return true;
    }

    public List<UserDetailsDto> findEditingDetailsByRole(RoleEnum roleEnum) {
        return userRepository.findUsersByRoles_Name(roleEnum)
                .stream()
                .map(mapper::userToUserDetailsDto)
                .collect(Collectors.toList());
    }

    public List<UserDetailsDto> findAllAdminsAndUsers() {
        return userRepository.findUsersWithRoleNotEqual(RoleEnum.SUPERADMIN)
                .stream()
                .map(mapper::userToUserDetailsDto)
                .collect(Collectors.toList());
    }

    @Transactional
    public UserDetailsDto changeCredentials(Long id, Boolean grantAdminRights) {
        User user = userRepository.findById(id).orElseThrow();
        UserRole adminRole = userRoleRepository.findByName(RoleEnum.ADMIN).orElseThrow();
        UserRole userRole = userRoleRepository.findByName(RoleEnum.USER).orElseThrow();
        if (grantAdminRights) {
            user.getRoles().add(adminRole);
        } else {
            user.getRoles().add(userRole);
            user.getRoles().remove(adminRole);
        }
        return mapper.userToUserDetailsDto(user);
    }

    public Optional<UserDetailsDto> findUserDetailsByEmail(String username) {
        return userRepository.findByEmail(username)
                .map(mapper::userToUserDetailsDto);
    }

    public void deleteUserById(Long id) {
        userRepository.deleteById(id);
    }

    public boolean checkIfExistsByEmail(String userEmail) {
        return userRepository.existsUserByEmail(userEmail);
    }
}
