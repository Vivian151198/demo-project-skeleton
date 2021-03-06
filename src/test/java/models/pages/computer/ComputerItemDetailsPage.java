package models.pages.computer;

import models.components.product.computers.ComputerEssentialComponent;
import models.pages.ItemDetailsPage;
import org.openqa.selenium.WebDriver;

public class ComputerItemDetailsPage extends ItemDetailsPage {

    private final WebDriver driver;
    private ComputerEssentialComponent computerEssentialComponent;

    public ComputerItemDetailsPage(WebDriver driver) {
        super(driver);
        this.driver = driver;
    }

    public ComputerEssentialComponent computerEssentialComp(){
        return computerEssentialComponent;
    }

}
