package backend.tangsquad.moim.service;

import backend.tangsquad.auth.jwt.UserDetailsImpl;
import backend.tangsquad.common.service.UserService;
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
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class MoimService  {

    final private MoimRepository moimRepository;
    final private UserService userService;

    public MoimResponse createMoim(MoimCreateRequest moimCreateRequest, UserDetailsImpl userDetails) {
        try {
            Moim moim = Moim.builder()
                    .user(userDetails.getUser())
                    .anonymous(moimCreateRequest.getAnonymous())
                    .moimName(moimCreateRequest.getMoimName())
                    .moimIntro(moimCreateRequest.getMoimIntro())
                    .moimDetails(moimCreateRequest.getMoimDetails())
                    .limitPeople(moimCreateRequest.getLimitPeople())
                    .expense(moimCreateRequest.getExpense())
                    .limitPeople(moimCreateRequest.getLimitPeople())
                    .locations(moimCreateRequest.getLocations())
                    .age(moimCreateRequest.getAge())
                    .moods(moimCreateRequest.getMoods())
                    .build();

            // Save moim
            Moim savedMoim = moimRepository.save(moim);
            return MoimResponse.builder()
                    .anonymous(savedMoim.getAnonymous())
                    .moimName(savedMoim.getMoimName())
                    .moimIntro(savedMoim.getMoimIntro())
                    .moimDetails(savedMoim.getMoimDetails())
                    .limitPeople(savedMoim.getLimitPeople())
                    .expense(savedMoim.getExpense())
                    .licenseLimit(savedMoim.getLicenseLimit())
                    .build();
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

            MoimJoinResponse moimJoinResponse = new MoimJoinResponse(moim.getRegisteredUsers());

            return moimJoinResponse;
        } catch (Exception e) {
            return null;
        }
    }




    public Moim updateMoimLeaderByName(Long moimId, Long newLeaderId) {
        Moim moim = moimRepository.findById(moimId)
                .orElseThrow(() -> new IllegalArgumentException("Moim not found"));

        // Find the new leader by ID, handle Optional
        User newLeader = userService.findById(newLeaderId)
                .orElseThrow(() -> new IllegalArgumentException("User not found with ID: " + newLeaderId));

        // Set the new leader of the Moim
        moim.setUser(newLeader);

        // Save the updated Moim
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
            MoimResponse moimReadResponse = MoimResponse.builder()
                    .userId(savedMoim.getUser().getId())
                    .anonymous(savedMoim.getAnonymous())
                    .moimName(savedMoim.getMoimName())
                    .moimIntro(savedMoim.getMoimIntro())
                    .moimDetails(savedMoim.getMoimDetails())
                    .limitPeople(savedMoim.getLimitPeople())
                    .expense(savedMoim.getExpense())
                    .licenseLimit(savedMoim.getLicenseLimit())
                    .build();

            // Save and return the updated logbook
            return moimReadResponse;
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

            List<MoimResponse> moimResponses = moims.stream()
                    .map(moim -> MoimResponse.builder()
                            .userId(moim.getUser().getId())
                            .anonymous(moim.getAnonymous())
                            .moimName(moim.getMoimName())
                            .moimIntro(moim.getMoimIntro())
                            .moimDetails(moim.getMoimDetails())
                            .limitPeople(moim.getLimitPeople())
                            .expense(moim.getExpense())
                            .licenseLimit(moim.getLicenseLimit())
                            .build()
                    ).collect(Collectors.toList());

            return moimResponses;
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

            // Return the updated MoimReadResponse in a list (to maintain consistency with previous GET mapping)
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

    public List<MoimResponse> getMoims(UserDetailsImpl userDetails)
    {
        try {
            // Retrieve the user's logs
            List<Moim> moims = moimRepository.findByUserId(userDetails.getId());

            List<MoimResponse> moimResponses = moims.stream()
                    .map(moim -> MoimResponse.builder()
                            .userId(moim.getUser().getId())
                            .anonymous(moim.getAnonymous())
                            .moimName(moim.getMoimName())
                            .moimIntro(moim.getMoimIntro())
                            .moimDetails(moim.getMoimDetails())
                            .limitPeople(moim.getLimitPeople())
                            .expense(moim.getExpense())
                            .licenseLimit(moim.getLicenseLimit())
                            .build()
                    ).collect(Collectors.toList());

            return moimResponses;

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

    public List<MoimResponse> getAllMoims() {
        try {
            List<Moim> moims = moimRepository.findAll();
            List<MoimResponse> moimResponses = moims.stream()
                    .map(moim -> MoimResponse.builder()
                            .userId(moim.getUser().getId())
                            .anonymous(moim.getAnonymous())
                            .moimName(moim.getMoimName())
                            .moimIntro(moim.getMoimIntro())
                            .moimDetails(moim.getMoimDetails())
                            .limitPeople(moim.getLimitPeople())
                            .expense(moim.getExpense())
                            .licenseLimit(moim.getLicenseLimit())
                            .build()
                    ).collect(Collectors.toList());
            return moimResponses;
        } catch (Exception e) {
            return null;
        }
    }
}
