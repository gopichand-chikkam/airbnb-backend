package com.gopi.airbnb.pricing.pricingStrategy;

import com.gopi.airbnb.dto.functionalDto.PricingDetails;
import com.gopi.airbnb.pricing.PricingStrategy;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class BasePricingStrategy implements PricingStrategy {
    @Override
    public Boolean isApplicable(PricingDetails pricingDetails) {
        return pricingDetails!=null;
    }

    @Override
    public BigDecimal apply(BigDecimal currentPrice, PricingDetails pricingDetails) {
        return pricingDetails.basePrice();
    }
}
