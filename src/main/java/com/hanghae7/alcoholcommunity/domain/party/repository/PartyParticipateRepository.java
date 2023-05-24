package com.hanghae7.alcoholcommunity.domain.party.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.hanghae7.alcoholcommunity.domain.member.entity.Member;
import com.hanghae7.alcoholcommunity.domain.party.entity.Party;
import com.hanghae7.alcoholcommunity.domain.party.entity.PartyParticipate;

public interface PartyParticipateRepository extends JpaRepository<PartyParticipate, Long> {

	PartyParticipate findByPartyAndMember(Party party, Member member);


}
