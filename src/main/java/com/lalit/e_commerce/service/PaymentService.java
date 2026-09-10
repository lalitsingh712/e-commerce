package com.lalit.e_commerce.service;

import com.lalit.e_commerce.dto.request.PaymentRequest;
import com.lalit.e_commerce.entity.Payment;

public interface PaymentService {

    Payment makePayment(Long orderId, PaymentRequest request);

    Payment getPaymentByOrderId(Long orderId);
}
