package backend.tangsquad.chat.service;


import backend.tangsquad.auth.jwt.UserDetailsImpl;
import backend.tangsquad.chat.entity.ChatMessage;
import backend.tangsquad.chat.entity.ChatUser;
import backend.tangsquad.chat.repository.ChatMessageRepository;
import backend.tangsquad.chat.repository.ChatUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
@Service
public class ChatMessageService {

    private final ChatMessageRepository chatMessageRepository;
    private final ChatUserRepository chatUserRepository;

    public void sendMessage(ChatMessage message){
        chatMessageRepository.saveMessage(message);
    }

    public List<Object> getMessages(UUID roomId){
        return chatMessageRepository.findMessages(roomId);
    }

    public List<Object> getMessagesAfter(UUID roomId, UserDetailsImpl userDetails){
        ChatUser chatUser = chatUserRepository.findByUserIdAndChatRoomId(userDetails.getId(), roomId);
        if(chatUser == null){
            throw new IllegalArgumentException("User is not in the chat room");
        }
        Timestamp timestamp = chatUser.getDisconnectedAt();
        chatUser.updateDisconnectedAt();
        chatUserRepository.save(chatUser);
        return chatMessageRepository.findMessagesAfter(roomId, timestamp);
    }
}
