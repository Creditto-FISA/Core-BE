package org.creditto.core_banking.domain.overseasremittance.repository;

import org.creditto.core_banking.domain.overseasremittance.entity.OverseasRemittance;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OverseasRemittanceRepository extends JpaRepository<OverseasRemittance,Long> {
}
