package com.gopi.airbnb.dto.requests;

import com.gopi.airbnb.entitys.User;

import java.math.BigDecimal;

public record ThirdPartyPaymentRequest(Long merchantOrderId, BigDecimal amount, Long UserId, String email) {
}
