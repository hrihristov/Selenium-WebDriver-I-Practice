package sausedemotests;

import core.BaseTests;
import org.example.BrowserTypes;
import org.junit.jupiter.api.*;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class ProductTests extends BaseTests {

    public static final String PRODUCT_TITLE_1 = "Sauce Labs Backpack";
    public static final String PRODUCT_TITLE_2 = "Sauce Labs Bolt T-Shirt";

    @BeforeEach
    public void setup(){
        //Login
        driver = startBrowser(BrowserTypes.CHROME);
        wait = new WebDriverWait(driver, Duration.ofSeconds(20));

        driver.get("https://www.saucedemo.com/");

        authenticateWithUser("standard_user", "secret_sauce");

        //Add 2 products to shopping cart
        getProductByTitle(PRODUCT_TITLE_1);
        getProductByTitle(PRODUCT_TITLE_2);

        //Go to shopping Cart
        WebElement shopingCart = driver.findElement(By.xpath("//*[@id=\"shopping_cart_container\"]/a"));
        shopingCart.click();
    }

    @Test
    public void productAddedToShoppingCart_when_addToCart(){
        //Assert correct items added
        var items = driver.findElements(By.className("inventory_item_name"));

        Assertions.assertEquals(2, items.size(), "Items count not as expected");

        Assertions.assertEquals(PRODUCT_TITLE_1, items.get(0).getText(), "Item title not as expected");
        Assertions.assertEquals(PRODUCT_TITLE_2, items.get(1).getText(), "Item title not as expected");
    }

    @Test
    public void userDetailsAdded_when_checkoutWithValidInformation(){
        // Assert Items and Totals
        driver.findElement(By.id("checkout")).click();

        // fill form
        fillShippingDetails("Fname", "lname", "zip");

        // click Continue button
        driver.findElement(By.id("continue")).click();

        var items = driver.findElements(By.className("inventory_item_name"));
        Assertions.assertEquals(2, items.size(), "Items count not as expected");

        var total = driver.findElement(By.className("summary_total_label")).getText();
        double expectedPrice = 29.99 + 15.99 + 3.68;
        String expectedTotal = String.format("Total: $%.2f", expectedPrice);

        Assertions.assertEquals(2, items.size(), "Items count not as expected");
        Assertions.assertEquals(PRODUCT_TITLE_1, items.get(0).getText(), "Item title not as expected");
        Assertions.assertEquals(PRODUCT_TITLE_2, items.get(1).getText(), "Item title not as expected");
        Assertions.assertEquals(expectedTotal, total, "Items total price not as expected");
    }

    @Test
    public void orderCompleted_when_addProduct_and_checkout_withConfirm(){
        // Assert Items and Totals
        driver.findElement(By.id("checkout")).click();

        // fill form
        fillShippingDetails("Fname", "lname", "zip");

        // click Continue button
        driver.findElement(By.id("continue")).click();

        // click Finish button
        driver.findElement(By.id("finish")).click();

        //Verify order is completed
        WebElement inventoryPageTitle = driver.findElement(By.xpath("//*[@id=\"header_container\"]/div[2]/span"));
        var actualResult = inventoryPageTitle.getText();
        Assertions.assertEquals("Checkout: Complete!", actualResult);

        //Verify Items are removed after from Shopping Cart after the order is completed
        var items = driver.findElements(By.className("inventory_item_name"));
        Assertions.assertEquals(0, items.size(), "Items count not as expected");
    }
}
