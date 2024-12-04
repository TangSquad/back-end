package backend.tangsquad.like.service;

import backend.tangsquad.auth.jwt.UserDetailsImpl;
import backend.tangsquad.common.repository.UserRepository;
import backend.tangsquad.converter.ConvertTo;
import backend.tangsquad.like.dto.request.LikeLogbookRequest;
import backend.tangsquad.like.entity.LikeLogbook;
import backend.tangsquad.like.repository.LikeLogbookRepository;
import backend.tangsquad.logbook.dto.request.LogbookRequest;
import backend.tangsquad.logbook.dto.response.LogbookResponse;
import backend.tangsquad.logbook.entity.Log;
import backend.tangsquad.logbook.entity.Logbook;
import backend.tangsquad.logbook.repository.LogbookRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static backend.tangsquad.converter.ConvertTo.convertToLogbookResponse;

@Service
@RequiredArgsConstructor
public class LikeLogbookService {

    private final UserRepository userRepository;
    private final LikeLogbookRepository likeLogbookRepository;
    private final LogbookRepository logbookRepository;

    public LogbookResponse createLike(Long logbookId, UserDetailsImpl userDetails) {

        Optional<LikeLogbook> likeLogbookOptional = likeLogbookRepository.findByLogbookId(logbookId);
        if (likeLogbookOptional.isPresent()) throw new RuntimeException("이미 좋아요를 누른 Logbook 입니다.");

        Logbook logbook = logbookRepository.findById(logbookId)
                .orElseThrow(() -> new IllegalArgumentException("Logbook 을 찾을 수 없습니다."));

        LikeLogbook likeLogbook = LikeLogbook.builder()
                        .logbookId(logbook.getId())
                                .userId(logbook.getUser().getId())
                                        .build();

        likeLogbookRepository.save(likeLogbook);

        List<LikeLogbook> logbookLikes = likeLogbookRepository.findAllByLogbookId(logbook.getId());

        logbook.updateLike(logbookLikes.stream().count());
        logbookRepository.save(logbook);

        return convertToLogbookResponse(logbook, userDetails.getUser().getUserProfile());
    }

    public List<LogbookResponse> getLikeLogbooks(UserDetailsImpl userDetails) {

        try {
            Optional<List<LikeLogbook>> likedLogbooksOptional = likeLogbookRepository.findAllByUserId(userDetails.getId());
            List<LikeLogbook> likedLogbooks = likedLogbooksOptional.get();

            List<Long> likeLogbookIds = likedLogbooks.stream()
                    .map(LikeLogbook::getLogbookId)
                    .collect(Collectors.toList());

            List<Logbook> logbooks = logbookRepository.findAllById(likeLogbookIds);

            return logbooks.stream().map(logbook -> convertToLogbookResponse(logbook, userDetails.getUser().getUserProfile()))
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
