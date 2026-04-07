package com.saisowmya.automation.pages;

import com.saisowmya.automation.config.FrameworkConfig;
import com.saisowmya.automation.utils.DriverManager;
import com.saisowmya.automation.utils.ScreenshotUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;
import java.util.stream.Collectors;

/**
 * BasePage — all page objects extend this.
 * Centralises WebDriver interactions, waits, and JS execution.
 *
 * Author: Sai Sowmya Bhogavilli
 */
public abstract class BasePage {

    protected final Logger log = LogManager.getLogger(getClass());
    protected final WebDriver driver;
    protected final WebDriverWait wait;
    protected final JavascriptExecutor js;
    protected final Actions actions;
    private static final FrameworkConfig config = FrameworkConfig.getInstance();

    protected BasePage() {
        this.driver  = DriverManager.getDriver();
        this.wait    = new WebDriverWait(driver, Duration.ofSeconds(config.getExplicitWait()));
        this.js      = (JavascriptExecutor) driver;
        this.actions = new Actions(driver);
        PageFactory.initElements(driver, this);
    }

    // ── Navigation ───────────────────────────────────────────────────────────

    public void navigateTo(String url) {
        log.info("Navigating to: {}", url);
        driver.get(url);
    }

    public String getPageTitle() {
        return driver.getTitle();
    }

    public String getCurrentUrl() {
        return driver.getCurrentUrl();
    }

    // ── Wait helpers ─────────────────────────────────────────────────────────

    protected WebElement waitForVisible(By locator) {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
    }

    protected WebElement waitForClickable(By locator) {
        return wait.until(ExpectedConditions.elementToBeClickable(locator));
    }

    protected boolean waitForUrlContains(String fragment) {
        return wait.until(ExpectedConditions.urlContains(fragment));
    }

    protected boolean waitForTextPresent(By locator, String text) {
        return wait.until(ExpectedConditions.textToBePresentInElementLocated(locator, text));
    }

    // ── Interaction helpers ──────────────────────────────────────────────────

    protected void click(By locator) {
        log.debug("Clicking: {}", locator);
        waitForClickable(locator).click();
    }

    protected void type(By locator, String text) {
        log.debug("Typing '{}' into: {}", text, locator);
        WebElement el = waitForVisible(locator);
        el.clear();
        el.sendKeys(text);
    }

    protected void typeAndSubmit(By locator, String text) {
        type(locator, text);
        waitForVisible(locator).sendKeys(Keys.RETURN);
    }

    protected String getText(By locator) {
        return waitForVisible(locator).getText().trim();
    }

    protected String getAttribute(By locator, String attribute) {
        return waitForVisible(locator).getAttribute(attribute);
    }

    protected boolean isDisplayed(By locator) {
        try {
            return driver.findElement(locator).isDisplayed();
        } catch (NoSuchElementException | StaleElementReferenceException e) {
            return false;
        }
    }

    protected List<String> getTextList(By locator) {
        return driver.findElements(locator)
                .stream()
                .map(el -> el.getText().trim())
                .collect(Collectors.toList());
    }

    // ── Dropdown ─────────────────────────────────────────────────────────────

    protected void selectByVisibleText(By locator, String text) {
        new Select(waitForVisible(locator)).selectByVisibleText(text);
    }

    protected void selectByValue(By locator, String value) {
        new Select(waitForVisible(locator)).selectByValue(value);
    }

    // ── JS helpers ───────────────────────────────────────────────────────────

    protected void jsClick(By locator) {
        log.debug("JS click on: {}", locator);
        js.executeScript("arguments[0].click();", driver.findElement(locator));
    }

    protected void scrollIntoView(By locator) {
        js.executeScript("arguments[0].scrollIntoView({block:'center'});", driver.findElement(locator));
    }

    protected void scrollToTop() {
        js.executeScript("window.scrollTo(0, 0);");
    }

    // ── Screenshot ───────────────────────────────────────────────────────────

    public String captureScreenshot(String testName) {
        return ScreenshotUtil.capture(driver, testName);
    }

    // ── Abstract contract ────────────────────────────────────────────────────

    /**
     * Each page must verify it has loaded correctly.
     * Called by tests after navigation to assert the right page is active.
     */
    public abstract boolean isPageLoaded();
}
