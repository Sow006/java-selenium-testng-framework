package com.saisowmya.automation.utils;

import com.saisowmya.automation.config.FrameworkConfig;
import org.apache.commons.io.FileUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * ScreenshotUtil — captures screenshots on test failure.
 *
 * Author: Sai Sowmya Bhogavilli
 */
public class ScreenshotUtil {

    private static final Logger log = LogManager.getLogger(ScreenshotUtil.class);
    private static final String SCREENSHOT_DIR = FrameworkConfig.getInstance().getScreenshotPath();

    private ScreenshotUtil() {}

    public static String capture(WebDriver driver, String testName) {
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"));
        String fileName  = SCREENSHOT_DIR + testName + "_" + timestamp + ".png";

        try {
            File src  = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
            File dest = new File(fileName);
            FileUtils.copyFile(src, dest);
            log.info("Screenshot saved: {}", fileName);
        } catch (IOException e) {
            log.error("Failed to capture screenshot: {}", e.getMessage());
        }

        return fileName;
    }
}
