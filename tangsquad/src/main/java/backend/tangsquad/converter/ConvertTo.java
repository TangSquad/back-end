package backend.tangsquad.converter;

import backend.tangsquad.common.entity.User;
import backend.tangsquad.common.entity.UserProfile;
import backend.tangsquad.diving.dto.response.DivingResponse;
import backend.tangsquad.diving.entity.Diving;
import backend.tangsquad.logbook.dto.response.LogResponse;
import backend.tangsquad.logbook.dto.response.LogbookResponse;
import backend.tangsquad.logbook.entity.Log;
import backend.tangsquad.logbook.entity.Logbook;
import backend.tangsquad.moim.dto.response.MoimResponse;
import backend.tangsquad.moim.entity.Moim;

import java.util.Collections;
import java.util.stream.Collectors;

public final class ConvertTo {
    public static DivingResponse convertToDivingResponse(Diving diving) {
        return DivingResponse.builder()
                .id(diving.getDivingId())
                .userId(diving.getUser().getId())
                .isPublic(diving.getIsPublic())
                .divingName(diving.getDivingName())
                .divingIntro(diving.getDivingIntro())
                .age(diving.getAge())
                .moods(diving.getMoods())
                .startDate(diving.getStartDate())
                .endDate(diving.getEndDate())
                .thumbnailUrl(diving.getThumbnailUrl())
                .location(diving.getLocation())
                .licenseLimit(diving.getLicenseLimit())
                .currentPeople(diving.getCurrentPeople())
                .limitPeople(diving.getLimitPeople())
                .chatRoomId(diving.getChatRoomId())
                .build();
    }

    public static MoimResponse convertToMoimResponse(Moim moim) {
        return MoimResponse.builder()
                .id(moim.getId())
                .userId(moim.getUser() != null ? moim.getUser().getId() : null)
                .thumbnailUrl(moim.getThumbnailUrl())
                .isPublic(moim.getIsPublic())
                .moimName(moim.getMoimName())
                .moimIntro(moim.getMoimIntro())
                .moimDetails(moim.getMoimDetails())
                .currentPeople(moim.getCurrentPeople())
                .limitPeople(moim.getLimitPeople())
                .expense(moim.getExpense())
                .licenseLimit(moim.getLicenseLimit())
                .locations(moim.getLocations() != null ? moim.getLocations() : Collections.emptyList())
                .registeredUserIds(moim.getRegisteredUsers() != null
                        ? moim.getRegisteredUsers().stream().map(User::getId).collect(Collectors.toList())
                        : Collections.emptyList())
                .age(moim.getAge())
                .moods(moim.getMoods() != null ? moim.getMoods() : Collections.emptyList())
                .chatRoomId(moim.getChatRoomId())
                .build();
    }

    public static LogbookResponse convertToLogbookResponse(Logbook logbook, UserProfile userProfile) {
        return LogbookResponse.builder()
                .id(logbook.getId())
                .date(logbook.getDate())
                .imageUrls(logbook.getImageUrls())
                .title(logbook.getTitle())
                .contents(logbook.getContents())
                .userCondition(logbook.getUserCondition())
                .equipment(logbook.getEquipment())
                .locations(logbook.getLocations())
                .build();
    }

    public static LogResponse convertToLogResponse(Log log) {
        return LogResponse.builder()
                .id(log.getId())
                .userId(log.getUser().getId())
                .locations(log.getLocations())
                .whether(log.getWhether())
                .airTemp(log.getAirTemp())
                .surfTemp(log.getSurfTemp())
                .bottTemp(log.getBottTemp())
                .viewSight(log.getViewSight())
                .tide(log.getTide())
                .wave(log.getWave())
                .surge(log.getSurge())
                .diveTime(log.getDiveTime())
                .subject(log.getSubject())
                .avgDepDepth(log.getAvgDepth())
                .maxDepth(log.getMaxDepth())
                .startBar(log.getStartBar())
                .endBar(log.getEndBar())
                .logbookId(log.getLogbook().getId())
                .build();
    }
}
