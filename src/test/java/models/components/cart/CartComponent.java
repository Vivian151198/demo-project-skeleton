package models.components.cart;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.ArrayList;
import java.util.List;

public class CartComponent {

    private final WebDriver driver;
    private final By itemTotalPriceSel = By.className("product-subtotal");
    private final By cartItemRowsSel = By.cssSelector("table[class='cart'] .cart-item-row");
    private final By productPictureSel = By.cssSelector(".product-picture img");
    private final By productNameLinkSel = By.className("product");
    private final By productAttributeSel = By.className("attributes");
    private final By productEditLinkSel = By.cssSelector(".edit-item a");
    private final By productPriceSel = By.className("product-unit-price");
    private final By productQuantitySel = By.className("qty-input");
    private final By productSubtotalSel = By.className("product-subtotal");


    public CartComponent(WebDriver driver) {
        this.driver = driver;
    }

    public List<CartItemRowsData> cartItemRowsData() {
        List<CartItemRowsData> cartItemRowsData = new ArrayList<>();
        List<WebElement> cartItemRowElems = driver.findElements(cartItemRowsSel);
        for (WebElement cartItemRowElem : cartItemRowElems) {
            String productImgSrc = cartItemRowElem.findElement(productPriceSel).getAttribute("src");
            String productName = cartItemRowElem.findElement(productNameLinkSel).getText();
            String productNameLink = cartItemRowElem.findElement(productNameLinkSel).getAttribute("href");

            //Item hasn't attribute => Way to avoid no such element
            List<WebElement> productAttributeElems = cartItemRowElem.findElements(productAttributeSel);
            String productAttributes = productAttributeElems.isEmpty() ? null : productAttributeElems.get(0).getText();

            double productPrice = Double.parseDouble(cartItemRowElem.findElement(productPriceSel).getText());
            double productQuantity = Double.parseDouble(cartItemRowElem.findElement(productQuantitySel).getAttribute("value"));
            double productSubtotal = Double.parseDouble(cartItemRowElem.findElement(productSubtotalSel).getText());

            //Item hasn't edit => Way to avoid no such element
            List<WebElement> productEditLinksElems = cartItemRowElem.findElements(productEditLinkSel);
            String productEditLink = productEditLinksElems.isEmpty() ? null : productEditLinksElems.get(0).getText();

            CartItemRowsData cartItemData = new CartItemRowsData(productImgSrc, productName, productNameLink,
                    productAttributes, productEditLink, productPrice, productQuantity, productSubtotal);
            cartItemRowsData.add(cartItemData);
        }

        return cartItemRowsData;
    }
    public static class CartItemRowsData{
        private final String imageSource;
        private final String productName;
        private final String productNameLink;
        private final String productAttribute;
        private final String productEditLink;
        private final double price;
        private final double quantity;
        private final double subTotal;

        public CartItemRowsData(String imageSource, String productName, String productNameLink,
                                String productAttribute, String productEditLink, double price,
                                double quantity, double subTotal) {
            this.imageSource = imageSource;
            this.productName = productName;
            this.productNameLink = productNameLink;
            this.productAttribute = productAttribute;
            this.productEditLink = productEditLink;
            this.price = price;
            this.quantity = quantity;
            this.subTotal = subTotal;
        }

        public String getImageSource() {
            return imageSource;
        }

        public String getProductName() {
            return productName;
        }

        public String getProductNameLink() {
            return productNameLink;
        }

        public String getProductAttribute() {
            return productAttribute;
        }

        public String getProductEditLink() {
            return productEditLink;
        }

        public double getPrice() {
            return price;
        }

        public double getQuantity() {
            return quantity;
        }

        public double getSubTotal() {
            return subTotal;
        }
    }
}
