package com.gopi.airbnb.pricing.pricingStrategy;

import com.gopi.airbnb.dto.functionalDto.PricingDetails;
import com.gopi.airbnb.pricing.PricingStrategy;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.DayOfWeek;
@Component
public class WeekendPricingStrategy implements PricingStrategy {
    @Override
    public Boolean isApplicable(PricingDetails pricingDetails) {
        return pricingDetails.checkIn()!=null;
    }

    @Override
    public BigDecimal apply(BigDecimal currentPrice, PricingDetails pricingDetails) {
        DayOfWeek day= pricingDetails.checkIn().getDayOfWeek();

        if(day==DayOfWeek.FRIDAY)return currentPrice.multiply(BigDecimal.valueOf(1.10));
        if(day==DayOfWeek.SATURDAY)return currentPrice.multiply(BigDecimal.valueOf(1.20));
        if(day==DayOfWeek.SUNDAY)return currentPrice.multiply(BigDecimal.valueOf(1.05));
        return currentPrice;
    }
}
