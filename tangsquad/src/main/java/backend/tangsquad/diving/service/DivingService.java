package backend.tangsquad.diving.service;

import backend.tangsquad.auth.jwt.UserDetailsImpl;
import backend.tangsquad.diving.dto.request.DivingRequest;
import backend.tangsquad.diving.dto.response.DivingResponse;
import backend.tangsquad.diving.entity.Diving;
import backend.tangsquad.diving.repository.DivingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DivingService {
    private final DivingRepository divingRepository;

    public Diving save(Diving diving) {
        return divingRepository.save(diving);
    }

    private DivingResponse returnDivingResponse(Diving diving) {
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
                .build();
    }

    private List<DivingResponse> returnDivingResponses(List<Diving> divings) {
        return divings.stream()
                .map(diving -> returnDivingResponse(diving))
                .collect(Collectors.toList());
    }

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

            // Save diving
            Diving savedDiving = divingRepository.save(diving);
            return returnDivingResponse(diving);
        } catch (Exception e) {
            return null;
        }
    }

    public List<DivingResponse> getMyDivings(UserDetailsImpl userDetails) {
        try {
            List<Diving> divings = divingRepository.findByUserId(userDetails.getId());

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

                return returnDivingResponse(diving);
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
            return returnDivingResponse(diving);
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

}
