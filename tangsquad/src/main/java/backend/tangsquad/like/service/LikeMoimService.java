package backend.tangsquad.like.service;

import backend.tangsquad.auth.jwt.UserDetailsImpl;
import backend.tangsquad.common.repository.UserRepository;
import backend.tangsquad.like.dto.request.LikeLogbookRequest;
import backend.tangsquad.like.dto.request.LikeMoimRequest;
import backend.tangsquad.like.entity.LikeLogbook;
import backend.tangsquad.like.entity.LikeMoim;
import backend.tangsquad.like.repository.LikeLogbookRepository;
import backend.tangsquad.like.repository.LikeMoimRepository;
import backend.tangsquad.logbook.repository.LogbookRepository;
import backend.tangsquad.moim.repository.MoimRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class LikeMoimService {

    private final UserRepository userRepository;
    private final LikeMoimRepository likeMoimRepository;
    private final MoimRepository moimRepository;

    public LikeMoimRequest createLike(Long moimId, UserDetailsImpl userDetails) {
        Long userId = userRepository.findById(userDetails.getId())
                .orElseThrow(() -> new RuntimeException("유저 정보를 찾을 수 없습니다."))
                .getId();

        Long savedMoimId = moimRepository.findById(moimId)
                .orElseThrow(() -> new RuntimeException("로그북을 찾을 수 없습니다."))
                .getId();

        LikeMoim like = new LikeMoim(userId, savedMoimId);
        LikeMoim savedLike = likeMoimRepository.save(like);
        LikeMoimRequest likeMoimRequest = new LikeMoimRequest(
                savedLike.getUserId(),
                savedLike.getMoimId()
        );

        return likeMoimRequest;
    }

    public List<LikeMoimRequest> getLikeMoims(UserDetailsImpl userDetails) {
        Long userId = userDetails.getId();  // Get the authenticated user's ID

        List<LikeMoim> likedMoims = likeMoimRepository.findAllByUserId(userId);

        // Map LikeLogbook entities to DTOs (e.g., LikeLogbookRequest)
        return likedMoims.stream()
                .map(likeMoim -> new LikeMoimRequest(
                        likeMoim.getUserId(),  // Assuming Logbook has a reference
                        likeMoim.getMoimId()      // Assuming User has a reference
                ))
                .collect(Collectors.toList());

    }
}
