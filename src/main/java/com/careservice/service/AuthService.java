package com.careservice.service;

import com.careservice.dto.auth.AuthResponse;
import com.careservice.dto.auth.LoginRequest;
import com.careservice.dto.auth.RegisterRequest;
import com.careservice.entity.*;
import com.careservice.repository.*;
import com.careservice.security.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AuthService {
    
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final CustomerRepository customerRepository;
    private final CaregiverRepository caregiverRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider tokenProvider;
    
    @Transactional
    public AuthResponse register(RegisterRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("Email is already taken!");
        }
        
        User user = new User();
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setFullName(request.getFullName());
        user.setPhoneNumber(request.getPhoneNumber());
        user.setAddress(request.getAddress());
        user.setEnabled(true);
        
        Set<Role> roles = new HashSet<>();
        
        if ("CAREGIVER".equalsIgnoreCase(request.getRole())) {
            Role caregiverRole = roleRepository.findByName(Role.RoleName.ROLE_CAREGIVER)
                    .orElseThrow(() -> new RuntimeException("Caregiver Role not found"));
            roles.add(caregiverRole);
            user.setStatus(User.UserStatus.PENDING_APPROVAL);
        } else {
            Role customerRole = roleRepository.findByName(Role.RoleName.ROLE_CUSTOMER)
                    .orElseThrow(() -> new RuntimeException("Customer Role not found"));
            roles.add(customerRole);
            user.setStatus(User.UserStatus.ACTIVE);
        }
        
        user.setRoles(roles);
        User savedUser = userRepository.save(user);
        
        // Create profile based on role
        if ("CAREGIVER".equalsIgnoreCase(request.getRole())) {
            Caregiver caregiver = new Caregiver();
            caregiver.setUser(savedUser);
            caregiver.setVerificationStatus(Caregiver.VerificationStatus.PENDING);
            caregiverRepository.save(caregiver);
        } else {
            Customer customer = new Customer();
            customer.setUser(savedUser);
            customerRepository.save(customer);
        }
        
        // Authenticate user
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
        );
        
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = tokenProvider.generateToken(authentication);
        
        List<String> roleNames = savedUser.getRoles().stream()
                .map(role -> role.getName().name())
                .collect(Collectors.toList());
        
        return new AuthResponse(jwt, savedUser.getId(), savedUser.getEmail(), 
                savedUser.getFullName(), roleNames);
    }
    
    public AuthResponse login(LoginRequest request) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
        );
        
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = tokenProvider.generateToken(authentication);
        
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("User not found"));
        
        List<String> roles = user.getRoles().stream()
                .map(role -> role.getName().name())
                .collect(Collectors.toList());
        
        return new AuthResponse(jwt, user.getId(), user.getEmail(), user.getFullName(), roles);
    }
}
