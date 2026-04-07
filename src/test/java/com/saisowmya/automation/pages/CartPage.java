package com.saisowmya.automation.pages;

import org.openqa.selenium.By;
import java.util.List;

/**
 * CartPage — Page Object for the shopping cart.
 *
 * Author: Sai Sowmya Bhogavilli
 */
public class CartPage extends BasePage {

    private static final By CART_ITEMS       = By.cssSelector(".cart_item_label .inventory_item_name");
    private static final By ITEM_PRICES      = By.cssSelector(".inventory_item_price");
    private static final By REMOVE_BUTTONS   = By.cssSelector("[data-test^='remove']");
    private static final By CONTINUE_SHOPPING = By.id("continue-shopping");
    private static final By CHECKOUT_BUTTON  = By.id("checkout");
    private static final By CART_QUANTITY    = By.cssSelector(".cart_quantity");

    public List<String> getCartItemNames() {
        return getTextList(CART_ITEMS);
    }

    public int getNumberOfItemsInCart() {
        return driver.findElements(CART_ITEMS).size();
    }

    public CartPage removeFirstItem() {
        log.info("Removing first item from cart");
        driver.findElements(REMOVE_BUTTONS).get(0).click();
        return this;
    }

    public boolean isCartEmpty() {
        return driver.findElements(CART_ITEMS).isEmpty();
    }

    public CheckoutPage proceedToCheckout() {
        log.info("Proceeding to checkout");
        click(CHECKOUT_BUTTON);
        return new CheckoutPage();
    }

    public ProductsPage continueShopping() {
        click(CONTINUE_SHOPPING);
        return new ProductsPage();
    }

    @Override
    public boolean isPageLoaded() {
        return waitForUrlContains("cart");
    }
}
