package com.saisowmya.automation.tests;

import com.saisowmya.automation.pages.*;
import org.testng.annotations.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * CheckoutE2ETest — End-to-end tests covering the complete purchase flow:
 *   Login → Browse Products → Add to Cart → Checkout → Order Confirmation
 *
 * Author: Sai Sowmya Bhogavilli
 */
public class CheckoutE2ETest extends BaseTest {

    @Test(description = "E2E: User can complete a full purchase for a single item")
    public void testCompletePurchaseFlow_SingleItem() {
        // Step 1: Login
        ProductsPage productsPage = new LoginPage()
                .open()
                .loginWith("standard_user", "secret_sauce");

        assertThat(productsPage.isPageLoaded()).isTrue();
        log.info("Step 1 PASSED: Login successful");

        // Step 2: Add one product to cart
        productsPage.addFirstItemToCart();

        assertThat(productsPage.getCartCount())
                .as("Cart badge should show 1 item")
                .isEqualTo(1);
        log.info("Step 2 PASSED: Item added to cart");

        // Step 3: Navigate to cart
        CartPage cartPage = productsPage.goToCart();

        assertThat(cartPage.isPageLoaded()).isTrue();
        assertThat(cartPage.getNumberOfItemsInCart())
                .as("Cart should contain 1 item")
                .isEqualTo(1);
        log.info("Step 3 PASSED: Cart contains correct item count");

        // Step 4: Proceed to checkout and fill info
        CheckoutPage checkoutPage = cartPage
                .proceedToCheckout()
                .fillShippingInfo("Sai Sowmya", "Bhogavilli", "85049")
                .clickContinue();

        log.info("Step 4 PASSED: Shipping info submitted");

        // Step 5: Verify order summary and finish
        assertThat(checkoutPage.getItemTotal())
                .as("Item total should be displayed")
                .isNotBlank();

        checkoutPage.clickFinish();

        // Step 6: Verify confirmation
        assertThat(checkoutPage.isOrderConfirmed())
                .as("Order confirmation message should be displayed")
                .isTrue();

        assertThat(checkoutPage.getConfirmationMessage())
                .as("Confirmation header should say 'Thank you'")
                .containsIgnoringCase("Thank you");

        log.info("Step 5 PASSED: Order confirmed successfully");
    }

    @Test(description = "E2E: User can add multiple items, verify cart count, and checkout")
    public void testCompletePurchaseFlow_MultipleItems() {
        ProductsPage productsPage = new LoginPage()
                .open()
                .loginWith("standard_user", "secret_sauce");

        // Add 3 items
        productsPage.addItemToCartByIndex(0)
                    .addItemToCartByIndex(1)
                    .addItemToCartByIndex(2);

        assertThat(productsPage.getCartCount())
                .as("Cart should show 3 items")
                .isEqualTo(3);

        CartPage cartPage = productsPage.goToCart();
        assertThat(cartPage.getNumberOfItemsInCart()).isEqualTo(3);

        CheckoutPage checkoutPage = cartPage
                .proceedToCheckout()
                .fillShippingInfo("Test", "User", "12345")
                .clickContinue();

        checkoutPage.clickFinish();

        assertThat(checkoutPage.isOrderConfirmed()).isTrue();
    }

    @Test(description = "Verify checkout form validation — required fields")
    public void testCheckoutFormValidation_EmptyFields() {
        new LoginPage().open().loginWith("standard_user", "secret_sauce")
                .addFirstItemToCart()
                .goToCart()
                .proceedToCheckout()
                .clickContinue();  // submit with empty fields

        CheckoutPage checkoutPage = new CheckoutPage();

        assertThat(checkoutPage.getErrorMessage())
                .as("Error should indicate first name is required")
                .contains("First Name is required");
    }

    @Test(description = "Verify user can remove item from cart before checkout")
    public void testRemoveItemFromCart() {
        ProductsPage productsPage = new LoginPage()
                .open()
                .loginWith("standard_user", "secret_sauce");

        productsPage.addFirstItemToCart()
                    .addItemToCartByIndex(1);

        assertThat(productsPage.getCartCount()).isEqualTo(2);

        CartPage cartPage = productsPage.goToCart();
        cartPage.removeFirstItem();

        assertThat(cartPage.getNumberOfItemsInCart())
                .as("Cart should have 1 item after removal")
                .isEqualTo(1);
    }

    @Test(description = "Verify product sort by price (low to high) works correctly")
    public void testProductSortByPriceLowToHigh() {
        ProductsPage productsPage = new LoginPage()
                .open()
                .loginWith("standard_user", "secret_sauce")
                .sortBy("Price (low to high)");

        java.util.List<String> prices = productsPage.getAllProductPrices();

        // Parse and verify ascending order
        java.util.List<Double> numericPrices = prices.stream()
                .map(p -> Double.parseDouble(p.replace("$", "")))
                .collect(java.util.stream.Collectors.toList());

        for (int i = 0; i < numericPrices.size() - 1; i++) {
            assertThat(numericPrices.get(i))
                    .as("Price at index %d should be <= price at index %d", i, i + 1)
                    .isLessThanOrEqualTo(numericPrices.get(i + 1));
        }
    }
}
