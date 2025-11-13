package org.creditto.core_banking.domain.overseasremittance.entity;

import jakarta.persistence.*;
import lombok.*;
import org.creditto.core_banking.domain.account.entity.Account;
import org.creditto.core_banking.domain.recipient.entity.Recipient;
import org.creditto.core_banking.domain.regularremittance.entity.RegularRemittance;
import org.creditto.core_banking.domain.remittancefee.entity.RemittanceFee;
import org.creditto.core_banking.global.common.BaseEntity;

import java.math.BigDecimal;

/**
 * 해외송금 거래 정보를 나타내는 엔티티입니다.
 * 한 건의 해외송금은 고객, 계좌, 수취인, 수수료, 환율 등 다양한 정보를 포함합니다.
 */
@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class OverseasRemittance extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long remittanceId;

    /**
     * 수취인 정보
     */
    @ManyToOne(fetch = FetchType.LAZY)
    private Recipient recipient;

    /**
     * 출금 계좌 정보
     */
    @ManyToOne(fetch = FetchType.LAZY)
    private Account account;

    /**
     * 송금을 요청한 고객의 ID
     */
    private String clientId;

    /**
     * 적용된 수수료 정보
     */
    @OneToOne(fetch = FetchType.LAZY)
    private RemittanceFee fee;

    /**
     * 정기송금 정보 (일회성 송금의 경우 null)
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "recur_id")
    private RegularRemittance recur;

    /**
     * 적용된 환율
     */
    private BigDecimal exchangeRate;

    /**
     * 보낸 금액 (원화 기준)
     */
    private BigDecimal sendAmount;

    /**
     * 최종 수취 금액 (수취인이 받게 될 금액)
     */
    private BigDecimal receiveAmount;

    /**
     * 송금 처리 상태
     */
    @Enumerated(EnumType.STRING)
    private RemittanceStatus remittanceStatus;

    /**
     * OverseasRemittance 엔티티를 생성하는 정적 팩토리 메서드입니다.
     * 초기 송금 상태는 PENDING으로 설정됩니다.
     *
     * @param recipient      수취인 엔티티
     * @param account        출금 계좌 엔티티
     * @param clientId       고객 ID
     * @param fee            수수료 엔티티
     * @param recur          정기송금 엔티티 (선택 사항)
     * @param exchangeRate   적용 환율
     * @param sendAmount     송금액
     * @param receiveAmount  수취액
     * @return 새로운 OverseasRemittance 객체
     */
    public static OverseasRemittance of(
            Recipient recipient,
            Account account,
            String clientId,
            RemittanceFee fee,
            RegularRemittance recur,
            BigDecimal exchangeRate,
            BigDecimal sendAmount,
            BigDecimal receiveAmount
    ){
        return OverseasRemittance.builder()
                .recipient(recipient)
                .account(account)
                .clientId(clientId)
                .fee(fee)
                .recur(recur)
                .exchangeRate(exchangeRate)
                .sendAmount(sendAmount)
                .receiveAmount(receiveAmount)
                .remittanceStatus(RemittanceStatus.PENDING)   //기본 상태 PENDING
                .build();
    }

}