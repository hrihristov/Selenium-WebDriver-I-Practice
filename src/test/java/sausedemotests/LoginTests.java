package sausedemotests;

import core.BaseTests;
import org.example.BrowserTypes;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class LoginTests extends BaseTests {

    @BeforeEach
    public void setup(){
        driver = startBrowser(BrowserTypes.CHROME);
        wait = new WebDriverWait(driver, Duration.ofSeconds(20));

        driver.get("https://www.saucedemo.com/");
    }

    @Test
    public void login_with_validCredentials(){
        //data-test="username"
        WebElement usernameField = driver.findElement(By.xpath("//input[@data-test='username']"));
        //data-test="password"
        WebElement passwordField = driver.findElement(By.xpath("//input[@data-test='password']"));
        //data-test="login-button"
        WebElement loginBtn = driver.findElement(By.xpath("//input[@data-test='login-button']"));

        usernameField.sendKeys("standard_user");
        passwordField.sendKeys("secret_sauce");
        loginBtn.click();

        //assert
        WebElement inventoryPageTitle = driver.findElement(By.xpath("//span[@data-test='title']"));
        var actualResult = inventoryPageTitle.getText();
        Assertions.assertEquals("Products", actualResult);

        logout();
    }

    @Test
    public void login_with_lockedUser(){
        //data-test="username"
        WebElement usernameField = driver.findElement(By.xpath("//input[@data-test='username']"));
        //data-test="password"
        WebElement passwordField = driver.findElement(By.xpath("//input[@data-test='password']"));
        //data-test="login-button"
        WebElement loginBtn = driver.findElement(By.xpath("//input[@data-test='login-button']"));

        usernameField.sendKeys("locked_out_user");
        passwordField.sendKeys("secret_sauce");
        loginBtn.click();

        //assert
        WebElement errorMessage = driver.findElement(By.xpath("//h3[@data-test='error']"));
        var actualResult = errorMessage.getText();

        Assertions.assertEquals("Epic sadface: Sorry, this user has been locked out.", actualResult);
    }
    @Test
    public void login_with_problematicUser(){
        //data-test="username"
        WebElement usernameField = driver.findElement(By.xpath("//input[@data-test='username']"));
        //data-test="password"
        WebElement passwordField = driver.findElement(By.xpath("//input[@data-test='password']"));
        //data-test="login-button"
        WebElement loginBtn = driver.findElement(By.xpath("//input[@data-test='login-button']"));

        usernameField.sendKeys("problem_user");
        passwordField.sendKeys("secret_sauce");
        loginBtn.click();

        //assert
        WebElement inventoryPageTitle = driver.findElement(By.xpath("//span[@data-test='title']"));
        var actualResult = inventoryPageTitle.getText();
        Assertions.assertEquals("Products", actualResult);

        logout();
    }

    @ParameterizedTest
    @CsvSource({
            "standard_user, secret_sauce",
            "problem_user, secret_sauce",
            "performance_glitch_user, secret_sauce",
            "error_user, secret_sauce",
            "visual_user, secret_sauce"
    })
    public void login(String username, String password) throws InterruptedException {
        //data-test="username"
        WebElement usernameField = driver.findElement(By.xpath("//input[@data-test='username']"));
        //data-test="password"
        WebElement passwordField = driver.findElement(By.xpath("//input[@data-test='password']"));
        //data-test="login-button"
        WebElement loginBtn = driver.findElement(By.xpath("//input[@data-test='login-button']"));

        usernameField.sendKeys(username);
        passwordField.sendKeys(password);
        loginBtn.click();

        //assert
        WebElement inventoryPageTitle = driver.findElement(By.xpath("//span[@data-test='title']"));
        var actualResult = inventoryPageTitle.getText();
        Assertions.assertEquals("Products", actualResult);

        logout();
    }
}
