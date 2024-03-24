package ru.alimov.limitservice.demo.dto;

import java.util.UUID;

public class LimitReservationResponseDto {
    private UUID transactionId;
    private Boolean limitReservationIsSuccess;

    public void setTransactionId(UUID transactionId) {
        this.transactionId = transactionId;
    }

    public void setLimitReservationIsSuccess(Boolean limitReservationIsSuccess) {
        this.limitReservationIsSuccess = limitReservationIsSuccess;
    }

    public UUID getTransactionId() {
        return transactionId;
    }

    public Boolean getLimitReservationIsSuccess() {
        return limitReservationIsSuccess;
    }

}
