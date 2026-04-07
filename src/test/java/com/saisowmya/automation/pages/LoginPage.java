package com.saisowmya.automation.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.WebElement;

/**
 * LoginPage — Page Object for the login/authentication flow.
 * Target site: https://www.saucedemo.com (publicly available demo app)
 *
 * Author: Sai Sowmya Bhogavilli
 */
public class LoginPage extends BasePage {

    // ── Locators ──────────────────────────────────────────────────────────────
    private static final By USERNAME_INPUT  = By.id("user-name");
    private static final By PASSWORD_INPUT  = By.id("password");
    private static final By LOGIN_BUTTON    = By.id("login-button");
    private static final By ERROR_MESSAGE   = By.cssSelector("[data-test='error']");
    private static final By ERROR_CLOSE_BTN = By.cssSelector(".error-button");

    // PageFactory alternative (demonstrates both approaches)
    @FindBy(id = "user-name")
    private WebElement usernameField;

    private static final String PAGE_URL = "https://www.saucedemo.com";

    // ── Actions ───────────────────────────────────────────────────────────────

    public LoginPage open() {
        log.info("Opening Login page");
        navigateTo(PAGE_URL);
        return this;
    }

    public LoginPage enterUsername(String username) {
        log.info("Entering username: {}", username);
        type(USERNAME_INPUT, username);
        return this;
    }

    public LoginPage enterPassword(String password) {
        log.info("Entering password");
        type(PASSWORD_INPUT, password);
        return this;
    }

    public ProductsPage clickLogin() {
        log.info("Clicking login button");
        click(LOGIN_BUTTON);
        return new ProductsPage();
    }

    /**
     * Convenience method — full login in one call.
     */
    public ProductsPage loginWith(String username, String password) {
        return enterUsername(username)
                .enterPassword(password)
                .clickLogin();
    }

    public LoginPage clickLoginExpectingFailure() {
        click(LOGIN_BUTTON);
        return this;
    }

    public String getErrorMessage() {
        return getText(ERROR_MESSAGE);
    }

    public boolean isErrorDisplayed() {
        return isDisplayed(ERROR_MESSAGE);
    }

    public LoginPage closeErrorMessage() {
        click(ERROR_CLOSE_BTN);
        return this;
    }

    @Override
    public boolean isPageLoaded() {
        boolean loaded = isDisplayed(LOGIN_BUTTON) && getCurrentUrl().contains("saucedemo.com");
        log.info("LoginPage loaded: {}", loaded);
        return loaded;
    }
}
