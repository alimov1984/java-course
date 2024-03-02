package ru.alimov.paymentservice.demo.integration.dto;

public class ProductErrorDto {
    private String code;
    private String message;

    public String getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public void setMessage(String message) {
        this.message = message;
    }

}
