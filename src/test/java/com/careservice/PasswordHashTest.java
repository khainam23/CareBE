package com.careservice;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class PasswordHashTest {
    public static void main(String[] args) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        
        // Test passwords
        String[] passwords = {"admin123", "support123", "customer123", "caregiver123"};
        
        System.out.println("=== Generating BCrypt Hashes ===");
        for (String password : passwords) {
            String hash = encoder.encode(password);
            System.out.println(password + " -> " + hash);
        }
        
        System.out.println("\n=== Testing Existing Hashes ===");
        
        // Test caregiver password
        String caregiverHash = "$2a$10$P7J1.6mL1PVrZjXGKLTQEujMuCmW3rPJwCgE9vFkrGBXRm/8pW/4O";
        boolean matches = encoder.matches("caregiver123", caregiverHash);
        System.out.println("caregiver123 matches hash: " + matches);
        
        // Test admin password
        String adminHash = "$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy";
        matches = encoder.matches("admin123", adminHash);
        System.out.println("admin123 matches hash: " + matches);
        
        // Test support password
        String supportHash = "$2a$10$8cjz47bjbR4Mn8GMg9IZx.vyjhLXR/SKKMSZ9.mP9vpMu0ssKi8GW";
        matches = encoder.matches("support123", supportHash);
        System.out.println("support123 matches hash: " + matches);
        
        // Test customer password
        String customerHash = "$2a$10$DKs.Pii4ggYjGZhkNg3HPu.YdOo3gPqFXQ8HpZ7.hFYhKJFAiQKh6";
        matches = encoder.matches("customer123", customerHash);
        System.out.println("customer123 matches hash: " + matches);
    }
}
