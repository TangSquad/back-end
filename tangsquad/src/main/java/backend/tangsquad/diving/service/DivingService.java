package backend.tangsquad.diving.service;

import backend.tangsquad.diving.entity.Diving;
import backend.tangsquad.diving.dto.request.DivingUpdateRequest;
import backend.tangsquad.diving.repository.DivingRepository;
import backend.tangsquad.common.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
public class DivingService {
    private final DivingRepository divingRepository;

    private UserService userService;

    @Autowired
    public DivingService(DivingRepository divingRepository) {
        this.divingRepository = divingRepository;
    }

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

    public Diving updateDiving(Long divingId, DivingUpdateRequest request) {
        // Retrieve the existing Logbook by userId and logId
        Optional<Diving> divingOptional = divingRepository.findById(divingId);

        if (!divingOptional.isPresent()) {
            throw new NoSuchElementException(", divingId: " + divingId);
        }

        // Get the existing logbook
        Diving diving = divingOptional.get();

        // Update fields from the request if they are not null
        if (request.getDivingIntro() != null) diving.setDivingIntro(request.getDivingIntro());
        if (request.getDivingName() != null) diving.setDivingName(request.getDivingName());
        if (request.getAge() != null) diving.setAge(request.getAge());
        if (request.getLocation() != null) diving.setLocation(request.getLocation());
        if (request.getLimitLicense() != null) diving.setLimitLicense(request.getLimitLicense());
        if (request.getLimitPeople() != null) diving.setLimitPeople(request.getLimitPeople());
        if (request.getStartDate() != null) diving.setStartDate(request.getStartDate());
        if (request.getEndDate() != null) diving.setEndDate(request.getEndDate());
        if (request.getMoodOne() != null) diving.setMoodOne(request.getMoodOne());
        if (request.getMoodTwo() != null) diving.setMoodTwo(request.getMoodTwo());

        // Save and return the updated logbook
        return divingRepository.save(diving);
    }

    public void deleteDiving(Long divingId) {
        // Retrieve the logbook based on logId
        Diving diving = divingRepository.findById(divingId)
                .orElseThrow(() -> new NoSuchElementException("Logbook not found for divingId: " + divingId));

        // Delete the logbook
        divingRepository.delete(diving);
    }

}
