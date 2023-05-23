package com.hanghae7.alcoholcommunity.domain.party.repository;

import org.hibernate.metamodel.model.convert.spi.JpaAttributeConverter;
import org.springframework.data.jpa.repository.JpaRepository;

import com.hanghae7.alcoholcommunity.domain.party.entity.Party;

/**
 * Please explain the class!!
 *
 * @fileName      : example
 * @author        : mycom
 * @since         : 2023-05-19
 */
public interface PartyRepository extends JpaRepository<Party, Long> {
}
