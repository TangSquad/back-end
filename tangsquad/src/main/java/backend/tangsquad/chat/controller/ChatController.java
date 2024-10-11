package backend.tangsquad.chat.controller;

import backend.tangsquad.auth.jwt.UserDetailsImpl;
import backend.tangsquad.chat.config.WebSocketJwtInterceptor;
import backend.tangsquad.chat.entity.ChatMessage;
import backend.tangsquad.chat.service.ChatMessageService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
@Controller
public class ChatController {

    private final SimpMessageSendingOperations messagingTemplate;
    private final ChatMessageService chatMessageService;

    @MessageMapping("/chat/message")
    public void message(ChatMessage message, @Header("simpSessionId") String sessionId) {
        // sessionId를 사용해 필요한 정보 불러오기
        Long senderId = WebSocketJwtInterceptor.getUserId(sessionId);
        String nickname = WebSocketJwtInterceptor.getNickname(sessionId);
        UUID roomId = WebSocketJwtInterceptor.getChatRoomId(sessionId);

        // 필요한 정보 채워넣기
        message.setSenderId(senderId);
        message.setSender(nickname);
        message.setRoomId(String.valueOf(roomId));

        if (ChatMessage.MessageType.ENTER.equals(message.getType())) {
            message.setMessage(nickname + "님이 입장했습니다.");
        }
        message.setCreatedAt(new java.sql.Timestamp(System.currentTimeMillis()));

        // 메시지 전송
        chatMessageService.sendMessage(message);
        messagingTemplate.convertAndSend("/sub/chat/room/" + roomId, message);
    }

    // 채팅 내역 조회
    @GetMapping("/room/messages/{roomId}")
    @ResponseBody
    @Operation(summary = "채팅 내역 조회", description = "특정 채팅방의 저장된 채팅 내역 중에, 내가 읽지 않은 채팅 내역을 조회합니다.", security = @SecurityRequirement(name = "AccessToken"))
    public List<Object> getMessages(@PathVariable UUID roomId, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return chatMessageService.getMessagesAfter(roomId, userDetails);
    }
}
