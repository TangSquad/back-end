package backend.tangsquad.chat.config;

import backend.tangsquad.auth.jwt.JwtTokenProvider;
import backend.tangsquad.auth.jwt.UserDetailsImpl;
import backend.tangsquad.common.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.messaging.support.MessageHeaderAccessor;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@RequiredArgsConstructor
public class WebSocketJwtInterceptor implements ChannelInterceptor {

    private static final ConcurrentHashMap<String, Long> sessionUserMap = new ConcurrentHashMap<>();
    private static final ConcurrentHashMap<String, UUID> sessionChatRoomMap = new ConcurrentHashMap<>();
    private static final ConcurrentHashMap<String, String> sessionNicknameMap = new ConcurrentHashMap<>();

    private final JwtTokenProvider jwtTokenProvider;
    private final UserDetailsService userDetailsService;
    private static final Logger logger = LoggerFactory.getLogger(WebSocketJwtInterceptor.class);


    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {
        StompHeaderAccessor accessor = MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor.class);

        if (accessor != null) {
            try {
                if (StompCommand.CONNECT.equals(accessor.getCommand())) {
                    handleConnect(accessor);
                } else if (StompCommand.SUBSCRIBE.equals(accessor.getCommand())) {
                    handleSubscribe(accessor);
                }
            } catch (IllegalArgumentException e) {
                logger.error("Error: {}", e.getMessage());

                // ERROR 프레임 생성
                StompHeaderAccessor errorAccessor = StompHeaderAccessor.create(StompCommand.ERROR);
                errorAccessor.setMessage(e.getMessage());
                errorAccessor.setSessionId(accessor.getSessionId());
                errorAccessor.setLeaveMutable(true);

                return MessageBuilder.createMessage(e.getMessage().getBytes(), errorAccessor.getMessageHeaders());
            }
        }
        return message;
    }

    private void handleConnect(StompHeaderAccessor accessor) {
        String token = accessor.getFirstNativeHeader("Authorization");

        if (token == null || !token.startsWith("Bearer ")) {
            throw new IllegalArgumentException("Authorization header is missing or invalid");
        }

        token = token.substring(7);
        if (!jwtTokenProvider.validateToken(token)) {
            throw new IllegalArgumentException("Invalid JWT token");
        }

        String nickname = jwtTokenProvider.getNickname(token);
        UserDetailsImpl userDetails = (UserDetailsImpl) userDetailsService.loadUserByUsername(nickname);
        if (userDetails == null) {
            throw new IllegalArgumentException("User not found");
        }

        User user = userDetails.getUser();
        String sessionId = accessor.getSessionId();
        sessionUserMap.put(sessionId, user.getId());
        sessionNicknameMap.put(sessionId, user.getNickname());

        String chatRoomId = accessor.getFirstNativeHeader("chatRoomId");
        if (chatRoomId != null) {
            sessionChatRoomMap.put(sessionId, UUID.fromString(chatRoomId));
        }

        logger.info("User connected: {}, Session ID: {}", user.getNickname(), sessionId);
    }

    private void handleSubscribe(StompHeaderAccessor accessor) {
        String sessionId = accessor.getSessionId();
        String destination = accessor.getDestination();

        if (destination != null && destination.startsWith("/sub/chat/room/")) {
            String chatRoomId = destination.split("/sub/chat/room/")[1];
            UUID roomId = UUID.fromString(chatRoomId);
            sessionChatRoomMap.put(sessionId, roomId);
            logger.info("Chat Room ID {} mapped for Session ID {}", roomId, sessionId);
        } else {
            throw new IllegalArgumentException("Invalid SUBSCRIBE destination");
        }
    }


    public static String getNickname(String sessionId) {
        return sessionNicknameMap.get(sessionId);
    }

    public static Long getUserId(String sessionId) {
        return sessionUserMap.get(sessionId);
    }

    public static UUID getChatRoomId(String sessionId) {
        return sessionChatRoomMap.get(sessionId);
    }

    public static void removeSession(String sessionId) {
        sessionUserMap.remove(sessionId);
        sessionChatRoomMap.remove(sessionId);
    }
}
