package org.creditto.core_banking.domain.overseasremittance.service;

import lombok.RequiredArgsConstructor;
import org.creditto.core_banking.domain.overseasremittance.dto.ExecuteRemittanceCommand;
import org.creditto.core_banking.domain.overseasremittance.dto.OverseasRemittanceRequestDto;
import org.creditto.core_banking.domain.overseasremittance.dto.OverseasRemittanceResponseDto;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 일회성 해외송금 요청을 처리하는 서비스입니다.
 * 이 서비스는 컨트롤러로부터 받은 요청 DTO를 내부 처리용 Command 객체로 변환하고,
 * 실제 송금 로직을 담당하는 {@link RemittanceProcessorService}에 작업을 위임합니다.
 */
@Service
@Transactional
@RequiredArgsConstructor
public class OneTimeRemittanceService {

    private final RemittanceProcessorService remittanceProcessorService;

    /**
     * 일회성 해외송금 요청을 처리합니다.
     *
     * @param request 송금 요청 정보를 담은 DTO
     * @return 송금 처리 결과를 담은 응답 DTO
     */
    public OverseasRemittanceResponseDto processRemittance(OverseasRemittanceRequestDto request) {
        // 요청 DTO를 실행 Command로 변환
        ExecuteRemittanceCommand command = ExecuteRemittanceCommand.from(request);

        // 공통 프로세서를 통해 실행
        return remittanceProcessorService.execute(command);
    }
}

