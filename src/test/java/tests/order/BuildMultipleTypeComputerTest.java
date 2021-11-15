package tests.order;

import io.qameta.allure.Description;
import models.components.product.computers.CheapComputerEssentialComponent;
import models.components.product.computers.StandardEssentialComponent;
import org.openqa.selenium.WebDriver;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import testdata.purchasing.ComputerDataObject;
import testdata.url.URL;
import testdata.users.UserDataObject;
import testflows.order.computer.BuyingComputerFlow;
import testflows.order.computer.ComputerPriceType;
import tests.BaseTest;
import utils.data.CommonData;
import utils.data.ComputerTestDataGenerator;

;import java.security.SecureRandom;

public class BuildMultipleTypeComputerTest extends BaseTest implements ComputerPriceType {

    @Test(description = "Buying a standard computer")
    @Description(value = "Buy 2 computer with different type")
    public void testBuildingStandardComputer() {

        //Get random data from 2 computer specs
        ComputerDataObject[] standardComputerTestData =
                ComputerTestDataGenerator.getTestDataFrom("/src/test/java/testdata/purchasing/StandardComputerDataList.json");
        ComputerDataObject standardComputer = standardComputerTestData[new SecureRandom().nextInt(standardComputerTestData.length)];

        ComputerDataObject[] cheapComputerTestData =
                ComputerTestDataGenerator.getTestDataFrom("/src/test/java/testdata/purchasing/CheapComputerDataList.json");
        ComputerDataObject cheapComputer = cheapComputerTestData[new SecureRandom().nextInt(cheapComputerTestData.length)];

        //Add 2 computer in the shopping cart
        WebDriver driver = getDriver();
        BuyingComputerFlow orderingComputerFlow = new BuyingComputerFlow<>(driver);

        // Go to standard computer item page
        goTo(URL.STANDARD_COMP_DETAILS);
        orderingComputerFlow.withComputerEssentialComp(StandardEssentialComponent.class);
        orderingComputerFlow.buildComputer(standardComputer);

        // Go to cheap computer item page
        goTo(URL.CHEAP_COMP_DETAILS);
        orderingComputerFlow.withComputerEssentialComp(CheapComputerEssentialComponent.class);
        orderingComputerFlow.buildComputer(cheapComputer);

        // Go to Shopping cart Page
        goTo(URL.CART);
    }
}
