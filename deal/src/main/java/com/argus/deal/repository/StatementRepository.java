package com.argus.deal.repository;

import com.argus.deal.entity.Statement;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface StatementRepository extends JpaRepository<Statement, UUID> {
}
