package backend.tangsquad.moim.service;

import backend.tangsquad.auth.jwt.UserDetailsImpl;
import backend.tangsquad.chat.entity.ChatRoom;
import backend.tangsquad.chat.repository.ChatRoomRepository;
import backend.tangsquad.chat.service.ChatRoomService;
import backend.tangsquad.converter.ConvertTo;
import backend.tangsquad.moim.dto.request.MoimCreateRequest;
import backend.tangsquad.moim.dto.request.MoimLeaderUsernameRequest;
import backend.tangsquad.moim.dto.response.*;
import backend.tangsquad.moim.entity.Moim;
import backend.tangsquad.common.entity.User;
import backend.tangsquad.moim.dto.request.MoimLeaderRequest;
import backend.tangsquad.moim.dto.request.MoimUpdateRequest;
import backend.tangsquad.moim.repository.MoimRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

import static backend.tangsquad.converter.ConvertTo.convertToMoimResponse;


@Service
@RequiredArgsConstructor
public class MoimService  {

    final private MoimRepository moimRepository;
    private final ChatRoomService chatRoomService;
    private final ChatRoomRepository chatRoomRepository;

    private List<MoimResponse> returnMoimResponses(List<Moim> moims) {
        return moims.stream()
                .map(ConvertTo::convertToMoimResponse
                ).collect(Collectors.toList());
    }


    public List<MoimResponse> getActiveMoims() {
        List<Moim> activeMoims = moimRepository.findAll();

        activeMoims.sort((a, b) -> Long.compare(b.getRegisteredUsers().size(), a.getRegisteredUsers().size()));

        return activeMoims.stream()
                .limit(3)
                .map(ConvertTo::convertToMoimResponse) // Map each Diving to DivingResponse
                .collect(Collectors.toList());
    }

    @Transactional
    public MoimResponse createMoim(MoimCreateRequest moimCreateRequest, UserDetailsImpl userDetails) {
        try {

            Moim moim = Moim.builder()
                    .user(userDetails.getUser())
                    .isPublic(moimCreateRequest.getIsPublic())
                    .thumbnailUrl(moimCreateRequest.getThumbnailUrl())
                    .currentPeople(moimCreateRequest.getCurrentPeople())
                    .moimName(moimCreateRequest.getMoimName())
                    .moimIntro(moimCreateRequest.getMoimIntro())
                    .moimDetails(moimCreateRequest.getMoimDetails())
                    .limitPeople(moimCreateRequest.getLimitPeople())
                    .expense(moimCreateRequest.getExpense())
                    .licenseLimit(moimCreateRequest.getLicenseLimit())
                    .locations(moimCreateRequest.getLocations())
                    .age(moimCreateRequest.getAge())
                    .moods(moimCreateRequest.getMoods())
                    .build();

            moim.update(userDetails.getUser());

            ChatRoom chatRoom = chatRoomService.createChatRoom(moim.getMoimName(), ChatRoom.RoomType.MOIM, moim.getId(), userDetails, true);
            moim.setChatRoomId(chatRoom.getId());

            Moim savedMoim = moimRepository.save(moim);
            return convertToMoimResponse(savedMoim);
        } catch (Exception e) {
            return null;
        }
    }


    public MoimJoinResponse joinMoim(Long moimId, UserDetailsImpl userDetails) {
        try {
            Optional<Moim> moimOptional = moimRepository.findById(moimId);
            if(moimOptional.isEmpty()) throw new NoSuchElementException();

            Moim moim = moimOptional.get();

            if (moim.getRegisteredUsers().contains(userDetails.getUser())) {
                return null;
            }

            moim.update(userDetails.getUser());

            if(moim.getChatRoomId() != null) {
                Optional<ChatRoom> chatRoom = chatRoomRepository.findById(moim.getChatRoomId());
                chatRoom.ifPresent(room -> chatRoomService.createAndSaveChatUser(userDetails, room));
            }

            moimRepository.save(moim);

            return new MoimJoinResponse(moim.getRegisteredUsers().stream().map(user -> user.getId().toString()).collect(Collectors.toList()));
        } catch (Exception e) {
            return null;
        }
    }

    public MoimJoinResponse getRegisteredUsers(Long moimId) {
        try {
            Optional<Moim> optionalMoim = moimRepository.findById(moimId);

            if (optionalMoim.isEmpty()) return null;

            Moim moim = optionalMoim.get();

            return new MoimJoinResponse(moim.getRegisteredUsers().stream().map(user -> user.getId().toString()).collect(Collectors.toList()));
        } catch (Exception e) {
            return null;
        }
    }


    public MoimResponse updateMoim(MoimUpdateRequest moimUpdateRequest, UserDetailsImpl userDetails) {

        try {
            Optional<Moim> moimOptional = moimRepository.findById(moimUpdateRequest.getMoimId());

            if (moimOptional.isEmpty()) {
                throw new NoSuchElementException();
            }

            Moim moim = moimOptional.get();

            if (userDetails.getUser() != moim.getUser()) {
                return null;
            }
            moim.update(moimUpdateRequest);

            return  convertToMoimResponse(moim);
        } catch (Exception e) {
            return null;
        }
    }

    public Optional<Moim> findById(Long moimId) {
        return moimRepository.findById(moimId);
    }

    public List<MoimResponse> getRegisteredMoims(UserDetailsImpl userDetails) {
        try {
            List<Moim> moims = moimRepository.findByRegisteredUsersContaining(userDetails.getUser());

            return returnMoimResponses(moims);
        } catch (Exception e) {
            return null;
        }
    }

    public MoimLeaderResponse updateMoimLeader(UserDetailsImpl userDetails, MoimLeaderRequest moimLeaderRequest) {

        try {
            Long moimId = moimLeaderRequest.getMoimId();

            Long changedUserId = moimLeaderRequest.getUserId();
            Optional<Moim> moimOptional = moimRepository.findById(moimId);

            if(moimOptional.isEmpty()) {
                throw new NoSuchElementException();
            }

            Moim moim = moimOptional.get();

            if (!Objects.equals(moim.getUser().getId(), userDetails.getId())) {
                return null;
            }

            moim.update(moimLeaderRequest);

            return MoimLeaderResponse.builder()
                    .id(moimId)
                    .userId(changedUserId)
                    .build();
        } catch (Exception e) {
            return null;
        }
    }

    public MoimLeaderUsernameResponse updateMoimLeaderByUsername(UserDetailsImpl userDetails, MoimLeaderUsernameRequest moimLeaderUsernameRequest) {

        try {
            Long moimId = moimLeaderUsernameRequest.getMoimId();
            Moim moim = moimRepository.findById(moimId).orElseThrow(NoSuchElementException::new);

            if (!Objects.equals(moim.getUser().getId(), userDetails.getUser().getId())) {
                throw new AccessDeniedException("User is not authorized to update this Moim");
            }

            moim.update(moimLeaderUsernameRequest);
            User newUser = moimLeaderUsernameRequest.getUser();

            return MoimLeaderUsernameResponse.builder()
                    .moimId(moimId)
                    .user(newUser)
                    .build();
        } catch (Exception e) {
            return null;
        }
    }


//    public boolean isAuthorizedToUpdateLeader(Long moimId, Long currentUserId) {
//        return moimRepository.findById(moimId)
//                .map(moim -> moim.getUser().getId().equals(currentUserId))
//                .orElse(false);
//    }

//    public MoimResponse getMoim(Long moimId, UserDetailsImpl userDetails) {
//        Optional<Moim> optionalMoim = moimRepository.findById(moimId);
//        if (optionalMoim.isEmpty()) return null;
//
//        Moim moim = optionalMoim.get();
//
//        return convertToMoimResponse(moim);
//    }

    public List<MoimResponse> getMoims(UserDetailsImpl userDetails)
    {
        try {
            List<Moim> moims = moimRepository.findByUserId(userDetails.getId());

            return returnMoimResponses(moims);

        } catch (Exception e) {
            return null;
        }
    }

    public MoimResponse getMoim(Long moimId) {
        Optional<Moim> moim = moimRepository.findById(moimId);
        if(moim.isEmpty()) throw new NoSuchElementException();
        return convertToMoimResponse(moim.get());
    }

    public boolean deleteMoim(Long moimId, UserDetailsImpl userDetails) {

        try {
            Optional<Moim> moimOptional = moimRepository.findById(moimId);

            if (moimOptional.isEmpty()) {
                return false;
            }

            Moim moim = moimOptional.get();
            Long moimOwnerId = moim.getUser().getId();

            if (!userDetails.getId().equals(moimOwnerId)) {
                return false;
            }

            moimRepository.delete(moim);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public void deleteAllMoims() {
        try {
            List<Moim> moims = moimRepository.findAll();

            moimRepository.deleteAll(moims);

            System.out.println("All logs have been successfully processed and deleted.");
        } catch (Exception e) {
            System.err.println("An error occurred while deleting logs: " + e.getMessage());
        }
    }

    public List<MoimResponse> getAllMoims() {
        try {
            List<Moim> moims = moimRepository.findAll();

            return returnMoimResponses(moims);
        } catch (Exception e) {
            return null;
        }
    }
}
