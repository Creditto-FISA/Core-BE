package org.creditto.core_banking.domain.account.entity;

import jakarta.persistence.*;
import lombok.*;
import org.creditto.core_banking.global.common.BaseEntity;
import org.creditto.core_banking.global.response.error.ErrorBaseCode;
import org.creditto.core_banking.global.response.exception.CustomBaseException;

import java.math.BigDecimal;
import java.util.Map;
import java.util.Random;

@Entity
@Getter
@Builder(access = AccessLevel.PROTECTED)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class Account extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String accountNo;

    private String accountName;

    @Column(precision = 20, scale = 2) // => 정수18자리, 소수점 부분 2자리
    private BigDecimal balance;

    @Enumerated(EnumType.STRING)
    private AccountType accountType;

    @Enumerated(EnumType.STRING)
    private AccountState accountState;

    private String clientId;

    public static Account of(String accountNo, String accountName, BigDecimal balance, AccountType accountType, AccountState accountState, String clientId) {
        return Account.builder()
                .accountNo(accountNo)
                .accountName(accountName)
                .balance(balance)
                .accountType(accountType)
                .accountState(accountState)
                .clientId(clientId)
                .build();
    }

    private static final Map<AccountType, String> ACCOUNT_TYPES_SETTING = Map.of(
            AccountType.DEPOSIT, "1002",
            AccountType.SAVINGS, "181",
            AccountType.LOAN, "207",
            AccountType.INVESTMENT, "520"
    );
    private static final int ACCOUNT_NO_LENGTH = 13;
    private static final Random RANDOM = new Random();

    // 계좌 번호 생성
    public static String generateAccountNo(AccountType accountType) {
        String prefix = ACCOUNT_TYPES_SETTING.get(accountType);
        if (prefix == null) {
            throw new CustomBaseException(ErrorBaseCode.INVALID_ACCOUNT_TYPE);
        }

        StringBuilder sb = new StringBuilder(prefix);
        int length = ACCOUNT_NO_LENGTH - prefix.length();
        for (int i = 0; i < length; i++) {
            sb.append(RANDOM.nextInt(10));
        }
        return sb.toString();
    }


    // 입금
    public void deposit(BigDecimal amount) {
        this.balance = this.balance.add(amount);
    }

    // 출금
    public void withdraw(BigDecimal amount) {
        if (this.balance.compareTo(amount) < 0) {
            throw new CustomBaseException(ErrorBaseCode.INSUFFICIENT_FUNDS);
        }

        this.balance = balance.subtract(amount);
    }

}
