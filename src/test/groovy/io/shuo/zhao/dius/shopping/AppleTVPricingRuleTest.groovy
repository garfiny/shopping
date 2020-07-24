package io.shuo.zhao.dius.shopping

import spock.lang.Specification
import spock.lang.Subject
import spock.lang.Unroll

import static io.shuo.zhao.dius.shopping.Item.*

class AppleTVPricingRuleTest extends Specification {

    @Subject
    def rule = new AppleTVPricingRule()

    @Unroll
    def "apply apply tv pricing rule - buy 2 get 1 free"() {
        expect:
        rule.apply(tvs.toList()).equals(total)

        where:
        tvs       | total
        [ATV]     |  ATV.unitPrice
        [ATV] * 2 |  ATV.unitPrice.multiply(2)
        [ATV] * 3 |  ATV.unitPrice.multiply(2)
        [ATV] * 4 |  ATV.unitPrice.multiply(2).plus(ATV.unitPrice)
        [ATV] * 5 |  ATV.unitPrice.multiply(4)
        [ATV] * 6 |  ATV.unitPrice.multiply(4)
        [ATV] * 8 |  ATV.unitPrice.multiply(6)
    }

    def "get eligible items for this rule"() {
        expect:
        rule.eligibleItems(inputItems) == eligibleItems

        where:
        inputItems           | eligibleItems
        [ATV, ATV]           |  [ATV, ATV]
        [ATV, MBP, VGA]      |  [ATV]
        [ATV, MBP, VGA, ATV] |  [ATV, ATV]
    }
}
