package saucetests;

import base.BaseSetup;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;


import java.text.DecimalFormatSymbols;
import java.util.Locale;

import static org.fgargov.Constans.*;
import static org.fgargov.WebElementsID.*;
import static org.fgargov.Xpaths.*;

public class LoginTests extends BaseSetup {
    @Test
    public void userAuthenticated_when_validCredentialsProvided(){
        String actualURL = driver.getCurrentUrl();
        String expectedURL="https://www.saucedemo.com/inventory.html";
        String expectedPageTittle="Swag Labs";

        WebElement inventoryPageTitle = driver.findElement(By.xpath(INVERTORY_PAGE_TITLE));
        wait.until(ExpectedConditions.visibilityOf(inventoryPageTitle));

        Assertions.assertEquals(expectedURL, actualURL, "User is not authenticated.");
        Assertions.assertEquals(expectedPageTittle, driver.getTitle(), "Page title is different than expected");
    }
}
