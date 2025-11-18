package com.shop.cosmetics.strategy;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import com.shop.cosmetics.entity.Product;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class HolidayPriceStrategy implements PricingStrategy {
    private final PricingStrategy wrapped;
    private static final LocalDate today = LocalDate.now();
    private final List<LocalDate> holidays = List.of(
            LocalDate.of(2025, 12, 25),
            LocalDate.of(2025, 10, 20),
            LocalDate.of(2025, 12, 30),
            LocalDate.of(2025, 9, 2)
    );

    @Override
    public double calculatePrice(double baseprice) {
        double price = wrapped.calculatePrice(baseprice);
        if (holidays.contains(today)) {
            price*=0.9;
        }
        return price;
    }
}