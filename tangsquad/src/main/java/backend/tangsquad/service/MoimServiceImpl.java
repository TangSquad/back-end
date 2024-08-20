package backend.tangsquad.service;

import backend.tangsquad.domain.Logbook;
import backend.tangsquad.domain.Moim;
import backend.tangsquad.domain.User;
import backend.tangsquad.dto.request.LogUpdateRequest;
import backend.tangsquad.dto.request.MoimUpdateRequest;
import backend.tangsquad.repository.MemoryMoimRepository;
import backend.tangsquad.repository.MoimRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.Collectors;


@Service
public class MoimServiceImpl implements MoimService {
    private final MoimRepository moimRepository = new MemoryMoimRepository();

    @Override
    public void make(Moim moim) {
        moimRepository.save(moim);
    }

    @Override
    public Optional<Moim> findMoimById(Long moimId) {
        return moimRepository.findById(moimId);
    }

    @Override
    public void deleteMoim(Long currentUserId, Long moimId) {
        Optional<Moim> moimOptional = moimRepository.findById(moimId);

        if (moimOptional.isPresent()) {
            Moim moim = moimOptional.get();

            // 모임의 소유자가 현재 유저인지 확인
            if (!moim.getUser().getId().equals(currentUserId)) {
                throw new AccessDeniedException("You do not have permission to delete this Moim");
            }

            // 모임 삭제
            moimRepository.delete(moim);
        } else {
            throw new EntityNotFoundException("Moim not found");
        }
    }

    // 수정 필요
    @Override
    public Moim updateMoim(Long moimId, MoimUpdateRequest request, Long userId) {
        // 모임을 조회
        Moim moim = moimRepository.findById(moimId)
                .orElseThrow(() -> new EntityNotFoundException("Moim not found"));

        // 요청한 사용자가 모임의 소유자인지 확인
        if (!moim.getUser().getId().equals(userId)) {
            throw new AccessDeniedException("You do not have permission to update this Moim");
        }

        // Update fields from the request
        if (request.getMoimMembers() != null) {
            request.setMoimMembers(request.getMoimMembers());
        }
        if (request.getStartDate() != null) {
            request.setStartDate(request.getStartDate());
        }
        if (request.getEndDate() != null) {
            request.setEndDate(request.getEndDate());
        }
        if (request.getMoimName() != null) {
            request.setMoimName(request.getMoimName());
        }
        if (request.getMoimIntro() != null) {
            request.setMoimIntro(request.getMoimIntro());
        }
        if (request.getMoimContents() != null) {
            request.setMoimContents(request.getMoimContents());
        }
        if (request.getMaxPeople() != null) {
            request.setMaxPeople(request.getMaxPeople());
        }
        if (request.getPrice() != null) {
            request.setPrice(request.getPrice());
        }
        if (request.getLicenseLimit() != null) {
            request.setLicenseLimit(request.getLicenseLimit());
        }
        if (request.getRegion() != null) {
            request.setRegion(request.getRegion());
        }
        if (request.getAge() != null) {
            request.setAge(request.getAge());
        }
        if (request.getMood() != null) {
            request.setMood(request.getMood());
        }
        // 업데이트된 모임 저장
        moimRepository.save(moim);

        return moim;
    }

//    @Override
//    public Moim findMoimByOwner(String owner) {
//        return moimRepository.findById(owner);
//    }
    @Override
    public List<Moim> getMoims(User user)
    {
        return moimRepository.findAll().stream()
                .filter(moim -> moim.getUser().equals(user))
                .collect(Collectors.toList());
    }

}
