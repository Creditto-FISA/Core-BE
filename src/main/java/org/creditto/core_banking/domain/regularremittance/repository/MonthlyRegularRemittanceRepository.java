package org.creditto.core_banking.domain.regularremittance.repository;

import org.creditto.core_banking.domain.regularremittance.entity.MonthlyRegularRemittance;
import org.creditto.core_banking.domain.regularremittance.entity.RegRemStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;

@Repository
public interface MonthlyRegularRemittanceRepository extends JpaRepository<MonthlyRegularRemittance, Long> {

    Page<MonthlyRegularRemittance> findMonthlyRegularRemittanceByScheduledDateInAndRegRemStatus(Collection<Integer> scheduledDates, RegRemStatus regRemStatus, Pageable pageable);

    Page<MonthlyRegularRemittance> findMonthlyRegularRemittanceByScheduledDateInAndRegRemStatusIn(
            Collection<Integer> scheduledDates,
            Collection<RegRemStatus> regRemStatuses,
            Pageable pageable
    );
}
