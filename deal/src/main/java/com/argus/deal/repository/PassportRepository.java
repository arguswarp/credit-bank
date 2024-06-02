package com.argus.deal.repository;

import com.argus.deal.entity.Passport;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface PassportRepository  extends JpaRepository<Passport, UUID> {

    Optional<Passport> findBySeriesAndNumber(String series, String number);
}
