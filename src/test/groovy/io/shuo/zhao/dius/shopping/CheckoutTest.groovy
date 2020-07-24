package io.shuo.zhao.dius.shopping

import spock.lang.Specification
import spock.lang.Subject

import static io.shuo.zhao.dius.shopping.Item.*
import static io.shuo.zhao.dius.shopping.Item.IPD

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
}
