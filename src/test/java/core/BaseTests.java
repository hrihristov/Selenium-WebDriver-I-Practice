package core;


import org.example.BrowserTypes;
import org.junit.jupiter.api.AfterEach;
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

import java.util.List;

public class BaseTests {

    public static WebDriver driver;
    public static WebDriverWait wait;

    @AfterEach
    public void afterTest(){
        driver.close();
    }

    protected static WebDriver startBrowser(BrowserTypes browserTypes){

        //Chrome, FF, Edge
        switch (browserTypes){
            case CHROME:
                ChromeOptions chromeOptions = new ChromeOptions(); //may not be needed
                return new ChromeDriver(chromeOptions);
            case FIREFOX:
                FirefoxOptions firefoxOptions = new FirefoxOptions();
                return new FirefoxDriver(firefoxOptions);
            case EDGE:
                EdgeOptions edgeOptions = new EdgeOptions();
                return new EdgeDriver(edgeOptions);
        }

        return null;
    }

    protected static void authenticateWithUser(String username, String password){
        //data-test="username"
        WebElement usernameField = driver.findElement(By.xpath("//input[@data-test='username']"));
        //data-test="password"
        WebElement passwordField = driver.findElement(By.xpath("//input[@data-test='password']"));
        //data-test="login-button"
        WebElement loginBtn = driver.findElement(By.xpath("//input[@data-test='login-button']"));

        usernameField.sendKeys(username);
        passwordField.sendKeys(password);
        loginBtn.click();

        WebElement inventoryPageTitle = driver.findElement(By.xpath("//div[@class='app_logo']"));
        wait.until(ExpectedConditions.visibilityOf(inventoryPageTitle));
    }

    public static void getProductByTitle(String title) {
        var container = driver.findElement(By.xpath(String.format("//div[@class='inventory_item' and descendant::div[text()='%s']]", title)));
        container.findElement(By.className("btn_inventory")).click();
    }

    protected List<WebElement> getAllProducts(){
        return driver.findElements(By.xpath("//div[@data-test='inventory-item']"));
    }

    protected static void logout(){
        WebElement burgerBtn = driver.findElement(By.xpath("//div[@class='bm-burger-button']"));
        burgerBtn.click();

        WebElement logoutBtn = driver.findElement(By.xpath("//a[@data-test='logout-sidebar-link']"));
        wait.until(ExpectedConditions.elementToBeClickable(logoutBtn));
        logoutBtn.click();
    }

    protected static void fillShippingDetails(String firstName, String lastName, String zip) {
        driver.findElement(By.id("first-name")).sendKeys(firstName);
        driver.findElement(By.id("last-name")).sendKeys(lastName);
        driver.findElement(By.id("postal-code")).sendKeys(zip);
    }
}
