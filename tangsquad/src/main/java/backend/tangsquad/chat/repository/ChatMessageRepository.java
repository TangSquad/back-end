package backend.tangsquad.chat.repository;

import backend.tangsquad.chat.entity.ChatMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Repository
public class ChatMessageRepository {

    private final RedisTemplate<String, Object> redisTemplate;

    // 메시지 저장 (12시간 동안 유지)
    public void saveMessage(ChatMessage message){
        String key = "chat:room:" + message.getRoomId();
        redisTemplate.opsForList().rightPush(key, message);
        redisTemplate.expire(key, 12, java.util.concurrent.TimeUnit.HOURS);
    }

    // 채팅방의 모든 메시지 조회
    public List<Object> findMessages(UUID roomId){
        String key = "chat:room:" + roomId.toString();
        return redisTemplate.opsForList().range(key, 0, -1);
    }

    // 특정 시간 이후의 메시지 조회
    public List<Object> findMessagesAfter(UUID roomId, Timestamp timestamp) {
        String key = "chat:room:" + roomId.toString();
        List<Object> messages = redisTemplate.opsForList().range(key, 0, -1);

        if (messages == null) {
            return Collections.emptyList(); // null일 경우 빈 리스트 반환
        }

        return messages.stream()
                .filter(message -> ((ChatMessage) message).getCreatedAt().after(timestamp))
                .collect(Collectors.toList());
    }
}
