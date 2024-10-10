package backend.tangsquad.diving.service;

import backend.tangsquad.diving.dto.request.DivingRequest;
import backend.tangsquad.diving.dto.response.DivingResponse;
import backend.tangsquad.diving.entity.Diving;
import backend.tangsquad.diving.repository.DivingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class DivingService {
    private final DivingRepository divingRepository;

    public Diving save(Diving diving) {
        return divingRepository.save(diving);
    }


    public List<Diving> getDivings(Long userId)
    {
        return divingRepository.findByUserId(userId);
    }

    public Optional<Diving> getDiving(Long divingId) {
        return divingRepository.findById(divingId);
    }



    // 수정 필요.

    public DivingResponse updateDiving(Long divingId, DivingRequest divingRequest) {
        // Retrieve the existing Logbook by userId and logId
        Optional<Diving> divingOptional = divingRepository.findById(divingId);

        if (!divingOptional.isPresent()) {
            throw new NoSuchElementException(", divingId: " + divingId);
        }

        // Get the existing logbook
        Diving diving = divingOptional.get();

        diving.update(divingRequest);

        Diving savedDiving = divingRepository.save(diving);
        DivingResponse divingResponse = DivingResponse.builder()
                .divingName(savedDiving.getDivingName())
                .divingIntro(savedDiving.getDivingIntro())
                .age(savedDiving.getAge())
                .moods(savedDiving.getMoods())
                .limitPeople(savedDiving.getLimitPeople())
                .limitLicense(savedDiving.getLimitLicense())
                .startDate(savedDiving.getStartDate())
                .endDate(savedDiving.getEndDate())
                .location(savedDiving.getLocation())
                .build();
        // Save and return the updated logbook
        return divingResponse;
    }

    public void deleteDiving(Long divingId) {
        // Retrieve the logbook based on logId
        Diving diving = divingRepository.findById(divingId)
                .orElseThrow(() -> new NoSuchElementException("Logbook not found for divingId: " + divingId));

        // Delete the logbook
        divingRepository.delete(diving);
    }

}
