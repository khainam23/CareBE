package com.careservice.service;

import com.careservice.dto.payment.PaymentDTO;
import com.careservice.dto.payment.PaymentRequest;
import com.careservice.entity.Booking;
import com.careservice.entity.Notification;
import com.careservice.entity.Payment;
import com.careservice.repository.BookingRepository;
import com.careservice.repository.PaymentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PaymentService {
    
    private final PaymentRepository paymentRepository;
    private final BookingRepository bookingRepository;
    private final NotificationService notificationService;
    
    @Transactional
    public PaymentDTO createPayment(PaymentRequest request) {
        Booking booking = bookingRepository.findById(request.getBookingId())
                .orElseThrow(() -> new RuntimeException("Booking not found"));
        
        // Check if payment already exists
        if (paymentRepository.findByBooking(booking).isPresent()) {
            throw new RuntimeException("Payment already exists for this booking");
        }
        
        Payment payment = new Payment();
        payment.setTransactionId(generateTransactionId());
        payment.setBooking(booking);
        payment.setAmount(booking.getTotalPrice());
        payment.setPaymentMethod(Payment.PaymentMethod.valueOf(request.getPaymentMethod()));
        payment.setStatus(Payment.PaymentStatus.PROCESSING);
        payment.setNotes(request.getNotes());
        
        Payment savedPayment = paymentRepository.save(payment);
        
        // Simulate payment processing
        processPayment(savedPayment);
        
        return convertToDTO(savedPayment);
    }
    
    @Transactional
    private void processPayment(Payment payment) {
        // Simulate payment gateway processing
        // In real scenario, this would integrate with actual payment gateway
        
        try {
            // Simulate success
            payment.setStatus(Payment.PaymentStatus.COMPLETED);
            payment.setPaidAt(LocalDateTime.now());
            payment.setPaymentGatewayResponse("SUCCESS");
            
            // Update booking status
            Booking booking = payment.getBooking();
            if (booking.getStatus() == Booking.BookingStatus.PENDING) {
                booking.setStatus(Booking.BookingStatus.CONFIRMED);
                bookingRepository.save(booking);
            }
            
            paymentRepository.save(payment);
            
            notificationService.createNotification(
                    booking.getCustomer().getUser(),
                    Notification.NotificationType.PAYMENT_SUCCESS,
                    "Payment Successful",
                    "Your payment of " + payment.getAmount() + " has been processed successfully"
            );
            
            if (booking.getCaregiver() != null) {
                notificationService.createNotification(
                        booking.getCaregiver().getUser(),
                        Notification.NotificationType.BOOKING_CONFIRMATION,
                        "Payment Received",
                        "Payment has been received for booking " + booking.getBookingCode()
                );
            }
            
        } catch (Exception e) {
            payment.setStatus(Payment.PaymentStatus.FAILED);
            payment.setPaymentGatewayResponse("FAILED: " + e.getMessage());
            paymentRepository.save(payment);
            
            notificationService.createNotification(
                    payment.getBooking().getCustomer().getUser(),
                    Notification.NotificationType.PAYMENT_FAILED,
                    "Payment Failed",
                    "Your payment has failed. Please try again."
            );
        }
    }
    
    public PaymentDTO getPaymentByBookingId(Long bookingId) {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new RuntimeException("Booking not found"));
        
        Payment payment = paymentRepository.findByBooking(booking)
                .orElseThrow(() -> new RuntimeException("Payment not found for this booking"));
        
        return convertToDTO(payment);
    }
    
    public List<PaymentDTO> getCustomerPayments(Long customerId) {
        return paymentRepository.findByCustomerId(customerId).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }
    
    private String generateTransactionId() {
        return "TXN" + System.currentTimeMillis() + new Random().nextInt(10000);
    }
    
    private PaymentDTO convertToDTO(Payment payment) {
        PaymentDTO dto = new PaymentDTO();
        dto.setId(payment.getId());
        dto.setTransactionId(payment.getTransactionId());
        dto.setBookingId(payment.getBooking().getId());
        dto.setBookingCode(payment.getBooking().getBookingCode());
        dto.setAmount(payment.getAmount());
        dto.setPaymentMethod(payment.getPaymentMethod().name());
        dto.setStatus(payment.getStatus().name());
        dto.setPaidAt(payment.getPaidAt());
        dto.setNotes(payment.getNotes());
        dto.setCreatedAt(payment.getCreatedAt());
        return dto;
    }
}
