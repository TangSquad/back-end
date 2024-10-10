package backend.tangsquad.chat.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@Entity

public class ChatRoom {

    public enum RoomType {
        DIVING, MOIM
    }

    @Id
    @Column(columnDefinition = "BINARY(16)")
    private UUID id;
    private String name;
    private RoomType type;
    private Long organizationId;

    public static ChatRoom create(String name, RoomType type, Long organizationId) {
        ChatRoom chatRoom = new ChatRoom();
        chatRoom.id = UUID.randomUUID();
        chatRoom.name = name;
        chatRoom.type = type;
        chatRoom.organizationId = organizationId;
        return chatRoom;
    }
}
