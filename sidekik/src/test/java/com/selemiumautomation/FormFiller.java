package com.selemiumautomation;

import org.apache.commons.io.FileUtils;

import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.util.Date;

public class FormFiller {

    public static void fillForm(WebDriver driver, PrintWriter logWriter, String profileImagePath, String email) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(15));
        int retryCount = 0;
        final int maxRetries = 5; // Prevent infinite loop

        try {
            logWriter.println("🔹 Clicking 'Complete your profile' button...");
            WebElement completeProfileButton = driver.findElement(By.xpath("//button[contains(text(),'Complete your profile')]"));
            completeProfileButton.click();
            Thread.sleep(5000); // Allow time for page transition
        } catch (Exception e) {
            logWriter.println("❌ Error clicking 'Complete your profile' button: " + e.getMessage());
            return;
        }

        logWriter.println("🔍 Checking which form is displayed...");

        while (retryCount < maxRetries) {
            logWriter.println("🔍 Waiting for the next form... (Attempt " + (retryCount + 1) + "/" + maxRetries + ")");

            try {
                wait.until(ExpectedConditions.or(
                    ExpectedConditions.presenceOfElementLocated(By.name("firstName")),
                    ExpectedConditions.presenceOfElementLocated(By.name("shortIntro")),
                    ExpectedConditions.presenceOfElementLocated(By.name("bankName")),
                    ExpectedConditions.presenceOfElementLocated(By.xpath(
                            "//div[contains(@class, 'flex') and contains(@class, 'justify-center') and contains(@class, 'items-center') and contains(@class, 'h-[80%]')]")),
                    ExpectedConditions.presenceOfElementLocated(By.xpath(
                        "//div[contains(@class, 'flex') and contains(@class, 'flex-col') and contains(@class, 'justify-center') and contains(@class, 'py-12') and contains(@class, 'cursor-pointer')]"))));
                
                if (Utils.isElementPresent(driver, By.name("firstName"))) {
                    logWriter.println("✅ Detected 'Basic Details' Form.");
                    BasicDetailsForm.fill(driver, logWriter, email, profileImagePath);
                } else if (Utils.isElementPresent(driver, By.name("shortIntro"))) {
                    logWriter.println("✅ Detected 'What You Do' Form.");
                    WhatYouDoForm.fill(driver, logWriter);
                } else if (Utils.isElementPresent(driver, By.name("bankName"))) {
                    logWriter.println("✅ Detected 'Payment Info' Form.");
                    PaymentInfoForm.fill(driver, logWriter);
                } else if (Utils.isElementPresent(driver, By.xpath(
                        "//div[contains(@class, 'flex') and contains(@class, 'justify-center') and contains(@class, 'items-center') and contains(@class, 'h-[80%]')]"))) {
                    logWriter.println("✅ Detected 'My Portfolio' Form.");
                    PortfolioForm.fill(driver, logWriter);
                } else if (Utils.isElementPresent(driver, By.xpath(
                    "//div[contains(@class, 'flex') and contains(@class, 'flex-col') and contains(@class, 'justify-center') and contains(@class, 'py-12') and contains(@class, 'cursor-pointer')]"))){
                    logWriter.println("✅ Detected 'My Package' Form.");
                    PackageForm.fill(driver, logWriter);
                }else {
                    retryCount++;
                    logWriter.println("⚠️ No recognizable form detected. Retrying in 2 seconds...");
                    Thread.sleep(2000);
                    continue;
                }

                // Reset retry count after a successful form is filled
                retryCount = 0;
                Thread.sleep(3000);

                // Check if more forms exist
                if (!Utils.isElementPresent(driver, By.name("firstName")) &&
                        !Utils.isElementPresent(driver, By.name("shortIntro")) &&
                        !Utils.isElementPresent(driver, By.name("bankName")) &&
                        !Utils.isElementPresent(driver, By.xpath(
                                "//div[contains(@class, 'flex') and contains(@class, 'justify-center') and contains(@class, 'items-center') and contains(@class, 'h-[80%]')]"))&&
                        !Utils.isElementPresent(driver, By.xpath(
                                "//div[contains(@class, 'flex') and contains(@class, 'flex-col') and contains(@class, 'justify-center') and contains(@class, 'py-12') and contains(@class, 'cursor-pointer')]"))) {
                    logWriter.println("✅ All forms completed. Logging out...");
                    logout(driver, logWriter);
                    break;
                }

            } catch (Exception e) {
                logWriter.println("❌ Error detecting form: " + e.getMessage());
                retryCount++;
            }
        }

        if (retryCount >= maxRetries) {
            logWriter.println("❌ Max retries reached. No valid form detected. Attempting logout...");
            logout(driver, logWriter);
        }
    }

    public static void logout(WebDriver driver, PrintWriter logWriter) {
        try {
            logWriter.println("🔹 Logging out...");
            WebElement profileDropdown = driver.findElement(By.xpath("//img[contains(@class, 'rounded-full') and contains(@class, 'object-cover')]"));
            profileDropdown.click();
            WebElement logoutButton = driver.findElement(By.xpath("//button[contains(text(),'Log Out')]"));
            logoutButton.click();
            logWriter.println("✅ Successfully logged out.");
        } catch (Exception e) {
            logWriter.println("❌ Error logging out: " + e.getMessage());
        }
    }

    public static void takeScreenshot(WebDriver driver, String testName) {
       String timestamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String fileName = "Screenshots/" + testName + "_" + timestamp + ".png";

        try {
            // Take screenshot
            File screenshot = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
            // Save screenshot to desired location
            FileUtils.copyFile(screenshot, new File(fileName));
            System.out.println("Screenshot saved: " + fileName);
        } catch (IOException e) {
            System.err.println("Error while saving screenshot: " + e.getMessage());
        }
    }
}