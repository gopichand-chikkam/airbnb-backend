package com.gopi.airbnb.pricing.pricingStrategy;

import com.gopi.airbnb.dto.functionalDto.PricingDetails;
import com.gopi.airbnb.pricing.PricingStrategy;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.DayOfWeek;

@Component

public class HolidayPricingStrategy implements PricingStrategy {
    @Override
    public Boolean isApplicable(PricingDetails pricingDetails) {
        return pricingDetails.holiday()!=null;
    }

    @Override
    public BigDecimal apply(BigDecimal currentPrice, PricingDetails pricingDetails) {

        if(pricingDetails.holiday())return currentPrice.multiply(BigDecimal.valueOf(1.20));
        return currentPrice;

    }
}
