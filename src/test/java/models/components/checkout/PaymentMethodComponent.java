package models.components.checkout;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public class PaymentMethodComponent {

    private final WebDriver driver;
    private final By codOptionSel = By.cssSelector("[value='Payments.CashOnDelivery']");
    private final By checkOptionSel = By.cssSelector("[value='Payments.CashOnDelivery']");
    private final By creditCardSel = By.cssSelector("[value='Payments.CashOnDelivery']");
    private final By purchaseSel = By.cssSelector("[value='Payments.CashOnDelivery']");
    private final By continueBtnSel = By.cssSelector(".payment-method-next-step-button");
    private final By paymentMethodDefaultSel = By.cssSelector("#paymentmethod_0");

    public PaymentMethodComponent(WebDriver driver) {
        this.driver = driver;
    }

    public WebElement codOption() {
        return driver.findElement(codOptionSel);
    }

    public WebElement checkOption() {
        return driver.findElement(checkOptionSel);
    }

    public WebElement creditCard() {
        return driver.findElement(creditCardSel);
    }

    public WebElement purchase() {
        return driver.findElement(purchaseSel);
    }

    public WebElement continueBtn() {
        return driver.findElement(continueBtnSel);
    }

    public WebElement paymentMethodDefault() {
        return driver.findElement(paymentMethodDefaultSel);
    }
}
