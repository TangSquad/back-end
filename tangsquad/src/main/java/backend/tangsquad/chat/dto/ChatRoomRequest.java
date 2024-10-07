package backend.tangsquad.chat.dto;

import backend.tangsquad.chat.entity.ChatRoom;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

@Getter
public class ChatRoomRequest {
    private final String name;

    private final ChatRoom.RoomType type;

    private final Long organizationId;

    @Builder
    public ChatRoomRequest(String name, ChatRoom.RoomType type, Long organizationId) {
        this.name = name;
        this.type = type;
        this.organizationId = organizationId;
    }
}
