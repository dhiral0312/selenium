package com.selemiumautomation;

import java.io.PrintWriter;
import java.time.Duration;

import java.util.List;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class Functionallity {

    public static void FunctionallityHandler(WebDriver driver, PrintWriter logWriter) {
        try {
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

            // Click on "Custom Packages"
            WebElement customPackagesButton = wait.until(ExpectedConditions.elementToBeClickable(
                    By.xpath("//a[contains(text(),'Custom Packages')]")));
            customPackagesButton.click();
            logWriter.println("Navigated to Custom Packages");

            for (int attempt = 0; attempt < 3; attempt++) { // Retry up to 3 times if stale element issue occurs
                try {
                    // **Fetch button containers dynamically to avoid stale elements**
                    List<WebElement> buttonContainers = wait.until(
                            ExpectedConditions.presenceOfAllElementsLocatedBy(
                                    By.cssSelector(".flex.flex-row.gap-2.items-center")));

                    for (WebElement container : buttonContainers) {
                        try {
                            // **Click the Eye button (View)**
                            WebElement eyeButton = wait.until(ExpectedConditions.elementToBeClickable(
                                    container.findElement(By.cssSelector(".lucide-eye"))));
                            eyeButton.click();
                            logWriter.println("✅ Eye button clicked - Viewing details");
                            Thread.sleep(1000);

                            // **Wait for Back button and click**
                            WebElement backButton = wait.until(ExpectedConditions.elementToBeClickable(
                                    driver.findElement(By.xpath(
                                            "//button[contains(@class, 'bg-sk-blue') and contains(text(), 'Back To My Packages')]"))));
                            backButton.click();
                            logWriter.println("🔙 Navigated back from view");
                            Thread.sleep(1000);

                            // // **Re-fetch containers after navigation**
                            // buttonContainers = wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(
                            //         By.cssSelector(".flex.flex-row.gap-2.items-center")));
                            // container = buttonContainers.get(0); // Get first available container

                            // // **Click the Trash button (Delete)**
                            // WebElement trashButton = wait.until(ExpectedConditions.elementToBeClickable(
                            //         container.findElement(By.cssSelector(".lucide-trash2"))));
                            // trashButton.click();
                            // logWriter.println("🗑️ Trash button clicked - Deleting item");

                            // // **Confirm deletion**
                            // WebElement deleteButton = wait.until(ExpectedConditions.elementToBeClickable(
                            //         driver.findElement(By.xpath(
                            //                 "//button[contains(@class, 'bg-[#0D2555]') and contains(text(), 'Delete')]"))));
                            // deleteButton.click();
                            // logWriter.println("✅ Item deleted successfully");
                            // Thread.sleep(1000);

                            // // **Click the Pencil button (Edit)**
                            // Thread.sleep(1000);
                            // WebElement pencilButton = wait.until(ExpectedConditions.elementToBeClickable(
                            //         container.findElement(By.cssSelector(".lucide-pencil"))));
                            // pencilButton.click();
                            // logWriter.println("✏️ Pencil button clicked - Editing item");
                            // Thread.sleep(1000);

                        } catch (Exception e) {
                            logWriter.println("⚠️ Error interacting with button container: " + e.getMessage());
                        }
                    }
                    break; // If everything succeeds, break retry loop
                } catch (Exception e) {
                    logWriter.println("🔄 Retrying due to stale element reference: " + e.getMessage());
                    if (attempt == 2)
                        logWriter.println("❌ Final attempt failed!");
                }
            }

            // **Handle Copy-Plus Button (If Present)**
            List<WebElement> copyPlusButtons = driver
                    .findElements(By.cssSelector(".flex.justify-center.items-center .lucide-copy-plus"));
            if (!copyPlusButtons.isEmpty()) {
                for (WebElement copyButton : copyPlusButtons) {
                    wait.until(ExpectedConditions.elementToBeClickable(copyButton)).click();
                    logWriter.println("📋 Copy-Plus button clicked - Copying item");
                    Thread.sleep(1000);

                    // **Enter a New Name**
                    String randomString = Utils.randomString();
                    WebElement name = wait
                            .until(ExpectedConditions.presenceOfElementLocated(By.xpath("//input[@name='title']")));

                    // Get the existing value of the field
                    String existingValue = name.getAttribute("value");

                    if (existingValue != null && !existingValue.isEmpty()) {
                        name.clear(); // Clear field before entering new value
                    }

                    name.sendKeys(randomString);
                    Thread.sleep(1000);

                    // **Click Submit Button with Retry Logic**
                    for (int i = 0; i < 3; i++) {
                        try {
                            WebElement submitButton = wait.until(ExpectedConditions.elementToBeClickable(
                                    By.xpath("//button[contains(@class, 'bg-sk-red') and contains(text(), 'Save')]")));

                            // Ensure no status message is blocking the button
                            wait.until(
                                    ExpectedConditions.invisibilityOfElementLocated(By.xpath("//li[@role='status']")));

                            new Actions(driver).moveToElement(submitButton).perform();
                            Thread.sleep(500);
                            submitButton.click();
                            logWriter.println("📌 Clicked 'Submit' button attempt " + (i + 1));
                            Thread.sleep(2000); // Wait for UI update
                            break; // Break loop if submit succeeds
                        } catch (Exception e) {
                            logWriter.println("⚠️ Attempt " + (i + 1) + " failed: " + e.getMessage());
                        }
                    }
                }
            } else {
                logWriter.println("🔍 No Copy-Plus button found.");
            }

            logWriter.flush();

        } catch (Exception e) {
            logWriter.println("❌ Error in FunctionallityHandler: " + e.getMessage());
            e.printStackTrace();
        }
    }
}