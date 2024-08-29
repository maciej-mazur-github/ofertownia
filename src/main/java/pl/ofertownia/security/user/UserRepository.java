package pl.ofertownia.security.user;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

interface UserRepository extends CrudRepository<User, Long> {
    Optional<User> findByEmail(String email);

    Optional<User> findByPasswordResetCode(String resetCode);

    boolean existsUserByEmail(String email);

    List<User> findUsersByRoles_Name(RoleEnum roleEnum);

    @Query("SELECT u FROM User u JOIN u.roles r WHERE NOT r.name = :role")
    List<User> findUsersWithRoleNotEqual(RoleEnum role);
}
