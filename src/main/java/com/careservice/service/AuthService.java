package com.careservice.service;

import com.careservice.dto.auth.*;
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

import java.math.BigDecimal;
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
    
    /**
     * Đăng ký tài khoản Khách hàng
     */
    @Transactional
    public AuthResponse registerCustomer(CustomerRegisterRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("Email đã được sử dụng!");
        }
        
        // Tạo User
        User user = new User();
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setFullName(request.getFullName());
        user.setPhoneNumber(request.getPhoneNumber());
        user.setAddress(request.getAddress());
        user.setEnabled(true);
        user.setStatus(User.UserStatus.ACTIVE);
        
        // Gán role CUSTOMER
        Set<Role> roles = new HashSet<>();
        Role customerRole = roleRepository.findByName(Role.RoleName.ROLE_CUSTOMER)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy quyền Khách hàng"));
        roles.add(customerRole);
        user.setRoles(roles);
        
        User savedUser = userRepository.save(user);
        
        // Tạo Customer profile
        Customer customer = new Customer();
        customer.setUser(savedUser);
        customer.setEmergencyContact(request.getEmergencyContactName());
        customer.setEmergencyPhone(request.getEmergencyContactPhone());
        customerRepository.save(customer);
        
        // Tạo JWT token
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
    
    /**
     * Đăng ký tài khoản Chuyên viên chăm sóc
     */
    @Transactional
    public AuthResponse registerCaregiver(CaregiverRegisterRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("Email đã được sử dụng!");
        }
        
        // Tạo User
        User user = new User();
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setFullName(request.getFullName());
        user.setPhoneNumber(request.getPhoneNumber());
        user.setAddress(request.getAddress());
        user.setEnabled(true);
        user.setStatus(User.UserStatus.PENDING_APPROVAL); // Chờ duyệt
        
        // Gán role CAREGIVER
        Set<Role> roles = new HashSet<>();
        Role caregiverRole = roleRepository.findByName(Role.RoleName.ROLE_CAREGIVER)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy quyền Chuyên viên"));
        roles.add(caregiverRole);
        user.setRoles(roles);
        
        User savedUser = userRepository.save(user);
        
        // Tạo Caregiver profile với thông tin bổ sung
        Caregiver caregiver = new Caregiver();
        caregiver.setUser(savedUser);
        caregiver.setBio(request.getBio());
        caregiver.setSkills(request.getSkills());
        caregiver.setExperience(request.getYearsOfExperience() != null ? 
            request.getYearsOfExperience() + " năm kinh nghiệm" : null);
        caregiver.setIdCardNumber(request.getIdCardNumber());
        caregiver.setHourlyRate(BigDecimal.valueOf(120000));
        caregiver.setCertificateUrls(request.getCertifications());
        caregiver.setVerificationStatus(Caregiver.VerificationStatus.PENDING);
        caregiver.setIsAvailable(false); // Chưa khả dụng cho đến khi được phê duyệt
        caregiverRepository.save(caregiver);
        
        // Tạo JWT token
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
    
    /**
     * Đăng ký chung (deprecated)
     */
    @Deprecated
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
    
    /**
     * Đăng nhập
     */
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
