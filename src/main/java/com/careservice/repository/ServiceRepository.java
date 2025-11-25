package com.careservice.repository;

import com.careservice.entity.Service;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ServiceRepository extends JpaRepository<Service, Long> {
    
    List<Service> findByIsActiveTrue();
    
    List<Service> findByIsActive(Boolean isActive);
    
    List<Service> findByCategory(Service.ServiceCategory category);
    
    List<Service> findByCategoryAndIsActiveTrue(Service.ServiceCategory category);
}
