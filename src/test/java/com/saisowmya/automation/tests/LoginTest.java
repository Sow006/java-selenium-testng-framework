package com.saisowmya.automation.tests;

import com.saisowmya.automation.pages.LoginPage;
import com.saisowmya.automation.pages.ProductsPage;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * LoginTest — covers valid login, invalid credentials, and locked-out user scenarios.
 *
 * Author: Sai Sowmya Bhogavilli
 */
public class LoginTest extends BaseTest {

    @Test(description = "Verify successful login with valid credentials redirects to Products page")
    public void testSuccessfulLogin() {
        ProductsPage productsPage = new LoginPage()
                .open()
                .loginWith("standard_user", "secret_sauce");

        assertThat(productsPage.isPageLoaded())
                .as("Products page should load after successful login")
                .isTrue();

        assertThat(productsPage.getPageTitle())
                .as("Page title should be 'Products'")
                .isEqualTo("Products");
    }

    @Test(description = "Verify error message shown for invalid username/password",
          dataProvider = "invalidCredentials")
    public void testLoginWithInvalidCredentials(String username, String password, String expectedError) {
        LoginPage loginPage = new LoginPage()
                .open()
                .enterUsername(username)
                .enterPassword(password)
                .clickLoginExpectingFailure();

        assertThat(loginPage.isErrorDisplayed())
                .as("Error message should be visible for invalid credentials")
                .isTrue();

        assertThat(loginPage.getErrorMessage())
                .as("Error message should match expected")
                .contains(expectedError);
    }

    @Test(description = "Verify locked-out user cannot log in")
    public void testLockedOutUserCannotLogin() {
        LoginPage loginPage = new LoginPage()
                .open()
                .enterUsername("locked_out_user")
                .enterPassword("secret_sauce")
                .clickLoginExpectingFailure();

        assertThat(loginPage.getErrorMessage())
                .as("Locked out error should be shown")
                .contains("locked out");
    }

    @Test(description = "Verify empty username shows appropriate validation error")
    public void testEmptyUsernameValidation() {
        LoginPage loginPage = new LoginPage()
                .open()
                .enterPassword("secret_sauce")
                .clickLoginExpectingFailure();

        assertThat(loginPage.getErrorMessage())
                .as("Should show username required message")
                .contains("Username is required");
    }

    @Test(description = "Verify error message can be dismissed")
    public void testErrorMessageCanBeClosed() {
        LoginPage loginPage = new LoginPage()
                .open()
                .enterUsername("wrong_user")
                .enterPassword("wrong_pass")
                .clickLoginExpectingFailure()
                .closeErrorMessage();

        assertThat(loginPage.isErrorDisplayed())
                .as("Error message should be hidden after closing")
                .isFalse();
    }

    // ── Data Provider ─────────────────────────────────────────────────────────

    @DataProvider(name = "invalidCredentials")
    public Object[][] provideInvalidCredentials() {
        return new Object[][] {
            { "invalid_user",  "secret_sauce",  "Username and password do not match" },
            { "standard_user", "wrong_pass",    "Username and password do not match" },
            { "",              "",              "Username is required" }
        };
    }
}
