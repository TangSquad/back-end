package backend.tangsquad.chat.config;

import backend.tangsquad.auth.jwt.JwtTokenProvider;
import backend.tangsquad.auth.jwt.UserDetailsImpl;
import backend.tangsquad.auth.jwt.UserDetailsServiceImpl;
import backend.tangsquad.common.entity.User;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;


public class WebSocketJwtInterceptor implements ChannelInterceptor {

    private static final ConcurrentHashMap<String, Long> sessionUserMap = new ConcurrentHashMap<>();
    private static final ConcurrentHashMap<String, UUID> sessionChatRoomMap = new ConcurrentHashMap<>();
    private static final ConcurrentHashMap<String, String> sessionNicknameMap = new ConcurrentHashMap<>();

    private final JwtTokenProvider jwtTokenProvider;
    private final UserDetailsService userDetailsService;
    private static final Logger logger = LoggerFactory.getLogger(WebSocketJwtInterceptor.class);

    public WebSocketJwtInterceptor(JwtTokenProvider jwtTokenProvider, UserDetailsServiceImpl userDetailsService) {
        this.jwtTokenProvider = jwtTokenProvider;
        this.userDetailsService = userDetailsService;
    }

    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {
        StompHeaderAccessor accessor = StompHeaderAccessor.wrap(message);

        if (StompCommand.CONNECT.equals(accessor.getCommand())) {
            // 사용자 인증 토큰 처리 및 사용자 정보 획득
            String token = accessor.getFirstNativeHeader("Authorization");
            if (token != null && token.startsWith("Bearer ")) {
                token = token.substring(7);
                if (jwtTokenProvider.validateToken(token)) {
                    String nickname = jwtTokenProvider.getNickname(token);
                    UserDetailsImpl userDetails = (UserDetailsImpl) userDetailsService.loadUserByUsername(nickname);
                    if (userDetails != null) {
                        User user = userDetails.getUser();
                        String sessionId = accessor.getSessionId();
                        sessionUserMap.put(sessionId, user.getId());
                        sessionNicknameMap.put(sessionId, user.getNickname());

                        // Optional: handle any chatRoomId passed during CONNECT (usually handled in SUBSCRIBE)
                        String chatRoomId = accessor.getFirstNativeHeader("chatRoomId");
                        if (chatRoomId != null) {
                            sessionChatRoomMap.put(sessionId, UUID.fromString(chatRoomId));
                        }

                        logger.info("User connected: " + user.getNickname() + ", User ID: " + user.getId() + ", Session ID: " + sessionId);
                    }
                }
            }
        } else if (StompCommand.SUBSCRIBE.equals(accessor.getCommand())) {
            String sessionId = accessor.getSessionId();
            logger.info("SUBSCRIBE: Session ID = " + sessionId);
            String destination = accessor.getDestination();
            if (destination != null && destination.startsWith("/sub/chat/room/")) {
                logger.info("SUBSCRIBE: Destination = " + destination);
                // Parse UUID for chat room ID
                String chatRoomId = destination.split("/sub/chat/room/")[1];
                sessionChatRoomMap.put(sessionId, UUID.fromString(chatRoomId));
                logger.info("Chat Room ID {} mapped for Session ID {}", chatRoomId, sessionId);
            }
        }
        return message;
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
