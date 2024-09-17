package backend.tangsquad.chat.service;

import backend.tangsquad.chat.entity.ChatRoom;
import backend.tangsquad.chat.repository.ChatRoomRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class ChatRoomService {

    private final ChatRoomRepository chatRoomRepository;

    public List<ChatRoom> findAllRoom() {
        List<ChatRoom> chatRoomList = chatRoomRepository.findAll();
        chatRoomList.sort((a, b) -> Long.compare(b.getId(), a.getId()));
        return chatRoomList;
    }

    public ChatRoom findRoomById(String roomId) {
        return chatRoomRepository.findByRoomId(roomId);
    }

    public ChatRoom createChatRoom(String name, ChatRoom.RoomType type) {
        ChatRoom chatRoom = ChatRoom.create(name, type);
        return chatRoomRepository.save(chatRoom);
    }
}
