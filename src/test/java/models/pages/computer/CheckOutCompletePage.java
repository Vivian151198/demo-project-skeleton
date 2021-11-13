package models.pages.computer;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.List;

public class CheckOutCompletePage {

    private final WebDriver driver;
    private final By pageTitleSel = By.className("page-title");
    private ChekOutPageDataComponent chekOutPageDataComponent;

    public CheckOutCompletePage(WebDriver driver) {
        this.driver = driver;
        WebDriverWait webDriverWait = new WebDriverWait(driver, 30);
        webDriverWait.until(ExpectedConditions.urlContains("/checkout/completed/"));
    }

    public WebElement pageTitle() {
        return driver.findElement(pageTitleSel);
    }

    public ChekOutPageDataComponent chekOutPageDataComponent() {
        return new ChekOutPageDataComponent(driver);
    }

    public static class ChekOutPageDataComponent {
        private final WebDriver driver;
        private final By componentSel = By.className("checkout-data");
        private WebElement component;
        private final By titleSel = By.className("title");
        private final By detailSel = By.cssSelector(".details li");
        private final By finishBtnSel = By.className("order-completed-continue-button");


        private ChekOutPageDataComponent(WebDriver driver) {
            this.driver = driver;
            component = driver.findElement(componentSel);
        }

        public WebElement title() {
            return component.findElement(titleSel);
        }

        public List<WebElement> details() {
            return component.findElements(detailSel);
        }

        public WebElement finishBtn() {
            return component.findElement(finishBtnSel);
        }

        public String titleText(){
            return title().getText();
        }

        public String orderTextId(){
            int orderIndex = 0;
            return details().get(0).getText().split(":")[1].trim();
        }

        public String orderDetailLink(){
            int orderDetailLink = 1;
            WebElement orderDetailLinkSel = details().get(orderDetailLink).findElement(By.tagName("a"));
            return orderDetailLinkSel.getAttribute("href");
        }
    }
}
