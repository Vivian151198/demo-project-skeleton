package testflows.order.computer;

import com.sun.source.tree.AssertTree;
import models.components.cart.AbstractCartComponent;
import models.components.cart.CartComponent;
import models.components.cart.SummaryCartComponent;
import models.components.checkout.*;
import models.components.product.computers.ComputerEssentialComponent;
import models.pages.ItemDetailsPage;
import models.pages.cart.ShoppingCartPage;
import models.pages.computer.CheckOutCompletePage;
import models.pages.computer.CheckOutOptionPage;
import models.pages.computer.CheckOutPage;
import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import testdata.purchasing.ComputerDataObject;
import testdata.purchasing.ComputerSpec;
import testdata.purchasing.UserDataObject;

import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

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

        //Verify item detail in cart
        for (CartComponent.CartItemRowsData cartItemRowsData : shoppingCartPage.cartComponent().cartItemRowsData()){
            Assert.assertTrue(cartItemRowsData.getProductAttribute().contains(ComputerSpec.valueOf(simpleComputer.getProcessorType()).value()), "[ERR] CPU info is not in item details");
            Assert.assertTrue(cartItemRowsData.getProductAttribute().contains(ComputerSpec.valueOf(simpleComputer.getHdd()).value()), "[ERR] HDD info is not in item details");
            Assert.assertEquals(cartItemRowsData.getPrice(), currentCompPrice, "[ERR] Display price is not correct!");
            String displayProductName = cartItemRowsData.getProductName();
            String displayProductNameLink = cartItemRowsData.getProductNameLink();
            Assert.assertNotNull(displayProductName, "[ERR] Product name is empty");
            //Assert.assertNotNull(displayProductNameLink, "[ERR] Product link is empty");
        }

        double cardFooterSubTotalPrice = shoppingCartPage.cartFooterComponent().getCartTotalComponent().priceMap().get(subTotal);
        double cardFooterShippingPrice = shoppingCartPage.cartFooterComponent().getCartTotalComponent().priceMap().get(shipping);
        double cardFooterTaxPrice = shoppingCartPage.cartFooterComponent().getCartTotalComponent().priceMap().get(tax);
        double cardFooterTotalPrice = shoppingCartPage.cartFooterComponent().getCartTotalComponent().priceMap().get(total);
        Assert.assertEquals(cardFooterSubTotalPrice, currentCompPrice, "[ERR] Sub-total is not correct in the cart footer");
        Assert.assertEquals(cardFooterTotalPrice, cardFooterSubTotalPrice + cardFooterTaxPrice + cardFooterShippingPrice,
                "[ERR] Total price is not correct in the cart footer");

        shoppingCartPage.cartFooterComponent().getCartTotalComponent().tosCheckBox().click();
        shoppingCartPage.cartFooterComponent().getCartTotalComponent().checkoutBtn().click();
    }

    public void checkOut(UserDataObject userDataObject, ComputerDataObject simpleComputer){
        System.out.println("USER DATA: " + userDataObject);
        CheckOutOptionPage checkOutOptionPage = new CheckOutOptionPage(driver);
        checkOutOptionPage.asGuestOrRegisteredUserComp().checkoutAsGuestBtn().click();

        CheckOutPage checkOutPage = new CheckOutPage(driver);
        BillingAddressComponent billingAddressComponent = checkOutPage.billingAddressComponent();

        billingAddressComponent.firstName().sendKeys(userDataObject.getFirstName());
        String expectedFirstName = userDataObject.getFirstName();
        billingAddressComponent.lastName().sendKeys(userDataObject.getLastName());
        String expectedLastName = userDataObject.getLastName();
        billingAddressComponent.email().sendKeys(userDataObject.getEmail());
        String expectedEmail = userDataObject.getEmail();
        billingAddressComponent.selectCountry(userDataObject.getCountry());
        String expectedCountry = userDataObject.getCountry();
        billingAddressComponent.city().sendKeys(userDataObject.getCity());
        String expectedCity = userDataObject.getCity();
        billingAddressComponent.address1().sendKeys(userDataObject.getAddress1());
        String expectedAddress1 = userDataObject.getAddress1();
        billingAddressComponent.zipCode().sendKeys(userDataObject.getZipPostalCode());
        String expectedZipPostalCode = userDataObject.getZipPostalCode();
        billingAddressComponent.phoneNumber().sendKeys(userDataObject.getPhoneNumber());
        String expectedPhoneNumber = userDataObject.getPhoneNumber();
        billingAddressComponent.continueBtn().click();


        ShippingAddressComponent shippingAddressComponent = checkOutPage.shippingAddressComponent();
        shippingAddressComponent.continueBtn().click();

        ShippingMethodComponent shippingMethodComponent = checkOutPage.shippingMethodComponent();
        shippingMethodComponent.continueBtn().click();
        String expectedShippingMethod = "Ground";

        PaymentMethodComponent paymentMethodComponent = checkOutPage.paymentMethodComponent();
        paymentMethodComponent.continueBtn().click();
        String expectedPaymentMethod = "Cash On Delivery (COD)";

        //Print out information from confirm order
        System.out.println("---------------------------------------");
        System.out.println("Expected First Name:" +expectedFirstName);
        System.out.println("Expected Last Name:" + expectedLastName);
        System.out.println("Expected Email:" +expectedEmail);
        System.out.println("Expected country:" +expectedCountry);
        System.out.println("Expected city:" +expectedCity);
        System.out.println("Expected address1:" +expectedAddress1);
        System.out.println("Expected zip postal code:" +expectedZipPostalCode);
        System.out.println("Expected phone number:" +expectedPhoneNumber);
        System.out.println("Expected payment method:" +expectedPaymentMethod);
        System.out.println("Expected shipping method:" +expectedShippingMethod);
        System.out.println("----------------------------------------");

        PaymentInformationComponent paymentInformationComponent = checkOutPage.paymentInformationComponent();
        paymentInformationComponent.continueBtn().click();

        ConfirmOrderComponent confirmOrderComponent = checkOutPage.confirmOrderComponent();

        //Compare information of billing address display on confirm order component
        String displayFirstNameBA = checkOutPage.confirmOrderComponent().billingInfoComponent().name().getText().split(" ")[0];
        System.out.println("Display first name in billing address:" +displayFirstNameBA);
        Assert.assertTrue(displayFirstNameBA.equals(expectedFirstName), "[ERR]Display first name in billing address is not correct");

        String displayLastNameBA = checkOutPage.confirmOrderComponent().billingInfoComponent().name().getText().split(" ")[1];
        System.out.println("Display last name in billing address:" +displayLastNameBA);
        Assert.assertTrue(displayLastNameBA.equals(expectedLastName), "[ERR]Display last name in billing address is not correct");

        String displayEmailBA = checkOutPage.confirmOrderComponent().billingInfoComponent().email().getText().split(":")[1].trim();
        System.out.println("Display email in billing address:" +displayEmailBA);
        Assert.assertTrue(displayEmailBA.equals(expectedEmail), "[ERR]Display email in billing address is not correct");

        String displayPhoneNumberBA = checkOutPage.confirmOrderComponent().billingInfoComponent().phone().getText().split(" ")[1].trim();
        System.out.println("Display phone in billing address:" +displayPhoneNumberBA);
        Assert.assertTrue(displayPhoneNumberBA.equals(expectedPhoneNumber), "[ERR]Display phone number in billing address is not correct");

        String displayAddress1BA = checkOutPage.confirmOrderComponent().billingInfoComponent().address1().getText();
        System.out.println("Display fax in billing address:" +displayAddress1BA);
        Assert.assertTrue(displayAddress1BA.equals(expectedAddress1), "[ERR]Display address1 in billing address is not correct");

        String displayCityBA = checkOutPage.confirmOrderComponent().billingInfoComponent().cityStateZip().getText().split(",")[0].trim();
        System.out.println("Display city in billing address:" +displayCityBA);
        Assert.assertTrue(displayCityBA.equals(expectedCity), "[ERR]Display city in billing address is not correct");

        String displayCountryBA= checkOutPage.confirmOrderComponent().billingInfoComponent().country().getText();
        System.out.println("Display country in billing address:" +displayCountryBA);
        Assert.assertTrue(displayCountryBA.equals(expectedCountry), "[ERR]Display country in billing address is not correct");

        String displayPaymentMethodBA= checkOutPage.confirmOrderComponent().billingInfoComponent().paymentMethod().getText();
        System.out.println("Display payment method in billing address:" +displayPaymentMethodBA);
        Assert.assertTrue(displayPaymentMethodBA.equals(expectedPaymentMethod), "[ERR]Display payment method in billing address is not correct");

        String displayZipStateBA = checkOutPage.confirmOrderComponent().billingInfoComponent().cityStateZip().getText().split(",")[1].trim();
        List<String> listZipAndStateBA = Arrays.asList(displayZipStateBA.split(" "));
        String displayZipBA = listZipAndStateBA.get((listZipAndStateBA.size()) - 1);
        System.out.println("Display zip state in billing address:" +displayZipBA);
        Assert.assertTrue(displayZipBA.equals(expectedZipPostalCode), "[ERR]Display zip state in billing address is not correct");

        System.out.println("===============================================================================");
        //Compare information of shipping address display on confirm order component
        String displayFirstNameSA = checkOutPage.confirmOrderComponent().shippingInfoComponent().name().getText().split(" ")[0];
        System.out.println("Display first name in shipping address:" +displayFirstNameSA);
        Assert.assertTrue(displayFirstNameSA.equals(expectedFirstName), "[ERR]Display first name in shipping address is not correct");

        String displayLastNameSA = checkOutPage.confirmOrderComponent().shippingInfoComponent().name().getText().split(" ")[1];
        System.out.println("Display last name in shipping address:" +displayLastNameSA);
        Assert.assertTrue(displayLastNameSA.equals(expectedLastName), "[ERR]Display last name in shipping address is not correct");

        String displayEmailSA = checkOutPage.confirmOrderComponent().shippingInfoComponent().email().getText().split(":")[1].trim();
        System.out.println("Display email in shipping address:" +displayEmailSA);
        Assert.assertTrue(displayEmailSA.equals(expectedEmail), "[ERR]Display email in shipping address is not correct");

        String displayPhoneNumberSA = checkOutPage.confirmOrderComponent().shippingInfoComponent().phone().getText().split(" ")[1].trim();
        System.out.println("Display phone in shipping address:" +displayPhoneNumberSA);
        Assert.assertTrue(displayPhoneNumberSA.equals(expectedPhoneNumber), "[ERR]Display phone number in shipping address is not correct");

        String displayAddress1SA = checkOutPage.confirmOrderComponent().shippingInfoComponent().address1().getText();
        System.out.println("Display fax in shipping address:" +displayAddress1SA);
        Assert.assertTrue(displayAddress1SA.equals(expectedAddress1), "[ERR]Display address1 in shipping address is not correct");

        String displayCitySA = checkOutPage.confirmOrderComponent().shippingInfoComponent().cityStateZip().getText().split(",")[0].trim();
        System.out.println("Display city in shipping address:" +displayCitySA);
        Assert.assertTrue(displayCitySA.equals(expectedCity), "[ERR]Display city in shipping address is not correct");

        String displayCountrySA= checkOutPage.confirmOrderComponent().shippingInfoComponent().country().getText();
        System.out.println("Display country in shipping address:" +displayCountrySA);
        Assert.assertTrue(displayCountrySA.equals(expectedCountry), "[ERR]Display country in shipping address is not correct");

        String displayShippingMethodSA= checkOutPage.confirmOrderComponent().shippingInfoComponent().paymentMethod().getText();
        System.out.println("Display shipping method in shipping address:" +displayShippingMethodSA);
        Assert.assertTrue(displayShippingMethodSA.equals(expectedShippingMethod), "[ERR]Display payment method in shipping address is not correct");

        String displayZipAndStateSA = checkOutPage.confirmOrderComponent().shippingInfoComponent().cityStateZip().getText().split(",")[1].trim();
        List<String> listZipAndStateSA = Arrays.asList(displayZipAndStateSA.split(" "));
        String displayZipSA = listZipAndStateSA.get((listZipAndStateSA.size()) - 1);
        System.out.println("Display zip state in shipping address:" +displayZipSA);
        Assert.assertTrue(displayZipSA.equals(expectedZipPostalCode), "[ERR]Display zip state in shipping address is not correct");

        //Confirm order || Cart and cart footer component
        List<AbstractCartComponent.CartItemRowsData> cartItemRowsDatas = checkOutPage.confirmOrderComponent().summaryCartComponent().cartItemRowsData();
        for(AbstractCartComponent.CartItemRowsData cartItemRowsData: cartItemRowsDatas){
            System.out.println("Image source in confirm order:"+cartItemRowsData.getImageSource());
            System.out.println("Price in confirm order:"+cartItemRowsData.getPrice());
            Assert.assertTrue(cartItemRowsData.getProductAttribute().contains(ComputerSpec.valueOf(simpleComputer.getProcessorType()).value()), "[ERR] CPU info is not in item details");
            String displayProductName = cartItemRowsData.getProductName();
            String displayProductNameLink = cartItemRowsData.getProductNameLink();
            String displayProductLink = cartItemRowsData.getProductEditLink();
            Assert.assertNotNull(displayProductName, "[ERR] Product name is empty");
            Assert.assertNotNull(displayProductNameLink, "[ERR] Product name link is empty");
            //Assert.assertNotNull(displayProductLink, "[ERR] Product edit link is empty");
        }

        //Get product sub-total price
        SummaryCartComponent summaryCartComponent = checkOutPage.confirmOrderComponent().summaryCartComponent();
        double subtotalPrice = summaryCartComponent.itemTotalPrice();
        System.out.println("Expected sub total price in summary cart component" +subtotalPrice);

        double sumInCartTotal = 0.0;
        Map<String, Double> priceMap = checkOutPage.confirmOrderComponent().cartFooterComponent().getCartTotalComponent().priceMap();
//        for( String key: priceMap.keySet()){
//            System.out.println(key + ":" + priceMap.get(key));
//            if(key == "Total") break;
//            sumInCartTotal += priceMap.get(key);
//        }

        //System.out.println("Sum in cart total:" +sumInCartTotal);
        double productPrice = priceMap.get("Sub-Total");
        double paymentMethodFee = priceMap.get("Payment method additional fee");
        double total = priceMap.get("Total");
        double sumProductPriceAndPaymentMethodFee = productPrice + paymentMethodFee;

        //Compare subtotal price and product price
        Assert.assertEquals(subtotalPrice, productPrice, "[ERR]Product price is not equal sub total price");

        // Compare sum of product price and payment method fee with total.
        Assert.assertEquals(total, sumProductPriceAndPaymentMethodFee, "[ERR]Total is not equal sum product price and payment method fee");
        //confirm order
        confirmOrderComponent.confirmBtn().click();

        //Go to check out complete page
        CheckOutCompletePage checkOutCompletePage = new CheckOutCompletePage(driver);
        Assert.assertTrue(checkOutCompletePage.pageTitle().getText().equalsIgnoreCase("Thank you"),"[ERR]The check out page has wrong title");
        String orderTextId = checkOutCompletePage.chekOutPageDataComponent().orderTextId();
        System.out.println("Order Text Id:" +orderTextId);
        String orderDetailsLink = checkOutCompletePage.chekOutPageDataComponent().orderDetailLink();
        System.out.println("Order details link:" +orderDetailsLink);
        boolean isValidOrderDetailsLink = orderDetailsLink.contains(orderTextId);
        Assert.assertTrue(isValidOrderDetailsLink, "[ERR]Order details link is invalid");
        checkOutCompletePage.chekOutPageDataComponent().finishBtn().click();
    }
}
