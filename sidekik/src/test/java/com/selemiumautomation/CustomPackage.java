package com.selemiumautomation;

import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.io.PrintWriter;
import java.time.Duration;
import java.util.List;
import java.util.Random;

public class CustomPackage {

    public static void handleCustomPackage(WebDriver driver, PrintWriter logWriter) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(15));

        try {
            logWriter.println("🔹 Clicking 'Custom Packages' button...");
            WebElement customPackagesButton = driver.findElement(By.xpath("//a[contains(text(),'Custom Packages')]"));
            customPackagesButton.click();
            Thread.sleep(5000); // Allow time for transition

            // Click "Add New Package"
            WebElement addNewPackageButton = wait.until(ExpectedConditions.elementToBeClickable(
                    By.xpath(
                            "//button[contains(@class, 'bg-sk-blue') and contains(@class, 'text-background') and contains(@class, 'flex') and contains(text(), 'Add New Package')]")));
            addNewPackageButton.click();
            Thread.sleep(1000); // Give time for the fields to appear
            logWriter.println("✅ Clicked on 'Add New Package' button.");
            String randomString = Utils.randomString();
            Utils.fillTextField(driver, "title", randomString, logWriter);
            // Utils.fillTextField(driver, "title", "Random", logWriter);
            Utils.fillTextField(driver, "subTitle", randomString, logWriter);
            WebElement textField = wait.until(ExpectedConditions.elementToBeClickable(
                    By.xpath("//div[@contenteditable='true']//p[@data-placeholder='Enter Inclusions']")));
            textField.sendKeys("ASED");
            try {
                WebElement priceInput = wait.until(ExpectedConditions.visibilityOfElementLocated(
                        By.xpath("//input[@placeholder='Enter Price' and @type='number']")));
                priceInput.clear();
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
                logWriter.println("❌ Error selecting service in Custom Package form: " + e.getMessage());
            }
            // ✅ Select Start Date (2 days from today)
            Utils.selectDate(driver, wait,
                    "(//button[contains(@class, 'text-left') and contains(@class, 'border-gray-300')])[1]", 2,
                    logWriter);

            // ✅ Select End Date (5 days from today)
            Thread.sleep(1000); // Ensures Start Date picker is closed before opening End Date
            Utils.selectDate(driver, wait,
                    "(//button[contains(@class, 'text-left') and contains(@class, 'border-gray-300')])[2]", 5,
                    logWriter);

            // Add New Buyer
            WebElement addNewBuyer = wait.until(
                    ExpectedConditions.elementToBeClickable(By.xpath("//button[contains(text(),'Add New Buyer')]")));
            addNewBuyer.click();
            // firstName
            Utils.fillTextField(driver, "firstName", randomString, logWriter);
            // lastName
            Utils.fillTextField(driver, "lastName", randomString, logWriter);

            Utils.fillTextField(driver, "email", randomString + "@gmail.com", logWriter);
            // ✅ Select Country (Dropdown 2)
            Utils.selectDropdown(driver, wait, "(//button[@role='combobox'])[2]", "Country", logWriter);

            // ✅ Select State (Dropdown 3)
            Utils.selectDropdown(driver, wait, "(//button[@role='combobox'])[3]", "State", logWriter);

            // ✅ Select City (Dropdown 4)
            Utils.selectDropdown(driver, wait, "(//button[@role='combobox'])[4]", "City", logWriter);
            // ✅ Select Language (Ensures dropdown closes after selection)
            Utils.selectLanguage(driver, wait, "//input[@role='combobox' and contains(@class,'bg-transparent')]",
                    logWriter);

            // ✅ Now, enter Company Name after ensuring language selection is complete
            try {
                WebElement companyNameField = wait
                        .until(ExpectedConditions.elementToBeClickable(By.name("companyName")));
                companyNameField.click();
                logWriter.println("🏢 Clicked on Company Name field.");
                // Fill in the company name
                Utils.fillTextField(driver, "companyName", randomString, logWriter);
                logWriter.println("🏢 Entered Company Name: " + randomString);
            } catch (Exception e) {
                logWriter.println("❌ Error entering Company Name: " + e.getMessage());
            }

            WebElement activeElement = driver.switchTo().activeElement();
            activeElement.sendKeys(Keys.TAB, Keys.TAB);

            // 🔹 Press Enter to click Save button
            activeElement = driver.switchTo().activeElement();
            activeElement.sendKeys(Keys.ENTER);

            // Alternative: Click it directly if needed
            activeElement.click();
            logWriter.println("✔ Clicked Save button using Tab + Enter method.");

            Thread.sleep(3000); // Allow time for the form to submit

            // Step 1: Wait for dropdown button and click it

            try {
                // Step 1: Wait for dropdown button and click it

                WebElement buyerDropdown = wait.until(ExpectedConditions.elementToBeClickable(
                        By.xpath("(//button[@role='combobox' and contains(@class, 'rounded-md')])[last()]")));

                ((JavascriptExecutor) driver).executeScript("arguments[0].click();", buyerDropdown);
                logWriter.println("📌 Clicked Buyer dropdown.");

                // Step 2: Wait for dropdown to expand
                wait.until(ExpectedConditions.attributeToBe(buyerDropdown, "aria-expanded", "true"));

                // Step 3: Fetch all visible buyer options
                List<WebElement> buyerOptions = wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(
                        By.xpath("//div[@role='option']")));

                if (!buyerOptions.isEmpty()) {
                    WebElement selectedBuyer = null;

                    for (WebElement option : buyerOptions) {
                        // Get text using JavaScript to avoid empty values
                        String optionText = (String) ((JavascriptExecutor) driver)
                                .executeScript("return arguments[0].textContent.trim();", option);

                        if (optionText.contains(randomString)) {
                            selectedBuyer = option;
                            break;
                        }
                    }
                    if (selectedBuyer != null) {
                        Thread.sleep(500);
                        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", selectedBuyer);
                        logWriter.println("✔ Selected Buyer: " + selectedBuyer.getText());
                    } else {
                        logWriter.println("⚠️ Buyer with name containing '" + randomString + "' not found.");
                    }
                } else {
                    logWriter.println("⚠️ No buyer options found.");
                }
            } catch (Exception e) {
                logWriter.println("❌ Error selecting buyer: " + e.getMessage());
            }
            Thread.sleep(2000); // Allow time for the form to submit
            // save custom package
            try {
                // Locate the Submit button
                WebElement submitButton = wait.until(ExpectedConditions.elementToBeClickable(
                        By.xpath("//button[contains(@class, 'bg-sk-red') and contains(text(), 'Save')]")));

                // Wait for any blocking toast notifications to disappear
                wait.until(ExpectedConditions.invisibilityOfElementLocated(By.xpath("//li[@role='status']")));

                // Scroll the button into view using Actions (instead of JavaScript)
                Actions actions = new Actions(driver);
                actions.moveToElement(submitButton).perform();

                // Give a short delay before clicking (to allow smooth interaction)
                Thread.sleep(500);

                // Click the Submit button normally
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
                logWriter.println("📌 Enabled UpFront Payment the Toggle Switch.");
            } else {
                logWriter.println("📌 Toggle Switch is already enabled for UpFront Payment.");
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
            try {
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
            Utils.submitForm(driver, logWriter, "CustomPackage");
        } catch (Exception e) {
            logWriter.println("❌ Error handling Custom Package: " + e.getMessage());
            Utils.takeScreenshot(driver, "CustomPackageFailure");
        }
    }
}
