package com.gopi.airbnb.pricing.pricingStrategy;

import com.gopi.airbnb.dto.functionalDto.PricingDetails;
import com.gopi.airbnb.pricing.PricingStrategy;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.chrono.ChronoLocalDate;
import java.time.temporal.ChronoUnit;


@Component
public class UrgencyPricingStrategy implements PricingStrategy {
    @Override
    public Boolean isApplicable(PricingDetails pricingDetails) {
        return pricingDetails.checkIn()!=null && pricingDetails.checkOut()!=null && pricingDetails.checkIn().isBefore(pricingDetails.checkOut());
    }

    @Override
    public BigDecimal apply(BigDecimal currentPrice, PricingDetails pricingDetails) {
        long days= ChronoUnit.DAYS.between(LocalDate.now(),pricingDetails.checkIn());
        if(days<2)return currentPrice.multiply(BigDecimal.valueOf(1.20));
        if(days<4)return currentPrice.multiply(BigDecimal.valueOf(1.10));
        if(days<=7)return currentPrice.multiply(BigDecimal.valueOf(1.05));
        return currentPrice;
    }
}
