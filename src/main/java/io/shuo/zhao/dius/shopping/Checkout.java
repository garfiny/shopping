package io.shuo.zhao.dius.shopping;

import java.math.BigDecimal;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

public class Checkout {

    private List<PricingRule> pricingRules;
    private List<Item> items;

    public Checkout(List<PricingRule> pricingRules) {
        this.pricingRules = pricingRules;
        this.items = new LinkedList<>();
    }

    public void scan(Item item) {
        if (item != null) {
            items.add(item);
        }
    }

    public BigDecimal total() {
        return pricingRules.stream().map(rule -> rule.apply(this.items))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
}
