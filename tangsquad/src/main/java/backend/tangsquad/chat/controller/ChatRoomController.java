package backend.tangsquad.chat.controller;

import backend.tangsquad.auth.jwt.UserDetailsImpl;
import backend.tangsquad.chat.dto.ChatRoomRequest;
import backend.tangsquad.chat.dto.ChatRoomResponse;
import backend.tangsquad.chat.entity.ChatRoom;
import backend.tangsquad.chat.service.ChatMessageService;
import backend.tangsquad.chat.service.ChatRoomService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
@Controller
@RequestMapping("/chat")
public class ChatRoomController {

    private final ChatRoomService chatRoomService;

    // 채팅 리스트 화면
    @GetMapping("/room")
    public String rooms(Model model) {
        return "/chat/room";
    }
    // 모든 채팅방 목록 반환
    @GetMapping("/rooms")
    @ResponseBody
    public List<ChatRoom> room() {
        return chatRoomService.findAllRoom();
    }
    // 채팅방 생성
    @PostMapping("/room")
    @ResponseBody
    @Operation(summary = "채팅방 생성", description = "채팅방을 생성합니다. type은 MOIM, DIVING 중 하나여야 합니다. organizationId는 해당 타입의 id여야 합니다.", security = @SecurityRequirement(name = "AccessToken"))
    public ChatRoom createRoom(@RequestBody ChatRoomRequest chatRoomRequest, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return chatRoomService.createChatRoom(chatRoomRequest.getName(), chatRoomRequest.getType(), chatRoomRequest.getOrganizationId(), userDetails);
    }
    // 채팅방 입장 화면
    @GetMapping("/room/enter/{roomId}")
    public String roomDetail(Model model, @PathVariable UUID roomId) {
        model.addAttribute("roomId", roomId);
        return "/chat/roomdetail";
    }
    // 특정 채팅방 조회
    @GetMapping("/room/{roomId}")
    @ResponseBody
    public ChatRoom roomInfo(@PathVariable UUID roomId) {
        return chatRoomService.findRoomById(roomId);
    }

    // 내 채팅방 목록 조회
    @GetMapping("/myrooms")
    @ResponseBody
    @Operation(summary = "내 채팅방 목록 조회", description = "내가 속한 채팅방 목록을 조회합니다.", security = @SecurityRequirement(name = "AccessToken"))
    public List<ChatRoomResponse> myRooms(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        return chatRoomService.findMyRooms(userDetails);
    }
}

