package io.shuo.zhao.dius.shopping

import spock.lang.Specification
import spock.lang.Subject

import static io.shuo.zhao.dius.shopping.Item.*

class IPadBulkBuyPricingRuleTest extends Specification {

    @Subject
    def rule = new IPadBulkBuyPricingRule()

    def "eligible items"() {
        expect:
        rule.eligibleItems(inputItems) == eligibleItems

        where:
        inputItems           | eligibleItems
        [IPD] * 3            |  [IPD, IPD, IPD]
        [IPD, ATV, MBP, IPD] |  [IPD, IPD]
    }

    def "apply ipad bulk buy pricing rule - buy 4 get lower price"() {
        expect:
        rule.apply(items.toList()).compareTo(total) == 0

        where:
        items       | total
        [IPD] * 3   |  IPD.unitPrice.multiply(3)
        [IPD] * 4   |  IPadBulkBuyPricingRule.DISCOUNT_PRICE.multiply(4)
        [IPD] * 7   |  IPadBulkBuyPricingRule.DISCOUNT_PRICE.multiply(7)
    }
}
