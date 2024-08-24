package backend.tangsquad.service;

import backend.tangsquad.domain.Diving;
import backend.tangsquad.domain.User;
import backend.tangsquad.dto.request.DivingCreateRequest;
import backend.tangsquad.dto.request.DivingUpdateRequest;
import backend.tangsquad.dto.response.DivingCreateResponse;
import backend.tangsquad.repository.DivingRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class DivingService {
    private final DivingRepository divingRepository;

    private UserService userService;

    @Autowired
    public DivingService(DivingRepository divingRepository) {
        this.divingRepository = divingRepository;
    }
//    public List<Diving> getDivings(Long userId)
//    {
//        return divingRepository.findByUserId(userId);
//    }
//
//    public Optional<Diving> getDiving(String username) {
//        return divingRepository.findById(username, id);
//    }

    public Diving createDiving(Long userId, DivingCreateRequest request) {
        // Find the user by ID
        User user = userService.findById(userId)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        // Create a new Diving instance
        Diving diving = new Diving();
        diving.setUser(user);
//        diving.setName(request.getDivingName());
//        diving.setAge(request.getAge());
//        diving.setDivingContents(request.getDivingContents());
//        diving.setDivingIntro(request.getDivingIntro());
//        diving.setDivingMembers(request.getDivingMembers());
//
//        // Save the diving instance
        Diving savedDiving = divingRepository.save(diving);

        // Create and return the response
//        return new DivingCreateResponse(
//                savedDiving.getId(),
//                savedDiving.getDivingName(),
//                savedDiving.getAge(),
//                savedDiving.getDivingContents(),
//                savedDiving.getDivingIntro(),
//                savedDiving.getDivingMembers()
//        );

        return diving;
    }

    // 수정 필요.

    public Diving updateDiving(Long userId, Long divingId, DivingUpdateRequest request) {
        // Retrieve the Diving entry to be updated
        Diving diving = divingRepository.findById(divingId);
        // Verify if the userId matches the owner of the Diving entry
        if (!diving.getUser().getId().equals(userId)) {
            throw new AccessDeniedException("You do not have permission to update this Diving entry");
        }

        // Update the Diving details
//        diving.setDivingName(request.getDivingName());
//        diving.setAge(request.getAge());
//        diving.setDivingContents(request.getDivingContents());
//        diving.setDivingIntro(request.getDivingIntro());
//        diving.setDivingMembers(request.getDivingMembers());
//
        // Save the updated Diving entry
        return divingRepository.save(diving);
    }

    public void deleteDiving(Long userId, Long divingId) {
        // Retrieve the Diving entry to be deleted
        Diving diving = divingRepository.findById(divingId);
        // Verify if the userId matches the owner of the Diving entry
        if (!diving.getUser().getId().equals(userId)) {
            throw new AccessDeniedException("You do not have permission to delete this Diving entry");
        }

        // Delete the Diving entry
        divingRepository.delete(diving);
    }}
