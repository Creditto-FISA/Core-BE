package org.creditto.core_banking.domain.overseasremittance.service;

import org.creditto.core_banking.domain.overseasremittance.dto.OverseasRemittanceRequestDto;
import org.creditto.core_banking.domain.overseasremittance.dto.OverseasRemittanceResponseDto;

import java.util.List;

public interface OverseasRemittanceService {

    // 해외송금 내역 조회
    List<OverseasRemittanceResponseDto> getRemittanceList(String clientId);

    // 해외송금 처리_일회성
    OverseasRemittanceResponseDto processRemittance(OverseasRemittanceRequestDto request);

}