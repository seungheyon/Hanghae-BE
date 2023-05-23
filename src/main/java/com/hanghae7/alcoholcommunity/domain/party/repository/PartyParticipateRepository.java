package com.hanghae7.alcoholcommunity.domain.party.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.core.userdetails.User;

import com.hanghae7.alcoholcommunity.domain.party.entity.Party;
import com.hanghae7.alcoholcommunity.domain.party.entity.PartyParticipate;

public interface PartyParticipateRepository extends JpaRepository<PartyParticipate, Long> {


}
