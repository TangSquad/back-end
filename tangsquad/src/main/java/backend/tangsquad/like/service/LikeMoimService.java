package backend.tangsquad.like.service;

import backend.tangsquad.auth.jwt.UserDetailsImpl;
import backend.tangsquad.common.entity.User;
import backend.tangsquad.common.repository.UserRepository;
import backend.tangsquad.diving.dto.response.DivingResponse;
import backend.tangsquad.diving.entity.Diving;
import backend.tangsquad.like.dto.request.LikeLogbookRequest;
import backend.tangsquad.like.dto.request.LikeMoimRequest;
import backend.tangsquad.like.entity.LikeDiving;
import backend.tangsquad.like.entity.LikeLogbook;
import backend.tangsquad.like.entity.LikeMoim;
import backend.tangsquad.like.repository.LikeLogbookRepository;
import backend.tangsquad.like.repository.LikeMoimRepository;
import backend.tangsquad.logbook.repository.LogbookRepository;
import backend.tangsquad.moim.dto.response.MoimResponse;
import backend.tangsquad.moim.entity.Moim;
import backend.tangsquad.moim.repository.MoimRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
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
                .orElseThrow(() -> new RuntimeException("모임을 찾을 수 없습니다."))
                .getId();

        Optional<LikeMoim> likeMoimOptional = likeMoimRepository.findByUserIdAndMoimId(userId, savedMoimId);

        if (likeMoimOptional.isPresent()) throw new RuntimeException("이미 좋아요를 누른 모임입니다.");

        LikeMoim like = new LikeMoim(userId, savedMoimId);
        LikeMoim savedLike = likeMoimRepository.save(like);

        return new LikeMoimRequest(
                savedLike.getUserId(),
                savedLike.getMoimId()
        );
    }

    public List<MoimResponse> getLikeMoims(UserDetailsImpl userDetails) {
        try {
            Long userId = userDetails.getId();  // Get the authenticated user's ID

            List<LikeMoim> likeMoims = likeMoimRepository.findAllByUserId(userId);

            List<Long> moimIds = likeMoims.stream()
                    .map(LikeMoim::getMoimId)
                    .collect(Collectors.toList());

            List<Moim> moims = moimRepository.findAllById(moimIds);
            return moims.stream().map(moim -> MoimResponse.builder()
                    .id(moim.getId())
                    .userId(moim.getUser().getId())
                    .thumbnailurl(moim.getThumbnailUrl())
                    .isPublic(moim.getIsPublic())
                    .moimName(moim.getMoimName())
                    .moimIntro(moim.getMoimIntro())
                    .currentPeople(moim.getCurrentPeople())
                    .limitPeople(moim.getLimitPeople())
                    .expense(moim.getExpense())
                    .licenseLimit(moim.getLicenseLimit())
                    .locations(moim.getLocations())
                    .registeredUserIds(moim.getRegisteredUsers().stream().map(User::getId).collect(Collectors.toList()))
                    .age(moim.getAge())
                    .moods(moim.getMoods())
                    .chatRoomId(moim.getChatRoomId())
                    .build()
            ).collect(Collectors.toList());

        } catch (Exception e) {
            return null;
        }
    }

    public void cancelLike(Long moimId, UserDetailsImpl userDetails) {
        Optional<LikeMoim> optionalLikeMoim = likeMoimRepository.findByUserIdAndMoimId(userDetails.getId(), moimId);
        if(optionalLikeMoim.isEmpty()) throw new RuntimeException("좋아요를 누르지 않은 모임입니다.");
        try{
            likeMoimRepository.deleteByUserIdAndMoimId(userDetails.getId(), moimId);
        } catch (Exception e) {
            throw new RuntimeException("좋아요 취소 중 오류가 발생했습니다.");
        }
    }
}
