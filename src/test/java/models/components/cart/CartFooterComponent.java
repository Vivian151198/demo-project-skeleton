package models.components.cart;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CartFooterComponent {
    private final WebDriver driver;
    private CartShippingComponent cartShippingComponent;
    private CartTotalComponent cartTotalComponent;

    public CartFooterComponent(WebDriver driver) {
        this.driver = driver;
    }

    public CartShippingComponent getCartShippingComponent() {
        return cartShippingComponent;
    }

    public CartTotalComponent getCartTotalComponent() {
        return new CartTotalComponent(driver);
    }

    // Inner classes scope: Khi co nhung class nho nhung chi ton tai trong object khac chu ben ngoai khong dung truc tiep thi luc do dung inner class.

    public static class CartShippingComponent{
        private final WebDriver driver;
        private final By countryOption = By.cssSelector("#CountryId");
        private final By stateProvinceOption = By.cssSelector("#StateProvinceId");
        private final By zipCode = By.cssSelector("#ZipPostalCode");
        private final By estimateShippingBtn = By.cssSelector("div.shipping > div > div.shipping-options > div:nth-child(4) > input");

        public CartShippingComponent(WebDriver driver) {
            this.driver = driver;
        }
    }

    public static class CartTotalComponent{
        private final WebDriver driver;
        private final By priceTableRows = By.cssSelector("table[class='cart-total'] tr");
        private final By tosCheckBox = By.cssSelector("#termsofservice");
        private final By checkoutBtn = By.cssSelector("#checkout");

        private CartTotalComponent(WebDriver driver) {
            this.driver = driver;
        }

        public Map<String, Double> priceMap() {
            Map<String, Double> priceMap = new HashMap<>();
            List<WebElement> priceTableRows = priceTableRows();
            for (WebElement priceTableRow : priceTableRows) {
                WebElement priceTypeElem = priceTableRow.findElement(By.className("cart-total-left"));
                String priceType = priceTypeElem.getText().replace(":", "").trim();

                WebElement priceValueElem = priceTableRow.findElement(By.className("cart-total-right"));
                double priceValue = Double.parseDouble(priceValueElem.getText());
                priceMap.put(priceType, priceValue);
            }
            return priceMap;
        }

        private List<WebElement> priceTableRows() {
            return driver.findElements(priceTableRows);
        }

        public WebElement tosCheckBox() {
            return driver.findElement(tosCheckBox);
        }

        public WebElement checkoutBtn() {
            return driver.findElement(checkoutBtn);
        }
    }
}

