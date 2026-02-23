package org.creditto.core_banking.domain.regularremittance.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.creditto.core_banking.domain.regularremittance.dto.*;
import org.creditto.core_banking.domain.regularremittance.dto.RegularRemittanceResponseDto;
import org.creditto.core_banking.domain.regularremittance.service.RegularRemittanceService;
import org.creditto.core_banking.global.response.ApiResponseUtil;
import org.creditto.core_banking.global.response.BaseResponse;
import org.creditto.core_banking.global.response.SuccessCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/core/remittance/schedule")
@Tag(name = "Remittance (Scheduled)", description = "정기송금 관리 API")
public class RegularRemittanceController {

    private final RegularRemittanceService regularRemittanceService;

    public RegularRemittanceController(RegularRemittanceService regularRemittanceService) {
        this.regularRemittanceService = regularRemittanceService;
    }

    /**
     * 특정 사용자의 모든 정기송금 설정 내역을 조회합니다.
     *
     * @param userId 사용자 ID
     * @return 해당 사용자의 모든 정기송금 설정 목록 ({@link RegularRemittanceResponseDto})
     */
    @Operation(summary = "정기송금 목록 조회", description = "사용자 ID 기준 정기송금 설정 목록을 조회합니다.")
    @GetMapping
    public ResponseEntity<BaseResponse<List<RegularRemittanceResponseDto>>> getScheduledRemittancesByUserId(@RequestParam("userId") Long userId) {
        return ApiResponseUtil.success(SuccessCode.OK, regularRemittanceService.getScheduledRemittancesByUserId(userId));
    }

    /**
     * 특정 정기송금의 세부사항을 조회합니다.
     *
     * @param userId 사용자 ID
     * @param regRemId 정기송금 ID
     * @return 해당 정기송금 설정의 세부 사항 목록
     */
    @Operation(summary = "정기송금 상세 조회", description = "정기송금 ID 기준 설정 상세를 조회합니다.")
    @GetMapping("/{regRemId}")
    public ResponseEntity<BaseResponse<RemittanceDetailDto>> getScheduledRemittanceDetail(@RequestParam("userId") Long userId, @PathVariable("regRemId") Long regRemId) {
        return ApiResponseUtil.success(SuccessCode.OK, regularRemittanceService.getScheduledRemittanceDetail(userId, regRemId));
    }

    /**
     * 특정 정기송금 설정에 대한 모든 송금 기록을 조회합니다.
     *
     * @param regRemId 정기송금 ID
     * @param userId 사용자 ID
     * @return 해당 정기송금 설정에 대한 모든 송금 기록 목록 ({@link RemittanceHistoryDto})
     */
    @Operation(summary = "정기송금 이력 조회", description = "정기송금 ID 기준 실행 이력 목록을 조회합니다.")
    @GetMapping("/history/{regRemId}")
    public ResponseEntity<BaseResponse<List<RemittanceHistoryDto>>> getRemittanceRecordsByRecurId(@PathVariable("regRemId") Long regRemId, @RequestParam("userId") Long userId) {
        return ApiResponseUtil.success(SuccessCode.OK, regularRemittanceService.getRegularRemittanceHistoryByRegRemId(userId, regRemId));
    }

    /**
     * 단일 정기송금 내역의 상세 정보를 조회합니다.
     *
     * @param regRemId 정기송금 ID
     * @param remittanceId 송금 ID
     * @param userId 사용자 ID
     * @return 해당 송금의 상세 정보 ({@link RemittanceHistoryDetailDto})
     */
    @Operation(summary = "정기송금 이력 상세 조회", description = "정기송금 이력 단건 상세를 조회합니다.")
    @GetMapping("/history/{regRemId}/{remittanceId}")
    public ResponseEntity<BaseResponse<RemittanceHistoryDetailDto>> getRemittanceHistoryDetail(
            @PathVariable Long regRemId,
            @PathVariable Long remittanceId,
            @RequestParam("userId") Long userId
    ) {
        return ApiResponseUtil.success(SuccessCode.OK, regularRemittanceService.getRemittanceHistoryDetail(userId, remittanceId, regRemId));
    }

    /**
     * 신규 정기송금을 등록합니다.
     *
     * @param userId 사용자 ID
     * @param dto 정기송금 생성에 필요한 정보를 담은 DTO
     * @return 생성된 정기송금 정보 ({@link RegularRemittanceResponseDto})
     */
    @Operation(summary = "정기송금 생성", description = "신규 정기송금 설정을 생성합니다.")
    @PostMapping("/add")
    public ResponseEntity<BaseResponse<RegularRemittanceResponseDto>> createScheduledRemittance(
            @RequestParam("userId") Long userId,
            @RequestBody RegularRemittanceCreateDto dto
    ) {
        RegularRemittanceResponseDto createdRemittance = regularRemittanceService.createScheduledRemittance(userId, dto);
        return ApiResponseUtil.success(SuccessCode.CREATED, createdRemittance);
    }

    /**
     * 기존 정기 해외송금 설정을 수정합니다.
     *
     * @param regRemId 정기송금 ID
     * @param userId 사용자 ID
     * @param dto 정기송금 수정에 필요한 정보를 담은 DTO
     * @return 성공 응답 (HTTP 200 OK)
     */
    @Operation(summary = "정기송금 수정", description = "기존 정기송금 설정을 수정합니다.")
    @PutMapping("/{regRemId}")
    public ResponseEntity<BaseResponse<Void>> updateScheduledRemittance(
            @PathVariable("regRemId") Long regRemId,
            @RequestParam("userId") Long userId,
            @RequestBody RegularRemittanceUpdateDto dto
    ) {
        regularRemittanceService.updateScheduledRemittance(regRemId, userId, dto);
        return ApiResponseUtil.success(SuccessCode.OK);
    }

    /**
     * 기존 정기 해외송금 설정을 삭제합니다.
     *
     * @param regRemId 정기송금 ID
     * @param userId 사용자 ID
     * @return 성공 응답 (HTTP 200 OK)
     */
    @Operation(summary = "정기송금 삭제", description = "기존 정기송금 설정을 삭제합니다.")
    @DeleteMapping("/{regRemId}")
    public ResponseEntity<BaseResponse<Void>> deleteScheduledRemittance(
            @PathVariable("regRemId") Long regRemId,
            @RequestParam("userId") Long userId
    ) {
        regularRemittanceService.deleteScheduledRemittance(regRemId, userId);
        return ApiResponseUtil.success(SuccessCode.OK);
    }

}
