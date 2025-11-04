package org.creditto.core_banking.domain.regularremittance.entity;

import lombok.Getter;

@Getter
public enum RegRemStatus {
    ACTIVE("정상"),
    PAUSED("일시중지"),
    CANCELLED("취소");

    private final String status;

    RegRemStatus(String status){
        this.status = status;
    }
}
