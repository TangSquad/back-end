package backend.tangsquad.chat.config;

import backend.tangsquad.chat.entity.ChatUser;
import backend.tangsquad.chat.repository.ChatUserRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

import java.util.UUID;

@RequiredArgsConstructor
@Component
public class WebSocketEventListener {

    private final ChatUserRepository chatUserRepository;
    private static final Logger logger = LoggerFactory.getLogger(WebSocketEventListener.class);

    @EventListener
    public void handleWebSocketDisconnectListener(SessionDisconnectEvent event) {
        StompHeaderAccessor headerAccessor = StompHeaderAccessor.wrap(event.getMessage());
        String sessionId = headerAccessor.getSessionId();

        // log
        logger.info("User disconnected: Session ID = " + sessionId);

        Long userId = WebSocketJwtInterceptor.getUserId(sessionId);
        UUID chatRoomId = WebSocketJwtInterceptor.getChatRoomId(sessionId);

        // log
        logger.info("User ID = " + userId + ", ChatRoom ID = " + chatRoomId);

        if (userId != null && chatRoomId != null) {
            logger.info("User disconnected: User ID = " + userId + ", ChatRoom ID = " + chatRoomId);

            try {
                // ChatUser 정보 업데이트
                ChatUser chatUser = chatUserRepository.findByUserIdAndChatRoomId(userId, chatRoomId);

                if (chatUser != null) {
                    chatUser.updateDisconnectedAt();
                    chatUserRepository.save(chatUser);
                    logger.info("ChatUser status updated: User ID = " + userId + ", ChatRoom ID = " + chatRoomId);
                } else {
                    logger.warn("ChatUser not found for User ID = " + userId + ", ChatRoom ID = " + chatRoomId);
                }
            } catch (Exception e) {
                logger.error("Error updating ChatUser status for User ID = " + userId + ", ChatRoom ID = " + chatRoomId, e);
            }

            // 세션 ID로 매핑된 데이터 제거
            WebSocketJwtInterceptor.removeSession(sessionId);
        } else {
            logger.warn("No user or chat room ID found for session ID: " + sessionId);
        }
    }
}
