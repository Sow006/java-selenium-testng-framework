package com.saisowmya.automation.pages;

import org.openqa.selenium.By;

/**
 * CheckoutPage — covers Step 1 (info entry), Step 2 (overview), and Order Confirmation.
 *
 * Author: Sai Sowmya Bhogavilli
 */
public class CheckoutPage extends BasePage {

    // Step 1 — Info
    private static final By FIRST_NAME     = By.id("first-name");
    private static final By LAST_NAME      = By.id("last-name");
    private static final By POSTAL_CODE    = By.id("postal-code");
    private static final By CONTINUE_BTN   = By.id("continue");
    private static final By CANCEL_BTN     = By.id("cancel");
    private static final By ERROR_MSG      = By.cssSelector("[data-test='error']");

    // Step 2 — Overview
    private static final By FINISH_BTN         = By.id("finish");
    private static final By ITEM_TOTAL_LABEL   = By.cssSelector(".summary_subtotal_label");
    private static final By TAX_LABEL          = By.cssSelector(".summary_tax_label");
    private static final By TOTAL_LABEL        = By.cssSelector(".summary_total_label");

    // Confirmation
    private static final By CONFIRMATION_HEADER = By.cssSelector(".complete-header");
    private static final By BACK_HOME_BTN       = By.id("back-to-products");

    // ── Step 1 actions ────────────────────────────────────────────────────────

    public CheckoutPage enterFirstName(String name) {
        type(FIRST_NAME, name);
        return this;
    }

    public CheckoutPage enterLastName(String name) {
        type(LAST_NAME, name);
        return this;
    }

    public CheckoutPage enterPostalCode(String code) {
        type(POSTAL_CODE, code);
        return this;
    }

    public CheckoutPage fillShippingInfo(String firstName, String lastName, String postalCode) {
        return enterFirstName(firstName)
                .enterLastName(lastName)
                .enterPostalCode(postalCode);
    }

    public CheckoutPage clickContinue() {
        click(CONTINUE_BTN);
        return this;
    }

    public String getErrorMessage() {
        return getText(ERROR_MSG);
    }

    // ── Step 2 actions ────────────────────────────────────────────────────────

    public String getItemTotal() {
        return getText(ITEM_TOTAL_LABEL);
    }

    public String getTax() {
        return getText(TAX_LABEL);
    }

    public String getOrderTotal() {
        return getText(TOTAL_LABEL);
    }

    public CheckoutPage clickFinish() {
        log.info("Finishing order");
        click(FINISH_BTN);
        return this;
    }

    // ── Confirmation ──────────────────────────────────────────────────────────

    public String getConfirmationMessage() {
        return getText(CONFIRMATION_HEADER);
    }

    public boolean isOrderConfirmed() {
        return isDisplayed(CONFIRMATION_HEADER) && getText(CONFIRMATION_HEADER).contains("Thank you");
    }

    public ProductsPage backToProducts() {
        click(BACK_HOME_BTN);
        return new ProductsPage();
    }

    @Override
    public boolean isPageLoaded() {
        return waitForUrlContains("checkout");
    }
}
