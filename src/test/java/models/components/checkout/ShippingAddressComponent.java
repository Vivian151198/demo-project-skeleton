package models.components.checkout;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public class ShippingAddressComponent {

    private final WebDriver driver;
    private WebElement shippingAddressComp;
    private final By continueBtnSel = By.cssSelector(".new-address-next-step-button");

    public ShippingAddressComponent(WebDriver driver) {

        this.driver = driver;
        this.shippingAddressComp = driver.findElement(By.cssSelector("#opc-shipping"));
    }

    public WebElement continueBtn() {
        return shippingAddressComp.findElement(continueBtnSel);
    }

}
