package com.careservice.service;

import com.careservice.dto.customer.CreateAddressRequest;
import com.careservice.dto.customer.CustomerAddressDTO;
import com.careservice.entity.CustomerAddress;
import com.careservice.entity.User;
import com.careservice.repository.CustomerAddressRepository;
import com.careservice.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CustomerAddressService {
    
    private final CustomerAddressRepository addressRepository;
    private final UserRepository userRepository;
    
    private User getCurrentUser() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }
    
    public List<CustomerAddressDTO> getMyAddresses() {
        User user = getCurrentUser();
        List<CustomerAddress> addresses = addressRepository.findByUserOrderByIsDefaultDescCreatedAtDesc(user);
        
        return addresses.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }
    
    @Transactional
    public CustomerAddressDTO createAddress(CreateAddressRequest request) {
        User user = getCurrentUser();
        
        // If this is set as default, unset other defaults
        if (Boolean.TRUE.equals(request.getIsDefault())) {
            addressRepository.findByUserAndIsDefaultTrue(user)
                    .ifPresent(addr -> {
                        addr.setIsDefault(false);
                        addressRepository.save(addr);
                    });
        }
        
        // If this is the first address, make it default
        List<CustomerAddress> existingAddresses = addressRepository.findByUserOrderByIsDefaultDescCreatedAtDesc(user);
        boolean isFirstAddress = existingAddresses.isEmpty();
        
        CustomerAddress address = new CustomerAddress();
        address.setUser(user);
        address.setAddress(request.getAddress());
        address.setLabel(request.getLabel());
        address.setIsDefault(isFirstAddress || Boolean.TRUE.equals(request.getIsDefault()));
        
        CustomerAddress saved = addressRepository.save(address);
        return convertToDTO(saved);
    }
    
    @Transactional
    public CustomerAddressDTO setDefaultAddress(Long addressId) {
        User user = getCurrentUser();
        
        CustomerAddress address = addressRepository.findByIdAndUser(addressId, user)
                .orElseThrow(() -> new RuntimeException("Address not found"));
        
        // Unset current default
        addressRepository.findByUserAndIsDefaultTrue(user)
                .ifPresent(addr -> {
                    addr.setIsDefault(false);
                    addressRepository.save(addr);
                });
        
        // Set new default
        address.setIsDefault(true);
        CustomerAddress saved = addressRepository.save(address);
        
        return convertToDTO(saved);
    }
    
    @Transactional
    public void deleteAddress(Long addressId) {
        User user = getCurrentUser();
        
        CustomerAddress address = addressRepository.findByIdAndUser(addressId, user)
                .orElseThrow(() -> new RuntimeException("Address not found"));
        
        boolean wasDefault = address.getIsDefault();
        addressRepository.delete(address);
        
        // If deleted address was default, set another as default
        if (wasDefault) {
            List<CustomerAddress> remaining = addressRepository.findByUserOrderByIsDefaultDescCreatedAtDesc(user);
            if (!remaining.isEmpty()) {
                CustomerAddress newDefault = remaining.get(0);
                newDefault.setIsDefault(true);
                addressRepository.save(newDefault);
            }
        }
    }
    
    public CustomerAddressDTO getDefaultAddress() {
        User user = getCurrentUser();
        return addressRepository.findByUserAndIsDefaultTrue(user)
                .map(this::convertToDTO)
                .orElse(null);
    }
    
    private CustomerAddressDTO convertToDTO(CustomerAddress address) {
        return CustomerAddressDTO.builder()
                .id(address.getId())
                .address(address.getAddress())
                .label(address.getLabel())
                .isDefault(address.getIsDefault())
                .createdAt(address.getCreatedAt())
                .build();
    }
}
