package com.gopi.airbnb.pricing.pricingStrategy;

import com.gopi.airbnb.dto.functionalDto.PricingDetails;
import com.gopi.airbnb.pricing.PricingStrategy;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.Month;

@Component
public class SeasonalPricingStrategy implements PricingStrategy {
    @Override
    public Boolean isApplicable(PricingDetails pricingDetails) {
        return pricingDetails.checkIn() != null;
    }

    @Override
    public BigDecimal apply(BigDecimal currentPrice, PricingDetails pricingDetails) {
        Month month = pricingDetails.checkIn().getMonth();

        if (month == Month.APRIL ||
                month == Month.MAY ||
                month == Month.DECEMBER) {

            return currentPrice.multiply(BigDecimal.valueOf(1.25));
        }

        if (month == Month.JUNE ||
                month == Month.JULY) {

            return currentPrice.multiply(BigDecimal.valueOf(0.90));
        }

        return currentPrice;
    }
}
