package base;

import org.fgargov.enums.BrowserTypes;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;

import static org.fgargov.Credentials.VALID_PASSWORD;
import static org.fgargov.Credentials.VALID_USERNAME;
import static org.fgargov.WebElementsID.*;
import static org.fgargov.Xpaths.*;

public class BaseSetup {
    public static WebDriver driver;
    public static WebDriverWait wait;

    @BeforeAll
    public static void beforeAllTests(){
        driver = startBrowser(BrowserTypes.EDGE);

        // Configure wait
        wait = new WebDriverWait(driver, Duration.ofSeconds(30));
        //driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(4));

        // Navigate to Sauce Demo
        driver.get("https://www.saucedemo.com/");

        authenticateWithUser(VALID_USERNAME, VALID_PASSWORD);
    }

    public static WebDriver startBrowser(BrowserTypes browserType) {
        // Setup Browser
        switch (browserType){
            case CHROME:
                ChromeOptions chromeOptions = new ChromeOptions();
                chromeOptions.addArguments("--headless");
                return new ChromeDriver(chromeOptions);
            case FIREFOX:
                FirefoxOptions firefoxOptions = new FirefoxOptions();
                firefoxOptions.addArguments("--headless");
                return new FirefoxDriver(firefoxOptions);
            case EDGE:
                EdgeOptions edgeOptions = new EdgeOptions();
                edgeOptions.addArguments("--headless");
                return new EdgeDriver(edgeOptions);
        }

        return null;
    }

    protected static void authenticateWithUser(String username, String pass) {
        WebElement usernameInput = driver.findElement(By.xpath(LOGIN_USERNAME_XPATH));
        usernameInput.sendKeys(username);

        WebElement password = driver.findElement(By.xpath(LOGIN_PASSWORD_XPATH));
        password.sendKeys(pass);

        WebElement loginButton = driver.findElement(By.xpath(LOGIN_LOGINING_BUTTON));
        loginButton.click();

        WebElement inventoryPageTitle = driver.findElement(By.xpath(INVERTORY_PAGE_TITLE));
        wait.until(ExpectedConditions.visibilityOf(inventoryPageTitle));
    }

    protected WebElement getProductByTitle(String title){ // По универсално търсене. ова част от израза се използва, за да се избере всеки <div> елемент, който е потомък на елемента, към който се прилага израза.
        return driver.findElement(By.xpath(String.format("//div[@class='cart_item_label' and descendant::div[text()='%s']]", title)));
    }

    protected static void fillShippingDetails(String firstName, String lastName, String zip) {
        driver.findElement(By.id(FIRST_NAME_BOX_ID)).sendKeys(firstName);
        driver.findElement(By.id(LAST_NAME_BOX_ID)).sendKeys(lastName);
        driver.findElement(By.id(POSTAL_CODE_BOX_ID)).sendKeys(zip);
    }

    protected static void add_Backpack_and_Tshirt_to_shopping_cart() {
        WebElement addBackPackToCart = driver.findElement(By.xpath(ADD_BACKPACK_TO_CART_BUTTON));
        addBackPackToCart.click();

        WebElement addTShirt = driver.findElement(By.xpath(ADD_TSHIRT_TO_CART_BUTTON));
        addTShirt.click();
        // Click on shopping Cart
        WebElement shoppingCart = driver.findElement(By.xpath(SHOPPING_CART_BUTTON));
        shoppingCart.click();
    }

    public static List<WebElement> basket() {
        return driver.findElements(By.xpath(SHOPPING_CART_ITEM));
    }

    @AfterEach
    public void resetState() {
        WebElement burgerButton = driver.findElement(By.xpath(BURGER_BUTTON));
        burgerButton.click();

        wait.until(ExpectedConditions.elementToBeClickable(By.xpath(RESET_APP_STATE_BUTTON)));
        WebElement resetButton = driver.findElement(By.xpath(RESET_APP_STATE_BUTTON));
        resetButton.click();
        driver.get("https://www.saucedemo.com/inventory.html");
    }

    @AfterAll
    public static void afterTest(){
        // close driver
        driver.close();
    }
}
