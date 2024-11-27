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

    public ChatRoom createChatRoom(String name, ChatRoom.RoomType type, Long typeId, UserDetailsImpl userDetails, boolean newOrganization) {
        validateUserDetails(userDetails);

        if (!newOrganization) {
            validateRoomTypeAndTypeId(type, typeId);
            validateChatRoomUniqueness(type, typeId);
        }

        ChatRoom chatRoom = ChatRoom.create(name, type, typeId);
        chatRoomRepository.save(chatRoom);

        createAndSaveChatUser(userDetails, chatRoom);

        return chatRoom;
    }

    public ChatRoom createChatRoom(String name, ChatRoom.RoomType type, Long typeId, UserDetailsImpl userDetails) {
        return createChatRoom(name, type, typeId, userDetails, false);
    }

    private void validateUserDetails(UserDetailsImpl userDetails) {
        if (userDetails == null) {
            throw new IllegalArgumentException("UserDetails is null");
        }
    }

    private void validateRoomTypeAndTypeId(ChatRoom.RoomType type, Long typeId) {
        if (type == ChatRoom.RoomType.DIVING) {
            if (divingRepository.findById(typeId).isEmpty()) {
                throw new IllegalArgumentException("Diving not found");
            }
        } else if (type == ChatRoom.RoomType.MOIM) {
            if (moimRepository.findById(typeId).isEmpty()) {
                throw new IllegalArgumentException("Moim not found");
            }
        } else {
            throw new IllegalArgumentException("Invalid type");
        }
    }

    private void validateChatRoomUniqueness(ChatRoom.RoomType type, Long typeId) {
        if (chatRoomRepository.findByTypeIdAndType(typeId, type) != null) {
            throw new IllegalArgumentException("ChatRoom already exists");
        }
    }

    public void createAndSaveChatUser(UserDetailsImpl userDetails, ChatRoom chatRoom) {
        ChatUser chatUser = ChatUser.builder()
                .user(userDetails.getUser())
                .chatRoom(chatRoom)
                .build();

        chatUserRepository.save(chatUser);
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
