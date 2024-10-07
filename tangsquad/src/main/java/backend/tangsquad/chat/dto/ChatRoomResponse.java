package backend.tangsquad.chat.dto;

import lombok.Builder;
import lombok.Getter;

import java.util.UUID;

@Getter
@Builder
public class ChatRoomResponse {
    private UUID id;
    private String name;
    private String type;

    public static ChatRoomResponse of(UUID id, String name, String type) {
        return ChatRoomResponse.builder()
                .id(id)
                .name(name)
                .type(type)
                .build();
    }
}
