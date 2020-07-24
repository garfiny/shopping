package io.shuo.zhao.dius.shopping

import spock.lang.Shared
import spock.lang.Specification
import spock.lang.Subject
import spock.lang.Unroll

import static io.shuo.zhao.dius.shopping.Item.*

class CheckoutTest extends Specification {

    @Subject
    def checkout

    @Shared
    def appleTvPricingRule = new AppleTVPricingRule()
    @Shared
    def ipadBulkBuyRule = new IPadBulkBuyPricingRule()
    @Shared
    def freeVgaAdapterRule = new FreeVGAAdapterPricingRule()

    def setup() {
        checkout = new Checkout([appleTvPricingRule, ipadBulkBuyRule, freeVgaAdapterRule])
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

    @Unroll
    def "calculate total prices - basic scenario"() {
        expect:
        items.each { item -> checkout.scan(item) }
        checkout.total().compareTo(total) == 0

        where:
        items                | total
        [ATV]                | ATV.unitPrice
        [ATV, IPD]           | ATV.unitPrice.add(IPD.unitPrice)
        [ATV, IPD, MBP]      | ATV.unitPrice.add(IPD.unitPrice)
                                            .add(MBP.unitPrice)
        [ATV, IPD, MBP, VGA] | ATV.unitPrice.add(IPD.unitPrice)
                                            .add(MBP.unitPrice)
    }

    @Unroll
    def "calculate total prices - discount scenarios"() {
        expect:
        items.each { item -> checkout.scan(item) }
        checkout.total().compareTo(total) == 0

        where:
        items                               | total
        [ATV, ATV, ATV, VGA]                | BigDecimal.valueOf(249.0)
        [ATV, IPD, IPD, ATV, IPD, IPD, IPD] | BigDecimal.valueOf(2718.95)
        [MBP, VGA, IPD]                     | BigDecimal.valueOf(1949.98)
    }

    @Unroll
    def "calculate total price - 3 for 2 apple tv deals"() {
        expect:
        items.each { item -> checkout.scan(item) }
        checkout.total().compareTo(total) == 0

        where:
        items                          | total
        [ATV, ATV, ATV]                | ATV.unitPrice.multiply(2)
        [ATV, ATV, ATV, IPD]           | ATV.unitPrice.multiply(2).add(IPD.unitPrice)
        [ATV, ATV, ATV, ATV, ATV, ATV] | ATV.unitPrice.multiply(4)
    }

    @Unroll
    def "calculate total price - IPad bulk deal"() {
        expect:
        items.each { item -> checkout.scan(item) }
        checkout.total().compareTo(total) == 0

        where:
        items                     | total
        [IPD, IPD, IPD]           | BigDecimal.valueOf(IPD.unitPrice).multiply(3)
        [IPD, IPD, IPD, IPD]      | BigDecimal.valueOf(499.99).multiply(4)
        [IPD, IPD, IPD, IPD, IPD] | BigDecimal.valueOf(499.99).multiply(5)
    }

    @Unroll
    def "calculate total price - free adapter deal"() {
        expect:
        items.each { item -> checkout.scan(item) }
        checkout.total().compareTo(total) == 0

        where:
        items                | total
        [MBP]                | MBP.unitPrice
        [MBP, VGA]           | MBP.unitPrice
        [MBP, VGA, MBP, VGA] | MBP.unitPrice.multiply(2)
        [MBP, VGA, IPD]      | MBP.unitPrice.add(IPD.unitPrice)
    }

    def "calculate total price - multple deals: 3 for 2 ATV and free vga"() {
        given:
        def items = [ATV, ATV, MBP, ATV, VGA]

        when:
        items.each { item -> checkout.scan(item) }

        then:
        def total = ATV.unitPrice.multiply(2).add(MBP.unitPrice)
        checkout.total().compareTo(total) == 0
    }

    def "calculate total price - multple deals: 3 for 2 ATV and bulk ipad"() {
        given:
        def items = [ATV, ATV, ATV, IPD, IPD, IPD, IPD]

        when:
        items.each { item -> checkout.scan(item) }

        then:
        def total = ATV.unitPrice.multiply(2).add(BigDecimal.valueOf(499.99).multiply(4))
        checkout.total().compareTo(total) == 0
    }

    def "calculate total price - multple deals: bulk ipad and free vga"() {
        given:
        def items = [MBP, IPD, IPD, IPD, IPD, VGA]

        when:
        items.each { item -> checkout.scan(item) }

        then:
        def total = BigDecimal.valueOf(MBP.unitPrice).add(BigDecimal.valueOf(499.99).multiply(4))
        checkout.total().compareTo(total) == 0
    }

    def "calculate total price - all three deals"() {
        given:
        def items = [MBP, IPD, IPD, IPD, IPD, VGA, ATV, ATV, ATV]

        when:
        items.each { item -> checkout.scan(item) }

        then:
        def total = BigDecimal.valueOf(ATV.unitPrice).multiply(2)
                              .add(BigDecimal.valueOf(499.99).multiply(4))
                              .add(MBP.unitPrice)
        checkout.total().compareTo(total) == 0
    }
}
