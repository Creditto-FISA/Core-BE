package org.creditto.core_banking.domain.account.service;

import lombok.RequiredArgsConstructor;
import org.creditto.core_banking.domain.account.dto.AccountRes;
import org.creditto.core_banking.domain.account.entity.Account;
import org.creditto.core_banking.domain.account.repository.AccountRepository;
import org.creditto.core_banking.global.response.error.ErrorBaseCode;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AccountServiceImpl implements AccountService {

    private final AccountRepository accountRepository;

    @Override
    public BigDecimal getBalance(Long id) {
        Account account = accountRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException(ErrorBaseCode.NOT_FOUND_ENTITY.getMessage()));

        return account.getBalance();
    }

    @Override
    public AccountRes findByAccountNo(String accountNo) {
        Account account = accountRepository.findByAccountNo(accountNo)
                .orElseThrow(() -> new IllegalArgumentException(ErrorBaseCode.NOT_FOUND_ENTITY.getMessage()));

        return AccountRes.from(account);
    }

    @Override
    public List<AccountRes> findByClientId(String clientId) {
        List<Account> accounts = accountRepository.findByClientId(clientId);

        return accounts.stream()
                .map(AccountRes::from)
                .toList();
    }
}
