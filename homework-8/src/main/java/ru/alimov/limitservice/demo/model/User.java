package ru.alimov.limitservice.demo.model;


import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import java.math.BigDecimal;

@Table(name = "users")
public class User {
    @Id
    private Long id;
    private Boolean isLocked;
    private BigDecimal maxDailyLimit;

    public void setId(Long id) {
        this.id = id;
    }

    public void setLocked(Boolean locked) {
        isLocked = locked;
    }

    public Long getId() {
        return id;
    }

    public Boolean getLocked() {
        return isLocked;
    }

    public BigDecimal getMaxDailyLimit() {
        return maxDailyLimit;
    }
    public void setMaxDailyLimit(BigDecimal maxDailyLimit) {
        this.maxDailyLimit = maxDailyLimit;
    }
}
