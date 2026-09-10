package com.lalit.e_commerce.mapper;

import com.lalit.e_commerce.dto.response.PaymentResponse;
import com.lalit.e_commerce.entity.Payment;
import com.lalit.e_commerce.entity.PaymentStatus;
import org.springframework.stereotype.Component;

@Component
public class PaymentMapper {

    public PaymentResponse toResponse(Payment payment){

        PaymentResponse response=new PaymentResponse();

        response.setId(payment.getId());
        response.setAmount(payment.getAmount());
        response.setOrderId(payment.getOrder().getId());
        response.setPaymentStatus(payment.getPaymentStatus().toString());
        response.setPaymentMethod(payment.getPaymentMethod().toString());
        response.setPaymentDate(payment.getPaymentDate());
        response.setTransactionId(payment.getTransactionId());

        return response;
    }
}
