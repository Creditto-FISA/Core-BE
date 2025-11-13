package org.creditto.core_banking.domain.remittancefee.repository;

import org.creditto.core_banking.domain.remittancefee.entity.PctServiceFee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PctServiceFeeRepository extends JpaRepository<PctServiceFee, Long> {
}
