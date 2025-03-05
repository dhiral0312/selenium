package com.selemiumautomation;

import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.io.PrintWriter;
import java.time.Duration;

public class SocialMediaLinks {

    public static void fill(WebDriver driver, PrintWriter logWriter) {
        logWriter.println("🔹 Filling Social Media Links...");
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

        try {
            WebElement facebookField = wait.until(ExpectedConditions.presenceOfElementLocated(
                    By.xpath("//input[contains(@id, 'form-item') and @placeholder='Enter your Facebook username']")));
            facebookField.sendKeys("asd");
            logWriter.println("✔ Facebook link filled.");

            WebElement instagramField = wait.until(ExpectedConditions.presenceOfElementLocated(
                    By.xpath("//input[contains(@id, 'form-item') and @placeholder='Enter your Instagram username']")));
            instagramField.sendKeys("lkj");
            logWriter.println("✔ Instagram link filled.");

            WebElement twitterField = wait.until(ExpectedConditions.presenceOfElementLocated(
                    By.xpath("//input[contains(@id, 'form-item') and @placeholder='Enter your X username']")));
            twitterField.sendKeys("mnb");
            logWriter.println("✔ Twitter link filled.");

            WebElement linkedInField = wait.until(ExpectedConditions.presenceOfElementLocated(
                    By.xpath("//input[contains(@id, 'form-item') and @placeholder='Enter your LinkedIn username']")));
            linkedInField.sendKeys("zxc");
            logWriter.println("✔ LinkedIn link filled.");

        } catch (Exception e) {
            logWriter.println("❌ Error filling social media links: " + e.getMessage());
        }
    }
}
