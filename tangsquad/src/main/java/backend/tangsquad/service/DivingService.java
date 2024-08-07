package backend.tangsquad.service;

import backend.tangsquad.domain.Diving;
import backend.tangsquad.domain.Logbook;
import backend.tangsquad.domain.User;
import backend.tangsquad.dto.request.DivingCreateRequest;
import backend.tangsquad.dto.request.DivingUpdateRequest;
import backend.tangsquad.dto.request.LogCreateRequest;
import backend.tangsquad.dto.request.LogUpdateRequest;
import backend.tangsquad.dto.response.DivingCreateResponse;
import backend.tangsquad.dto.response.LogCreateResponse;
import backend.tangsquad.repository.DivingRepository;
import backend.tangsquad.repository.LogRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class DivingService {
    private final DivingRepository divingRepository;

    @Autowired
    public DivingService(DivingRepository divingRepository) {
        this.divingRepository = divingRepository;
    }
    public List<Diving> getDivings(User user)
    {
        return divingRepository.findAll().stream()
                .filter(diving -> diving.getUser().equals(user))
                .collect(Collectors.toList());
    }

    public Optional<Diving> getDiving(String username, Long id) {
        return divingRepository.findById(username, id);
    }

    public DivingCreateResponse createDiving(DivingCreateRequest request) {
        return null;
    }

    // 수정 필요.
    public Diving updateDiving(String username, Long id, DivingUpdateRequest request) {
        Diving diving = new Diving();
        return diving;
    }

    public void deleteDiving(String username, Long id) {
    }
}
