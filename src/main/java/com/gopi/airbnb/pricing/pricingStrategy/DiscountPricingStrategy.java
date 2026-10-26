package com.gopi.airbnb.Services.Pricing;

import com.gopi.airbnb.dto.functionalDto.PricingDetails;
import com.gopi.airbnb.pricing.PricingStrategy;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class DiscountPricingStrategy implements PricingStrategy {


    @Override
    public Boolean isApplicable(PricingDetails pricingDetails) {
        return pricingDetails.discountActive();
    }

    @Override
    public BigDecimal apply(BigDecimal currentPrice, PricingDetails pricingDetails) {
        return currentPrice.multiply(BigDecimal.valueOf(0.90));
    }
}