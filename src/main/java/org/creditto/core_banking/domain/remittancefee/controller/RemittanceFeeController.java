package org.creditto.core_banking.domain.remittancefee.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.creditto.core_banking.domain.remittancefee.dto.RemittanceFeeReq;
import org.creditto.core_banking.domain.remittancefee.entity.FeeRecord;
import org.creditto.core_banking.domain.remittancefee.service.RemittanceFeeService;
import org.creditto.core_banking.global.response.ApiResponseUtil;
import org.creditto.core_banking.global.response.BaseResponse;
import org.creditto.core_banking.global.response.SuccessCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/remittance-fee")
@RequiredArgsConstructor
@Tag(name = "Remittance Fee", description = "송금 수수료 계산 API")
public class RemittanceFeeController {

    private final RemittanceFeeService remittanceFeeService;

    // 테스트를 위한 API
    @Operation(summary = "송금 수수료 계산", description = "요청된 송금 조건 기준 수수료를 계산하고 저장합니다.")
    @PostMapping("/calculate")
    public ResponseEntity<BaseResponse<FeeRecord>> calculateRemittanceFee(@RequestBody RemittanceFeeReq dto) {
        FeeRecord feeRecord = remittanceFeeService.calculateAndSaveFee(dto);
        return ApiResponseUtil.success(SuccessCode.OK, feeRecord);
    }
}
