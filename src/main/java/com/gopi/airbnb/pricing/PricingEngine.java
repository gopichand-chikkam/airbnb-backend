package com.gopi.airbnb.pricing;

import com.gopi.airbnb.dto.functionalDto.PricingDetails;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;

@Component
public class PricingEngine {

    private final List<PricingStrategy> pricingStrategyList;

    public PricingEngine(List<PricingStrategy> pricingStrategyList) {
        this.pricingStrategyList = pricingStrategyList;
    }

    public BigDecimal calculatePrice(PricingDetails pricingDetails){
        BigDecimal currentPrice= pricingDetails.basePrice();
        for(PricingStrategy pricingStrategy:pricingStrategyList){
          if(pricingStrategy.isApplicable(pricingDetails))  currentPrice= pricingStrategy.apply(currentPrice,pricingDetails);
        }
        return currentPrice;
    }
}
