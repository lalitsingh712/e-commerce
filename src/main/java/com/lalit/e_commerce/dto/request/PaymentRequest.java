package com.lalit.e_commerce.dto.request;

import com.lalit.e_commerce.entity.PaymentMethod;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NonNull;

@Data
public class PaymentRequest {

    @NotNull(message = "Payment method is required")
    private PaymentMethod paymentMethod;

}
