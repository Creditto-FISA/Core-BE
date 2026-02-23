package org.creditto.core_banking.domain.account.service.strategy;

import org.creditto.core_banking.common.vo.Money;
import org.creditto.core_banking.domain.account.entity.Account;
import org.creditto.core_banking.domain.transaction.entity.TxnType;
import org.creditto.core_banking.domain.transaction.service.TransactionService;
import org.creditto.core_banking.global.common.CurrencyCode;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class WithdrawalStrategy extends AbstractTransactionStrategy {

    public WithdrawalStrategy(TransactionService transactionService) {
        super(transactionService);
    }

    @Override
    protected void process(Account account, BigDecimal amount, Long typeId) {
        account.withdraw(Money.of(amount, CurrencyCode.KRW));
    }

    @Override
    public TxnType getTxnType() {
        return TxnType.WITHDRAWAL;
    }
}
