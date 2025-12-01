package com.careservice.dto.chat;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TypingIndicatorMessage {
    private Long chatRoomId;
    private Long userId;
    private String userName;
    private boolean isTyping;
}
