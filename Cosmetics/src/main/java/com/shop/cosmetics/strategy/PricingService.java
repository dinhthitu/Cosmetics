package com.shop.cosmetics.strategy;

import com.shop.cosmetics.entity.Product;
import org.springframework.stereotype.Service;


@Service
public class PricingService {
    public double calculateDynamicPrice(double price) {
        PricingStrategy pricingStrategy = new BasesPriceStrategy();
        pricingStrategy = new HolidayPriceStrategy(pricingStrategy);
        return pricingStrategy.calculatePrice(price);
    }

    public double calculatePaymentPrice(double orderTotal) {
        double price = orderTotal;
        if(orderTotal >= 200000){
            price = orderTotal;
        } else{
            price += 30000;
        }
        if(orderTotal >= 1000000){
            price -= price *0.25;
        }
        return price;
    }
}