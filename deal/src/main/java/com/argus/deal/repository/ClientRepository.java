package com.argus.deal.repository;

import com.argus.deal.entity.Client;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;
import java.util.UUID;

/**
 * ClientRepository.
 *
 * @author Maxim Chistyakov
 */
public interface ClientRepository extends JpaRepository<Client, UUID> {
    @Query(value = "SELECT * FROM {h-schema}client WHERE client.passport ->> 'series' = ?1 AND client.passport ->> 'number' = ?2", nativeQuery = true)
    Optional<Client> findByPassportSeriesAndNumber(String series, String number);
}
