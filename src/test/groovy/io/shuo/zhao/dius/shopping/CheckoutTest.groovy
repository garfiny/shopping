package io.shuo.zhao.dius.shopping

import spock.lang.Specification
import spock.lang.Subject

import static io.shuo.zhao.dius.shopping.Item.*

class CheckoutTest extends Specification {

    @Subject
    def checkout

    def setup() {
        checkout = new Checkout()
    }

    def "Receive price rules when initiate checkout"() {
        when:
        def checkout = new Checkout(new ArrayList<PricingRule>())

        then:
        notThrown Exception
    }

    def "Handle null pricing rules input"() {
        when:
        def checkout = new Checkout(null)

        then:
        notThrown Exception
    }

    def "Receive empty pricing rules"() {
        when:
        def checkout = new Checkout(new ArrayList<PricingRule>())

        then:
        notThrown Exception
    }

    def "Scan items into the system"() {
        when:
        items.each { item ->
            checkout.scan(item)
        }

        then:
        notThrown Exception

        where:
        items << [ATV, IPD, MBP]
    }

    def "Allow scan null value"() {
        when:
        checkout.scan(null)

        then:
        notThrown Exception
    }

    def "calculate total prices - basic scenario"() {
        expect:
        items.each { item -> checkout.scan(item) }
        checkout.total() == total

        where:
        items                                    | total
        [ATV]                | ATV.unitPrice
        [ATV, IPD]           | ATV.unitPrice.plus(IPD.unitPrice)
        [ATV, IPD, MBP]      | ATV.unitPrice.plus(IPD.unitPrice)
                                            .plus(MBP.unitPrice)
        [ATV, IPD, MBP, VGA] | ATV.unitPrice.plus(IPD.unitPrice)
                                            .plus(MBP.unitPrice)
                                            .plus(VGA.unitPrice)
    }

    def "calculate total prices - discount scenarios"() {
        expect:
        items.each { item -> checkout.scan(item) }
        checkout.total() == total

        where:
        items                               | total
        [ATV, ATV, ATV, VGA]                | BigDecimal.valueOf(249.0)
        [ATV, IPD, IPD, ATV, IPD, IPD, IPD] | BigDecimal.valueOf(2718.95)
        [MBP, VGA, IPD]                     | BigDecimal.valueOf(1949.98)
    }

    def "calculate total price - 3 for 2 apple tv deals"() {
        expect:
        items.each { item -> checkout.scan(item) }
        checkout.total() == total

        where:
        items                          | total
        [ATV, ATV, ATV]                | BigDecimal.valueOf(ATV.unitPrice).multiply(2)
        [ATV, ATV, ATV, IPD]           | BigDecimal.valueOf(ATV.unitPrice).multiply(2).plus(IPD.unitPrice)
        [ATV, ATV, ATV, ATV, ATV, ATV] | BigDecimal.valueOf(ATV.unitPrice).multiply(4)
    }

    def "calculate total price - IPad bulk deal"() {
        expect:
        items.each { item -> checkout.scan(item) }
        checkout.total() == total

        where:
        items                          | total
        [IPAD, IPAD, IPAD]             | BigDecimal.valueOf(IPAD.unitPrice).multiply(3)
        [IPAD, IPAD, IPAD, IPAD]       | BigDecimal.valueOf(499.99).multiply(4)
        [IPAD, IPAD, IPAD, IPAD, IPAD] | BigDecimal.valueOf(499.99).multiply(5)
    }

    def "calculate total price - free adapter deal"() {
        expect:
        items.each { item -> checkout.scan(item) }
        checkout.total() == total

        where:
        items                | total
        [MBP]                | BigDecimal.valueOf(IPD.unitPrice).multiply(3)
        [MBP, VGA]           | BigDecimal.valueOf(MBP.unitPrice)
        [MBP, VGA, MBP, VGA] | BigDecimal.valueOf(MBP.unitPrice).multiply(2)
        [MBP, VGA, IPD]      | BigDecimal.valueOf(MBP.unitPrice).plus(IPD.unitPrice)
    }
}
