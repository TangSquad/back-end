package backend.tangsquad.chat.repository;

import backend.tangsquad.chat.entity.ChatUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface ChatUserRepository extends JpaRepository<ChatUser, UUID> {
    ChatUser findByUserIdAndChatRoomId(Long userId, UUID ChatRoomId);
    List<ChatUser> findByUserId(Long userId);
    List<ChatUser> findByChatRoomId(UUID chatRoomId);
    void deleteByUserIdAndChatRoomId(Long userId, UUID chatRoomId);
    void deleteByChatRoomId(UUID chatRoomId);
}
