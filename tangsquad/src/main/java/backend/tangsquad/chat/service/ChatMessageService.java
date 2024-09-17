package backend.tangsquad.chat.service;


import backend.tangsquad.chat.entity.ChatMessage;
import backend.tangsquad.chat.repository.ChatMessageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class ChatMessageService {

    private final ChatMessageRepository chatMessageRepository;

    public void sendMessage(ChatMessage message){
        chatMessageRepository.saveMessage(message);
    }

    public List<Object> getMessages(String roomId){
        return chatMessageRepository.findMessages(roomId);
    }
}
