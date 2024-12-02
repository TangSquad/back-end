package backend.tangsquad.like.service;

import backend.tangsquad.auth.jwt.UserDetailsImpl;
import backend.tangsquad.common.repository.UserRepository;
import backend.tangsquad.converter.ConvertTo;
import backend.tangsquad.diving.dto.response.DivingResponse;
import backend.tangsquad.diving.entity.Diving;
import backend.tangsquad.diving.repository.DivingRepository;
import backend.tangsquad.like.entity.LikeDiving;
import backend.tangsquad.like.repository.LikeDivingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class LikeDivingService {

    private final UserRepository userRepository;

    private final LikeDivingRepository likeDivingRepository;

    private final DivingRepository divingRepository;


    public DivingResponse createLike(Long divingId, UserDetailsImpl userDetails) {
        Long userId = userRepository.findById(userDetails.getId())
                .orElseThrow(() -> new RuntimeException("유저 정보를 찾을 수 없습니다."))
                .getId();

        Long savedDiving = divingRepository.findById(divingId)
                .orElseThrow(() -> new RuntimeException("다이빙을 찾을 수 없습니다.")).getDivingId();


        Optional<LikeDiving> optionalLikeDiving = likeDivingRepository.findByUserIdAndDivingId(userId, savedDiving);

        if (optionalLikeDiving.isPresent()) throw new RuntimeException("이미 좋아요를 누른 다이빙입니다.");

        LikeDiving like = new LikeDiving(userId, savedDiving);
        LikeDiving savedLike = likeDivingRepository.save(like);

        Optional<Diving> diving = divingRepository.findById(savedDiving);
        if (diving.isEmpty()) new RuntimeException("다이빙을 찾을 수 없습니다.");


        return ConvertTo.convertToDivingResponse(diving.get());
    }

    public List<DivingResponse> getLikeDivings(UserDetailsImpl userDetails) {

        try {
            Long userId = userDetails.getId();  // Get the authenticated user's ID

            List<LikeDiving> likedDivings = likeDivingRepository.findAllByUserId(userId);

            List<Long> divingIds = likedDivings.stream()
                    .map(LikeDiving::getDivingId)
                    .collect(Collectors.toList());

            List<Diving> divings = divingRepository.findAllById(divingIds);
            return divings.stream().map(diving -> ConvertTo.convertToDivingResponse(diving)
            ).collect(Collectors.toList());

        } catch (Exception e) {
            return null;
        }
    }

    public void cancelLike(Long divingId, UserDetailsImpl userDetails) {
        Optional<LikeDiving> optionalLikeDiving = likeDivingRepository.findByUserIdAndDivingId(userDetails.getId(), divingId);
        if(optionalLikeDiving.isEmpty()) throw new RuntimeException("좋아요를 누르지 않은 모임입니다.");
        try{
            likeDivingRepository.delete(optionalLikeDiving.get());
        } catch (Exception e) {
            throw new RuntimeException("좋아요 취소 중 오류가 발생했습니다.");
        }
    }
}
