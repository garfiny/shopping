package io.shuo.zhao.dius.shopping;

import java.math.BigDecimal;
import java.util.List;

public interface PricingRule {
    BigDecimal apply(List<Item> items);

    List<Item> eligibleItems(List<Item> items);
}
