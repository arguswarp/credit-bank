package com.argus.deal.repository;

import com.argus.deal.entity.Client;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface ClientRepository extends JpaRepository<Client, UUID> {

    Optional<Client> findByPassportSeriesAndPassportNumber(String series, String number);
}
