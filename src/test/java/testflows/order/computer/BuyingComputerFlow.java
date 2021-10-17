package testflows.order.computer;

import models.components.product.ComputerEssentialComponent;
import models.pages.ItemDetailsPage;
import models.pages.cart.ShoppingCartPage;
import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import testdata.purchasing.ComputerDataObject;
import testdata.purchasing.ComputerSpec;

import java.lang.reflect.InvocationTargetException;

public class BuyingComputerFlow<T extends ComputerEssentialComponent> implements ComputerPriceType{

    private final WebDriver driver;
    private T essentialCompGeneric;

    public BuyingComputerFlow(WebDriver driver) {
        this.driver = driver;
    }

    public BuyingComputerFlow<T> withComputerEssentialComp(Class<T> computerType) {
        try {
            essentialCompGeneric = computerType.getConstructor(WebDriver.class).newInstance(driver);
        } catch (InstantiationException | InvocationTargetException | NoSuchMethodException | IllegalAccessException e) {
            throw new IllegalArgumentException(e.getMessage());
        }
        return this;
    }

    public void buildComputer(ComputerDataObject compData) {
        if (essentialCompGeneric == null) {
            throw new RuntimeException("Please call withComputerType to specify computer type!");
        }
        essentialCompGeneric.unSelectAllDefaultOption();
        ItemDetailsPage detailsPage = new ItemDetailsPage(driver);

        // Build Comp specs
        essentialCompGeneric.selectProcessorType(compData.getProcessorType());
        essentialCompGeneric.selectRAM(compData.getRam());
        essentialCompGeneric.selectHDD(compData.getHdd());

        // Add To cart
        essentialCompGeneric.clickOnAddToCartBtn();
        try {
            detailsPage.waitUntilItemAddedToCart();
        } catch (Exception e) {
            throw new Error("[ERR] Item is not added after 15s!");
        }
    }

    public void verifyComputerAdded(ComputerDataObject simpleComputer, double startPrice) {
        ShoppingCartPage shoppingCartPage = new ShoppingCartPage(driver);
//
//        // TODO: need to handle this price
//        final double fixedPrice = 800.0;

        // Get additional fee
        double additionalFees = 0.0;
        additionalFees += ComputerSpec.valueOf(simpleComputer.getProcessorType()).additionPrice();
        additionalFees += ComputerSpec.valueOf(simpleComputer.getRam()).additionPrice();
        additionalFees += ComputerSpec.valueOf(simpleComputer.getHdd()).additionPrice();

        // Get Total current price for computer
        double currentCompPrice = startPrice + additionalFees;

        //Print all price list for the items
        double subTotalValue = shoppingCartPage.cartFooterComponent().getCartTotalComponent().priceMap().get(subTotal);
        double shippingValue = shoppingCartPage.cartFooterComponent().getCartTotalComponent().priceMap().get(shipping);
        double taxValue = shoppingCartPage.cartFooterComponent().getCartTotalComponent().priceMap().get(tax);
        System.out.println(subTotalValue);
        System.out.println(shippingValue);
        System.out.println(taxValue);

        // Compare
        double itemTotalPrice = subTotalValue + shippingValue + taxValue;
        Assert.assertEquals(itemTotalPrice, currentCompPrice, "[ERR] Total price is not correct!");

        shoppingCartPage.cartFooterComponent().getCartTotalComponent().tosCheckBox().click();
        shoppingCartPage.cartFooterComponent().getCartTotalComponent().checkoutBtn().click();
    }
}
