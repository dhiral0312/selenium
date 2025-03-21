package com.selemiumautomation;

import java.io.PrintWriter;
import java.time.Duration;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class Summary {
    public static void summaryForm(WebDriver driver, PrintWriter logWriter) {
         WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));
        logWriter.println("📝 Clicking on 'Go to Dashboard'");
        try {
            WebElement button = wait.until(ExpectedConditions.elementToBeClickable(By.xpath(
                "//button[contains(@class, 'inline-flex') and contains(@class, 'items-center') and contains(text(), 'Go to Dashboard')]")));
            button.click();
            
            logWriter.println("✅ Successfully clicked on 'Go to Dashboard'");
        } catch (Exception e) {
            logWriter.println("❌ Error filling 'Summary' Form: " + e.getMessage());
        }
    }
    
}
