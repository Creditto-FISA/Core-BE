package org.creditto.core_banking.domain.account.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.creditto.core_banking.domain.account.dto.AccountCreateReq;
import org.creditto.core_banking.domain.account.dto.AccountPasswordConfirmReq;
import org.creditto.core_banking.domain.account.dto.AccountRes;
import org.creditto.core_banking.domain.account.dto.AccountSummaryRes;
import org.creditto.core_banking.domain.account.service.AccountService;
import org.creditto.core_banking.global.response.ApiResponseUtil;
import org.creditto.core_banking.global.response.BaseResponse;
import org.creditto.core_banking.global.response.SuccessCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/core/account")
@Tag(name = "Account", description = "계좌 관련 API")
public class AccountController {

    private final AccountService accountService;

    @Operation(summary = "계좌 생성", description = "사용자 ID 기준으로 신규 계좌를 생성합니다.")
    @PostMapping("/{userId}")
    public ResponseEntity<BaseResponse<AccountRes>> createAccount(@RequestBody AccountCreateReq request,
                                                                  @PathVariable Long userId) {
        return ApiResponseUtil.success(SuccessCode.CREATED, accountService.createAccount(request, userId));
    }

    @Operation(summary = "계좌 비밀번호 검증", description = "요청된 계좌 비밀번호 일치 여부를 검증합니다.")
    @PostMapping("/{accountId}/verify-password")
    public ResponseEntity<BaseResponse<Void>> verifyPassword(@PathVariable Long accountId,
                                                             @RequestBody AccountPasswordConfirmReq request) {
        accountService.verifyPassword(accountId, request.password());
        return ApiResponseUtil.success(SuccessCode.OK);
    }

    @Operation(summary = "계좌 단건 조회", description = "계좌 ID로 계좌 정보를 조회합니다.")
    @GetMapping("/{accountId}")
    public ResponseEntity<BaseResponse<AccountRes>> getAccountByAccountId(@PathVariable Long accountId) {
        return ApiResponseUtil.success(SuccessCode.OK, accountService.getAccountById(accountId));
    }

    @Operation(summary = "계좌 잔액 조회", description = "계좌 ID로 현재 잔액을 조회합니다.")
    @GetMapping("/{accountId}/balance")
    public ResponseEntity<BaseResponse<BigDecimal>> getAccountBalanceByAccountId(@PathVariable Long accountId) {
        return ApiResponseUtil.success(SuccessCode.OK, accountService.getAccountBalanceById(accountId));
    }

    @Operation(summary = "계좌번호 조회", description = "계좌번호로 계좌 정보를 조회합니다.")
    @GetMapping(params = "accountNo")
    public ResponseEntity<BaseResponse<AccountRes>> getAccountByAccountNo(@RequestParam(name = "accountNo") String accountNo) {
        return ApiResponseUtil.success(SuccessCode.OK, accountService.getAccountByAccountNo(accountNo));
    }

    @Operation(summary = "사용자 계좌 목록 조회", description = "사용자 ID로 보유 계좌 목록을 조회합니다.")
    @GetMapping(params = "userId")
    public ResponseEntity<BaseResponse<List<AccountRes>>> getAccountByClientId(@RequestParam(name = "userId") Long userId) {
        return ApiResponseUtil.success(SuccessCode.OK, accountService.getAccountByUserId(userId));
    }

    @Operation(summary = "사용자 총 잔액 조회", description = "사용자 ID 기준 전체 계좌 수와 총 잔액을 조회합니다.")
    @GetMapping("/balance/total")
    public ResponseEntity<BaseResponse<AccountSummaryRes>> getTotalBalanceByUserId(@RequestParam(name = "userId") Long userId) {
        return ApiResponseUtil.success(SuccessCode.OK, accountService.getTotalBalanceByUserId(userId));
    }
}
