package com.careservice.service;

import com.careservice.repository.ServiceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ServiceService {
    
    private final ServiceRepository serviceRepository;
    
    public List<com.careservice.entity.Service> getAllServices() {
        return serviceRepository.findAll();
    }
    
    public List<com.careservice.entity.Service> getActiveServices() {
        return serviceRepository.findByIsActiveTrue();
    }
    
    public com.careservice.entity.Service getServiceById(Long id) {
        return serviceRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Service not found"));
    }
    
    public List<com.careservice.entity.Service> getServicesByCategory(com.careservice.entity.Service.ServiceCategory category) {
        return serviceRepository.findByCategoryAndIsActiveTrue(category);
    }
    
    @Transactional
    public com.careservice.entity.Service createService(com.careservice.entity.Service service) {
        return serviceRepository.save(service);
    }
    
    @Transactional
    public com.careservice.entity.Service updateService(Long id, com.careservice.entity.Service serviceDetails) {
        com.careservice.entity.Service service = getServiceById(id);
        
        service.setName(serviceDetails.getName());
        service.setDescription(serviceDetails.getDescription());
        service.setCategory(serviceDetails.getCategory());
        service.setBasePrice(serviceDetails.getBasePrice());
        service.setDurationMinutes(serviceDetails.getDurationMinutes());
        service.setImageUrl(serviceDetails.getImageUrl());
        service.setRequirements(serviceDetails.getRequirements());
        
        return serviceRepository.save(service);
    }
    
    @Transactional
    public void toggleServiceStatus(Long id) {
        com.careservice.entity.Service service = getServiceById(id);
        service.setIsActive(!service.getIsActive());
        serviceRepository.save(service);
    }
    
    @Transactional
    public void deleteService(Long id) {
        com.careservice.entity.Service service = getServiceById(id);
        service.setIsActive(false);
        serviceRepository.save(service);
    }
}
