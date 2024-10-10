package backend.tangsquad.like.service;

import backend.tangsquad.auth.jwt.UserDetailsImpl;
import backend.tangsquad.common.repository.UserRepository;
import backend.tangsquad.diving.dto.request.DivingRequest;
import backend.tangsquad.diving.entity.Diving;
import backend.tangsquad.diving.repository.DivingRepository;
import backend.tangsquad.like.dto.request.LikeDivingRequest;
import backend.tangsquad.like.entity.LikeDiving;
import backend.tangsquad.like.repository.LikeDivingRepository;
import backend.tangsquad.logbook.repository.LogbookRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class LikeDivingService {

    private final UserRepository userRepository;

    private final LogbookRepository logbookRepository;

    private final LikeDivingRepository likeDivingRepository;

    private final DivingRepository divingRepository;


    public LikeDivingRequest createLike(Long divingId, UserDetailsImpl userDetails) {
        Long userId = userRepository.findById(userDetails.getId())
                .orElseThrow(() -> new RuntimeException("유저 정보를 찾을 수 없습니다."))
                .getId();

        Long savedLogbookId = logbookRepository.findById(divingId)
                .orElseThrow(() -> new RuntimeException("로그북을 찾을 수 없습니다."))
                .getId();

        LikeDiving like = new LikeDiving(userId, savedLogbookId);
        LikeDiving savedLike = likeDivingRepository.save(like);
        LikeDivingRequest likeDivingRequest = new LikeDivingRequest(
                savedLike.getUserId(),
                savedLike.getDivingId()
        );

        return likeDivingRequest;
    }

    public List<DivingRequest> getLikeDivings(UserDetailsImpl userDetails) {

        try {
            Long userId = userDetails.getId();  // Get the authenticated user's ID

            List<LikeDiving> likedDivings = likeDivingRepository.findAllByUserId(userId);

            List<Long> divingIds = likedDivings.stream()
                    .map(likeDiving -> likeDiving.getDivingId())
                    .collect(Collectors.toList());

            List<Diving> divings = divingRepository.findAllById(divingIds);
            return divings.stream().map(diving -> DivingRequest.builder()
                    .divingName(diving.getDivingName())
                    .divingIntro(diving.getDivingIntro())
                    .age(diving.getAge())
                    .moods(diving.getMoods())
                    .limitPeople(diving.getLimitPeople())
                    .limitLicense(diving.getLimitLicense())
                    .build()
            ).collect(Collectors.toList());

        } catch (Exception e) {
            return null;
        }
    }

}
