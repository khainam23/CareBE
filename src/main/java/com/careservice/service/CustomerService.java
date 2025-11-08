package com.careservice.service;

import com.careservice.dto.customer.CustomerProfileDTO;
import com.careservice.dto.customer.UpdateCustomerProfileRequest;
import com.careservice.entity.Customer;
import com.careservice.entity.User;
import com.careservice.repository.CustomerRepository;
import com.careservice.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CustomerService {
    
    private final CustomerRepository customerRepository;
    private final UserRepository userRepository;
    
    public CustomerProfileDTO getCustomerProfile() {
        User currentUser = getCurrentUser();
        Customer customer = customerRepository.findByUser(currentUser)
                .orElseThrow(() -> new RuntimeException("Customer profile not found"));
        
        return mapToDTO(customer);
    }
    
    @Transactional
    public CustomerProfileDTO updateCustomerProfile(UpdateCustomerProfileRequest request) {
        User currentUser = getCurrentUser();
        Customer customer = customerRepository.findByUser(currentUser)
                .orElseThrow(() -> new RuntimeException("Customer profile not found"));
        
        // Update user info
        currentUser.setFullName(request.getName());
        currentUser.setPhoneNumber(request.getPhone());
        currentUser.setAddress(request.getAddress());
        currentUser.setDateOfBirth(request.getDateOfBirth());
        currentUser.setGender(request.getGender());
        userRepository.save(currentUser);
        
        // Update customer specific info
        customer.setEmergencyContact(request.getEmergencyContact());
        customer.setEmergencyPhone(request.getEmergencyPhone());
        customer.setSpecialRequirements(request.getNotes());
        customerRepository.save(customer);
        
        return mapToDTO(customer);
    }
    
    private CustomerProfileDTO mapToDTO(Customer customer) {
        User user = customer.getUser();
        return CustomerProfileDTO.builder()
                .id(customer.getId())
                .name(user.getFullName())
                .email(user.getEmail())
                .phone(user.getPhoneNumber())
                .address(user.getAddress())
                .dateOfBirth(user.getDateOfBirth())
                .gender(user.getGender())
                .notes(customer.getSpecialRequirements())
                .emergencyContact(customer.getEmergencyContact())
                .emergencyPhone(customer.getEmergencyPhone())
                .build();
    }
    
    private User getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }
}
