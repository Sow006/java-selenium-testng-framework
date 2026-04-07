package com.saisowmya.automation.listeners;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.IRetryAnalyzer;
import org.testng.ITestResult;

/**
 * RetryListener — retries failed tests up to MAX_RETRY_COUNT times.
 * Handles transient failures (network flakiness, timing issues).
 *
 * Author: Sai Sowmya Bhogavilli
 */
public class RetryListener implements IRetryAnalyzer {

    private static final Logger log = LogManager.getLogger(RetryListener.class);
    private static final int MAX_RETRY_COUNT = 2;
    private int retryCount = 0;

    @Override
    public boolean retry(ITestResult result) {
        if (retryCount < MAX_RETRY_COUNT) {
            retryCount++;
            log.warn("Retrying test '{}' — attempt {}/{}", result.getName(), retryCount, MAX_RETRY_COUNT);
            return true;
        }
        return false;
    }
}
