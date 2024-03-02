package ru.alimov.paymentservice.demo.dto;


import java.math.BigDecimal;

public class ProductDto {
    private Long id;
    private String accountNumber;
    private BigDecimal balance;
    private String type;

    public void setId(Long id) {
        this.id = id;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }

    public void setType(String type) {
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

    public String getType() {
        return type;
    }


    @Override
    public String toString() {
        return String.format("{id=%s, accountNumber=%s,balance=%s,type=%s}\n",
                this.id, this.accountNumber, this.balance, this.type);
    }
}
