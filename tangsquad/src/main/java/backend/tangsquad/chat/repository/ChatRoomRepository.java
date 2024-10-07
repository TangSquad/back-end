package backend.tangsquad.chat.repository;

import backend.tangsquad.chat.entity.ChatRoom;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
public interface ChatRoomRepository extends JpaRepository<ChatRoom, UUID> {
    @NotNull
    Optional<ChatRoom> findById(@NotNull UUID id);
    ChatRoom findByOrganizationIdAndType(Long organizationId, ChatRoom.RoomType type);
}
