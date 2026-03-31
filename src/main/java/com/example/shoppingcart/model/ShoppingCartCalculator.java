package com.example.shoppingcart.model;


import java.util.List;

public class ShoppingCartCalculator {

    public double calculateItemTotal(double price, int quantity) {
        return price * quantity;
    }

    public double calculateCartTotal(List<CartItem> items) {
        return items.stream()
                .mapToDouble(CartItem::getTotal)
                .sum();
    }
}