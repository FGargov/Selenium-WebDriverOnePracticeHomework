package saucetests;

import base.BaseSetup;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;


import java.text.DecimalFormatSymbols;
import java.util.Locale;

import static org.fgargov.Constans.*;
import static org.fgargov.WebElementsID.*;
import static org.fgargov.Xpaths.*;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.List;
import java.util.Locale;

public class ProductsTest extends BaseSetup {
    @Test
    public void productAddedToShoppingCard_when_addToCart() {
        // Add Backpack and T-shirt to shopping cart
        add_Backpack_and_Tshirt_to_shopping_cart();

        //Може да използваме и двата вариянта.
        WebElement backPackInShoppingCart = getProductByTitle(BACKPACK_TITTLE);
        WebElement tShirtInShoppingCart = getProductByTitle(T_SHIRT_TITTLE);

        // Assert Items and Totals
        Assertions.assertTrue(backPackInShoppingCart.getText().contains(BACKPACK_TITTLE), "Item tittle is not correct.");
        Assertions.assertTrue(backPackInShoppingCart.getText().contains(BACKPACK_PRICE), "Item price is not correct.");
        Assertions.assertTrue(backPackInShoppingCart.getText().contains(BACKPACK_DESCRIPTION), "Item description is wrong.");

        Assertions.assertTrue(tShirtInShoppingCart.getText().contains(T_SHIRT_TITTLE), "Item tittle is not correct.");
        Assertions.assertTrue(tShirtInShoppingCart.getText().contains(T_SHIRT_PRICE), "Item price is not correct.");
        Assertions.assertTrue(tShirtInShoppingCart.getText().contains(T_SHIRT_DESCRIPTION), "Item description is wrong.");
    }

    @ParameterizedTest
    @CsvSource({ "Sauce Labs Backpack,$29.99,carry.allTheThings() with the sleek, " +
            "streamlined Sly Pack that melds uncompromising " +
            "style with unequaled laptop and tablet protection.","Sauce Labs Bolt T-Shirt,$15.99,Get your testing superhero " +
            "on with the Sauce Labs bolt T-shirt. " +
            "From American Apparel, 100% ringspun " +
            "combed cotton, heather gray with red bolt."})
    public void productAddedToShoppingCar_when_addToCartParameteryzed(String tittle,String price,String description) {
        add_Backpack_and_Tshirt_to_shopping_cart();

        WebElement backpack = getProductByTitle(tittle);

        // Assert Items and Totals
        Assertions.assertTrue(backpack.getText().contains(tittle), "Item tittle is not correct.");
        Assertions.assertTrue(backpack.getText().contains(price), "Item price is not correct.");
        Assertions.assertTrue(backpack.getText().contains(description), "Item description is wrong.");

    }

    @Test
    public void userDetailsAdded_when_checkoutWithValidInformation(){
        // Add Backpack and T-shirt to shopping cart go inside
        add_Backpack_and_Tshirt_to_shopping_cart();

        // Checkout
        WebElement checkoutButton = driver.findElement(By.xpath(CHECKOUT_BUTTON));
        checkoutButton.click();

        // Fill Contact Details
        fillShippingDetails(FIRST_NAME, LAST_NAME, ZIP_CODE);

        WebElement firstName = driver.findElement(By.id(FIRST_NAME_BOX_ID));
        WebElement lastName = driver.findElement(By.id(LAST_NAME_BOX_ID));
        WebElement postCode = driver.findElement(By.id(POSTAL_CODE_BOX_ID));

        // Assert Details in next step
        Assertions.assertTrue(firstName.getAttribute("value").equals(FIRST_NAME), "First name is not correct.");
        Assertions.assertTrue(lastName.getAttribute("value").equals(LAST_NAME),"Last name is not correct.");
        Assertions.assertTrue(postCode.getAttribute("value").equals(ZIP_CODE),"Post code name is not correct.");

        WebElement continueButton = driver.findElement(By.xpath(CONTINUE_BUTTON));
        continueButton.click();

        String expectedResult = "https://www.saucedemo.com/checkout-step-two.html";
        String actualResult = driver.getCurrentUrl();

        Assertions.assertEquals(expectedResult, actualResult, "User wasn't redirected to the correct page.");
    }

    @Test
    public void orderCompleted_when_addProduct_and_checkout_withConfirm(){
        // Add Backpack and T-shirt to shopping cart
        add_Backpack_and_Tshirt_to_shopping_cart();

        basket();

        Assertions.assertEquals(2, basket().size(), "Items count not as expected");

        WebElement checkoutButton = driver.findElement(By.xpath(CHECKOUT_BUTTON));
        checkoutButton.click();

        // Fill Contact Details
        fillShippingDetails(FIRST_NAME, LAST_NAME, ZIP_CODE);

        // Complete Order
        WebElement continueButton = driver.findElement(By.xpath(CONTINUE_BUTTON));
        continueButton.click();

        DecimalFormatSymbols symbols = new DecimalFormatSymbols(Locale.US);
        symbols.setDecimalSeparator('.');
        symbols.setGroupingSeparator(',');
        String total = driver.findElement(By.className("summary_total_label")).getText();

        double expectedPrice = 29.99 + 15.99 + 3.68;
        String expectedTotal = String.format("Total: $%s", expectedPrice);
        Assertions.assertEquals(expectedTotal, total, "Items total price not as expected");

        WebElement finistButton = driver.findElement(By.xpath(FINISH_BUTTON));
        finistButton.click();

        // Assert Items removed from Shopping Cart
        WebElement backToShoppingCart = driver.findElement(By.xpath(BACK_TO_SHOPPING_CART_BUTTON));
        backToShoppingCart.click();

        basket();

        Assertions.assertEquals(0, basket().size(), "Items count not as expected");
    }
}
