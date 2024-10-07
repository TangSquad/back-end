package backend.tangsquad.like.service;

import backend.tangsquad.auth.jwt.UserDetailsImpl;
import backend.tangsquad.common.repository.UserRepository;
import backend.tangsquad.like.dto.request.LikeDivingRequest;
import backend.tangsquad.like.dto.request.LikeLogbookRequest;
import backend.tangsquad.like.entity.LikeDiving;
import backend.tangsquad.like.entity.LikeLogbook;
import backend.tangsquad.like.repository.LikeDivingRepository;
import backend.tangsquad.like.repository.LikeLogbookRepository;
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

    public List<LikeDivingRequest> getLikeDivings(UserDetailsImpl userDetails) {
        Long userId = userDetails.getId();  // Get the authenticated user's ID

        List<LikeDiving> likedDivings = likeDivingRepository.findAllByUserId(userId);

        // Map LikeLogbook entities to DTOs (e.g., LikeLogbookRequest)
        return likedDivings.stream()
                .map(likeDiving -> new LikeDivingRequest(
                        likeDiving.getUserId(),  // Assuming Logbook has a reference
                        likeDiving.getDivingId()      // Assuming User has a reference
                ))
                .collect(Collectors.toList());

    }

}
