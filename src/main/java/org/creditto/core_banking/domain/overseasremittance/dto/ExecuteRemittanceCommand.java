package org.creditto.core_banking.domain.overseasremittance.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import org.creditto.core_banking.domain.account.entity.Account;
import org.creditto.core_banking.domain.account.repository.AccountRepository;
import org.creditto.core_banking.domain.exchange.dto.ExchangeRateRes;
import org.creditto.core_banking.domain.exchange.service.ExchangeService;
import org.creditto.core_banking.domain.recipient.entity.Recipient;
import org.creditto.core_banking.domain.recipient.repository.RecipientRepository;
import org.creditto.core_banking.global.response.error.ErrorBaseCode;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

/**
 * 서비스 계층 내부에서 해외송금 실행에 필요한 모든 데이터를 전달하는 불변 Command 객체입니다.
 * CQRS 패턴의 Command에 해당하며, 송금 처리에 필요한 모든 정보를 포함합니다.
 * 정적 팩토리 메서드 {@link #from(OverseasRemittanceRequestDto, AccountRepository, RecipientRepository, ExchangeService)}를 통해 생성됩니다.
 */
@Getter
@Builder
@AllArgsConstructor
public class ExecuteRemittanceCommand {

    /**
     * 고객 식별자
     */
    private String clientId;

    /**
     * 수취인 엔티티의 식별자
     */
    private Long recipientId;

    /**
     * 출금계좌 엔티티의 식별자
     */
    private Long accountId;

    /**
     * 수수료 내역 엔티티의 식별자
     */
    private Long feeId;

    /**
     * 정기송금 엔티티의 식별자 (일회성 송금의 경우 null)
     */
    private Long regRemId;

    /**
     * 송금에 적용될 환율
     */
    private BigDecimal exchangeRate;

    /**
     * 보내는 통화 (e.g., "KRW")
     */
    private String sendCurrency;

    /**
     * 받는 통화 (e.g., "USD")
     */
    private String currencyCode;

    /**
     * 보내는 금액 (송금 통화 기준)
     */
    private BigDecimal sendAmount;

    /**
     * 송금 시작일 (정기송금의 경우 사용)
     */
    private LocalDate startDate;


    /**
     * 외부 요청 DTO와 도메인 서비스/리포지토리를 사용하여 송금 실행에 필요한 모든 정보를 포함하는 {@link ExecuteRemittanceCommand} 객체를 생성합니다.
     * 이 메서드는 Command 객체 생성을 위한 모든 로직(엔티티 조회, 환율 정보 조회 등)을 캡슐화합니다.
     *
     * @param request           클라이언트로부터 받은 해외송금 요청 DTO
     * @param accountRepository 계좌 조회를 위한 리포지토리
     * @param recipientRepository 수취인 생성을 위한 리포지토리
     * @param exchangeService   환율 조회를 위한 서비스
     * @return 모든 정보가 포함된 새로운 Command 객체
     */
    public static ExecuteRemittanceCommand from(
            OverseasRemittanceRequestDto request,
            AccountRepository accountRepository,
            RecipientRepository recipientRepository,
            ExchangeService exchangeService
    ) {
        // 1. 출금 계좌 조회 및 ID 확보
        // TODO: AccountService.findAccountByNumber(request.getAccountNumber()) 호출로 대체 필요 (현재 AccountService는 id를 반환하지 않아 직접 호출)
        Account account = accountRepository.findByAccountNo(request.getAccountNumber())
                .orElseThrow(() -> new RuntimeException(ErrorBaseCode.NOT_FOUND_ACCOUNT.getMessage())); // TODO: Custom Exception

        // 2. 수취인 정보 처리 (현재는 항상 신규 생성, 추후 find-or-create 로직으로 개선 필요)
        // TODO: RecipientService.findOrCreateRecipient(recipientInfo) 호출로 대체 필요
        OverseasRemittanceRequestDto.RecipientInfo recipientInfo = request.getRecipientInfo();
        Recipient newRecipient = Recipient.of(
                recipientInfo.getName(),
                recipientInfo.getPhoneNo(),
                null, // phoneCc는 DTO에 없으므로 null 처리
                recipientInfo.getBankName(),
                recipientInfo.getBankCode(),
                recipientInfo.getAccountNumber(),
                recipientInfo.getCountry(),
                request.getReceiveCurrency()
        );
        Recipient savedRecipient = recipientRepository.save(newRecipient);

        // 3. 환율 정보 조회
        List<ExchangeRateRes> exchangeRates = exchangeService.getLatestRates();
        BigDecimal exchangeRate = exchangeRates.stream()
                .filter(rate -> rate.getCurrencyUnit().equals(request.getReceiveCurrency()))
                .map(rate -> new BigDecimal(rate.getBaseRate().replace(",", ""))) // getBaseRate() 사용 및 파싱
                .findFirst()
                .orElseThrow(() -> new RuntimeException(ErrorBaseCode.CURRENCY_NOT_SUPPORTED.getMessage())); // TODO: Custom Exception

        // 4. 수수료 ID 결정 (현재는 null, 추후 로직 추가 필요)
        Long feeId = null; // TODO: 수수료 계산 및 FeeRecord 생성 로직 추가 (Fee 도메인 서비스 필요)

        // 5. ExecuteRemittanceCommand 생성 및 반환
        return ExecuteRemittanceCommand.builder()
                .clientId(request.getClientId())
                .recipientId(savedRecipient.getRecipientId())
                .accountId(account.getId()) // getAccountId() -> getId()
                .feeId(feeId)
                .regRemId(request.getRecurId()) // 정기송금 ID는 DTO에서 받은 값 사용
                .exchangeRate(exchangeRate)
                .sendCurrency(request.getSendCurrency())
                .currencyCode(request.getReceiveCurrency()) // 수취 통화를 Command의 currencyCode로 사용
                .sendAmount(request.getSendAmount())
                .startDate(request.getStartDate())
                .build();
    }
}
