package io.shuo.zhao.dius.shopping;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public class Checkout {

    private List<PricingRule> pricingRules;
    private List<Item> items;
    private List<Item> itemsNotEligibleToPricingRules;

    public Checkout(List<PricingRule> pricingRules) {
        this.pricingRules = pricingRules == null ? new ArrayList<>() : pricingRules;
        this.items = new LinkedList<>();
        this.itemsNotEligibleToPricingRules = new LinkedList<>();
    }

    public void scan(Item item) {
        if (item == null) {
            return;
        }
        if (eligibleForPricingRule(Arrays.asList(item))) {
            items.add(item);
        } else {
            itemsNotEligibleToPricingRules.add(item);
        }
    }

    private boolean eligibleForPricingRule(List<Item> items) {
        return pricingRules.stream()
                .anyMatch(pricingRule -> pricingRule.eligibleItems(items).size() > 0);
    }

    public BigDecimal total() {
        BigDecimal totalPriceAppliedWithRules =
                pricingRules
                        .stream()
                        .map(rule -> {
                            List<Item> eligibleItems = rule.eligibleItems(this.items);
                            return rule.apply(eligibleItems);
                        })
                        .reduce(BigDecimal.ZERO, BigDecimal::add);
        BigDecimal totalPriceWithoutPricingRules =
                itemsNotEligibleToPricingRules
                        .stream()
                        .map(item -> item.getUnitPrice())
                        .reduce(BigDecimal.ZERO, BigDecimal::add);
        return totalPriceAppliedWithRules.add(totalPriceWithoutPricingRules);

    }
}
