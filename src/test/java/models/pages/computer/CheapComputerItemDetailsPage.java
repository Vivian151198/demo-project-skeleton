package models.pages.computer;

import models.components.product.computers.CheapComputerEssentialComponent;
import models.pages.ItemDetailsPage;
import org.openqa.selenium.WebDriver;

public class CheapComputerItemDetailsPage extends ItemDetailsPage {

    private final WebDriver driver;

    public CheapComputerItemDetailsPage(WebDriver driver) {
        super(driver);
        this.driver = driver;
    }

    public CheapComputerEssentialComponent computerEssentialComp(){
        return new CheapComputerEssentialComponent(driver);
    }
}
