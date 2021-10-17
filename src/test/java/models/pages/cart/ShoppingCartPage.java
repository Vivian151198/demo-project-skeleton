package models.pages.cart;

import models.components.cart.CartFooterComponent;
import models.components.cart.ShoppingCartItemComponent;
import org.openqa.selenium.WebDriver;

public class ShoppingCartPage {

    private final WebDriver driver;
    private CartFooterComponent cartFooterComponent;

    public ShoppingCartPage(WebDriver driver) {
        this.driver = driver;
    }

    public ShoppingCartItemComponent shoppingCartItemComp(){
        return new ShoppingCartItemComponent(driver);
    }

    public CartFooterComponent cartFooterComponent(){
        return new CartFooterComponent(driver);
    }
}
