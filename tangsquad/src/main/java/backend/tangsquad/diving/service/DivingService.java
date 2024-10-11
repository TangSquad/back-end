package backend.tangsquad.diving.service;

import backend.tangsquad.auth.jwt.UserDetailsImpl;
import backend.tangsquad.common.entity.User;
import backend.tangsquad.diving.controller.DivingController;
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

    public DivingResponse createDiving(DivingRequest divingRequest, UserDetailsImpl userDetails) {
        try {
            Diving diving = Diving.builder()
                    .user(userDetails.getUser())
                    .divingName(divingRequest.getDivingName())
                    .divingIntro(divingRequest.getDivingIntro())
                    .age(divingRequest.getAge())
                    .moods(divingRequest.getMoods())
                    .limitPeople(divingRequest.getLimitPeople())
                    .limitLicense(divingRequest.getLimitLicense())
                    .startDate(divingRequest.getStartDate())
                    .endDate(divingRequest.getEndDate())
                    .location(divingRequest.getLocation())
                    .build();

            // Save diving
            Diving savedDiving = divingRepository.save(diving);
            return DivingResponse.builder()
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
        } catch (Exception e) {
            return null;
        }
    }

    public List<DivingResponse> getMyDivings(UserDetailsImpl userDetails) {
        try {
            List<Diving> divings = divingRepository.findByUserId(userDetails.getId());

            return divings.stream()
                    .map(diving -> DivingResponse.builder()
                            .divingName(diving.getDivingName())
                            .divingIntro(diving.getDivingIntro())
                            .age(diving.getAge())
                            .moods(diving.getMoods())
                            .limitPeople(diving.getLimitPeople())
                            .limitLicense(diving.getLimitLicense())
                            .startDate(diving.getStartDate())
                            .endDate(diving.getEndDate())
                            .location(diving.getLocation())
                            .build()
                    )
                    .collect(Collectors.toList());

        } catch (Exception e) {
            return null;
        }
    }

    public DivingResponse getDiving(Long divingId) {
        try {
            Optional<Diving> divingOptional = divingRepository.findById(divingId);
            if (divingOptional.isPresent()) {
                Diving diving = divingOptional.get();

                // Convert diving to LogReadResponse
                return DivingResponse.builder()
                        .divingName(diving.getDivingName())
                        .divingIntro(diving.getDivingIntro())
                        .age(diving.getAge())
                        .moods(diving.getMoods())
                        .limitPeople(diving.getLimitPeople())
                        .limitLicense(diving.getLimitLicense())
                        .startDate(diving.getStartDate())
                        .endDate(diving.getEndDate())
                        .location(diving.getLocation())
                        .build();
            } else {
                return null;
            }
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

            return divingResponse;
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
