package io.shuo.zhao.dius.shopping;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static io.shuo.zhao.dius.shopping.Item.*;

public class FreeVGAAdapterPricingRule implements PricingRule {

    @Override
    public BigDecimal apply(List<Item> items) {
        List<Item> macBookPros = getMacBookProItems(items);
        List<Item> vgaAdapters = getVgaAdapterItems(items);

        int numOfMacBookPros = macBookPros.size();
        int numOfVgaAdapter = vgaAdapters.size();

        return totalPriceOfMacBookPro(numOfMacBookPros)
                .add(totalPriceOfAdaptorNeedToBePaid(numOfMacBookPros, numOfVgaAdapter));
    }

    private BigDecimal totalPriceOfMacBookPro(int numOfMacBookPros) {
        return MBP.getUnitPrice().multiply(BigDecimal.valueOf(numOfMacBookPros));
    }

    private BigDecimal totalPriceOfAdaptorNeedToBePaid(int numOfMacBookPros, int numOfVgaAdapter) {
        int diffOfAdapterAndMacBookPros = numOfVgaAdapter - numOfMacBookPros;
        int numOfAdapterNeedToBePaid = diffOfAdapterAndMacBookPros > 0 ? diffOfAdapterAndMacBookPros : 0;
        return VGA.getUnitPrice().multiply(BigDecimal.valueOf(numOfAdapterNeedToBePaid));
    }

    @Override
    public List<Item> eligibleItems(List<Item> items) {
        return Stream.concat(getMacBookProItems(items).stream(),
                             getVgaAdapterItems(items).stream())
                     .collect(Collectors.toList());
    }

    private List<Item> getMacBookProItems(List<Item> items) {
        return items.stream()
                .filter(item -> item.equals(MBP))
                .collect(Collectors.toList());
    }

    private List<Item> getVgaAdapterItems(List<Item> items) {
        return items.stream()
                .filter(item -> item.equals(VGA))
                .collect(Collectors.toList());
    }
}
