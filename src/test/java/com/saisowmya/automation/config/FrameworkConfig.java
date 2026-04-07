package com.saisowmya.automation.config;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Centralised configuration reader.
 * Reads from config.properties and allows CLI overrides via -D system properties.
 *
 * Author: Sai Sowmya Bhogavilli
 */
public class FrameworkConfig {

    private static final Logger log = LogManager.getLogger(FrameworkConfig.class);
    private static final Properties properties = new Properties();
    private static FrameworkConfig instance;

    private FrameworkConfig() {
        loadProperties();
    }

    public static synchronized FrameworkConfig getInstance() {
        if (instance == null) {
            instance = new FrameworkConfig();
        }
        return instance;
    }

    private void loadProperties() {
        String env = System.getProperty("env", "staging");
        String configFile = "config/config-" + env + ".properties";

        try (InputStream is = getClass().getClassLoader().getResourceAsStream(configFile)) {
            if (is != null) {
                properties.load(is);
                log.info("Loaded config for environment: {}", env);
            } else {
                log.warn("Config file not found: {}, using defaults", configFile);
            }
        } catch (IOException e) {
            log.error("Failed to load config: {}", e.getMessage());
        }
    }

    /**
     * System property overrides config file value — enables CI parameterisation.
     */
    private String get(String key) {
        return System.getProperty(key, properties.getProperty(key, ""));
    }

    public String getBrowser() {
        return get("browser").isEmpty() ? "chrome" : get("browser").toLowerCase();
    }

    public String getBaseUrl() {
        return get("base.url");
    }

    public boolean isHeadless() {
        return Boolean.parseBoolean(System.getProperty("headless", properties.getProperty("headless", "false")));
    }

    public int getImplicitWait() {
        return Integer.parseInt(properties.getProperty("implicit.wait", "10"));
    }

    public int getExplicitWait() {
        return Integer.parseInt(properties.getProperty("explicit.wait", "20"));
    }

    public int getPageLoadTimeout() {
        return Integer.parseInt(properties.getProperty("page.load.timeout", "30"));
    }

    public String getScreenshotPath() {
        return properties.getProperty("screenshot.path", "screenshots/");
    }

    public String getReportPath() {
        return properties.getProperty("report.path", "test-output/reports/");
    }
}
