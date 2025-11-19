package org.creditto.core_banking.domain.regularremittance.repository;

import org.creditto.core_banking.domain.regularremittance.entity.RegRemStatus;
import org.creditto.core_banking.domain.regularremittance.entity.WeeklyRegularRemittance;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.DayOfWeek;

@Repository
public interface WeeklyRegularRemittanceRepository extends JpaRepository<WeeklyRegularRemittance, Long> {

    Page<WeeklyRegularRemittance> findWeeklyRegularRemittanceByScheduledDayAndRegRemStatus(DayOfWeek scheduledDay, RegRemStatus regRemStatus, Pageable pageable);
}
