package org.creditto.core_banking.domain.overseasremittance.service;

import lombok.RequiredArgsConstructor;
import org.creditto.core_banking.domain.account.entity.Account;
import org.creditto.core_banking.domain.account.repository.AccountRepository;
import org.creditto.core_banking.domain.exchange.entity.Exchange;
import org.creditto.core_banking.domain.exchange.repository.ExchangeRepository;
import org.creditto.core_banking.domain.overseasremittance.dto.ExecuteRemittanceCommand;
import org.creditto.core_banking.domain.overseasremittance.dto.OverseasRemittanceResponseDto;
import org.creditto.core_banking.domain.overseasremittance.entity.OverseasRemittance;
import org.creditto.core_banking.domain.regularremittance.entity.RegularRemittance;
import org.creditto.core_banking.domain.overseasremittance.repository.OverseasRemittanceRepository;
import org.creditto.core_banking.domain.regularremittance.repository.RegularRemittanceRepository;
import org.creditto.core_banking.domain.recipient.entity.Recipient;
import org.creditto.core_banking.domain.recipient.repository.RecipientRepository;
import org.creditto.core_banking.domain.remittancefee.entity.FeeRecord;
import org.creditto.core_banking.domain.remittancefee.repository.RemittanceFeeRepository;
import org.creditto.core_banking.domain.transaction.entity.Transaction;
import org.creditto.core_banking.domain.transaction.entity.TxnType;
import org.creditto.core_banking.domain.transaction.repository.TransactionRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.Optional;

/**
 * 해외송금의 핵심 비즈니스 로직을 실제로 실행하는 Domain Service 입니다.
 * 이 서비스는 외부와 격리되어, 오직 내부 Command 객체({@link ExecuteRemittanceCommand})만을 받아
 * 송금 실행에 필요한 모든 도메인 규칙(엔티티 조회, 잔액 확인, 출금, 거래 내역 생성 등)을 트랜잭션 내에서 수행합니다.
 */
@Service
@Transactional
@RequiredArgsConstructor
public class RemittanceProcessorService {

    private final OverseasRemittanceRepository remittanceRepository;
    private final AccountRepository accountRepository;
    private final RemittanceFeeRepository remittanceFeeRepository;
    private final RecipientRepository recipientRepository;
    private final TransactionRepository transactionRepository;
    private final RegularRemittanceRepository regularRemittanceRepository;
    private final ExchangeRepository exchangeRepository;

    /**
     * 전달된 Command를 기반으로 해외송금의 모든 단계를 실행합니다.
     * 1. Command에 포함된 ID를 사용하여 관련 엔티티(계좌, 수취인 등)를 조회합니다.
     * 2. 출금될 총액(송금액 + 수수료)을 계산하고, 계좌 잔액을 확인합니다.
     * 3. 환전(Exchange) 엔티티를 생성하고 저장합니다.
     * 4. 해외송금(OverseasRemittance) 엔티티를 생성하고 저장합니다.
     * 5. 실제 계좌에서 수수료 및 송금액을 출금하고, 각 출금에 대한 거래(Transaction) 내역을 생성합니다.
     *
     * @param command 송금 실행에 필요한 모든 데이터가 포함된 Command 객체
     * @return 송금 처리 결과를 담은 응답 DTO
     * @throws IllegalArgumentException 관련 엔티티를 찾을 수 없거나 잔액이 부족할 경우 발생
     */
    public OverseasRemittanceResponseDto execute(ExecuteRemittanceCommand command) {

        // 관련 엔티티 조회
        Account account = accountRepository.findById(command.getAccountId())
                .orElseThrow(() -> new IllegalArgumentException("계좌를 찾을 수 없습니다."));

        Recipient recipient = recipientRepository.findById(command.getRecipientId())
                .orElseThrow(() -> new IllegalArgumentException("수취인을 찾을 수 없습니다."));

        // TODO: 수수료 정보가 ExecuteRemittanceCommand에 제대로 전달되지 않아 임시로 주석 처리.
        // FeeRecord fee = remittanceFeeRepository.findById(command.getFeeId())
        //         .orElseThrow(() -> new IllegalArgumentException("수수료 정보를 찾을 수 없습니다."));
        FeeRecord fee = null; // 임시로 null 처리

        // 정기 송금 정보 조회 (regRemId가 있을 경우)
        RegularRemittance regularRemittance = Optional.ofNullable(command.getRegRemId())
                .map(id -> regularRemittanceRepository.findById(id)
                        .orElseThrow(() -> new IllegalArgumentException("정기송금 정보를 찾을 수 없습니다.")))
                .orElse(null);

        // 금액 및 수수료 계산 (수수료는 임시로 0으로 가정)
        BigDecimal sendAmount = command.getSendAmount();
        BigDecimal totalFee = BigDecimal.ZERO; // TODO: 실제 수수료 계산 로직으로 대체 필요
        // if (fee != null) {
        //     totalFee = fee.getBaseFee().add(fee.getVariableFee());
        // }
        BigDecimal totalDeduction = sendAmount.add(totalFee);

        // 잔액 확인
        if (account.getBalance().compareTo(totalDeduction) < 0) {
            // TODO: InsufficientBalanceException으로 변경 및 GlobalExceptionHandler에서 처리
            throw new IllegalArgumentException("잔액이 부족합니다.");
        }

        // 수취 금액 계산
        BigDecimal receiveAmount = sendAmount.divide(command.getExchangeRate(), 2, RoundingMode.HALF_UP); // TODO: 정확한 계산 로직 확인 필요

        // Exchange 엔티티 생성 및 저장
        Exchange exchange = Exchange.builder()
                .account(account)
                .fromCurrency(command.getSendCurrency())
                .toCurrency(command.getCurrencyCode())
                .country(recipient.getCountry()) // 수취인 국가 사용
                .fromAmount(sendAmount)
                .toAmount(receiveAmount)
                .exchangeRate(command.getExchangeRate())
                .build();
        Exchange savedExchange = exchangeRepository.save(exchange);


        // 송금 이력 생성
        OverseasRemittance overseasRemittance = OverseasRemittance.of(
                recipient,
                account,
                regularRemittance,
                savedExchange, // 생성된 Exchange 엔티티 전달
                fee, // TODO: 실제 FeeRecord 엔티티 전달
                command.getClientId(),
                command.getSendCurrency(),
                command.getCurrencyCode(), // receiveCurrency
                sendAmount,
                receiveAmount,
                command.getStartDate()
        );
        remittanceRepository.save(overseasRemittance);

        // 수수료 차감 및 거래 내역 생성 (수수료가 0이므로 현재는 동작하지 않음)
        if (totalFee.compareTo(BigDecimal.ZERO) > 0) {
            account.withdraw(totalFee);
            Transaction feeTransaction = Transaction.of(
                    account,
                    totalFee,
                    TxnType.FEE,
                    account.getBalance(),
                    LocalDateTime.now()
            );
            transactionRepository.save(feeTransaction);
        }


        // 송금액 차감 및 거래 내역 생성
        account.withdraw(sendAmount);
        Transaction withdrawalTransaction = Transaction.of(
                account,
                sendAmount,
                TxnType.WITHDRAWAL,
                account.getBalance(),
                LocalDateTime.now()
        );
        transactionRepository.save(withdrawalTransaction);

        accountRepository.save(account);

        return OverseasRemittanceResponseDto.from(overseasRemittance);
    }
}