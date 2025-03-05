package com.selemiumautomation;

import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;


import java.io.PrintWriter;
import java.util.List;
import java.util.Random;
import java.time.Duration;

public class PortfolioForm {

    public static void fill(WebDriver driver, PrintWriter logWriter) {
        try {
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

            // Click "Add New Portfolio"
            WebElement portfolioContainer = wait.until(ExpectedConditions.elementToBeClickable(
                    By.xpath("//div[contains(@class, 'flex') and contains(@class, 'justify-center') and contains(@class, 'items-center') and contains(@class, 'h-[80%]')]")));
            portfolioContainer.click();
            Thread.sleep(1000);
            logWriter.println("✅ Clicked on 'Add New Portfolio' button.");

            // Select a random Material Type
            WebElement materialTypeDropdown = wait.until(ExpectedConditions.elementToBeClickable(By.name("materialType")));
            Select materialTypeSelect = new Select(materialTypeDropdown);
            List<WebElement> options = materialTypeSelect.getOptions();

            if (options.size() > 1) {
                Random random = new Random();
                int randomIndex = random.nextInt(options.size());
                materialTypeSelect.selectByIndex(randomIndex);
                logWriter.println("📌 Selected 'Material Type': " + options.get(randomIndex).getText());
            } else {
                logWriter.println("⚠️ No options available in 'Material Type' dropdown.");
            }

            // Enter Material Name
            WebElement materialName = wait.until(ExpectedConditions.visibilityOfElementLocated(By.name("materialName")));
            materialName.sendKeys("Sample Portfolio Item");
            logWriter.println("📌 Entered Material Name: Sample Portfolio Item");

            try {
                // Upload Cover Image
                @SuppressWarnings("unused")
                WebElement coverImageUpload = wait.until(ExpectedConditions.presenceOfElementLocated(
                        By.xpath("//span[contains(text(), 'Upload Cover Image')]")));
                Thread.sleep(1000);
                WebElement coverImageInput = wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//input[@type='file']")));
                if (!coverImageInput.isDisplayed()) {
                    ((JavascriptExecutor) driver).executeScript("arguments[0].style.display='block';", coverImageInput);
                }
                coverImageInput.sendKeys("C:\\Users\\admin6\\Downloads\\web1.jpg"); // Update with actual path
                logWriter.println("📌 Uploaded Cover Image.");

                // Upload File
                @SuppressWarnings("unused")
                WebElement fileUpload = wait.until(ExpectedConditions.presenceOfElementLocated(
                        By.xpath("//span[contains(text(), 'Upload Files')]")));
                Thread.sleep(1000);
                WebElement fileInput = wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//input[@type='file']")));
                if (!fileInput.isDisplayed()) {
                    ((JavascriptExecutor) driver).executeScript("arguments[0].style.display='block';", fileInput);
                }
                fileInput.sendKeys("C:\\Users\\admin6\\Downloads\\web2.jpg"); // Update with actual path
                logWriter.println("📌 Uploaded Portfolio File.");

            } catch (Exception e) {
                logWriter.println("❌ Error uploading images or files: " + e.getMessage());
            }

            // Enter Portfolio Link
            WebElement linkInput = wait.until(ExpectedConditions.visibilityOfElementLocated(By.name("links.0.url")));
            linkInput.sendKeys("https://example.com");
            logWriter.println("📌 Entered Portfolio Link: https://example.com");

            // Toggle Switch
            WebElement toggleSwitch = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//button[@role='switch']")));
            if ("false".equals(toggleSwitch.getAttribute("aria-checked"))) {
                toggleSwitch.click();
                logWriter.println("📌 Enabled the Toggle Switch.");
            } else {
                logWriter.println("📌 Toggle Switch is already enabled.");
            }

            logWriter.println("✅ Successfully filled the 'My Portfolio' section.");

            // Click Submit Button
            try {
                WebElement submitButton = wait.until(ExpectedConditions.elementToBeClickable(
                        By.xpath("//button[contains(@class, 'bg-sk-red') and contains(text(), 'Submit')]")));
                submitButton.click();
                logWriter.println("📌 Clicked 'Submit' button.");
                Thread.sleep(2000);
            } catch (Exception e) {
                logWriter.println("❌ Error clicking Submit: " + e.getMessage());
            }

            Utils.submitForm(driver, logWriter, "My Portfolio");

        } catch (Exception e) {
            logWriter.println("❌ Error filling My Portfolio: " + e.getMessage());
        }
    }
}
