package com.selemiumautomation;


import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.io.PrintWriter;
import java.time.Duration;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
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
            // Selecting Start Date
            try {
                // Click the Start Date button
                WebElement startDateButton = wait.until(ExpectedConditions.elementToBeClickable(
                        By.xpath(
                                "//button[contains(@class, 'text-left') and contains(@class, 'border-gray-300')][1]")));
                startDateButton.click();
                logWriter.println("📅 Clicked on Start Date button.");

                // Wait for the date picker to appear
                wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//div[@role='dialog']")));

                // Get today's date and the target date (+2 days)
                LocalDate today = LocalDate.now();
                LocalDate targetDate = today.plusDays(2);
                String targetDay = targetDate.format(DateTimeFormatter.ofPattern("d")); // e.g., "12", "25"

                // Locate all selectable dates (excluding 'Today')
                List<WebElement> dateOptions = wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(
                        By.xpath(
                                "//button[contains(@class, 'aria-selected:opacity-100') and not(contains(@aria-label, 'Today'))]")));

                boolean dateSelected = false;

                for (WebElement dateOption : dateOptions) {
                    String dayText = dateOption.getText().trim();
                    if (dayText.equals(targetDay)) {
                        Actions actions = new Actions(driver);
                        actions.moveToElement(dateOption).click().perform(); // Ensures selection
                        logWriter.println("✔ Selected Start Date: " + targetDay);
                        dateSelected = true;
                        break;
                    }
                }

                if (!dateSelected) {
                    logWriter.println("⚠️ Target date not found. Selecting a random available date.");
                    if (!dateOptions.isEmpty()) {
                        WebElement randomDate = dateOptions.get(new Random().nextInt(dateOptions.size()));
                        Actions actions = new Actions(driver);
                        actions.moveToElement(randomDate).click().perform();
                        logWriter.println("✔ Selected Random Available Date: " + randomDate.getText());
                    } else {
                        logWriter.println("⚠️ No selectable dates found.");
                    }
                }

            } catch (Exception e) {
                logWriter.println("❌ Error selecting Start Date: " + e.getMessage());
            }
            // Selecting End Date
            try {
                // Ensure Start Date picker is closed before opening End Date picker
                Thread.sleep(1000);

                // Click the End Date button
                WebElement endDateButton = wait.until(ExpectedConditions.elementToBeClickable(
                        By.xpath(
                                "(//button[contains(@class, 'text-left') and contains(@class, 'border-gray-300')])[2]"))); // Ensure
                                                                                                                           // correct
                                                                                                                           // button

                endDateButton.click();
                logWriter.println("📅 Clicked on End Date button.");

                // Ensure the End Date picker is visible
                @SuppressWarnings("unused")
                WebElement datePicker = wait.until(ExpectedConditions.presenceOfElementLocated(
                        By.xpath("//div[@role='dialog']") // Ensuring the date picker loads
                ));

                // Small delay to ensure the date elements are fully loaded
                Thread.sleep(1000);

                // Get today's date and target date (+5 days)
                LocalDate today = LocalDate.now();
                LocalDate targetDate = today.plusDays(5);
                String targetDay = targetDate.format(DateTimeFormatter.ofPattern("d")); // Extracts only the day number

                // Locate all selectable dates
                List<WebElement> dateOptions = wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(
                        By.xpath("//button[contains(@class, 'h-8') and not(contains(@aria-label, 'Today'))]")));

                boolean dateSelected = false;

                for (WebElement dateOption : dateOptions) {
                    String dayText = dateOption.getText().trim();
                    if (dayText.equals(targetDay)) {
                        try {
                            Actions actions = new Actions(driver);
                            actions.moveToElement(dateOption).pause(500).click().perform(); // Ensures proper selection
                            logWriter.println("✔ Selected End Date: " + targetDay);
                            dateSelected = true;
                            break;
                        } catch (Exception clickError) {
                            logWriter.println("⚠️ Could not click on the specific date, trying another.");
                        }
                    }
                }

                // If the preferred date isn’t found, select a random available date
                if (!dateSelected) {
                    logWriter.println("⚠️ Target date not found. Selecting a random available date.");
                    if (!dateOptions.isEmpty()) {
                        WebElement randomDate = dateOptions.get(new Random().nextInt(dateOptions.size()));
                        Actions actions = new Actions(driver);
                        actions.moveToElement(randomDate).pause(500).click().perform();
                        logWriter.println("✔ Selected Random Available End Date: " + randomDate.getText());
                    } else {
                        logWriter.println("⚠️ No selectable dates found.");
                    }
                }

                // Small delay to ensure selection registers
                Thread.sleep(1000);

            } catch (Exception e) {
                logWriter.println("❌ Error selecting End Date: " + e.getMessage());
            }
            // Add New Buyer
            WebElement addNewBuyer = wait.until(
                    ExpectedConditions.elementToBeClickable(By.xpath("//button[contains(text(),'Add New Buyer')]")));
            addNewBuyer.click();
            // firstName
            Utils.fillTextField(driver, "firstName", randomString, logWriter);
            // lastName
            Utils.fillTextField(driver, "lastName", randomString, logWriter);

            Utils.fillTextField(driver, "email", randomString + "@gmail.com", logWriter);
            // selecting country
            try {
                WebElement countryDropdown = wait
                        .until(ExpectedConditions.elementToBeClickable(By.xpath("(//button[@role='combobox'])[2]")));
                countryDropdown.click();

                // Wait for dropdown to expand
                wait.until(ExpectedConditions.attributeToBe(countryDropdown, "aria-expanded",
                        "true"));

                // Fetch all country options
                List<WebElement> countryOptions = wait
                        .until(ExpectedConditions.visibilityOfAllElementsLocatedBy(By.xpath("//div[@role='option']")));

                if (!countryOptions.isEmpty()) {
                    int randomIndex = new Random().nextInt(countryOptions.size());
                    WebElement selectedCountry = countryOptions.get(randomIndex);
                    selectedCountry.click();
                    logWriter.println("✔ Selected Country: " + selectedCountry.getText());
                } else {
                    logWriter.println("⚠️ No country options found.");
                }
            } catch (Exception e) {
                logWriter.println("❌ Error selecting country in Custom Package form: " +
                        e.getMessage());
            }
            // selecting state
            try {
                WebElement stateDropdown = wait
                        .until(ExpectedConditions.elementToBeClickable(By.xpath("(//button[@role='combobox'])[3]")));
                stateDropdown.click();

                // Wait for dropdown to expand
                wait.until(ExpectedConditions.attributeToBe(stateDropdown, "aria-expanded",
                        "true"));

                // Fetch all state options
                List<WebElement> stateOptions = wait
                        .until(ExpectedConditions.visibilityOfAllElementsLocatedBy(By.xpath("//div[@role='option']")));

                if (!stateOptions.isEmpty()) {
                    int randomIndex = new Random().nextInt(stateOptions.size());
                    WebElement selectedState = stateOptions.get(randomIndex);
                    selectedState.click();
                    logWriter.println("✔ Selected State: " + selectedState.getText());
                } else {
                    logWriter.println("⚠️ No state options found.");
                }
            } catch (Exception e) {
                logWriter.println("❌ Error selecting state in Custom Package form: " +
                        e.getMessage());
            }
            // selecting city
            try {
                WebElement cityDropdown = wait
                        .until(ExpectedConditions.elementToBeClickable(By.xpath("(//button[@role='combobox'])[4]")));
                cityDropdown.click();

                // Wait for dropdown to expand
                wait.until(ExpectedConditions.attributeToBe(cityDropdown, "aria-expanded",
                        "true"));

                // Fetch all city options
                List<WebElement> cityOptions = wait
                        .until(ExpectedConditions.visibilityOfAllElementsLocatedBy(By.xpath("//div[@role='option']")));

                if (!cityOptions.isEmpty()) {
                    int randomIndex = new Random().nextInt(cityOptions.size());
                    WebElement selectedCity = cityOptions.get(randomIndex);
                    selectedCity.click();
                    logWriter.println("✔ Selected City: " + selectedCity.getText());
                } else {
                    logWriter.println("⚠️ No city options found.");
                }
            } catch (Exception e) {
                logWriter.println("❌ Error selecting city in Custom Package form: " +
                        e.getMessage());
            }
            // Selecting Language
            try {
                WebElement languageInput = wait.until(ExpectedConditions.elementToBeClickable(
                        By.xpath("//input[@role='combobox' and contains(@class,'bg-transparent')]")));
                languageInput.click();
                logWriter.println("📌 Clicked on Language input field.");

                // Wait for the dropdown to expand
                wait.until(ExpectedConditions.attributeToBe(languageInput, "aria-expanded",
                        "true"));

                // Fetch all available language options
                List<WebElement> languageOptions = wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(
                        By.xpath("//div[@role='option']")));

                if (!languageOptions.isEmpty()) {
                    int randomIndex = new Random().nextInt(languageOptions.size());
                    WebElement selectedLanguage = languageOptions.get(randomIndex);

                    // Enter the language name into the input field before clicking
                    String languageText = selectedLanguage.getText();
                    languageInput.sendKeys(languageText);
                    selectedLanguage.click();
                    logWriter.println("✔ Selected Language: " + languageText);

                    // Wait until the dropdown closes before proceeding
                    wait.until(ExpectedConditions.attributeToBe(languageInput, "aria-expanded",
                            "false"));
                    logWriter.println("📌 Language dropdown closed.");

                    // Add a slight delay to ensure proper field switch
                    Thread.sleep(500);
                } else {
                    logWriter.println("⚠️ No language options found.");
                }
            } catch (Exception e) {
                logWriter.println("❌ Error selecting language in Custom Package form: " +
                        e.getMessage());
            }

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
