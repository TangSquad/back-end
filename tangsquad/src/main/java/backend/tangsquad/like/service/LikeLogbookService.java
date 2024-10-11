package backend.tangsquad.like.service;

import backend.tangsquad.auth.jwt.UserDetailsImpl;
import backend.tangsquad.common.repository.UserRepository;
import backend.tangsquad.like.dto.request.LikeLogbookRequest;
import backend.tangsquad.like.entity.LikeLogbook;
import backend.tangsquad.like.repository.LikeLogbookRepository;
import backend.tangsquad.logbook.dto.request.LogbookRequest;
import backend.tangsquad.logbook.dto.response.LogbookResponse;
import backend.tangsquad.logbook.entity.Logbook;
import backend.tangsquad.logbook.repository.LogbookRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class LikeLogbookService {

    private final UserRepository userRepository;
    private final LikeLogbookRepository likeLogbookRepository;
    private final LogbookRepository logbookRepository;

    public LikeLogbookRequest createLike(Long logbookId, UserDetailsImpl userDetails) {
        Long userId = userRepository.findById(userDetails.getId())
                .orElseThrow(() -> new RuntimeException("유저 정보를 찾을 수 없습니다."))
                .getId();

        Long savedLogbookId = logbookRepository.findById(logbookId)
                .orElseThrow(() -> new RuntimeException("로그북을 찾을 수 없습니다."))
                .getId();

        LikeLogbook like = new LikeLogbook(userId, savedLogbookId);
        LikeLogbook savedLike = likeLogbookRepository.save(like);
        LikeLogbookRequest likeLogbookRequest = new LikeLogbookRequest(
                savedLike.getUserId(),
                savedLike.getLogbookId()
        );

        return likeLogbookRequest;
    }

    public List<LogbookResponse> getLikeLogbooks(UserDetailsImpl userDetails) {

        try {
            List<LikeLogbook> likedLogbooks = likeLogbookRepository.findAllByUserId(userDetails.getId());

            List<Long> likeLogbookIds = likedLogbooks.stream()
                    .map(likeLogbook -> likeLogbook.getLogbookId())
                    .collect(Collectors.toList());

            List<Logbook> logbooks = logbookRepository.findAllById(likeLogbookIds);

            return logbooks.stream().map(logbook -> LogbookResponse.builder()
                            .userId(logbook.getUser().getId())
                            .date(logbook.getDate())
                            .title(logbook.getTitle())
                            .build())
                    .collect(Collectors.toList());
        } catch (Exception e) {
            return null;
        }
    }
}
