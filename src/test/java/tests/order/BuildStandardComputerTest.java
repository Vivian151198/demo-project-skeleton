package tests.order;

import io.qameta.allure.Description;
import models.components.product.computers.StandardEssentialComponent;
import org.openqa.selenium.WebDriver;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import testdata.purchasing.ComputerDataObject;
import testdata.purchasing.UserDataObject;
import testdata.url.URL;
import testflows.order.computer.BuyingComputerFlow;
import testflows.order.computer.ComputerPriceType;
import tests.BaseTest;
import utils.data.ComputerTestDataGenerator;

public class BuildStandardComputerTest extends BaseTest implements ComputerPriceType {

    @Test(dataProvider = "standardCompsDataSet", description = "Buying a standard computer")
    @Description(value = "Using a set of utils.data with different computer specs and check total price in cart")
    public void testBuildingStandardComputer(ComputerDataObject computerDataObject) {
        WebDriver driver = getDriver();
        BuyingComputerFlow<StandardEssentialComponent> orderingComputerFlow = new BuyingComputerFlow<>(driver);

        // Go to standard computer item page
        goTo(URL.STANDARD_COMP_DETAILS);
        orderingComputerFlow.withComputerEssentialComp(StandardEssentialComponent.class);
        orderingComputerFlow.buildComputer(computerDataObject);

        // Go to Shopping cart Page
        goTo(URL.CART);
        orderingComputerFlow.verifyComputerAdded(computerDataObject, standardComputerStartPrice);

        //checkout
        //checkout
        UserDataObject userDataObject = new UserDataObject();
        userDataObject.setFirstName("Vivian");
        userDataObject.setLastName("Vo");
        userDataObject.setCountry("Viet Nam");
        userDataObject.setCity("Ho Chi Minh");
        userDataObject.setZipPostalCode("1511");
        userDataObject.setPhoneNumber("0856383954");
        userDataObject.setEmail("vanminho296@gmail.com");
        userDataObject.setAddress1("12/4 Duong so 8");
        System.out.println("==============STANDARD COMPUTER=======================");
        orderingComputerFlow.checkOut(userDataObject, computerDataObject);
    }

    @DataProvider()
    public ComputerDataObject[] standardCompsDataSet() {
        ComputerDataObject[] computerTestData =
                ComputerTestDataGenerator
                        .getTestDataFrom("/src/test/java/testdata/purchasing/StandardComputerDataList.json");
        return computerTestData;
    }

}
