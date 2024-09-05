package backend.tangsquad.service;

import backend.tangsquad.domain.Logbook;
import backend.tangsquad.domain.Moim;
import backend.tangsquad.domain.User;
import backend.tangsquad.dto.request.LogUpdateRequest;
import backend.tangsquad.dto.request.MoimUpdateRequest;
import backend.tangsquad.repository.MoimRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.Collectors;


@Service
public class MoimService  {
    private MoimRepository moimRepository;

    @Autowired
    public MoimService (MoimRepository moimRepository) {
        this.moimRepository = moimRepository;
    }

    public Moim save(Moim moim) {
        return moimRepository.save(moim);
    }


    // 수정 필요
    public Moim updateMoim(Long moimId, MoimUpdateRequest request) {
        // Retrieve the existing Logbook by userId and logId
        Optional<Moim> moimOptional = moimRepository.findById(moimId);

        if (!moimOptional.isPresent()) {
            throw new NoSuchElementException(", logId: " + moimId);
        }

        // Get the existing logbook
        Moim moim = moimOptional.get();

        // Update fields from the request if they are not null
        if (request.getAnonymous() != null) moim.setAnonymous(request.getAnonymous());
        if (request.getMoimName() != null) moim.setMoimName(request.getMoimName());
        if (request.getMoimIntro() != null) moim.setMoimIntro(request.getMoimIntro());
        if (request.getMoimDetails() != null) moim.setMoimDetails(request.getMoimDetails());
        if (request.getAge() != null) moim.setAge(request.getAge());
        if (request.getLimitPeople() != null) moim.setLimitPeople(request.getLimitPeople());
        if (request.getLicenseLimit() != null) moim.setLicenseLimit(request.getLicenseLimit());
        if (request.getLocationOne() != null) moim.setLocationOne(request.getLocationOne());
        if (request.getLocationTwo() != null) moim.setLocationTwo(request.getLocationTwo());
        if (request.getLocationThree() != null) moim.setLocationThree(request.getLocationThree());
        if (request.getExpense() != null) moim.setExpense(request.getExpense());
        if (request.getMoodOne() != null) moim.setMoodOne(request.getMoodOne());
        if (request.getMoodTwo() != null) moim.setMoodTwo(request.getMoodTwo());


        // Save and return the updated moim
        return moimRepository.save(moim);
    }


    public List<Moim> getMoims(Long userId)
    {
        return moimRepository.findByUserId(userId);
    }


}
