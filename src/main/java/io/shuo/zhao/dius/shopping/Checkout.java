package io.shuo.zhao.dius.shopping;

import java.math.BigDecimal;
import java.util.LinkedList;
import java.util.List;

public class Checkout {

    private List<PricingRule> pricingRules;
    private List<Item> items;

    public Checkout(List<PricingRule> pricingRules) {
        this.pricingRules = pricingRules;
        this.items = new LinkedList<>();
    }

    public void scan(Item item) {

    }

    public BigDecimal total() {
        return BigDecimal.ZERO;
    }
}
