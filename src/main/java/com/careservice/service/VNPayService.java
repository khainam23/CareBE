package com.careservice.service;

import com.careservice.dto.payment.VNPayRequest;
import com.careservice.dto.payment.VNPayResponse;
import com.careservice.entity.Booking;
import com.careservice.entity.Payment;
import com.careservice.repository.BookingRepository;
import com.careservice.repository.PaymentRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class VNPayService {
    
    private final PaymentRepository paymentRepository;
    private final BookingRepository bookingRepository;
    private final NotificationService notificationService;
    
    @Value("${vnpay.tmnCode}")
    private String tmnCode;
    
    @Value("${vnpay.hashSecret}")
    private String hashSecret;
    
    @Value("${vnpay.apiUrl}")
    private String apiUrl;
    
    @Value("${vnpay.returnUrl}")
    private String returnUrl;
    
    @Value("${vnpay.notifyUrl}")
    private String notifyUrl;
    
    @Transactional
    public VNPayResponse generatePaymentUrl(VNPayRequest request) {
        Booking booking = bookingRepository.findById(request.getBookingId())
                .orElseThrow(() -> new RuntimeException("Booking not found"));
        
        // Check if payment already exists
        if (paymentRepository.findByBooking(booking).isPresent()) {
            throw new RuntimeException("Payment already exists for this booking");
        }
        
        // Create payment record
        Payment payment = new Payment();
        String transactionId = generateTransactionId();
        payment.setTransactionId(transactionId);
        payment.setBooking(booking);
        payment.setAmount(booking.getTotalPrice());
        payment.setPaymentMethod(Payment.PaymentMethod.VNPAY);
        payment.setStatus(Payment.PaymentStatus.PENDING);
        payment.setNotes(request.getNotes());
        
        Payment savedPayment = paymentRepository.save(payment);
        
        // Generate VNPay URL
        String paymentUrl = buildVNPayUrl(savedPayment);
        
        return VNPayResponse.builder()
                .paymentUrl(paymentUrl)
                .transactionId(transactionId)
                .bookingId(booking.getId())
                .amount(booking.getTotalPrice().longValue())
                .orderInfo("Booking payment for booking code: " + booking.getBookingCode())
                .createTime(new SimpleDateFormat("yyyyMMddHHmmss").format(new Date()))
                .build();
    }
    
    private String buildVNPayUrl(Payment payment) {
        Map<String, String> vnpParams = new TreeMap<>();
        vnpParams.put("vnp_Version", "2.1.0");
        vnpParams.put("vnp_Command", "pay");
        vnpParams.put("vnp_TmnCode", tmnCode);
        vnpParams.put("vnp_Locale", "vn");
        vnpParams.put("vnp_CurrCode", "VND");
        vnpParams.put("vnp_TxnRef", payment.getTransactionId());
        vnpParams.put("vnp_OrderInfo", "Booking payment: " + payment.getBooking().getBookingCode());
        vnpParams.put("vnp_OrderType", "other");
        vnpParams.put("vnp_Amount", String.valueOf(payment.getAmount().longValue() * 100));
        vnpParams.put("vnp_ReturnUrl", returnUrl);
        vnpParams.put("vnp_NotifyUrl", notifyUrl);
        
        String createDate = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
        vnpParams.put("vnp_CreateDate", createDate);
        vnpParams.put("vnp_ExpireDate", getExpireDate(createDate));
        
        String queryString = buildQueryString(vnpParams);
        String vnpSecureHash = hmacSHA512(hashSecret, queryString);
        
        return apiUrl + "?" + queryString + "&vnp_SecureHash=" + vnpSecureHash;
    }
    
    private String buildQueryString(Map<String, String> params) {
        StringBuilder result = new StringBuilder();
        params.forEach((key, value) -> {
            if (result.length() > 0) {
                result.append("&");
            }
            try {
                result.append(key).append("=").append(java.net.URLEncoder.encode(value, StandardCharsets.UTF_8.toString()));
            } catch (UnsupportedEncodingException e) {
                log.error("Error encoding query string", e);
            }
        });
        return result.toString();
    }
    
    public String hmacSHA512(String key, String data) {
        try {
            Mac hmac = Mac.getInstance("HmacSHA512");
            SecretKeySpec secretKeySpec = new SecretKeySpec(key.getBytes(StandardCharsets.UTF_8), "HmacSHA512");
            hmac.init(secretKeySpec);
            byte[] hash = hmac.doFinal(data.getBytes(StandardCharsets.UTF_8));
            StringBuilder hexString = new StringBuilder();
            for (byte b : hash) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) hexString.append('0');
                hexString.append(hex);
            }
            return hexString.toString();
        } catch (Exception e) {
            log.error("Error computing HMAC SHA512", e);
            throw new RuntimeException(e);
        }
    }
    
    @Transactional
    public void handleVNPayReturn(Map<String, String> params) {
        String vnpResponseCode = params.get("vnp_ResponseCode");
        String txnRef = params.get("vnp_TxnRef");
        
        Payment payment = paymentRepository.findByTransactionId(txnRef)
                .orElseThrow(() -> new RuntimeException("Payment not found for transaction: " + txnRef));
        
        if ("00".equals(vnpResponseCode)) {
            // Payment successful
            payment.setStatus(Payment.PaymentStatus.COMPLETED);
            payment.setPaidAt(java.time.LocalDateTime.now());
            payment.setPaymentGatewayResponse("SUCCESS");
            
            // Update booking status
            Booking booking = payment.getBooking();
            if (booking.getStatus() == Booking.BookingStatus.PENDING) {
                booking.setStatus(Booking.BookingStatus.CONFIRMED);
                bookingRepository.save(booking);
            }
            
            paymentRepository.save(payment);
            
            // Send notifications
            notificationService.createNotification(
                    booking.getCustomer().getUser(),
                    com.careservice.entity.Notification.NotificationType.PAYMENT_SUCCESS,
                    "Payment Successful",
                    "Your payment of " + payment.getAmount() + "đ has been processed successfully"
            );
            
            if (booking.getCaregiver() != null) {
                notificationService.createNotification(
                        booking.getCaregiver().getUser(),
                        com.careservice.entity.Notification.NotificationType.BOOKING_CONFIRMATION,
                        "Payment Received",
                        "Payment has been received for booking " + booking.getBookingCode()
                );
            }
            
            log.info("Payment processed successfully: {}", txnRef);
        } else {
            // Payment failed
            payment.setStatus(Payment.PaymentStatus.FAILED);
            payment.setPaymentGatewayResponse("FAILED: " + vnpResponseCode);
            paymentRepository.save(payment);
            
            notificationService.createNotification(
                    payment.getBooking().getCustomer().getUser(),
                    com.careservice.entity.Notification.NotificationType.PAYMENT_FAILED,
                    "Payment Failed",
                    "Your payment has failed. Please try again."
            );
            
            log.error("Payment failed: {} - Response code: {}", txnRef, vnpResponseCode);
        }
    }
    
    private String generateTransactionId() {
        return "VNP" + System.currentTimeMillis() + new Random().nextInt(10000);
    }
    
    private String getExpireDate(String createDate) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(sdf.parse(createDate));
            calendar.add(Calendar.MINUTE, 15);
            return sdf.format(calendar.getTime());
        } catch (Exception e) {
            log.error("Error calculating expire date", e);
            return createDate;
        }
    }
}
