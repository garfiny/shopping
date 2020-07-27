## Requirement

You can find the original requirement document from the link below:
https://github.com/DiUS/coding-tests/blob/master/dius_shopping.md


## How to build
This app uses gradle as the build tool. The project includes gradle wrapper so that 
you don't need to install it on your local machine to run it.

- To build the package, under the project root
```shell script
./gradlew build
```

- To run the tests, under the project root
```shell script
./gradlew test
```

## Run example App

The example application defined in App.java
You can simply run:
```shell script
./gradlew run
```
And it will show you the result like this: <br>
Checkout Example 1:<br>
Scanned Items: , Apple TV, Apple TV, Apple TV, VGA adapter <br>
Total Price: $249.00 <br>

Checkout Example 2:<br>
Scanned Items: , Apple TV, Super iPad, Super iPad, Apple TV, Super iPad, Super iPad, Super iPad<br>
Total Price: $2718.95<br>

Checkout Example 3:<br>
Scanned Items: , MacBook Pro, VGA adapter, Super iPad<br>
Total Price: $1949.98<br>

## API Interface

Checkout class is the main class of the checkout system

```java
Checkout co = new Checkout(Arrays.asList(PRICING_RULES));
Arrays.stream(items).forEach(co::scan);
return co.total();
```

The PRICING_RULES defined as an Item array which contains the all applying 
pricing rules implementations.

Pricing Rule defined as an interface: <br>
- The apply method applying the rule to the scanned items
- the eligibleItems method returns all the items can be applied to the rule
```java
public interface PricingRule {
    BigDecimal apply(List<Item> items);

    List<Item> eligibleItems(List<Item> items);
}
```
There are current 3 pricing rule implementations according to the requirement.

Item class defines the scanned item name and price as an enum:
```java

public enum Item {
    IPD("Super iPad", BigDecimal.valueOf(549.99)),
    MBP("MacBook Pro", BigDecimal.valueOf(1399.99)),
    ATV("Apple TV", BigDecimal.valueOf(109.50)),
    VGA("VGA adapter", BigDecimal.valueOf(30.0));
}
```

## Some Notes

- you can implement different PricingRule to apply discount at different stage.
- Item currently defined as an Enum because it satisfies the current stage requirement.
- If dynamic item types and prices need to be supported we introduce configuration 
files to define the item's metadata.
- I choose spock as my testing framework because I like its flexiblity and the 
data driven testing concept. 
