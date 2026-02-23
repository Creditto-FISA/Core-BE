package org.creditto.core_banking.domain.transaction.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.creditto.core_banking.domain.transaction.dto.TransactionRes;
import org.creditto.core_banking.domain.transaction.service.TransactionService;
import org.creditto.core_banking.global.response.ApiResponseUtil;
import org.creditto.core_banking.global.response.BaseResponse;
import org.creditto.core_banking.global.response.SuccessCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/core/transactions")
@Tag(name = "Transaction", description = "거래 내역 조회 API")
public class TransactionController {

    private final TransactionService transactionService;

    @Operation(summary = "계좌 거래내역 조회", description = "계좌 ID 기준 거래내역 목록을 조회합니다.")
    @GetMapping("/{accountId}")
    public ResponseEntity<BaseResponse<List<TransactionRes>>> getTransactions(@PathVariable Long accountId) {
        return ApiResponseUtil.success(SuccessCode.OK, transactionService.findByAccountId(accountId));
    }
}
