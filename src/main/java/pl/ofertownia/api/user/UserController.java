package pl.ofertownia.api.user;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.ofertownia.security.user.UserService;
import pl.ofertownia.security.user.dto.UserDetailsDto;

@RestController
@RequestMapping("/api/admin")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/credentials/{id}")
    ResponseEntity<UserDetailsDto> changeCredentials(@RequestBody Boolean grantAdminRights,
                                              @PathVariable Long id) {
        UserDetailsDto userDetailsDto = userService.changeCredentials(id, grantAdminRights);
        return ResponseEntity.ok(userDetailsDto);
    }

    @PostMapping("/delete/{id}")
    ResponseEntity<?> deleteUser(@PathVariable Long id) {
        userService.deleteUserById(id);
        return ResponseEntity.noContent().build();
    }
}
