package pl.ofertownia.security.user.mapper;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import pl.ofertownia.security.user.RoleEnum;
import pl.ofertownia.security.user.User;
import pl.ofertownia.security.user.UserRole;
import pl.ofertownia.security.user.UserRoleRepository;
import pl.ofertownia.security.user.dto.UserCredentialsDto;
import pl.ofertownia.security.user.dto.UserDetailsDto;
import pl.ofertownia.security.user.dto.UserEditingDto;
import pl.ofertownia.security.user.dto.UserRegistrationDto;

import java.util.NoSuchElementException;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class Mapper {
    private final PasswordEncoder passwordEncoder;
    private final UserRoleRepository userRoleRepository;

    public Mapper(PasswordEncoder passwordEncoder, UserRoleRepository userRoleRepository) {
        this.passwordEncoder = passwordEncoder;
        this.userRoleRepository = userRoleRepository;
    }

    public User userRegistrationDtoToUser(UserRegistrationDto userRegistrationDto) {
        User user = new User();
        user.setEmail(userRegistrationDto.getEmail());
        String encodedPassword = passwordEncoder.encode(userRegistrationDto.getPassword());
        user.setPassword(encodedPassword);
        user.setFirstName(userRegistrationDto.getFirstName());
        user.setLastName(userRegistrationDto.getLastName());
        userRoleRepository.findByName(RoleEnum.USER)
                .ifPresentOrElse(
                        role -> user.getRoles().add(role),
                        () -> {
                            throw new NoSuchElementException();
                        }
                );
        return user;
    }

    public UserCredentialsDto userToUserCredentialsDto(User user) {
        Set<String> roles = user.getRoles()
                .stream()
                .map(UserRole::getName)
                .map(RoleEnum::name)
                .collect(Collectors.toSet());
        return new UserCredentialsDto(
                user.getEmail(),
                user.getPassword(),
                roles
        );
    }

    public UserEditingDto userToUserEditingDto(User user) {
        String rolesString = user.getRoles()
                .stream()
                .map(UserRole::getName)
                .map(RoleEnum::getTranslation)
                .collect(Collectors.joining(", "));
        UserEditingDto userEditingDto = new UserEditingDto();
        userEditingDto.setFirstName(user.getFirstName());
        userEditingDto.setLastName(user.getLastName());
        userEditingDto.setEmail(user.getEmail());
        userEditingDto.setOldPassword(""); // hasło na tym etapie nie będzie potrzebne kontrolerowi ani szablonowi
        userEditingDto.setNewPassword("");
        userEditingDto.setRoles(rolesString);
        return userEditingDto;
    }

    public UserDetailsDto userToUserDetailsDto(User user) {
        UserDetailsDto userDetailsDto = new UserDetailsDto();
        userDetailsDto.setId(user.getId());
        userDetailsDto.setFirstName(user.getFirstName());
        userDetailsDto.setLastName(user.getLastName());
        userDetailsDto.setEmail(user.getEmail());

        Set<RoleEnum> roles = user.getRoles()
                .stream()
                .map(UserRole::getName)
                .collect(Collectors.toSet());

        userDetailsDto.setRoles(roles);
        return userDetailsDto;
    }
}