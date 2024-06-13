package com.argus.deal.repository;

import com.argus.deal.entity.Credit;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

/**
 * CreditRepository.
 *
 * @author Maxim Chistyakov
 */
public interface CreditRepository extends JpaRepository<Credit, UUID> {
}
