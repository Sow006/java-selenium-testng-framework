# 🧪 UI Automation Framework
### Selenium WebDriver + TestNG + Page Object Model

[![CI](https://github.com/saisowmya/ui-automation-framework/actions/workflows/ui-regression.yml/badge.svg)](https://github.com/saisowmya/ui-automation-framework/actions)
[![Java](https://img.shields.io/badge/Java-11-orange)](https://openjdk.org/projects/jdk/11/)
[![Selenium](https://img.shields.io/badge/Selenium-4.18-green)](https://www.selenium.dev/)
[![TestNG](https://img.shields.io/badge/TestNG-7.9-blue)](https://testng.org/)

> **Author:** Sai Sowmya Bhogavilli | Test Automation Engineer | ISTQB Certified  
> **Target App:** [SauceDemo](https://www.saucedemo.com) — publicly available e-commerce demo

---

## 📋 Framework Overview

Production-grade UI test automation framework demonstrating senior-level engineering practices:

- **Page Object Model (POM)** — clean separation of page interactions from test logic
- **Thread-safe WebDriver** via `ThreadLocal` — supports parallel test execution
- **Multi-browser support** — Chrome, Firefox, Edge with auto driver management
- **CI/CD integration** — GitHub Actions pipeline with headless execution
- **Rich HTML reporting** — ExtentReports with screenshots on failure
- **Retry mechanism** — automatic retry for flaky tests (configurable)
- **Data-driven testing** — TestNG `@DataProvider` for parameterised scenarios

---

## 🏗️ Project Structure

```
ui-automation-framework/
├── src/test/java/com/saisowmya/automation/
│   ├── config/
│   │   └── FrameworkConfig.java        # Centralised config reader (env + CLI overrides)
│   ├── pages/
│   │   ├── BasePage.java               # All shared WebDriver helpers & waits
│   │   ├── LoginPage.java              # Authentication flows
│   │   ├── ProductsPage.java           # Product listing & sorting
│   │   ├── CartPage.java               # Shopping cart management
│   │   └── CheckoutPage.java           # Checkout Step 1, 2, Confirmation
│   ├── tests/
│   │   ├── BaseTest.java               # TestNG hooks, driver lifecycle, reporting
│   │   ├── LoginTest.java              # Login scenarios (valid, invalid, locked)
│   │   └── CheckoutE2ETest.java        # Full E2E purchase flow tests
│   ├── utils/
│   │   ├── DriverManager.java          # ThreadLocal WebDriver factory
│   │   ├── ExtentReportManager.java    # HTML report management
│   │   └── ScreenshotUtil.java         # Failure screenshot capture
│   └── listeners/
│       └── RetryListener.java          # Auto-retry on test failure
├── src/test/resources/
│   ├── config/
│   │   └── config-staging.properties   # Environment-specific settings
│   └── testng.xml                      # Test suite definition
├── .github/workflows/
│   └── ui-regression.yml               # CI/CD pipeline
└── pom.xml
```

---

## ✅ Test Scenarios Covered

### Login Tests (`LoginTest.java`)
| Test | Description |
|------|-------------|
| `testSuccessfulLogin` | Valid credentials → Products page loads |
| `testLoginWithInvalidCredentials` | Data-driven: wrong user/pass → error shown |
| `testLockedOutUserCannotLogin` | Locked user → specific error message |
| `testEmptyUsernameValidation` | Empty form submission → validation message |
| `testErrorMessageCanBeClosed` | Error dismissal works correctly |

### E2E Checkout Tests (`CheckoutE2ETest.java`)
| Test | Description |
|------|-------------|
| `testCompletePurchaseFlow_SingleItem` | Full E2E: Login → Add → Cart → Checkout → Confirm |
| `testCompletePurchaseFlow_MultipleItems` | 3 items added and purchased |
| `testCheckoutFormValidation_EmptyFields` | Required field validation on checkout form |
| `testRemoveItemFromCart` | Item removal reduces cart count correctly |
| `testProductSortByPriceLowToHigh` | Sort order verified programmatically |

---

## 🚀 How to Run

### Prerequisites
- Java 11+
- Maven 3.8+
- Chrome/Firefox browser installed

### Run all tests
```bash
mvn clean test
```

### Run with specific browser
```bash
mvn clean test -Dbrowser=firefox
```

### Run in headless mode (CI)
```bash
mvn clean test -Dheadless=true
```

### Run against different environment
```bash
mvn clean test -Denv=staging
```

### Run specific test class
```bash
mvn clean test -Dtest=LoginTest
```

---

## 📊 Test Reports

After execution, HTML reports are generated at:
```
test-output/reports/Report_<timestamp>.html
```

Screenshots on failure are saved at:
```
screenshots/<TestName>_<timestamp>.png
```

---

## 🔧 Configuration

All settings in `src/test/resources/config/config-staging.properties`:

```properties
base.url=https://www.saucedemo.com
browser=chrome
headless=false
implicit.wait=10
explicit.wait=20
```

Override any property via Maven CLI: `-Dbrowser=firefox -Dheadless=true`

---

## 🛠️ Tech Stack

| Tool | Version | Purpose |
|------|---------|---------|
| Selenium WebDriver | 4.18.1 | Browser automation |
| TestNG | 7.9 | Test framework & parallel execution |
| WebDriverManager | 5.7 | Auto browser driver management |
| ExtentReports | 5.1.1 | HTML test reports |
| AssertJ | 3.25 | Fluent assertions |
| Log4j2 | 2.23 | Structured logging |
| GitHub Actions | — | CI/CD pipeline |
