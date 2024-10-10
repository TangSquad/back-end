package backend.tangsquad.chat.service;

import backend.tangsquad.auth.jwt.UserDetailsImpl;
import backend.tangsquad.chat.dto.ChatRoomResponse;
import backend.tangsquad.chat.entity.ChatRoom;
import backend.tangsquad.chat.entity.ChatUser;
import backend.tangsquad.chat.repository.ChatRoomRepository;
import backend.tangsquad.chat.repository.ChatUserRepository;
import backend.tangsquad.diving.repository.DivingRepository;
import backend.tangsquad.moim.repository.MoimRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class ChatRoomService {

    private final ChatRoomRepository chatRoomRepository;
    private final DivingRepository divingRepository;
    private final MoimRepository moimRepository;
    private final ChatUserRepository chatUserRepository;

    public List<ChatRoom> findAllRoom() {
        return chatRoomRepository.findAll();
    }

    public ChatRoom findRoomById(UUID roomId) {
        return chatRoomRepository.findById(roomId).orElse(null);
    }

    public ChatRoom createChatRoom(String name, ChatRoom.RoomType type, Long organizationId, UserDetailsImpl userDetails) {
        if(userDetails == null) {
            throw new IllegalArgumentException("UserDetails is null");
        }

        if(type == ChatRoom.RoomType.DIVING) {
            if(divingRepository.findById(organizationId).isEmpty()) {
                throw new IllegalArgumentException("Diving not found");
            }
        } else if(type == ChatRoom.RoomType.MOIM) {
            if(moimRepository.findById(organizationId).isEmpty()) {
                throw new IllegalArgumentException("Moim not found");
            }
        } else {
            throw new IllegalArgumentException("Invalid type");
        }
        // 이미 해당 organizationId로 생성된 채팅방이 있는지 확인
        if(chatRoomRepository.findByOrganizationIdAndType(organizationId, type) != null) {
            throw new IllegalArgumentException("ChatRoom already exists");
        }

        ChatRoom chatRoom = ChatRoom.create(name, type, organizationId);

        chatRoomRepository.save(chatRoom);

        ChatUser chatUser = ChatUser.builder()
                .user(userDetails.getUser())
                .chatRoom(chatRoom)
                .build();

        chatUserRepository.save(chatUser);

        return chatRoom;
    }

    public List<ChatRoomResponse> findMyRooms(UserDetailsImpl userDetails) {
        if(userDetails == null) {
            throw new IllegalArgumentException("UserDetails is null");
        }
        List<ChatUser> chatUserList = chatUserRepository.findByUserId(userDetails.getId());
        return chatUserList.stream()
                .map(chatUser -> {
                    ChatRoom chatRoom = chatUser.getChatRoom();
                    return ChatRoomResponse.of(
                            chatRoom.getId(),
                            chatRoom.getName(),
                            chatRoom.getType().name()  // RoomType을 문자열로 변환
                    );
                })
                .collect(Collectors.toList());
    }
}
