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

    public NoticeParticipantResponseDto(Notice notice, String imgUrl, String participantName, Long participantId, Boolean participateIs){
        super(notice.getNoticeId(),notice.getPartyId(), notice.getPartyTitle(), notice.getNoticeCode(), notice.getIsRead());
        this.imgUrl = imgUrl;
        this.participantName = participantName;
        this.participantId = participantId;
        this.participateIs = participateIs;
    }

    public NoticeParticipantResponseDto(Long noticeId, Long partyId, String partyTitle, Integer noticeCode, Boolean isRead, String imgUrl, String participantName, Long participantId, Boolean participateIs) {
        super(noticeId, partyId, partyTitle, noticeCode, isRead);
        this.imgUrl = imgUrl;
        this.participantName = participantName;
        this.participantId = participantId;
        this.participateIs = participateIs;
    }
}
