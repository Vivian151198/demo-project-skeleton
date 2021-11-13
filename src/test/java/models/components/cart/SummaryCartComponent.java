package models.components.cart;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class SummaryCartComponent extends AbstractCartComponent{

    public SummaryCartComponent(WebDriver driver) {
        super(driver);
    }

    @Override
    protected By productPriceSel(){
        return By.className("qty");
    }

    @Override
    protected boolean isSummaryCartComponent(){
        return true;
    }
}
