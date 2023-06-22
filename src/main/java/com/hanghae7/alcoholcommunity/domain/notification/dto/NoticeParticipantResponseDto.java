package com.hanghae7.alcoholcommunity.domain.notification.dto;

import com.hanghae7.alcoholcommunity.domain.notification.entity.Notice;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
public class NoticeParticipantResponseDto extends AbsenceNoticeDto{
    private String imgUrl;
    private String participantName;
    private Long participantId;
    private Boolean participateIs;

    public NoticeParticipantResponseDto(Long partyId, String partyTitle, Integer noticeCode, String imgUrl, String participantName, Long participantId, Boolean participateIs) {
        super(partyId, partyTitle, noticeCode);
        this.imgUrl = imgUrl;
        this.participantName = participantName;
        this.participantId = participantId;
        this.participateIs = participateIs;
    }
}
