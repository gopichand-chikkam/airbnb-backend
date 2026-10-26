package com.gopi.airbnb.pricing.pricingStrategy;


import com.gopi.airbnb.dto.functionalDto.PricingDetails;
import com.gopi.airbnb.pricing.PricingStrategy;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
@Component
public class OccupancyPricingStrategy implements PricingStrategy {
    @Override
    public Boolean isApplicable(PricingDetails pricingDetails) {
        return pricingDetails!=null && pricingDetails.totalRooms()>1;
    }

    @Override
    public BigDecimal apply(BigDecimal currentPrice, PricingDetails pricingDetails) {
        double occupancyPercentage= (double) pricingDetails.bookedRooms() /pricingDetails.totalRooms();

        if(occupancyPercentage>0.90)return currentPrice.multiply(BigDecimal.valueOf(1.30));
        if(occupancyPercentage>0.70)return currentPrice.multiply(BigDecimal.valueOf(1.20));
        if(occupancyPercentage>0.50)return currentPrice.multiply(BigDecimal.valueOf(1.10));
        return currentPrice;
    }
}
