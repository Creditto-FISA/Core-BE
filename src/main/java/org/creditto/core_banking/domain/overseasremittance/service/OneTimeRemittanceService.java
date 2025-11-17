package org.creditto.core_banking.domain.overseasremittance.service;

import lombok.RequiredArgsConstructor;
import org.creditto.core_banking.domain.account.repository.AccountRepository;
import org.creditto.core_banking.domain.exchange.service.ExchangeService;
import org.creditto.core_banking.domain.recipient.repository.RecipientRepository;
import org.creditto.core_banking.domain.overseasremittance.dto.ExecuteRemittanceCommand;
import org.creditto.core_banking.domain.overseasremittance.dto.OverseasRemittanceRequestDto;
import org.creditto.core_banking.domain.overseasremittance.dto.OverseasRemittanceResponseDto;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 해외송금 유스케이스를 조정하는 Application Service 입니다.
 * 외부(Controller)의 요청을 받아 도메인 로직을 실행하도록 오케스트레이션 역할을 수행합니다.
 * 1. {@link ExecuteRemittanceCommand#from(OverseasRemittanceRequestDto, AccountRepository, RecipientRepository, ExchangeService)}를 호출하여 Command 객체 생성을 위임합니다.
 * 2. 생성된 Command를 실제 비즈니스 로직을 담고 있는 Domain Service ({@link RemittanceProcessorService})에 전달하여 실행을 위임합니다.
 */
@Service
@Transactional
@RequiredArgsConstructor
public class OneTimeRemittanceService {

    private final RemittanceProcessorService remittanceProcessorService;
    private final ExchangeService exchangeService;
    private final AccountRepository accountRepository;
    private final RecipientRepository recipientRepository;

    /**
     * 클라이언트의 해외송금 요청을 받아 전체 송금 프로세스를 조정합니다.
     *
     * @param request 클라이언트로부터 받은 해외송금 요청 데이터
     * @return 송금 처리 결과
     */
    public OverseasRemittanceResponseDto processRemittance(OverseasRemittanceRequestDto request) {
        // 1. Command 생성 위임: DTO를 내부 Command 객체로 변환
        ExecuteRemittanceCommand command = ExecuteRemittanceCommand.from(
            request,
            accountRepository,
            recipientRepository,
            exchangeService
        );

        // 2. Command 실행 위임: 생성된 Command를 통해 실제 송금 로직 실행
        return remittanceProcessorService.execute(command);
    }
}
