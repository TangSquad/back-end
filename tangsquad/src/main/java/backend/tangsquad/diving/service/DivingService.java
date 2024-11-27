package backend.tangsquad.diving.service;

import backend.tangsquad.auth.jwt.UserDetailsImpl;
import backend.tangsquad.chat.entity.ChatRoom;
import backend.tangsquad.chat.repository.ChatRoomRepository;
import backend.tangsquad.chat.service.ChatRoomService;
import backend.tangsquad.diving.dto.request.DivingRequest;
import backend.tangsquad.diving.dto.response.DivingJoinResponse;
import backend.tangsquad.diving.dto.response.DivingResponse;
import backend.tangsquad.diving.entity.Diving;
import backend.tangsquad.diving.entity.Location;
import backend.tangsquad.diving.repository.DivingRepository;
import backend.tangsquad.logbook.entity.Log;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DivingService {
    private final DivingRepository divingRepository;
    private final ChatRoomService chatRoomService;
    private final ChatRoomRepository chatRoomRepository;

    public Diving save(Diving diving) {
        return divingRepository.save(diving);
    }

    private DivingResponse convertToDivingResponse(Diving diving) {
        return DivingResponse.builder()
                .id(diving.getDivingId())
                .userId(diving.getUser().getId())
                .isPublic(diving.getIsPublic())
                .divingName(diving.getDivingName())
                .divingIntro(diving.getDivingIntro())
                .age(diving.getAge())
                .moods(diving.getMoods())
                .startDate(diving.getStartDate())
                .endDate(diving.getEndDate())
                .thumbnailUrl(diving.getThumbnailUrl())
                .location(diving.getLocation())
                .licenseLimit(diving.getLicenseLimit())
                .currentPeople(diving.getCurrentPeople())
                .limitPeople(diving.getLimitPeople())
                .chatRoomId(diving.getChatRoomId())
                .build();
    }


    public List<Location> getPopularSpots() {
        List<Diving> allDivings = divingRepository.findAll();

        Map<Location, Long> locationVisitCount = allDivings.stream()
                .collect(Collectors.groupingBy(Diving::getLocation, Collectors.counting()));

        return locationVisitCount.entrySet().stream()
                .sorted((entry1, entry2) -> Long.compare(entry2.getValue(), entry1.getValue()))
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());
    }

    public List<DivingResponse> getRecentDivings() {

        List<Diving> allDivings = divingRepository.findAll();

        allDivings.sort((a, b) -> Long.compare(b.getDivingId(), a.getDivingId()));

        return allDivings.stream()
                .limit(3)
                .map(this::convertToDivingResponse) // Map each Diving to DivingResponse
                .collect(Collectors.toList());
    }

    private List<DivingResponse> returnDivingResponses(List<Diving> divings) {
        return divings.stream()
                .map(diving -> convertToDivingResponse(diving))
                .collect(Collectors.toList());
    }

    @Transactional
    public DivingResponse createDiving(DivingRequest divingRequest, UserDetailsImpl userDetails) {
        try {
            Diving diving = Diving.builder()
                    .user(userDetails.getUser())
                    .isPublic(divingRequest.getIsPublic())
                    .divingName(divingRequest.getDivingName())
                    .divingIntro(divingRequest.getDivingIntro())
                    .age(divingRequest.getAge())
                    .moods(divingRequest.getMoods())
                    .startDate(divingRequest.getStartDate())
                    .endDate(divingRequest.getEndDate())
                    .thumbnailUrl(divingRequest.getThumbnailUrl())
                    .location(divingRequest.getLocation())
                    .licenseLimit(divingRequest.getLicenseLimit())
                    .currentPeople(divingRequest.getCurrentPeople())
                    .limitPeople(divingRequest.getLimitPeople())
                    .build();

            // 다이빙을 위한 채팅방 생성
            ChatRoom chatRoom = chatRoomService.createChatRoom(divingRequest.getDivingName(), ChatRoom.RoomType.DIVING, diving.getDivingId(), userDetails, true);

            // 다이빙에 채팅방 id 설정
            diving.setChatRoomId(chatRoom.getId());

            divingRepository.save(diving);
            return convertToDivingResponse(diving);
        } catch (Exception e) {
            return null;
        }
    }

    public DivingJoinResponse joinDiving(Long divingId, UserDetailsImpl userDetails) {

        try {
            Optional<Diving> optionalDiving = divingRepository.findById(divingId);
            if (optionalDiving.isEmpty()) return null;
            Diving diving = optionalDiving.get();

            diving.join(userDetails);

            if(diving.getChatRoomId() != null) {
                Optional<ChatRoom> chatRoom = chatRoomRepository.findById(diving.getChatRoomId());
                chatRoom.ifPresent(room -> chatRoomService.createAndSaveChatUser(userDetails, room));
            }

            divingRepository.save(diving);

            return new DivingJoinResponse(
                    divingId,
                    diving.getRegisteredUsers().stream().map(user -> user.getId().toString()).collect(Collectors.toList())
            );
        } catch (Exception e) {
            return null;
        }
    }

    public DivingJoinResponse getMemberList(Long divingId) {
        try {
            Optional<Diving> divingOptional = divingRepository.findById(divingId);

            if (divingOptional.isEmpty()) return null;

            Diving diving = divingOptional.get();

            return new DivingJoinResponse(divingId, diving.getRegisteredUsers().stream().map(user -> user.getId().toString()).collect(Collectors.toList()));
        } catch (Exception e) {
            return null;
        }
    }

    public List<DivingResponse> getMyDivings(Long userId) {
        try {
            List<Diving> divings = divingRepository.findByUserId(userId);

            return returnDivingResponses(divings);

        } catch (Exception e) {
            return null;
        }
    }

    public List<DivingResponse> getAllDivings() {
        try {
            List<Diving> divings = divingRepository.findAll();

            return returnDivingResponses(divings);
        } catch (Exception e) {
            return null;
        }
    }

    public DivingResponse getDiving(Long divingId) {
        try {
            Optional<Diving> divingOptional = divingRepository.findById(divingId);
            if (divingOptional.isPresent()) {
                Diving diving = divingOptional.get();

                return convertToDivingResponse(diving);
            } else {
                return null;
            }
        } catch (Exception e) {
            return null;
        }
    }

    public List<DivingResponse> getRegisteredDivings(UserDetailsImpl userDetails) {
        try {
            List<Diving> divings = divingRepository.findByRegisteredUsersContaining(userDetails.getUser());

            return returnDivingResponses(divings);
        } catch (Exception e) {
            return null;
        }
    }

    public DivingResponse updateDiving(Long divingId, DivingRequest divingRequest, UserDetailsImpl userDetails) {

        try {
            Optional<Diving> divingOptional = divingRepository.findById(divingId);

            if (!divingOptional.isPresent()) {
                throw new NoSuchElementException(", divingId: " + divingId);
            }

            Diving diving = divingOptional.get();

            diving.update(divingRequest);

            divingRepository.save(diving);
            return convertToDivingResponse(diving);
        } catch (Exception e) {
            return null;
        }
    }

    public boolean deleteDiving(Long divingId, UserDetailsImpl userDetails) {

        try {
            Diving diving = divingRepository.findById(divingId)
                    .orElseThrow(() -> new NoSuchElementException("Logbook not found for divingId: " + divingId));

            if (userDetails.getId() != diving.getUser().getId()) {
                return false;
            }

            divingRepository.delete(diving);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public void deleteAll() {
        try {
            List<Diving> divings = divingRepository.findAll();

            divings.stream().forEach(log -> {
                divingRepository.delete(log);
            });

            System.out.println("All logs have been successfully processed and deleted.");
        } catch (Exception e) {
            System.err.println("An error occurred while deleting logs: " + e.getMessage());
        }

    }

}
