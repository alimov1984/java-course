package ru.alimov.limitservice.demo.dto;

import java.math.BigDecimal;
import java.util.UUID;

public class LimitReservationRequestDto {
    private UUID transactionId;
    private BigDecimal sum;

    public void setTransactionId(UUID transactionId) {
        this.transactionId = transactionId;
    }

    public void setSum(BigDecimal sum) {
        this.sum = sum;
    }

    public UUID getTransactionId() {
        return transactionId;
    }

    public BigDecimal getSum() {
        return sum;
    }

}
