package com.saisowmya.automation.utils;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import com.aventstack.extentreports.reporter.configuration.Theme;
import com.saisowmya.automation.config.FrameworkConfig;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * ExtentReportManager — thread-safe HTML report generation.
 *
 * Author: Sai Sowmya Bhogavilli
 */
public class ExtentReportManager {

    private static final Logger log = LogManager.getLogger(ExtentReportManager.class);
    private static ExtentReports extent;
    private static final ThreadLocal<ExtentTest> testThread = new ThreadLocal<>();

    private ExtentReportManager() {}

    public static synchronized void initReports() {
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd_HH-mm-ss"));
        String reportPath = FrameworkConfig.getInstance().getReportPath() + "Report_" + timestamp + ".html";

        ExtentSparkReporter spark = new ExtentSparkReporter(reportPath);
        spark.config().setTheme(Theme.DARK);
        spark.config().setDocumentTitle("UI Automation Report — Sai Sowmya Bhogavilli");
        spark.config().setReportName("Selenium + TestNG Regression Suite");
        spark.config().setTimeStampFormat("MMM dd, yyyy HH:mm:ss");

        extent = new ExtentReports();
        extent.attachReporter(spark);
        extent.setSystemInfo("Author",   "Sai Sowmya Bhogavilli");
        extent.setSystemInfo("Framework","Selenium WebDriver + TestNG");
        extent.setSystemInfo("Browser",  FrameworkConfig.getInstance().getBrowser());
        extent.setSystemInfo("OS",       System.getProperty("os.name"));
        extent.setSystemInfo("Java",     System.getProperty("java.version"));

        log.info("ExtentReport initialised at: {}", reportPath);
    }

    public static ExtentTest createTest(String testName) {
        ExtentTest test = extent.createTest(testName);
        testThread.set(test);
        return test;
    }

    public static void setCurrentTest(ExtentTest test) {
        testThread.set(test);
    }

    public static ExtentTest getCurrentTest() {
        return testThread.get();
    }

    public static synchronized void flushReports() {
        if (extent != null) {
            extent.flush();
            log.info("ExtentReport flushed.");
        }
    }
}
