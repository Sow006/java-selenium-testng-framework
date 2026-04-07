package com.saisowmya.automation.tests;

import com.saisowmya.automation.utils.DriverManager;
import com.saisowmya.automation.utils.ExtentReportManager;
import com.saisowmya.automation.utils.ScreenshotUtil;
import com.aventstack.extentreports.ExtentTest;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.ITestResult;
import org.testng.annotations.*;

/**
 * BaseTest — all test classes extend this.
 * Manages driver lifecycle and ExtentReport integration.
 *
 * Author: Sai Sowmya Bhogavilli
 */
public class BaseTest {

    protected final Logger log = LogManager.getLogger(getClass());

    @BeforeSuite(alwaysRun = true)
    public void initReport() {
        ExtentReportManager.initReports();
        log.info("=== Test Suite Started ===");
    }

    @AfterSuite(alwaysRun = true)
    public void flushReport() {
        ExtentReportManager.flushReports();
        log.info("=== Test Suite Finished ===");
    }

    @BeforeMethod(alwaysRun = true)
    public void setUp(java.lang.reflect.Method method) {
        log.info("---- Starting test: {} ----", method.getName());
        DriverManager.initDriver();

        // Create a test node in the Extent Report
        ExtentTest test = ExtentReportManager.createTest(method.getName());
        ExtentReportManager.setCurrentTest(test);
    }

    @AfterMethod(alwaysRun = true)
    public void tearDown(ITestResult result) {
        String testName = result.getMethod().getMethodName();

        switch (result.getStatus()) {
            case ITestResult.FAILURE -> {
                log.error("FAILED: {}", testName);
                String screenshotPath = ScreenshotUtil.capture(DriverManager.getDriver(), testName);
                ExtentReportManager.getCurrentTest()
                        .fail("Test FAILED — Screenshot: " + screenshotPath)
                        .fail(result.getThrowable());
            }
            case ITestResult.SKIP -> {
                log.warn("SKIPPED: {}", testName);
                ExtentReportManager.getCurrentTest().skip("Test SKIPPED: " + result.getThrowable().getMessage());
            }
            default -> {
                log.info("PASSED: {}", testName);
                ExtentReportManager.getCurrentTest().pass("Test PASSED");
            }
        }

        DriverManager.quitDriver();
        log.info("---- Finished test: {} ----", testName);
    }
}
