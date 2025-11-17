package org.creditto.core_banking.domain.account.service;

import lombok.RequiredArgsConstructor;
import org.creditto.core_banking.domain.account.entity.Account;
import org.creditto.core_banking.domain.transaction.entity.TxnResult;
import org.creditto.core_banking.domain.transaction.entity.TxnType;
import org.creditto.core_banking.domain.transaction.service.TransactionService;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
@RequiredArgsConstructor
public class DepositStrategy implements TransactionStrategy {

    private final TransactionService transactionService;

    @Override
    public TxnType getTxnType() {
        return TxnType.DEPOSIT;
    }

    @Override
    public void execute(Account account, BigDecimal amount, Long typeId) {
            TxnResult result = TxnResult.SUCCESS;
            account.deposit(amount);
            transactionService.saveTransaction(account, amount, getTxnType(), typeId, result);


    }

}
