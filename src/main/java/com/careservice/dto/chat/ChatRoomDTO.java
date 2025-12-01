package com.careservice.dto.chat;

import com.careservice.entity.ChatRoom.ChatRoomStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ChatRoomDTO {
    private Long id;
    private Long bookingId;
    private Long customerId;
    private String customerName;
    private Long caregiverId;
    private String caregiverName;
    private ChatRoomStatus status;
    private LocalDateTime createdAt;
    private LocalDateTime lastMessageAt;
    private String lastMessagePreview;
    private long unreadCount;
}
