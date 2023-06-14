package com.hanghae7.alcoholcommunity.domain.party.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * Please explain the class!!
 *
 * @fileName      : PartyJoinRequestDto
 * @author        : mycom
 * @since         : 2023-06-03
 */
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class PartyJoinRequestDto {
	String reason;
	String amountAlcohol;
}
