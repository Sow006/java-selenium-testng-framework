package com.saisowmya.automation.pages;

import org.openqa.selenium.By;
import java.util.List;

/**
 * ProductsPage — Page Object for the main product listing page.
 *
 * Author: Sai Sowmya Bhogavilli
 */
public class ProductsPage extends BasePage {

    // ── Locators ──────────────────────────────────────────────────────────────
    private static final By PAGE_TITLE        = By.cssSelector(".title");
    private static final By PRODUCT_ITEMS     = By.cssSelector(".inventory_item_name");
    private static final By PRODUCT_PRICES    = By.cssSelector(".inventory_item_price");
    private static final By SORT_DROPDOWN     = By.cssSelector("[data-test='product-sort-container']");
    private static final By ADD_TO_CART_BTNS  = By.cssSelector("[data-test^='add-to-cart']");
    private static final By CART_BADGE        = By.cssSelector(".shopping_cart_badge");
    private static final By CART_ICON         = By.cssSelector(".shopping_cart_link");
    private static final By BURGER_MENU       = By.id("react-burger-menu-btn");
    private static final By LOGOUT_LINK       = By.id("logout_sidebar_link");

    // ── Actions ───────────────────────────────────────────────────────────────

    public List<String> getAllProductNames() {
        log.info("Retrieving all product names");
        return getTextList(PRODUCT_ITEMS);
    }

    public List<String> getAllProductPrices() {
        log.info("Retrieving all product prices");
        return getTextList(PRODUCT_PRICES);
    }

    public ProductsPage sortBy(String option) {
        log.info("Sorting products by: {}", option);
        selectByVisibleText(SORT_DROPDOWN, option);
        return this;
    }

    public ProductsPage addFirstItemToCart() {
        log.info("Adding first item to cart");
        driver.findElements(ADD_TO_CART_BTNS).get(0).click();
        return this;
    }

    public ProductsPage addItemToCartByIndex(int index) {
        log.info("Adding item at index {} to cart", index);
        driver.findElements(ADD_TO_CART_BTNS).get(index).click();
        return this;
    }

    public int getCartCount() {
        if (!isDisplayed(CART_BADGE)) return 0;
        return Integer.parseInt(getText(CART_BADGE));
    }

    public CartPage goToCart() {
        log.info("Navigating to cart");
        click(CART_ICON);
        return new CartPage();
    }

    public LoginPage logout() {
        log.info("Logging out via burger menu");
        click(BURGER_MENU);
        waitForClickable(LOGOUT_LINK);
        click(LOGOUT_LINK);
        return new LoginPage();
    }

    public String getPageTitle() {
        return getText(PAGE_TITLE);
    }

    @Override
    public boolean isPageLoaded() {
        boolean loaded = waitForUrlContains("inventory");
        log.info("ProductsPage loaded: {}", loaded);
        return loaded;
    }
}
