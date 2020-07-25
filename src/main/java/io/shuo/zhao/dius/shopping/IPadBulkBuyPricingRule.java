package io.shuo.zhao.dius.shopping;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

import static io.shuo.zhao.dius.shopping.Item.IPD;

public class IPadBulkBuyPricingRule implements PricingRule {

    public static final BigDecimal DISCOUNT_PRICE = BigDecimal.valueOf(499.99);
    public static final int BULK_BUY_NUM = 4;

    @Override
    public BigDecimal apply(List<Item> items) {
        List<Item> eligibleItems = eligibleItems(items);
        if (eligibleItems.size() >= BULK_BUY_NUM) {
            return DISCOUNT_PRICE.multiply(BigDecimal.valueOf(items.size()));
        } else {
            return IPD.getUnitPrice().multiply(BigDecimal.valueOf(items.size()));
        }
    }

    @Override
    public List<Item> eligibleItems(List<Item> items) {
        return items.stream()
                .filter(item -> item.equals(IPD))
                .collect(Collectors.toList());
    }
}
