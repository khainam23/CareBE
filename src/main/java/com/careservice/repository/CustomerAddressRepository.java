package com.careservice.repository;

import com.careservice.entity.CustomerAddress;
import com.careservice.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CustomerAddressRepository extends JpaRepository<CustomerAddress, Long> {
    
    List<CustomerAddress> findByUserOrderByIsDefaultDescCreatedAtDesc(User user);
    
    Optional<CustomerAddress> findByUserAndIsDefaultTrue(User user);
    
    Optional<CustomerAddress> findByIdAndUser(Long id, User user);
}
