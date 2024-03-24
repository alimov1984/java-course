package ru.alimov.limitservice.demo.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.UUID;

@Table(name = "limits")
public class Limit {
    @Id
    private Long id;
    private Timestamp rowDate;
    private UUID transactionId;
    private Timestamp transactionDate;
    private Long userId;
    private BigDecimal sum;

    public void setId(Long id) {
        this.id = id;
    }

    public void setRowDate(Timestamp rowDate) {
        this.rowDate = rowDate;
    }

    public void setTransactionId(UUID transactionId) {
        this.transactionId = transactionId;
    }

    public void setTransactionDate(Timestamp transactionDate) {
        this.transactionDate = transactionDate;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public void setSum(BigDecimal sum) {
        this.sum = sum;
    }

    public Long getId() {
        return id;
    }

    public Timestamp getRowDate() {
        return rowDate;
    }

    public UUID getTransactionId() {
        return transactionId;
    }

    public Timestamp getTransactionDate() {
        return transactionDate;
    }

    public Long getUserId() {
        return userId;
    }

    public BigDecimal getSum() {
        return sum;
    }
}
