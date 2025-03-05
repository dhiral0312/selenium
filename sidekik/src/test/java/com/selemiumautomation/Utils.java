package com.selemiumautomation;

import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.Select;

import java.io.PrintWriter;
import java.util.List;
import java.util.Random;

public class Utils {

    public static void fillTextField(WebDriver driver, String fieldName, String value, PrintWriter logWriter) {
        try {
            WebElement field = driver.findElement(By.name(fieldName));
            if (field.getAttribute("value").isEmpty()) {
                field.sendKeys(value);
                logWriter.println("✔ Filled " + fieldName + " with: " + value);
            }
        } catch (Exception e) {
            logWriter.println("⚠️ Could not fill " + fieldName + ": " + e.getMessage());
        }
    }

    public static void selectRandomDropdown(WebDriver driver, String fieldName, PrintWriter logWriter) {
        try {
            Select dropdown = new Select(driver.findElement(By.name(fieldName)));
            List<WebElement> options = dropdown.getOptions();
            if (options.size() > 1) {
                int randomIndex = new Random().nextInt(options.size() - 1) + 1;
                dropdown.selectByIndex(randomIndex);
                logWriter.println("✔ Selected " + fieldName + ": " + options.get(randomIndex).getText());
            }
        } catch (Exception e) {
            logWriter.println("⚠️ Could not select " + fieldName + ": " + e.getMessage());
        }
    }

    public static void submitForm(WebDriver driver, PrintWriter logWriter, String formName) {
        try {
            logWriter.println("🔹 Attempting to submit '" + formName + "' Form...");
            WebElement submitButton = driver.findElement(By.xpath("//button[contains(text(),'Save')]"));
            if (submitButton.isEnabled()) {
                submitButton.click();
                logWriter.println("✅ Successfully submitted '" + formName + "' Form.");
                Thread.sleep(10000); // Wait for form submission
            } else {
                logWriter.println("⚠️ '" + formName + "' Save button is disabled. Ensure all fields are filled.");
            }
        } catch (Exception e) {
            logWriter.println("❌ Error submitting '" + formName + "' Form: " + e.getMessage());
        }
    }

    public static boolean isElementPresent(WebDriver driver, By locator) {
        return driver.findElements(locator).size() > 0;
    }

    public static String randomString() {
        Random random = new Random();
        int length = 8;
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < length; i++) {
            int randomChar = random.nextInt(26) + 'a';
            sb.append((char) randomChar);
        }
        return sb.toString();  // Now it returns a String
    }
}
