package com.careservice.service;

import com.careservice.dto.admin.*;
import com.careservice.dto.caregiver.CaregiverDTO;
import com.careservice.entity.*;
import com.careservice.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AdminService {
    
    private final UserRepository userRepository;
    private final CaregiverRepository caregiverRepository;
    private final BookingRepository bookingRepository;
    private final PaymentRepository paymentRepository;
    private final SupportTicketRepository supportTicketRepository;
    private final ServiceRepository serviceRepository;
    private final CustomerRepository customerRepository;
    private final RoleRepository roleRepository;
    private final NotificationService notificationService;
    private final PasswordEncoder passwordEncoder;
    
    public DashboardStatsDTO getDashboardStats() {
        DashboardStatsDTO stats = new DashboardStatsDTO();
        
        stats.setTotalUsers(userRepository.count());
        stats.setTotalCustomers(userRepository.countByRoleName(Role.RoleName.ROLE_CUSTOMER));
        stats.setTotalCaregivers(userRepository.countByRoleName(Role.RoleName.ROLE_CAREGIVER));
        stats.setPendingCaregivers(caregiverRepository.countByVerificationStatus(Caregiver.VerificationStatus.PENDING));
        stats.setApprovedCaregivers(caregiverRepository.countByVerificationStatus(Caregiver.VerificationStatus.APPROVED));
        
        stats.setTotalBookings(bookingRepository.count());
        stats.setPendingBookings(bookingRepository.countByStatus(com.careservice.entity.Booking.BookingStatus.PENDING));
        stats.setCompletedBookings(bookingRepository.countByStatus(com.careservice.entity.Booking.BookingStatus.COMPLETED));
        stats.setCancelledBookings(bookingRepository.countByStatus(com.careservice.entity.Booking.BookingStatus.CANCELLED));
        
        stats.setTotalPayments(paymentRepository.countCompletedPayments());
        Double totalRevenue = paymentRepository.getTotalRevenue();
        stats.setTotalRevenue(totalRevenue != null ? BigDecimal.valueOf(totalRevenue) : BigDecimal.ZERO);
        
        stats.setOpenTickets(supportTicketRepository.countByStatus(com.careservice.entity.SupportTicket.TicketStatus.OPEN));
        Long inProgress = supportTicketRepository.countByStatus(com.careservice.entity.SupportTicket.TicketStatus.IN_PROGRESS);
        Long escalated = supportTicketRepository.countByStatus(com.careservice.entity.SupportTicket.TicketStatus.ESCALATED);
        stats.setUnresolvedTickets((inProgress != null ? inProgress : 0L) + (escalated != null ? escalated : 0L));
        
        return stats;
    }
    
    @Transactional
    public CaregiverDTO approveCaregiver(Long caregiverId) {
        Caregiver caregiver = caregiverRepository.findById(caregiverId)
                .orElseThrow(() -> new RuntimeException("Caregiver not found"));
        
        caregiver.setVerificationStatus(Caregiver.VerificationStatus.APPROVED);
        caregiver.setVerifiedAt(LocalDateTime.now());
        caregiver.setIsAvailable(true);
        
        User user = caregiver.getUser();
        user.setStatus(User.UserStatus.ACTIVE);
        userRepository.save(user);
        
        Caregiver savedCaregiver = caregiverRepository.save(caregiver);
        
        notificationService.createNotification(
                user,
                com.careservice.entity.Notification.NotificationType.ACCOUNT_VERIFIED,
                "Account Verified",
                "Your caregiver account has been approved. You can now start accepting bookings."
        );
        
        return convertToDTO(savedCaregiver);
    }
    
    @Transactional
    public CaregiverDTO rejectCaregiver(Long caregiverId, String reason) {
        Caregiver caregiver = caregiverRepository.findById(caregiverId)
                .orElseThrow(() -> new RuntimeException("Caregiver not found"));
        
        caregiver.setVerificationStatus(Caregiver.VerificationStatus.REJECTED);
        caregiver.setRejectionReason(reason);
        
        User user = caregiver.getUser();
        user.setStatus(User.UserStatus.REJECTED);
        userRepository.save(user);
        
        Caregiver savedCaregiver = caregiverRepository.save(caregiver);
        
        notificationService.createNotification(
                user,
                com.careservice.entity.Notification.NotificationType.ACCOUNT_REJECTED,
                "Account Rejected",
                "Your caregiver application has been rejected. Reason: " + reason
        );
        
        return convertToDTO(savedCaregiver);
    }
    
    @Transactional
    public void suspendUser(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        
        user.setStatus(User.UserStatus.SUSPENDED);
        user.setEnabled(false);
        userRepository.save(user);
    }
    
    @Transactional
    public void activateUser(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        
        user.setStatus(User.UserStatus.ACTIVE);
        user.setEnabled(true);
        userRepository.save(user);
    }
    
    public List<CaregiverDTO> getPendingCaregivers() {
        return caregiverRepository.findByVerificationStatus(Caregiver.VerificationStatus.PENDING)
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }
    
    private CaregiverDTO convertToDTO(Caregiver caregiver) {
        CaregiverDTO dto = new CaregiverDTO();
        dto.setId(caregiver.getId());
        dto.setUserId(caregiver.getUser().getId());
        dto.setEmail(caregiver.getUser().getEmail());
        dto.setFullName(caregiver.getUser().getFullName());
        dto.setPhoneNumber(caregiver.getUser().getPhoneNumber());
        dto.setAddress(caregiver.getUser().getAddress());
        dto.setBio(caregiver.getBio());
        dto.setSkills(caregiver.getSkills());
        dto.setExperience(caregiver.getExperience());
        dto.setIdCardNumber(caregiver.getIdCardNumber());
        dto.setIdCardUrl(caregiver.getIdCardUrl());
        dto.setCertificateUrls(caregiver.getCertificateUrls());
        dto.setVerificationStatus(caregiver.getVerificationStatus().name());
        dto.setRejectionReason(caregiver.getRejectionReason());
        dto.setIsAvailable(caregiver.getIsAvailable());
        dto.setAvailableSchedule(caregiver.getAvailableSchedule());
        dto.setHourlyRate(caregiver.getHourlyRate());
        dto.setRating(caregiver.getRating());
        dto.setTotalReviews(caregiver.getTotalReviews());
        dto.setCompletedBookings(caregiver.getCompletedBookings());
        dto.setTotalEarnings(caregiver.getTotalEarnings());
        dto.setVerifiedAt(caregiver.getVerifiedAt());
        dto.setCreatedAt(caregiver.getCreatedAt());
        return dto;
    }
    
    // ==================== User Management Methods ====================
    
    public Page<UserDTO> getAllUsers(Pageable pageable) {
        return userRepository.findAll(pageable).map(this::convertUserToDTO);
    }
    
    public UserDTO getUserById(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + userId));
        return convertUserToDTO(user);
    }
    
    @Transactional
    public UserDTO createUser(CreateUserRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("Email already exists: " + request.getEmail());
        }
        
        User user = new User();
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setFullName(request.getFullName());
        user.setPhoneNumber(request.getPhoneNumber());
        user.setAddress(request.getAddress());
        user.setStatus(User.UserStatus.ACTIVE);
        user.setEnabled(true);
        
        // Assign role - convert string to enum
        String roleStr = request.getRole().startsWith("ROLE_") ? request.getRole() : "ROLE_" + request.getRole();
        Role.RoleName roleName;
        switch (roleStr) {
            case "ROLE_ADMIN":
                roleName = Role.RoleName.ROLE_ADMIN;
                break;
            case "ROLE_SUPPORT":
                roleName = Role.RoleName.ROLE_SUPPORT;
                break;
            case "ROLE_CUSTOMER":
                roleName = Role.RoleName.ROLE_CUSTOMER;
                break;
            case "ROLE_CAREGIVER":
                roleName = Role.RoleName.ROLE_CAREGIVER;
                break;
            default:
                throw new RuntimeException("Invalid role: " + roleStr);
        }
        
        Role role = roleRepository.findByName(roleName)
                .orElseThrow(() -> new RuntimeException("Role not found: " + roleName));
        Set<Role> roles = new HashSet<>();
        roles.add(role);
        user.setRoles(roles);
        
        User savedUser = userRepository.save(user);
        
        // Create associated profile based on role
        if (roleName.equals("ROLE_CUSTOMER")) {
            Customer customer = new Customer();
            customer.setUser(savedUser);
            customerRepository.save(customer);
        } else if (roleName.equals("ROLE_CAREGIVER")) {
            Caregiver caregiver = new Caregiver();
            caregiver.setUser(savedUser);
            caregiver.setVerificationStatus(Caregiver.VerificationStatus.PENDING);
            caregiverRepository.save(caregiver);
        }
        
        return convertUserToDTO(savedUser);
    }
    
    @Transactional
    public UserDTO updateUser(Long userId, UpdateUserRequest request) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + userId));
        
        if (request.getEmail() != null && !request.getEmail().equals(user.getEmail())) {
            if (userRepository.existsByEmail(request.getEmail())) {
                throw new RuntimeException("Email already exists: " + request.getEmail());
            }
            user.setEmail(request.getEmail());
        }
        
        if (request.getPassword() != null && !request.getPassword().isEmpty()) {
            user.setPassword(passwordEncoder.encode(request.getPassword()));
        }
        
        if (request.getFullName() != null) {
            user.setFullName(request.getFullName());
        }
        
        if (request.getPhoneNumber() != null) {
            user.setPhoneNumber(request.getPhoneNumber());
        }
        
        if (request.getAddress() != null) {
            user.setAddress(request.getAddress());
        }
        
        if (request.getRole() != null) {
            String roleStr = request.getRole().startsWith("ROLE_") ? request.getRole() : "ROLE_" + request.getRole();
            Role.RoleName roleName;
            switch (roleStr) {
                case "ROLE_ADMIN":
                    roleName = Role.RoleName.ROLE_ADMIN;
                    break;
                case "ROLE_SUPPORT":
                    roleName = Role.RoleName.ROLE_SUPPORT;
                    break;
                case "ROLE_CUSTOMER":
                    roleName = Role.RoleName.ROLE_CUSTOMER;
                    break;
                case "ROLE_CAREGIVER":
                    roleName = Role.RoleName.ROLE_CAREGIVER;
                    break;
                default:
                    throw new RuntimeException("Invalid role: " + roleStr);
            }
            
            Role role = roleRepository.findByName(roleName)
                    .orElseThrow(() -> new RuntimeException("Role not found: " + roleName));
            Set<Role> roles = new HashSet<>();
            roles.add(role);
            user.setRoles(roles);
        }
        
        if (request.getStatus() != null) {
            user.setStatus(User.UserStatus.valueOf(request.getStatus()));
            user.setEnabled(request.getStatus().equals("ACTIVE"));
        }
        
        User updatedUser = userRepository.save(user);
        return convertUserToDTO(updatedUser);
    }
    
    @Transactional
    public void deleteUser(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + userId));
        
        // Unassign support tickets where this user is the assignee
        List<SupportTicket> assignedTickets = supportTicketRepository.findByAssignedTo(user);
        for (SupportTicket ticket : assignedTickets) {
            ticket.setAssignedTo(null);
            supportTicketRepository.save(ticket);
        }
        
        // Delete support tickets created by this user
        List<SupportTicket> createdTickets = supportTicketRepository.findByUser(user);
        supportTicketRepository.deleteAll(createdTickets);
        
        // Notifications, Customer, and Caregiver profiles will be cascade deleted automatically
        
        userRepository.delete(user);
    }
    
    private UserDTO convertUserToDTO(User user) {
        Set<String> roleNames = user.getRoles().stream()
                .map(role -> role.getName().name())
                .collect(Collectors.toSet());
        
        return UserDTO.builder()
                .id(user.getId())
                .email(user.getEmail())
                .fullName(user.getFullName())
                .phoneNumber(user.getPhoneNumber())
                .address(user.getAddress())
                .roles(roleNames)
                .status(user.getStatus().name())
                .createdAt(user.getCreatedAt())
                .updatedAt(user.getUpdatedAt())
                .build();
    }
    
    // ==================== Caregiver Management Methods ====================
    
    public Page<CaregiverDTO> getAllCaregivers(Pageable pageable) {
        return caregiverRepository.findAll(pageable).map(this::convertToDTO);
    }
    
    public CaregiverDTO getCaregiverById(Long caregiverId) {
        Caregiver caregiver = caregiverRepository.findById(caregiverId)
                .orElseThrow(() -> new RuntimeException("Caregiver not found with id: " + caregiverId));
        return convertToDTO(caregiver);
    }
    
    @Transactional
    public CaregiverDTO createCaregiver(CreateUserRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("Email already exists: " + request.getEmail());
        }
        
        // Create user
        User user = new User();
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setFullName(request.getFullName());
        user.setPhoneNumber(request.getPhoneNumber());
        user.setAddress(request.getAddress());
        user.setStatus(User.UserStatus.PENDING_APPROVAL);
        user.setEnabled(true);
        
        // Assign caregiver role
        Role role = roleRepository.findByName(Role.RoleName.ROLE_CAREGIVER)
                .orElseThrow(() -> new RuntimeException("Caregiver role not found"));
        Set<Role> roles = new HashSet<>();
        roles.add(role);
        user.setRoles(roles);
        
        User savedUser = userRepository.save(user);
        
        // Create caregiver profile
        Caregiver caregiver = new Caregiver();
        caregiver.setUser(savedUser);
        caregiver.setVerificationStatus(Caregiver.VerificationStatus.PENDING);
        caregiver.setIsAvailable(false);
        caregiver.setHourlyRate(BigDecimal.ZERO);
        caregiver.setRating(0.0);
        caregiver.setTotalReviews(0);
        caregiver.setCompletedBookings(0);
        caregiver.setTotalEarnings(BigDecimal.ZERO);
        
        Caregiver savedCaregiver = caregiverRepository.save(caregiver);
        return convertToDTO(savedCaregiver);
    }
    
    @Transactional
    public CaregiverDTO updateCaregiver(Long caregiverId, UpdateUserRequest request) {
        Caregiver caregiver = caregiverRepository.findById(caregiverId)
                .orElseThrow(() -> new RuntimeException("Caregiver not found with id: " + caregiverId));
        
        User user = caregiver.getUser();
        
        if (request.getEmail() != null && !request.getEmail().equals(user.getEmail())) {
            if (userRepository.existsByEmail(request.getEmail())) {
                throw new RuntimeException("Email already exists: " + request.getEmail());
            }
            user.setEmail(request.getEmail());
        }
        
        if (request.getPassword() != null && !request.getPassword().isEmpty()) {
            user.setPassword(passwordEncoder.encode(request.getPassword()));
        }
        
        if (request.getFullName() != null) {
            user.setFullName(request.getFullName());
        }
        
        if (request.getPhoneNumber() != null) {
            user.setPhoneNumber(request.getPhoneNumber());
        }
        
        if (request.getAddress() != null) {
            user.setAddress(request.getAddress());
        }
        
        if (request.getStatus() != null) {
            user.setStatus(User.UserStatus.valueOf(request.getStatus()));
            user.setEnabled(request.getStatus().equals("ACTIVE"));
        }
        
        userRepository.save(user);
        return convertToDTO(caregiver);
    }
    
    @Transactional
    public void deleteCaregiver(Long caregiverId) {
        Caregiver caregiver = caregiverRepository.findById(caregiverId)
                .orElseThrow(() -> new RuntimeException("Caregiver not found with id: " + caregiverId));
        caregiverRepository.delete(caregiver);
    }
    
    // ==================== Booking Management Methods ====================
    
    public Page<com.careservice.dto.booking.BookingDTO> getAllBookings(Pageable pageable) {
        return bookingRepository.findAll(pageable).map(this::convertBookingToDTO);
    }
    
    public com.careservice.dto.booking.BookingDTO getBookingById(Long bookingId) {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new RuntimeException("Booking not found with id: " + bookingId));
        return convertBookingToDTO(booking);
    }
    
    @Transactional
    public com.careservice.dto.booking.BookingDTO createBooking(CreateBookingRequest request) {
        Customer customer = customerRepository.findById(request.getCustomerId())
                .orElseThrow(() -> new RuntimeException("Customer not found with id: " + request.getCustomerId()));
        
        com.careservice.entity.Service service = serviceRepository.findById(request.getServiceId())
                .orElseThrow(() -> new RuntimeException("Service not found with id: " + request.getServiceId()));
        
        Booking booking = new Booking();
        booking.setBookingCode(generateBookingCode());
        booking.setCustomer(customer);
        booking.setService(service);
        booking.setScheduledStartTime(request.getScheduledStartTime());
        booking.setScheduledEndTime(request.getScheduledEndTime());
        booking.setTotalPrice(request.getTotalPrice());
        booking.setCustomerNote(request.getCustomerNote());
        booking.setLocation(request.getLocation());
        booking.setStatus(Booking.BookingStatus.PENDING);
        
        if (request.getCaregiverId() != null) {
            Caregiver caregiver = caregiverRepository.findById(request.getCaregiverId())
                    .orElseThrow(() -> new RuntimeException("Caregiver not found with id: " + request.getCaregiverId()));
            booking.setCaregiver(caregiver);
            booking.setStatus(Booking.BookingStatus.ASSIGNED);
        }
        
        Booking savedBooking = bookingRepository.save(booking);
        return convertBookingToDTO(savedBooking);
    }
    
    @Transactional
    public com.careservice.dto.booking.BookingDTO updateBooking(Long bookingId, UpdateBookingRequest request) {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new RuntimeException("Booking not found with id: " + bookingId));
        
        if (request.getCaregiverId() != null) {
            Caregiver caregiver = caregiverRepository.findById(request.getCaregiverId())
                    .orElseThrow(() -> new RuntimeException("Caregiver not found with id: " + request.getCaregiverId()));
            booking.setCaregiver(caregiver);
        }
        
        if (request.getScheduledStartTime() != null) {
            booking.setScheduledStartTime(request.getScheduledStartTime());
        }
        
        if (request.getScheduledEndTime() != null) {
            booking.setScheduledEndTime(request.getScheduledEndTime());
        }
        
        if (request.getTotalPrice() != null) {
            booking.setTotalPrice(request.getTotalPrice());
        }
        
        if (request.getStatus() != null) {
            booking.setStatus(Booking.BookingStatus.valueOf(request.getStatus()));
        }
        
        if (request.getCustomerNote() != null) {
            booking.setCustomerNote(request.getCustomerNote());
        }
        
        if (request.getCaregiverNote() != null) {
            booking.setCaregiverNote(request.getCaregiverNote());
        }
        
        if (request.getLocation() != null) {
            booking.setLocation(request.getLocation());
        }
        
        if (request.getCancellationReason() != null) {
            booking.setCancellationReason(request.getCancellationReason());
            if (booking.getStatus() == Booking.BookingStatus.CANCELLED) {
                booking.setCancelledAt(LocalDateTime.now());
            }
        }
        
        Booking updatedBooking = bookingRepository.save(booking);
        return convertBookingToDTO(updatedBooking);
    }
    
    @Transactional
    public com.careservice.dto.booking.BookingDTO updateBookingStatus(Long bookingId, String status) {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new RuntimeException("Booking not found with id: " + bookingId));
        
        booking.setStatus(Booking.BookingStatus.valueOf(status));
        
        if (status.equals("CANCELLED")) {
            booking.setCancelledAt(LocalDateTime.now());
        }
        
        Booking updatedBooking = bookingRepository.save(booking);
        return convertBookingToDTO(updatedBooking);
    }
    
    @Transactional
    public void deleteBooking(Long bookingId) {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new RuntimeException("Booking not found with id: " + bookingId));
        bookingRepository.delete(booking);
    }
    
    private com.careservice.dto.booking.BookingDTO convertBookingToDTO(Booking booking) {
        com.careservice.dto.booking.BookingDTO dto = new com.careservice.dto.booking.BookingDTO();
        dto.setId(booking.getId());
        dto.setBookingCode(booking.getBookingCode());
        dto.setCustomerId(booking.getCustomer().getId());
        dto.setCustomerName(booking.getCustomer().getUser().getFullName());
        
        if (booking.getCaregiver() != null) {
            dto.setCaregiverId(booking.getCaregiver().getId());
            dto.setCaregiverName(booking.getCaregiver().getUser().getFullName());
        }
        
        dto.setServiceId(booking.getService().getId());
        dto.setServiceName(booking.getService().getName());
        dto.setScheduledStartTime(booking.getScheduledStartTime());
        dto.setScheduledEndTime(booking.getScheduledEndTime());
        dto.setActualStartTime(booking.getActualStartTime());
        dto.setActualEndTime(booking.getActualEndTime());
        dto.setTotalPrice(booking.getTotalPrice());
        dto.setStatus(booking.getStatus().name());
        dto.setCustomerNote(booking.getCustomerNote());
        dto.setCaregiverNote(booking.getCaregiverNote());
        dto.setLocation(booking.getLocation());
        dto.setCancellationReason(booking.getCancellationReason());
        dto.setCreatedAt(booking.getCreatedAt());
        dto.setUpdatedAt(booking.getUpdatedAt());
        
        return dto;
    }
    
    private String generateBookingCode() {
        return "BK" + System.currentTimeMillis();
    }
    
    // ==================== Service Management Methods ====================
    
    public Page<ServiceDTO> getAllServices(Pageable pageable) {
        return serviceRepository.findAll(pageable).map(this::convertServiceToDTO);
    }
    
    public ServiceDTO getServiceById(Long serviceId) {
        com.careservice.entity.Service service = serviceRepository.findById(serviceId)
                .orElseThrow(() -> new RuntimeException("Service not found with id: " + serviceId));
        return convertServiceToDTO(service);
    }
    
    @Transactional
    public ServiceDTO createService(CreateServiceRequest request) {
        com.careservice.entity.Service service = new com.careservice.entity.Service();
        service.setName(request.getName());
        service.setDescription(request.getDescription());
        service.setCategory(com.careservice.entity.Service.ServiceCategory.valueOf(request.getCategory()));
        service.setBasePrice(request.getBasePrice());
        service.setDurationMinutes(request.getDurationMinutes());
        service.setIsActive(request.getIsActive() != null ? request.getIsActive() : true);
        
        com.careservice.entity.Service savedService = serviceRepository.save(service);
        return convertServiceToDTO(savedService);
    }
    
    @Transactional
    public ServiceDTO updateService(Long serviceId, UpdateServiceRequest request) {
        com.careservice.entity.Service service = serviceRepository.findById(serviceId)
                .orElseThrow(() -> new RuntimeException("Service not found with id: " + serviceId));
        
        if (request.getName() != null) {
            service.setName(request.getName());
        }
        
        if (request.getDescription() != null) {
            service.setDescription(request.getDescription());
        }
        
        if (request.getCategory() != null) {
            service.setCategory(com.careservice.entity.Service.ServiceCategory.valueOf(request.getCategory()));
        }
        
        if (request.getBasePrice() != null) {
            service.setBasePrice(request.getBasePrice());
        }
        
        if (request.getDurationMinutes() != null) {
            service.setDurationMinutes(request.getDurationMinutes());
        }
        
        if (request.getIsActive() != null) {
            service.setIsActive(request.getIsActive());
        }
        
        com.careservice.entity.Service updatedService = serviceRepository.save(service);
        return convertServiceToDTO(updatedService);
    }
    
    @Transactional
    public ServiceDTO toggleServiceStatus(Long serviceId) {
        com.careservice.entity.Service service = serviceRepository.findById(serviceId)
                .orElseThrow(() -> new RuntimeException("Service not found with id: " + serviceId));
        
        service.setIsActive(!service.getIsActive());
        com.careservice.entity.Service updatedService = serviceRepository.save(service);
        return convertServiceToDTO(updatedService);
    }
    
    @Transactional
    public void deleteService(Long serviceId) {
        com.careservice.entity.Service service = serviceRepository.findById(serviceId)
                .orElseThrow(() -> new RuntimeException("Service not found with id: " + serviceId));
        serviceRepository.delete(service);
    }
    
    public List<ServiceDTO> getActiveServices() {
        return serviceRepository.findByIsActive(true)
                .stream()
                .map(this::convertServiceToDTO)
                .collect(Collectors.toList());
    }
    
    private ServiceDTO convertServiceToDTO(com.careservice.entity.Service service) {
        return ServiceDTO.builder()
                .id(service.getId())
                .name(service.getName())
                .description(service.getDescription())
                .category(service.getCategory().name())
                .basePrice(service.getBasePrice())
                .durationMinutes(service.getDurationMinutes())
                .isActive(service.getIsActive())
                .createdAt(service.getCreatedAt())
                .updatedAt(service.getUpdatedAt())
                .build();
    }
}
