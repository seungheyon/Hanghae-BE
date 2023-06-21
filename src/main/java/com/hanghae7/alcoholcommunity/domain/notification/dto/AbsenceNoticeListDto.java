package com.hanghae7.alcoholcommunity.domain.notification.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
@Builder
public class AbsenceNoticeListDto {
    private List<AbsenceNoticeDto> absenceNoticeDtoList;
}
