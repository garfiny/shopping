package io.shuo.zhao.dius.shopping;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

import static io.shuo.zhao.dius.shopping.Item.ATV;

public class AppleTVPricingRule implements PricingRule {

    @Override
    public BigDecimal apply(List<Item> items) {
        List<Item> appleTvItems =
                items.stream()
                        .filter(item -> item.equals(ATV))
                        .collect(Collectors.toList());
        int numOfTvs = appleTvItems.size();
        int numOfTvsForCharging = (numOfTvs / 3) * 2 + (numOfTvs % 3);
        return ATV.getUnitPrice().multiply(BigDecimal.valueOf(numOfTvsForCharging));
    }
}
