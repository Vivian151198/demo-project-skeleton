package models.components.checkout;

import models.components.cart.CartFooterComponent;
import models.components.cart.SummaryCartComponent;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public class ConfirmOrderComponent {

    private final WebDriver driver;
    private final By confirmBtnSel = By.cssSelector(".confirm-order-next-step-button");
    private final By finishBtnSel = By.cssSelector(".order-completed-continue-button");
    private BillingInfoComponent billingInfoComponent;
    private ShippingInfoComponent shippingInfoComponent;
    private SummaryCartComponent summaryCartComponent;
    private CartFooterComponent cartFooterComponent;

    public ConfirmOrderComponent(WebDriver driver) {

        this.driver = driver;
    }

    public WebElement confirmBtn() {

        return driver.findElement(confirmBtnSel);
    }
    public WebElement finishBtn() {

        return driver.findElement(finishBtnSel);
    }


    public BillingInfoComponent billingInfoComponent() {
        return new BillingInfoComponent(driver);
    }

    public ShippingInfoComponent shippingInfoComponent(){
        return new ShippingInfoComponent(driver);
    }

    public SummaryCartComponent summaryCartComponent(){
        return new SummaryCartComponent(driver);
    }
    public CartFooterComponent cartFooterComponent(){
        return new CartFooterComponent(driver);
    }

    private static abstract class InfoComponent{

        private final WebDriver driver;
        protected final WebElement component;

        private By nameSel = By.className("name");
        private By emailSel = By.className("email");
        private By phoneSel = By.className("phone");
        private By faxSel = By.className("fax");
        private By address1Sel = By.className("address1");
        private By cityStateZipSel = By.className("city-state-zip");
        private By countrySel = By.className("country");

        abstract By componentSel();

        public InfoComponent(WebDriver driver) {
            this.driver = driver;
            this.component = driver.findElement(componentSel());
        }

        public WebElement getComponent() {
            return this.component;
        }

        public WebElement name() {
            return component.findElement(nameSel);
        }

        public WebElement email() {
            return component.findElement(emailSel);
        }

        public WebElement phone() {
            return component.findElement(phoneSel);
        }

        public WebElement fax() {
            return component.findElement(faxSel);
        }

        public WebElement address1() {
            return component.findElement(address1Sel);
        }

        public WebElement cityStateZip() {
            return component.findElement(cityStateZipSel);
        }

        public WebElement country() {
            return component.findElement(countrySel);
        }

    }

    public static class BillingInfoComponent extends InfoComponent{

        private final By paymentMethodSel = By.className("payment-method");
        public BillingInfoComponent(WebDriver driver) {
            super(driver);
        }

        @Override
        By componentSel(){
            return By.className("billing-info");
        }

        public WebElement paymentMethod() {
            return component.findElement(paymentMethodSel);
        }

    }

    public static class ShippingInfoComponent extends InfoComponent{

        private final By shippingMethodSel = By.className("shipping-method");
        public ShippingInfoComponent(WebDriver driver) {
            super(driver);
        }

        @Override
        By componentSel(){
            return By.className("shipping-info");
        }

        public WebElement paymentMethod() {
            return component.findElement(shippingMethodSel);
        }

    }
}
