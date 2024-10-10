package backend.tangsquad.moim.service;

import backend.tangsquad.auth.jwt.UserDetailsImpl;
import backend.tangsquad.common.service.UserService;
import backend.tangsquad.like.repository.LikeMoimRepository;
import backend.tangsquad.moim.dto.request.MoimCreateRequest;
import backend.tangsquad.moim.dto.request.MoimLeaderUsernameRequest;
import backend.tangsquad.moim.dto.response.*;
import backend.tangsquad.moim.entity.Moim;
import backend.tangsquad.common.entity.User;
import backend.tangsquad.moim.dto.request.MoimLeaderRequest;
import backend.tangsquad.moim.dto.request.MoimUpdateRequest;
import backend.tangsquad.moim.repository.MoimRepository;
import backend.tangsquad.common.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class MoimService  {

    final private MoimRepository moimRepository;
    final private UserService userService;

    public MoimCreateResponse createMoim(MoimCreateRequest moimCreateRequest, UserDetailsImpl userDetails) {
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
            return MoimCreateResponse.builder()
                    .anonymous(savedMoim.getAnonymous())
                    .moimName(savedMoim.getMoimName())
                    .moimIntro(savedMoim.getMoimIntro())
                    .moimDetails(savedMoim.getMoimDetails())
                    .limitPeople(savedMoim.getLimitPeople())
                    .expense(savedMoim.getExpense())
                    .licenseLimit(savedMoim.getLicenseLimit())
                    .locations(savedMoim.getLocations())
                    .age(savedMoim.getAge())
                    .moods(savedMoim.getMoods())
                    .build();
        } catch (Exception e) {
            return null;
        }
    }

    public MoimJoinResponse joinMoim(Long moimId, UserDetailsImpl userDetails) {
        try {
            // Find the Moim by ID
            Optional<Moim> moimOptional = moimRepository.findById(moimId);
            Moim moim = moimOptional.get();

            // Check if the user is already joined
            if (moim.getRegisteredUsers().contains(userDetails.getUser())) {
                return null;
            }
            // Register the user
            moim.getRegisteredUsers().add(userDetails.getUser());
            moimRepository.save(moim);

            MoimJoinResponse moimJoinResponse = MoimJoinResponse.builder()
                    .registeredUsers(moim.getRegisteredUsers())
                    .build();

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
    public MoimReadResponse updateMoim(MoimUpdateRequest moimUpdateRequest, UserDetailsImpl userDetails) {

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
            MoimReadResponse moimReadResponse = MoimReadResponse.builder()
                    .moimId(savedMoim.getId())
                    .userId(savedMoim.getUser().getId())
                    .anonymous(savedMoim.getAnonymous())
                    .moimName(savedMoim.getMoimName())
                    .moimIntro(savedMoim.getMoimIntro())
                    .moimDetails(savedMoim.getMoimDetails())
                    .limitPeople(savedMoim.getLimitPeople())
                    .expense(savedMoim.getExpense())
                    .licenseLimit(savedMoim.getLicenseLimit())
                    .locations(savedMoim.getLocations())
                    .age(savedMoim.getAge())
                    .moods(savedMoim.getMoods())
                    .registeredUsers(savedMoim.getRegisteredUsers())
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

    public List<MoimReadResponse> getRegisteredMoims(UserDetailsImpl userDetails) {
        try {
            List<Moim> moims = moimRepository.findByRegisteredUsersContaining(userDetails.getUser());

            List<MoimReadResponse> moimReadResponses = moims.stream()
                    .map(moim -> new MoimReadResponse(
                            moim.getId(),
                            moim.getUser().getId(),
                            moim.getAnonymous(),
                            moim.getMoimName(),
                            moim.getMoimIntro(),
                            moim.getMoimDetails(),
                            moim.getLimitPeople(),
                            moim.getExpense(),
                            moim.getLicenseLimit(),
                            moim.getLocations(),
                            moim.getAge(),
                            moim.getMoods(),
                            moim.getRegisteredUsers()
                    ))
                    .collect(Collectors.toList());

            return moimReadResponses;
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

    public List<MoimReadResponse> getMoims(UserDetailsImpl userDetails)
    {
        try {
            // Retrieve the user's logs
            List<Moim> moims = moimRepository.findByUserId(userDetails.getId());

            // Map Diving entities to DivingReadResponse DTOs
            List<MoimReadResponse> moimReadResponses = moims.stream()
                    .map(moim -> new MoimReadResponse(
                            moim.getId(),
                            moim.getUser().getId(),
                            moim.getAnonymous(),
                            moim.getMoimName(),
                            moim.getMoimIntro(),
                            moim.getMoimDetails(),
                            moim.getLimitPeople(),
                            moim.getExpense(),
                            moim.getLicenseLimit(),
                            moim.getLocations(),
                            moim.getAge(),
                            moim.getMoods(),
                            moim.getRegisteredUsers()
                    ))
                    .collect(Collectors.toList());

            // Return the list of DivingReadResponse
            return moimReadResponses;

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

    public List<MoimReadResponse> getAllMoims() {
        try {
            List<Moim> moims = moimRepository.findAll();
            List<MoimReadResponse> moimReadResponses = moims.stream()
                    .map(moim -> new MoimReadResponse(
                            moim.getId(),
                            moim.getUser().getId(),
                            moim.getAnonymous(),
                            moim.getMoimName(),
                            moim.getMoimIntro(),
                            moim.getMoimDetails(),
                            moim.getLimitPeople(),
                            moim.getExpense(),
                            moim.getLicenseLimit(),
                            moim.getLocations(),
                            moim.getAge(),
                            moim.getMoods(),
                            moim.getRegisteredUsers()
                    ))
                    .collect(Collectors.toList());

            return moimReadResponses;
        } catch (Exception e) {
            return null;
        }
    }
}
