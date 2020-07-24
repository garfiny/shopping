package io.shuo.zhao.dius.shopping;

import java.math.BigDecimal;

public enum Item {

    IPD("Super iPad", BigDecimal.valueOf(549.99)),
    MBP("MacBook Pro", BigDecimal.valueOf(1399.99)),
    ATV("Apple TV", BigDecimal.valueOf(109.50)),
    VGA("VGA adapter", BigDecimal.valueOf(30.0));

    private final String description;
    private final BigDecimal unitPrice;

    Item(String description, BigDecimal unitPrice) {
        this.description = description;
        this.unitPrice = unitPrice;
    }

    public String getDescription() {
        return description;
    }

    public BigDecimal getUnitPrice() {
        return unitPrice;
    }
}
