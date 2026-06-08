package com.payment.repository;

import com.payment.entity.Idempotency;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface IdempotencyRepository extends JpaRepository<Idempotency, String> {

    Optional<Idempotency> findByIdempotencyKeyAndOperationType(
            String idempotencyKey, String operationType);
}

