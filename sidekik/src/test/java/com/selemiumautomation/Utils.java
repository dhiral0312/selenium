package com.selemiumautomation;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Date;
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

    public static void takeScreenshot(WebDriver driver, String testName) {
        String timestamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String fileName = "Screenshots/" + testName + "_" + timestamp + ".png";

        try {
            // Take screenshot
            File screenshot = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
            FileUtils.copyFile(screenshot, new File("./screenshots/test-failure.png"));
            // Save screenshot to desired location
            FileUtils.copyFile(screenshot, new File(fileName));
            System.out.println("Screenshot saved: " + fileName);
        } catch (IOException e) {
            System.err.println("Error while saving screenshot: " + e.getMessage());
        }
    }

    public static void selectDate(WebDriver driver, WebDriverWait wait, String dateButtonXpath, int daysToAdd,
            PrintWriter logWriter) {
        try {
            // ✅ Click on Date Picker Button
            WebElement dateButton = wait.until(ExpectedConditions.elementToBeClickable(By.xpath(dateButtonXpath)));
            dateButton.click();
            logWriter.println("📅 Clicked on Date Picker Button.");

            // ✅ Wait for Date Picker Dialog to Open
            @SuppressWarnings("unused")
            WebElement datePickerDialog = wait
                    .until(ExpectedConditions.presenceOfElementLocated(By.xpath("//div[@role='dialog']")));
            logWriter.println("📅 Date Picker Dialog Opened.");

            // ✅ Calculate Target Date
            LocalDate targetDate = LocalDate.now().plusDays(daysToAdd);
            String targetDay = targetDate.format(DateTimeFormatter.ofPattern("d")); // Extracts only the day number

            // ✅ Get All Selectable Dates
            List<WebElement> dateOptions = wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(
                    By.xpath("//button[contains(@class, 'h-8') and not(contains(@aria-label, 'Today'))]")));

            boolean dateSelected = false;

            for (WebElement dateOption : dateOptions) {
                String dayText = dateOption.getText().trim();
                if (dayText.equals(targetDay)) {
                    try {
                        Actions actions = new Actions(driver);
                        actions.moveToElement(dateOption).pause(300).click().perform(); // First Click Attempt
                        Thread.sleep(500);

                        // ✅ Confirm Selection by Checking the Field Value
                        if (!dateButton.getText().contains(targetDay)) {
                            actions.moveToElement(dateOption).pause(300).click().perform();
                            Thread.sleep(500);
                        }

                        logWriter.println("✔ Selected Date: " + targetDay);
                        dateSelected = true;
                        break;
                    } catch (Exception clickError) {
                        logWriter.println("⚠️ Could not click on date, retrying.");
                    }
                }
            }

            // ✅ If Preferred Date Isn’t Found, Select a Random Available Date
            if (!dateSelected) {
                logWriter.println("⚠️ Target date not found. Selecting a random available date.");
                if (!dateOptions.isEmpty()) {
                    WebElement randomDate = dateOptions.get(new Random().nextInt(dateOptions.size()));
                    ((JavascriptExecutor) driver).executeScript("arguments[0].click();", randomDate);
                    logWriter.println("✔ Selected Random Available Date: " + randomDate.getText());
                } else {
                    logWriter.println("⚠️ No selectable dates found.");
                }
            }

            // ✅ Force Close Date Picker if It Stays Open
            try {
                WebElement body = driver.findElement(By.tagName("body"));
                body.click();
                Thread.sleep(500);
            } catch (Exception ignore) {
            }

        } catch (Exception e) {
            logWriter.println("❌ Error selecting date: " + e.getMessage());
        }
    }

    // ✅ Generic method to select a random option from a dropdown
    public static void selectDropdown(WebDriver driver, WebDriverWait wait, String dropdownXpath, String dropdownName,
            PrintWriter logWriter) {
        try {
            // Click the dropdown to expand it
            WebElement dropdown = wait.until(ExpectedConditions.elementToBeClickable(By.xpath(dropdownXpath)));
            dropdown.click();
            logWriter.println("📌 Clicked on " + dropdownName + " dropdown.");

            // Wait for the dropdown to fully expand
            wait.until(ExpectedConditions.attributeToBe(dropdown, "aria-expanded", "true"));

            // Fetch all available options
            List<WebElement> options = wait
                    .until(ExpectedConditions.visibilityOfAllElementsLocatedBy(By.xpath("//div[@role='option']")));

            if (!options.isEmpty()) {
                // Select a random option
                int randomIndex = new Random().nextInt(options.size());
                WebElement selectedOption = options.get(randomIndex);
                selectedOption.click();
                logWriter.println("✔ Selected " + dropdownName + ": " + selectedOption.getText());
            } else {
                logWriter.println("⚠️ No options found for " + dropdownName);
            }

        } catch (Exception e) {
            logWriter.println("❌ Error selecting " + dropdownName + ": " + e.getMessage());
        }
    }

    // ✅ Generic method to select a random language and ensure dropdown closes
    public static void selectLanguage(WebDriver driver, WebDriverWait wait, String languageDropdownXpath,
            PrintWriter logWriter) {
        try {
            // Click on the language dropdown to expand it
            WebElement languageInput = wait
                    .until(ExpectedConditions.elementToBeClickable(By.xpath(languageDropdownXpath)));
            languageInput.click();
            logWriter.println("📌 Clicked on Language input field.");

            // Wait for the dropdown to expand
            wait.until(ExpectedConditions.attributeToBe(languageInput, "aria-expanded", "true"));

            // Fetch all available language options
            List<WebElement> languageOptions = wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(
                    By.xpath("//div[@role='option']")));

            if (!languageOptions.isEmpty()) {
                int randomIndex = new Random().nextInt(languageOptions.size());
                WebElement selectedLanguage = languageOptions.get(randomIndex);

                // Enter the language name into the input field before clicking
                String languageText = selectedLanguage.getText();
                languageInput.sendKeys(languageText); // This ensures the dropdown recognizes the selection
                selectedLanguage.click(); // Click the selected language

                logWriter.println("✔ Selected Language: " + languageText);

                // ✅ Wait for the dropdown to close properly
                Thread.sleep(500); // Small delay for stability
                wait.until(ExpectedConditions.attributeToBe(languageInput, "aria-expanded", "false"));
                logWriter.println("📌 Language dropdown closed successfully.");

                // ✅ Press TAB to shift focus away (prevents dropdown from staying open)
                languageInput.sendKeys(Keys.TAB);

            } else {
                logWriter.println("⚠️ No language options found.");
            }
        } catch (Exception e) {
            logWriter.println("❌ Error selecting language: " + e.getMessage());
        }
    }

    public static void logout(WebDriver driver, PrintWriter logWriter) {
        try {
            logWriter.println("🔹 Logging out...");
            WebElement profileDropdown = driver.findElement(
                    By.xpath("//img[contains(@class, 'rounded-full') and contains(@class, 'object-cover')]"));
            profileDropdown.click();
            WebElement logoutButton = driver.findElement(By.xpath("//button[contains(text(),'Log Out')]"));
            logoutButton.click();
            logWriter.println("✅ Successfully logged out.");
        } catch (Exception e) {
            logWriter.println("❌ Error logging out: " + e.getMessage());
        }
    }

    public static String randomString() {
        Random random = new Random();
        int length = 8;
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < length; i++) {
            int randomChar = random.nextInt(26) + 'a';
            sb.append((char) randomChar);
        }
        return sb.toString(); // Now it returns a String
    }
}
