package backend.tangsquad.chat.controller;

import backend.tangsquad.chat.entity.ChatMessage;
import backend.tangsquad.chat.service.ChatMessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@RequiredArgsConstructor
@Controller
public class ChatController {

    private final SimpMessageSendingOperations messagingTemplate;
    private final ChatMessageService chatMessageService;

    @MessageMapping("/chat/message")
    public void message(ChatMessage message) {
        if (ChatMessage.MessageType.ENTER.equals(message.getType())) {
            message.setMessage(message.getSender() + "님이 입장했습니다.");
        }
        message.setCreatedAt(new java.sql.Timestamp(System.currentTimeMillis()));
        chatMessageService.sendMessage(message);
        messagingTemplate.convertAndSend("/sub/chat/room/" + message.getRoomId(), message);
    }

    @GetMapping("/chat/room/messages/{roomId}")
    @ResponseBody
    public List<Object> getMessages(@PathVariable String roomId) {
        return chatMessageService.getMessages(roomId);
    }
}
