package backend.tangsquad.moim.service;

import backend.tangsquad.auth.jwt.UserDetailsImpl;
import backend.tangsquad.chat.entity.ChatRoom;
import backend.tangsquad.chat.service.ChatRoomService;
import backend.tangsquad.common.service.UserService;
import backend.tangsquad.diving.dto.response.DivingResponse;
import backend.tangsquad.diving.entity.Diving;
import backend.tangsquad.logbook.entity.Log;
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

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class MoimService  {

    final private MoimRepository moimRepository;
    final private UserService userService;
    private final ChatRoomService chatRoomService;

    private List<MoimResponse> returnMoimResponses(List<Moim> moims) {
        return moims.stream()
                .map(moim -> convertToMoimResponse(moim)
                ).collect(Collectors.toList());
    }

    private MoimResponse convertToMoimResponse(Moim moim) {
        return MoimResponse.builder()
                .id(moim.getId())
                .userId(moim.getUser().getId())
                .thumbnailurl(moim.getThumbnailUrl())
                .isPublic(moim.getIsPublic())
                .moimName(moim.getMoimName())
                .moimIntro(moim.getMoimIntro())
                .moimDetails(moim.getMoimDetails())
                .currentPeople(moim.getCurrentPeople())
                .limitPeople(moim.getLimitPeople())
                .locations(moim.getLocations())
                .licenseLimit(moim.getLicenseLimit())
                .age(moim.getAge())
                .moods(moim.getMoods())
                .registeredUserIds(moim.getRegisteredUsers().stream().map(user -> user.getId().toString()).collect(Collectors.toList()))
                .chatRoomId(moim.getChatRoomId())
                .build();
    }

    public List<MoimResponse> getActiveMoims() {
        List<Moim> activeMoims = moimRepository.findAll();

        activeMoims.sort((a, b) -> Long.compare(b.getRegisteredUsers().size(), a.getRegisteredUsers().size()));

        return activeMoims.stream()
                .limit(3)
                .map(this::convertToMoimResponse) // Map each Diving to DivingResponse
                .collect(Collectors.toList());
    }

    @Transactional
    public MoimResponse createMoim(MoimCreateRequest moimCreateRequest, UserDetailsImpl userDetails) {
        try {
            Moim moim = Moim.builder()
                    .user(userDetails.getUser())
                    .thumbnailUrl(moimCreateRequest.getThumbnailurl())
                    .isPublic(moimCreateRequest.getIsPublic())
                    .moimName(moimCreateRequest.getMoimName())
                    .moimIntro(moimCreateRequest.getMoimIntro())
                    .moimDetails(moimCreateRequest.getMoimDetails())
                    .currentPeople(moimCreateRequest.getCurrentPeople())
                    .limitPeople(moimCreateRequest.getLimitPeople())
                    .expense(moimCreateRequest.getExpense())
                    .licenseLimit(moimCreateRequest.getLicenseLimit())
                    .locations(moimCreateRequest.getLocations())
                    .age(moimCreateRequest.getAge())
                    .moods(moimCreateRequest.getMoods())
                    .build();

            ChatRoom chatRoom = chatRoomService.createChatRoom(moim.getMoimName(), ChatRoom.RoomType.MOIM, moim.getId(), userDetails, true);
            moim.setChatRoomId(chatRoom.getId());

            moimRepository.save(moim);
            return convertToMoimResponse(moim);
        } catch (Exception e) {
            return null;
        }
    }

    public MoimJoinResponse joinMoim(Long moimId, UserDetailsImpl userDetails) {
        try {
            Optional<Moim> moimOptional = moimRepository.findById(moimId);
            Moim moim = moimOptional.get();

            if (moim.getRegisteredUsers().contains(userDetails.getUser())) {
                return null;
            }
            moim.getRegisteredUsers().add(userDetails.getUser());
            moimRepository.save(moim);

            MoimJoinResponse moimJoinResponse = new MoimJoinResponse(moim.getRegisteredUsers().stream().map(user -> user.getId().toString()).collect(Collectors.toList()));

            return moimJoinResponse;
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

    public Moim updateMoimLeaderByName(Long moimId, Long newLeaderId) {
        Moim moim = moimRepository.findById(moimId)
                .orElseThrow(() -> new IllegalArgumentException("Moim not found"));

        User newLeader = userService.findById(newLeaderId)
                .orElseThrow(() -> new IllegalArgumentException("User not found with ID: " + newLeaderId));

        moim.setUser(newLeader);

        return moimRepository.save(moim);
    }


    // 수정 필요
    public MoimResponse updateMoim(MoimUpdateRequest moimUpdateRequest, UserDetailsImpl userDetails) {

        try {
            Optional<Moim> moimOptional = moimRepository.findById(moimUpdateRequest.getMoimId());

            if (!moimOptional.isPresent()) {
                throw new NoSuchElementException();
            }

            // Get the existing logbook
            Moim moim = moimOptional.get();

            if (userDetails.getUser() != moim.getUser()) {
                return null;
            }
            // Update the logbook using the new update method
            moim.update(moimUpdateRequest);

            Moim savedMoim = moimRepository.save(moim);
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
            Moim moim = moimOptional.get();

            if (moim.getUser().getId() != userDetails.getId()) {
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
            Moim moim = moimRepository.findById(moimId).get();

            // Ensure the user is authorized to update this moim leader
            if (moim.getUser().getId() != userDetails.getUser().getId()) {
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


    public boolean isAuthorizedToUpdateLeader(Long moimId, Long currentUserId) {
        return moimRepository.findById(moimId)
                .map(moim -> moim.getUser().getId().equals(currentUserId))
                .orElse(false);
    }

    public MoimResponse getMoim(Long moimId, UserDetailsImpl userDetails) {
        Optional<Moim> optionalMoim = moimRepository.findById(moimId);
        if (optionalMoim.isEmpty()) return null;

        Moim moim = optionalMoim.get();

        return convertToMoimResponse(moim);
    }

    public List<MoimResponse> getMoims(UserDetailsImpl userDetails)
    {
        try {
            // Retrieve the user's logs
            List<Moim> moims = moimRepository.findByUserId(userDetails.getId());

            return returnMoimResponses(moims);

        } catch (Exception e) {
            return null;
        }
    }

    public Optional<Moim> getMoim(Long moimId) {
        return moimRepository.findById(moimId);
    }

    public boolean deleteMoim(Long moimId, UserDetailsImpl userDetails) {

        try {
            // Retrieve the diving from the service
            Optional<Moim> moimOptional = moimRepository.findById(moimId);

            if (!moimOptional.isPresent()) {
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

            moims.stream().forEach(log -> {
                moimRepository.delete(log);
            });

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
