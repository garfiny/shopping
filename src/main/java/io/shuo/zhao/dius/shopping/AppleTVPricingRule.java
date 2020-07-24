package io.shuo.zhao.dius.shopping;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

import static io.shuo.zhao.dius.shopping.Item.ATV;

public class AppleTVPricingRule implements PricingRule {

    public static final int LEAST_CHARGING_NUM = 2;
    public static final int FREE_TV_NUM = 1;

    @Override
    public BigDecimal apply(List<Item> items) {
        List<Item> appleTvItems = eligibleItems(items);
        int numOfTvs = appleTvItems.size();
        int chargeGroupSize = LEAST_CHARGING_NUM + FREE_TV_NUM;
        int numOfTvsForCharging =
                (numOfTvs / chargeGroupSize) * LEAST_CHARGING_NUM + (numOfTvs % chargeGroupSize);
        return ATV.getUnitPrice().multiply(BigDecimal.valueOf(numOfTvsForCharging));
    }

    @Override
    public List<Item> eligibleItems(List<Item> items) {
        return items.stream()
                .filter(item -> item.equals(ATV))
                .collect(Collectors.toList());
    }
}
