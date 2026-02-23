package org.creditto.core_banking.domain.exchange.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.creditto.core_banking.domain.creditscore.service.CreditScoreService;
import org.creditto.core_banking.domain.exchange.dto.*;
import org.creditto.core_banking.domain.exchange.service.ExchangeService;
import org.creditto.core_banking.global.response.ApiResponseUtil;
import org.creditto.core_banking.global.response.BaseResponse;
import org.creditto.core_banking.global.response.SuccessCode;
import org.springframework.http.ResponseEntity;
import org.creditto.core_banking.global.common.CurrencyCode;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/core/exchange")
@RequiredArgsConstructor
@Tag(name = "Exchange", description = "환율 및 우대환율 조회 API")
public class ExchangeController {

    private final ExchangeService exchangeService;
    private final CreditScoreService  creditScoreService;

    /**
     * 최신 환율 정보 조회
     *
     * @return 성공 응답 및 최신 환율 정보 리스트
     */
    @Operation(summary = "최신 환율 목록 조회", description = "지원 통화의 최신 환율 목록을 조회합니다.")
    @GetMapping
    public ResponseEntity<BaseResponse<Map<String, ExchangeRateRes>>> getExchangeRates() {
        return ApiResponseUtil.success(SuccessCode.OK, exchangeService.getLatestRates());
    }

    /**
     * 특정 통화 최신 환율 정보 조회
     *
     * @param currency 조회할 통화
     * @return 성공 응답 및 특정 통화 최신 환율 정보
     */
    @Operation(summary = "통화별 환율 조회", description = "특정 통화의 최신 환율을 조회합니다.")
    @GetMapping("/{currency}")
    public ResponseEntity<BaseResponse<SingleExchangeRateRes>> getExchangeRate(@PathVariable String currency) {
        CurrencyCode currencyCode = CurrencyCode.from(currency);
        return ApiResponseUtil.success(SuccessCode.OK, exchangeService.getRateByCurrency(currencyCode));
    }

    @Operation(summary = "우대환율 조회", description = "사용자와 통화 기준 우대환율 정보를 조회합니다.")
    @GetMapping("/preferential-rate/{userId}/{currency}")
    public ResponseEntity<BaseResponse<PreferentialRateRes>> getPreferentialRate(
            @PathVariable Long userId,
            @PathVariable String currency
    ) {
        CurrencyCode currencyCode = CurrencyCode.from(currency);
        return ApiResponseUtil.success(SuccessCode.OK, exchangeService.getPreferentialRateInfo(userId, currencyCode));
    }
}
