package models.pages.computer;

import models.components.checkout.*;
import org.openqa.selenium.WebDriver;

public class CheckOutPage {

    private final WebDriver driver;
    private BillingAddressComponent billingAddressComponent;
    private ConfirmOrderComponent confirmOrderComponent;
    private PaymentInformationComponent paymentInformationComponent;
    private PaymentMethodComponent paymentMethodComponent;
    private ShippingAddressComponent shippingAddressComponent;
    private ShippingMethodComponent shippingMethodComponent;


    public CheckOutPage(WebDriver driver) {
        this.driver = driver;
    }

    public BillingAddressComponent billingAddressComponent() {
        return new BillingAddressComponent(driver);
    }

    public ConfirmOrderComponent confirmOrderComponent() {
        return new ConfirmOrderComponent(driver);
    }

    public PaymentInformationComponent paymentInformationComponent() {
        return new PaymentInformationComponent(driver);
    }

    public PaymentMethodComponent paymentMethodComponent() {
        return new PaymentMethodComponent(driver);
    }

    public ShippingAddressComponent shippingAddressComponent() {
        return new ShippingAddressComponent(driver) ;
    }

    public ShippingMethodComponent shippingMethodComponent() {
        return new ShippingMethodComponent(driver) ;
    }
}
