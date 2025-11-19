package org.creditto.core_banking.domain.regularremittance.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.creditto.core_banking.domain.overseasremittance.dto.ExecuteRemittanceCommand;
import org.creditto.core_banking.domain.overseasremittance.service.RemittanceProcessorService;
import org.creditto.core_banking.domain.regularremittance.entity.MonthlyRegularRemittance;
import org.creditto.core_banking.domain.regularremittance.entity.RegRemStatus;
import org.creditto.core_banking.domain.regularremittance.entity.RegularRemittance;
import org.creditto.core_banking.domain.regularremittance.entity.WeeklyRegularRemittance;
import org.creditto.core_banking.domain.regularremittance.repository.MonthlyRegularRemittanceRepository;
import org.creditto.core_banking.domain.regularremittance.repository.WeeklyRegularRemittanceRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class RegularRemittanceScheduler {

    private final RemittanceProcessorService remittanceProcessorService;
    private final MonthlyRegularRemittanceRepository monthlyRegularRemittanceRepository;
    private final WeeklyRegularRemittanceRepository weeklyRegularRemittanceRepository;

    private static final int SIZE = 1000;
    private static final ZoneId ZONE_ID = ZoneId.of("Asia/Seoul");

    @Scheduled(cron = "${scheduler.remittance.monthly-cron}")
    public void executeMonthlyRegularRemittance() {
        LocalDate now = LocalDate.now(ZONE_ID);
        log.info("[RegularRemittanceScheduler {}년 {}월 {}일] 월간 정기 해외송금 Job Start",
                now.getYear(), now.getMonth(), now.getDayOfMonth()
        );
        int nowDayOfMonth = now.getDayOfMonth();

        int page = 0;
        long total = 0L;

        Page<MonthlyRegularRemittance> slice;

        do {
            slice = monthlyRegularRemittanceRepository
                    .findMonthlyRegularRemittanceByScheduledDateAndRegRemStatus(
                            nowDayOfMonth,
                            RegRemStatus.ACTIVE,
                            PageRequest.of(page, SIZE)
                    );

            executeRemittanceForRegRemList(slice.getContent());

            total += slice.getNumberOfElements();
            page++;
        } while (slice.hasNext());

        log.info("[RegularRemittanceScheduler {}년 {}월 {}일] 월간 정기 해외송금 Job : 수행한 송금 수 = {}",
                now.getYear(), now.getMonth(), now.getDayOfMonth(), total
        );
    }

    @Scheduled(cron = "${scheduler.remittance.weekly-cron}")
    public void executeWeeklyRegularRemittance() {
        LocalDate now = LocalDate.now(ZONE_ID);
        DayOfWeek dayOfWeek = now.getDayOfWeek();
        log.info("[RegularRemittanceScheduler {}년 {}월 {}일] 주간 정기 해외송금 Job Start",
                now.getYear(), now.getMonth(), now.getDayOfMonth()
        );

        int page = 0;
        long total = 0L;

        Page<WeeklyRegularRemittance> slice;

        do {
            slice = weeklyRegularRemittanceRepository
                    .findWeeklyRegularRemittanceByScheduledDayAndRegRemStatus(
                            dayOfWeek,
                            RegRemStatus.ACTIVE,
                            PageRequest.of(page, SIZE)
                    );

            executeRemittanceForRegRemList(slice.getContent());

            total += slice.getNumberOfElements();
            page++;
        } while (slice.hasNext());

        log.info("[RegularRemittanceScheduler {}년 {}월 {}일] 주간 정기 해외송금 Job : 수행한 송금 수 = {}",
                now.getYear(), now.getMonth(), now.getDayOfMonth(), total
        );
    }

    private void executeRemittanceForRegRemList(List<? extends RegularRemittance> remittances) {
        remittances.forEach(remittance -> {
            ExecuteRemittanceCommand remittanceCommand = ExecuteRemittanceCommand.of(remittance);
            remittanceProcessorService.execute(remittanceCommand);
        });
    }
}
