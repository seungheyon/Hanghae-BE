package com.hanghae7.alcoholcommunity.domain.member.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.hanghae7.alcoholcommunity.domain.member.entity.Login;


@Repository
public interface LoginRepository extends JpaRepository<Login, String> {

	Optional<Login> findByMemberEmailId(String memberEmailId);

}
