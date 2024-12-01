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
import java.util.Optional;
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

        Optional<LikeLogbook> likeLogbookOptional = likeLogbookRepository.findByUserIdAndLogbookId(userId, savedLogbookId);

        if (likeLogbookOptional.isPresent()) throw new RuntimeException("이미 좋아요를 누른 로그북입니다.");

        LikeLogbook like = new LikeLogbook(userId, savedLogbookId);
        LikeLogbook savedLike = likeLogbookRepository.save(like);

        return new LikeLogbookRequest(
                savedLike.getUserId(),
                savedLike.getLogbookId()
        );
    }

    public List<LogbookResponse> getLikeLogbooks(UserDetailsImpl userDetails) {

        try {
            List<LikeLogbook> likedLogbooks = likeLogbookRepository.findAllByUserId(userDetails.getId());

            List<Long> likeLogbookIds = likedLogbooks.stream()
                    .map(LikeLogbook::getLogbookId)
                    .collect(Collectors.toList());

            List<Logbook> logbooks = logbookRepository.findAllById(likeLogbookIds);

            return logbooks.stream().map(logbook -> LogbookResponse.builder()
                    .logbookId(logbook.getId())
                    .userId(logbook.getUser().getId())
                    .thumbnailUrl(logbook.getThumbnailUrl())
                    .isPublic(logbook.getIsPublic())
                    .title(logbook.getTitle())
                    .contents(logbook.getContents())
                    .date(logbook.getDate())
                    .logs(logbook.getLogs())
                    .userCondition(logbook.getUserCondition())
                    .build())
                .collect(Collectors.toList());
        } catch (Exception e) {
            return null;
        }
    }

    public void cancelLike(Long logId, UserDetailsImpl userDetails) {
        Optional<LikeLogbook> likeLogbook = likeLogbookRepository.findByUserIdAndLogbookId(userDetails.getId(), logId);
        if(likeLogbook.isPresent()) {
            likeLogbookRepository.delete(likeLogbook.get());
        } else {
            throw new RuntimeException("좋아요를 누르지 않은 로그북입니다.");
        }
    }

}
