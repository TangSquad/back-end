package backend.tangsquad.chat.repository;

import backend.tangsquad.chat.entity.ChatMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

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
    public List<Object> findMessages(String roomId){
        String key = "chat:room:" + roomId;
        return redisTemplate.opsForList().range(key, 0, -1);
    }

}
