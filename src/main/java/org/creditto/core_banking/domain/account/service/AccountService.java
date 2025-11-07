package org.creditto.core_banking.domain.account.service;


import org.creditto.core_banking.domain.account.dto.AccountRes;

import java.math.BigDecimal;
import java.util.List;

public interface AccountService {

    BigDecimal getBalance(Long id);

    AccountRes findByAccountNo(String accountNo);

    List<AccountRes> findByClientId(String clientId);
}
