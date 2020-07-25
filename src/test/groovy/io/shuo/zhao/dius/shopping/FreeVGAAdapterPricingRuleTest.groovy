package io.shuo.zhao.dius.shopping

import spock.lang.Specification
import spock.lang.Subject
import spock.lang.Unroll

import static io.shuo.zhao.dius.shopping.Item.*

class FreeVGAAdapterPricingRuleTest extends Specification {

    @Subject
    def rule = new FreeVGAAdapterPricingRule()

    @Unroll
    def "apply free vga adapter pricing rule - buy mac pro get 1 vga adapter free"() {
        expect:
        rule.apply(items.toList()).compareTo(total) == 0

        where:
        items                | total
        [MBP]                | MBP.unitPrice
        [VGA]                | VGA.unitPrice
        [MBP, VGA]           | MBP.unitPrice
        [MBP, VGA, VGA]      | MBP.unitPrice.plus(VGA.unitPrice)
        [MBP, VGA, MBP]      | MBP.unitPrice.multiply(2)
        [MBP, VGA, MBP, VGA] | MBP.unitPrice.multiply(2)
    }

    def "get eligible items for this rule"() {
        expect:
        rule.eligibleItems(inputItems).containsAll(eligibleItems)

        where:
        inputItems           | eligibleItems
        [VGA, MBP]           |  [VGA, MBP]
        [VGA, MBP, ATV]      |  [VGA, MBP]
        [VGA, MBP, ATV, VGA] |  [VGA, MBP, VGA]
        [VGA, IPD, MBP, ATV] |  [VGA, MBP]
    }
}
