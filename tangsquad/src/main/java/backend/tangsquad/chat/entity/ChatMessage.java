package backend.tangsquad.chat.entity;

import lombok.Getter;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;
import java.sql.Timestamp;

@Getter
@Setter
public class ChatMessage implements Serializable {
    public enum MessageType {
        ENTER, TALK, IMAGE, LEAVE
    }
    @Serial
    private static final long serialVersionUID = 1L;
    private Long senderId;
    private String sender;
    private String roomId;
    private String message;
    private MessageType type;
    // 생성일
    private Timestamp createdAt;
}
