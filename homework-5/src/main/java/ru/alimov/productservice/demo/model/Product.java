package ru.alimov.productservice.demo.model;

import java.math.BigDecimal;

public class Product {
    private Long id;
    private String accountNumber;
    private BigDecimal balance;
    private ProductType type;
    private Long userId;

    public void setId(Long id) {
        this.id = id;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }

    public void setType(ProductType type) {
        this.type = type;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getId() {
        return id;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public ProductType getType() {
        return type;
    }

    public Long getUserId() {
        return userId;
    }

    @Override
    public String toString() {
        return String.format("{id=%s, accountNumber=%s,balance=%s,type=%s,userId=%s }\n",
                this.id, this.accountNumber, this.balance, this.type.name(), this.userId);
    }
}
