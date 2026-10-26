package com.gopi.airbnb.pricing;

import com.gopi.airbnb.dto.functionalDto.PricingDetails;

import java.math.BigDecimal;

public interface PricingStrategy {
    Boolean isApplicable(PricingDetails pricingDetails);
    BigDecimal apply(BigDecimal currentPrice, PricingDetails pricingDetails);
}
