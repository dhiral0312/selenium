package com.selemiumautomation;

import java.io.PrintWriter;
import java.time.Duration;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class AddNewBuyer {
    public static void addNewBuyer(WebDriver driver, PrintWriter logWriter) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(15));
        try {
            // Click on Buyers tab
            WebElement buyersTab = wait
                    .until(ExpectedConditions.elementToBeClickable(By.xpath("//a[contains(text(),'Buyer')]")));
            buyersTab.click();

            WebElement exportCSV = wait
                    .until(ExpectedConditions.elementToBeClickable(By.xpath("//button[contains(text(),'Export CSV')]")));
            exportCSV.click();
            Thread.sleep(3000);
            logWriter.println("✔ Exported CSV file.");
            // Add New Buyer
            WebElement addNewBuyer = wait.until(
                    ExpectedConditions.elementToBeClickable(By.xpath("//button[contains(text(),'Add New Buyer')]")));
            addNewBuyer.click();
            String randomString = Utils.randomString();
            // firstName
            Utils.fillTextField(driver, "firstName", randomString, logWriter);
            // lastName
            Utils.fillTextField(driver, "lastName", randomString, logWriter);

            Utils.fillTextField(driver, "email", randomString + "@gmail.com", logWriter);
            // ✅ Select Country (Dropdown 2)
            Utils.selectDropdown(driver, wait, "(//button[@role='combobox'])[1]", "Country", logWriter);

            // ✅ Select State (Dropdown 3)
            Utils.selectDropdown(driver, wait, "(//button[@role='combobox'])[2]", "State", logWriter);

            // ✅ Select City (Dropdown 4)
            Utils.selectDropdown(driver, wait, "(//button[@role='combobox'])[3]", "City", logWriter);
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

        } catch (Exception e) {
            logWriter.println("❌ Error adding new buyer: " + e.getMessage());
        }
    }
}
