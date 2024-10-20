package pl.ofertownia.security.config;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import java.util.Scanner;

public class TestUsersPasswordGenerator {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter the original password:");
        String originalPassword = scanner.nextLine();
        System.out.println("Encoded password:");
        System.out.println("{bcrypt}" + new BCryptPasswordEncoder().encode(originalPassword));
    }
}
