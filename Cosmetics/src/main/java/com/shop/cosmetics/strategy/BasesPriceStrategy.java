package com.shop.cosmetics.strategy;

import com.shop.cosmetics.entity.Product;

import java.math.BigDecimal;
import java.time.LocalDate;

public class BasesPriceStrategy implements PricingStrategy {
    @Override
    public double calculatePrice(double price) {
        return price;
    }
}