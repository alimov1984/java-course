package ru.alimov.productservice.demo.dto;

import ru.alimov.productservice.demo.model.ProductType;

import java.math.BigDecimal;

public class ProductDto {
    private Long id;
    private String accountNumber;
    private BigDecimal balance;
    private ProductType type;

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


    @Override
    public String toString() {
        return String.format("{id=%s, accountNumber=%s,balance=%s,type=%s}\n",
                this.id, this.accountNumber, this.balance, this.type.name());
    }
}
