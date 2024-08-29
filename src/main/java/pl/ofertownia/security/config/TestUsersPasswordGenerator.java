package pl.ofertownia.security.config;

import org.springframework.security.crypto.argon2.Argon2PasswordEncoder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.scrypt.SCryptPasswordEncoder;

public class TestUsersPasswordGenerator {
    public static void main(String[] args) {
        //jan / asdf
        System.out.println("{bcrypt}" + new BCryptPasswordEncoder().encode("asdf"));

        //piotr / qwerty
        System.out.println("{argon2}" + Argon2PasswordEncoder.defaultsForSpringSecurity_v5_8().encode("qwerty"));

        // anna / zxcv
        System.out.println("{scrypt}" + SCryptPasswordEncoder.defaultsForSpringSecurity_v5_8().encode("zxcv"));

        //karol@kowal.com / mnbv
        System.out.println("{bcrypt}" + new BCryptPasswordEncoder().encode("mnbv"));

        //john@smith.com / hjkl
        System.out.println("{bcrypt}" + new BCryptPasswordEncoder().encode("hjkl"));

        //kate@moss.com / hard
        System.out.println("{bcrypt}" + new BCryptPasswordEncoder().encode("hard"));
    }
}
