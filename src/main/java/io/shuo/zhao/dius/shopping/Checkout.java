package io.shuo.zhao.dius.shopping;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class Checkout {

    private List<PricingRule> pricingRules;
    private List<Item> items;

    public Checkout(List<PricingRule> pricingRules) {
        this.pricingRules = pricingRules == null ? new ArrayList<>() : pricingRules;
        this.items = new LinkedList<>();
    }

    public void scan(Item item) {
        if (item != null) {
            items.add(item);
        }
    }

    public BigDecimal total() {
        return pricingRules.stream()
                .map(rule -> {
                    List<Item> eligibleItems = rule.eligibleItems(this.items);
                    return rule.apply(eligibleItems);
                })
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
}
