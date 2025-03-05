package com.selemiumautomation;

import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;



import java.io.PrintWriter;
import java.util.List;
import java.util.Random;
import java.time.Duration;

public class BasicDetailsForm {

    public static void fill(WebDriver driver, PrintWriter logWriter, String email, String profileImagePath) {
        logWriter.println("📝 Filling 'Basic Details' Form...");

        try {
            Utils.fillTextField(driver, "firstName", "John", logWriter);
            Utils.fillTextField(driver, "lastName", "Doe", logWriter);
        } catch (Exception e) {
            logWriter.println("⚠️ Error filling name fields: " + e.getMessage());
        }

        try {
            // Select Country
            Select countrySelect = new Select(driver.findElement(By.xpath("(//select[@aria-hidden='true'])[1]")));
            countrySelect.selectByVisibleText("India");
            logWriter.println("✔ Selected Country: India");
        } catch (Exception e) {
            logWriter.println("⚠️ Could not select country: " + e.getMessage());
        }

        try {
            // Select Random State
            Select stateSelect = new Select(driver.findElement(By.xpath("(//select[@aria-hidden='true'])[2]")));
            List<WebElement> stateOptions = stateSelect.getOptions();
            if (!stateOptions.isEmpty()) {
                int randomIndex = new Random().nextInt(stateOptions.size());
                stateSelect.selectByIndex(randomIndex);
                logWriter.println("✔ Selected State: " + stateOptions.get(randomIndex).getText());
            }
        } catch (Exception e) {
            logWriter.println("⚠️ Could not select state: " + e.getMessage());
        }

        try {
            // Select Random City
            Select citySelect = new Select(driver.findElement(By.xpath("(//select[@aria-hidden='true'])[3]")));
            List<WebElement> cityOptions = citySelect.getOptions();
            if (!cityOptions.isEmpty()) {
                int randomIndex = new Random().nextInt(cityOptions.size());
                citySelect.selectByIndex(randomIndex);
                logWriter.println("✔ Selected City: " + cityOptions.get(randomIndex).getText());
            }
        } catch (Exception e) {
            logWriter.println("⚠️ Could not select city: " + e.getMessage());
        }

        try {
            // Phone verification logic
            WebElement phoneField = driver.findElement(By.xpath("//input[@type='tel']"));
            if (phoneField.getAttribute("value").isEmpty() || phoneField.getAttribute("value").equals("+91")) {
                phoneField.clear();
                phoneField.sendKeys("7043390661");
                Thread.sleep(5000);

                WebElement verifyButton = driver.findElement(By.xpath("//button[normalize-space()='VERIFY']"));
                verifyButton.click();
                logWriter.println("✔ Clicked VERIFY button.");
            }
        } catch (Exception e) {
            logWriter.println("⚠️ Could not verify phone number: " + e.getMessage());
        }

        try {
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
            WebElement fileInput = wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//input[@type='file']")));
            if (!fileInput.isDisplayed()) {
                ((JavascriptExecutor) driver).executeScript("arguments[0].style.display='block';", fileInput);
            }
            fileInput.sendKeys(profileImagePath);
            logWriter.println("✔ Uploaded Profile Image.");
        } catch (Exception e) {
            logWriter.println("⚠️ Could not upload profile image: " + e.getMessage());
        }

        try {
            SocialMediaLinks.fill(driver, logWriter);
        } catch (Exception e) {
            logWriter.println("⚠️ Error filling social media links: " + e.getMessage());
        }

        Utils.submitForm(driver, logWriter, "Basic Details");
    }
}
