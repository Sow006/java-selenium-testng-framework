package com.saisowmya.automation.utils;

import com.saisowmya.automation.config.FrameworkConfig;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;

import java.time.Duration;

/**
 * Thread-safe WebDriver manager using ThreadLocal.
 * Supports Chrome, Firefox, Edge — headless mode for CI.
 *
 * Author: Sai Sowmya Bhogavilli
 */
public class DriverManager {

    private static final Logger log = LogManager.getLogger(DriverManager.class);
    private static final ThreadLocal<WebDriver> driverThread = new ThreadLocal<>();
    private static final FrameworkConfig config = FrameworkConfig.getInstance();

    private DriverManager() {}

    public static void initDriver() {
        String browser = config.getBrowser();
        boolean headless = config.isHeadless();
        log.info("Initialising {} driver | headless={}", browser, headless);

        WebDriver driver = switch (browser) {
            case "firefox" -> createFirefoxDriver(headless);
            case "edge"    -> createEdgeDriver(headless);
            default        -> createChromeDriver(headless);
        };

        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(config.getImplicitWait()));
        driver.manage().timeouts().pageLoadTimeout(Duration.ofSeconds(config.getPageLoadTimeout()));
        driver.manage().window().maximize();

        driverThread.set(driver);
        log.info("Driver initialised: {}", driver.getClass().getSimpleName());
    }

    public static WebDriver getDriver() {
        if (driverThread.get() == null) {
            throw new IllegalStateException("Driver not initialised. Call initDriver() first.");
        }
        return driverThread.get();
    }

    public static void quitDriver() {
        if (driverThread.get() != null) {
            driverThread.get().quit();
            driverThread.remove();
            log.info("Driver quit and removed from ThreadLocal.");
        }
    }

    // ── Private factory methods ──────────────────────────────────────────────

    private static WebDriver createChromeDriver(boolean headless) {
        WebDriverManager.chromedriver().setup();
        ChromeOptions options = new ChromeOptions();
        if (headless) options.addArguments("--headless=new");
        options.addArguments(
            "--no-sandbox",
            "--disable-dev-shm-usage",
            "--disable-gpu",
            "--window-size=1920,1080",
            "--disable-extensions",
            "--remote-allow-origins=*"
        );
        return new ChromeDriver(options);
    }

    private static WebDriver createFirefoxDriver(boolean headless) {
        WebDriverManager.firefoxdriver().setup();
        FirefoxOptions options = new FirefoxOptions();
        if (headless) options.addArguments("--headless");
        return new FirefoxDriver(options);
    }

    private static WebDriver createEdgeDriver(boolean headless) {
        WebDriverManager.edgedriver().setup();
        EdgeOptions options = new EdgeOptions();
        if (headless) options.addArguments("--headless=new");
        return new EdgeDriver(options);
    }
}
