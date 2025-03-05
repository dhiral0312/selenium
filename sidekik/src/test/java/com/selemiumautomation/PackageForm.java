package com.selemiumautomation;

import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.io.PrintWriter;
import java.util.List;
import java.util.Random;
import java.time.Duration;

public class PackageForm {
    public static void fill(WebDriver driver, PrintWriter logWriter) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        try {

            // Wait for the <div> containing the SVG to be clickable
            WebElement addNewPackage = wait.until(ExpectedConditions.elementToBeClickable(
                    By.xpath(
                            "//div[contains(@class, 'flex') and contains(@class, 'flex-col') and contains(@class, 'justify-center') and contains(@class, 'py-12') and contains(@class, 'cursor-pointer')]")));
            addNewPackage.click();
            Thread.sleep(1000); // Give time for the fields to appear
            logWriter.println("✅ Clicked on 'Add New Package' button.");
            String randomString = Utils.randomString();
            Utils.fillTextField(driver, "title", randomString, logWriter);
            // Utils.fillTextField(driver, "title", "Random", logWriter);
            Utils.fillTextField(driver, "subTitle", randomString, logWriter);
            WebElement textField = wait.until(ExpectedConditions.elementToBeClickable(
                    By.xpath("//div[@contenteditable='true']//p[@data-placeholder='Type something...']")));
            textField.sendKeys("ASED");
            try {
                WebElement priceInput = wait
                        .until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//input[@name='price']")));

                // Clear existing value
                priceInput.clear();

                // Enter new value
                priceInput.sendKeys("50");

                logWriter.println("✔ Entered Price: 50");
            } catch (Exception e) {
                logWriter.println("❌ Error entering Price: " + e.getMessage());
            }

            // Selecting Service
            try {
                WebElement serviceDropdown = wait
                        .until(ExpectedConditions.elementToBeClickable(By.xpath("(//button[@role='combobox'])[1]")));
                serviceDropdown.click();

                // Wait for dropdown to expand
                wait.until(ExpectedConditions.attributeToBe(serviceDropdown, "aria-expanded", "true"));

                // Fetch all service options
                List<WebElement> serviceOptions = wait
                        .until(ExpectedConditions.visibilityOfAllElementsLocatedBy(By.xpath("//div[@role='option']")));

                if (!serviceOptions.isEmpty()) {
                    int randomIndex = new Random().nextInt(serviceOptions.size());
                    WebElement selectedService = serviceOptions.get(randomIndex);
                    selectedService.click();
                    logWriter.println("✔ Selected Service: " + selectedService.getText());
                } else {
                    logWriter.println("⚠️ No service options found.");
                }
            } catch (Exception e) {
                logWriter.println("❌ Error selecting service in Package form: " + e.getMessage());
            }
            Utils.fillTextField(driver, "duration", "20", logWriter);
            // Selecting Duration
            try {
                WebElement durationDropdown = wait
                        .until(ExpectedConditions.elementToBeClickable(By.xpath("(//button[@role='combobox'])[2]")));
                durationDropdown.click();

                // Wait for dropdown to expand
                wait.until(ExpectedConditions.attributeToBe(durationDropdown, "aria-expanded", "true"));

                // Fetch all duration options
                List<WebElement> durationOptions = wait
                        .until(ExpectedConditions.visibilityOfAllElementsLocatedBy(By.xpath("//div[@role='option']")));

                if (!durationOptions.isEmpty()) {
                    int randomIndex = new Random().nextInt(durationOptions.size());
                    WebElement selectedDuration = durationOptions.get(randomIndex);
                    selectedDuration.click();
                    logWriter.println("✔ Selected Duration: " + selectedDuration.getText());
                } else {
                    logWriter.println("⚠️ No duration options found.");
                }
            } catch (Exception e) {
                logWriter.println("❌ Error selecting duration: " + e.getMessage());
            }

            // Toggle "Public" Button
            WebElement toggleSwitch = wait
                    .until(ExpectedConditions.elementToBeClickable(By.xpath("(//button[@role='switch'])[1]")));
            if ("false".equals(toggleSwitch.getAttribute("aria-checked"))) {
                toggleSwitch.click();
                logWriter.println("📌 Enabled Public the Toggle Switch.");
            } else {
                logWriter.println("📌 Toggle Switch is already enabled.");
            }

            // Toggle "Popular" Button
            WebElement toggleSwitch2 = wait
                    .until(ExpectedConditions.elementToBeClickable(By.xpath("(//button[@role='switch'])[2]")));
            if ("false".equals(toggleSwitch2.getAttribute("aria-checked"))) {
                toggleSwitch2.click();
                logWriter.println("📌 Enabled Popular the Toggle Switch.");
            } else {
                logWriter.println("📌 Toggle Switch is already enabled.");
            }

            try {
                WebElement submitButton = wait.until(ExpectedConditions.elementToBeClickable(
                        By.xpath("//button[contains(@class, 'bg-sk-red') and contains(text(), 'Save')]")));

                submitButton.click();
                logWriter.println("📌 Clicked 'Submit' button.");

                // Optional: Wait for confirmation message or next page load
                Thread.sleep(2000);
            } catch (Exception e) {
                logWriter.println("❌ Error clicking Submit: " + e.getMessage());
            }

            WebElement addMilestone = wait.until(
                    ExpectedConditions.elementToBeClickable(By.xpath("//button[contains(text(), 'Add Milestone')]")));
            addMilestone.click();
            Utils.fillTextField(driver, "name", randomString, logWriter);
            try {
                WebElement description = wait
                        .until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//input[@name='description']")));

                // Clear existing value
                description.clear();

                // Enter new value
                description.sendKeys(randomString);

                logWriter.println("✔ Entered Description: " + description.getText());
            } catch (Exception e) {
                logWriter.println("❌ Error entering Description: " + e.getMessage());
            }
            // Utils.fillTextField(driver, "percentage", "100", logWriter);
            try {
                WebElement percentage = wait
                        .until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//input[@name='percentage']")));

                // Clear existing value
                percentage.clear();

                // Enter new value
                percentage.sendKeys("100");

                logWriter.println("✔ Entered Percentage: " + percentage.getText());
            } catch (Exception e) {
                logWriter.println("❌ Error entering Precentage: " + e.getMessage());
            }
            WebElement toggleSwitch3 = wait
                    .until(ExpectedConditions.elementToBeClickable(By.xpath("(//button[@role='switch'])[1]")));
            if ("false".equals(toggleSwitch3.getAttribute("aria-checked"))) {
                toggleSwitch3.click();
                logWriter.println("📌 Enabled Popular the Toggle Switch.");
            } else {
                logWriter.println("📌 Toggle Switch is already enabled.");
            }
            try {
                WebElement submitButton = wait.until(ExpectedConditions.elementToBeClickable(
                        By.xpath("//button[contains(@class, 'bg-[#0D2555]') and contains(@class, 'text-white')]")));
                submitButton.click();
                logWriter.println("📌 Clicked 'Submit' button.");

                // Optional: Wait for confirmation message or next page load
                Thread.sleep(2000);
            } catch (Exception e) {
                logWriter.println("❌ Error clicking Submit: " + e.getMessage());
            }
            Utils.submitForm(driver, logWriter, "MileStones");

            // Payment Methods
            // Locate the button element that acts as the checkbox for Stripe
            try{
            WebElement stripeCheckboxButton = wait.until(ExpectedConditions.elementToBeClickable(
                    By.xpath(
                            "//div[contains(@class,'flex-row-reverse')]//label[text()='Stripe']/following-sibling::button[@role='checkbox']")));

            // Log the initial state using the aria-checked attribute
            String initialState = stripeCheckboxButton.getAttribute("aria-checked");
            logWriter.println("Initial Stripe Checkbox state: " + initialState);

            // If not selected, click the checkbox using Actions
            if (!"true".equalsIgnoreCase(initialState)) {
                Actions actions = new Actions(driver);
                actions.moveToElement(stripeCheckboxButton).click().perform();
                // Wait until the aria-checked attribute becomes "true"
                wait.until(ExpectedConditions.attributeToBe(stripeCheckboxButton, "aria-checked", "true"));
                logWriter.println("Checkbox Stripe has been selected.");
            } else {
                logWriter.println("Checkbox Stripe is already selected.");
            }

            // Log the final state
            String finalState = stripeCheckboxButton.getAttribute("aria-checked");
            logWriter.println("Final Stripe Checkbox state: " + finalState);

            Utils.submitForm(driver, logWriter, "Payment Methods");
        } catch (Exception e) {
            logWriter.println("❌ Error selecting Payment Methods: " + e.getMessage());
        }
        Utils.submitForm(driver, logWriter, "Package");
        } catch (

        Exception e) {
            logWriter.println("❌ Error filling Package form: " + e.getMessage());
        }
    }

}
