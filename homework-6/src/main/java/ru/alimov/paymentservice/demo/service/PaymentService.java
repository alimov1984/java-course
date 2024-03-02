package ru.alimov.paymentservice.demo.service;

import ru.alimov.paymentservice.demo.dto.PaymentDto;

public interface PaymentService {
    void executePayment(PaymentDto paymentDto);
}
