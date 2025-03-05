package com.selemiumautomation;

import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.io.PrintWriter;
import java.time.Duration;
import java.util.List;
import java.util.Random;

public class WhatYouDoForm {

    public static void fill(WebDriver driver, PrintWriter logWriter) {
        logWriter.println("📝 Filling 'What You Do' section...");
         WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

        try {
            Utils.fillTextField(driver, "shortIntro", "YouTube content creator specialized in fashion", logWriter);
            Utils.fillTextField(driver, "longIntro", "I create engaging fashion content on YouTube, focusing on trends, styling tips, and fashion hacks.", logWriter);
                 try {
                WebElement priserviceDropdown = wait
                        .until(ExpectedConditions.elementToBeClickable(By.xpath("(//input[@role='combobox'])[1]")));
                priserviceDropdown.click();                 // Wait for dropdown to expand
                wait.until(ExpectedConditions.attributeToBe(priserviceDropdown, "aria-expanded", "true"));

                // Fetch all service options
                List<WebElement> priserviceOptions = wait
                        .until(ExpectedConditions.visibilityOfAllElementsLocatedBy(By.xpath("//div[@role='option']")));

                if (!priserviceOptions.isEmpty()) {
                    int randomIndex = new Random().nextInt(priserviceOptions.size());
                    WebElement selectedService = priserviceOptions.get(randomIndex);
                    selectedService.click();
                    logWriter.println("✔ Selected Service: " + selectedService.getText());
                } else {
                    logWriter.println("⚠️ No primary service options found.");
                }
            } catch (Exception e) {
                logWriter.println("❌ Error selecting primary service in Package form: " + e.getMessage());
            }
            try {
                WebElement othserviceDropdown = wait
                        .until(ExpectedConditions.elementToBeClickable(By.xpath("(//input[@role='combobox'])[2]")));
                othserviceDropdown.click();                 // Wait for dropdown to expand
                wait.until(ExpectedConditions.attributeToBe(othserviceDropdown, "aria-expanded", "true"));

                // Fetch all service options
                List<WebElement> othserviceOptions = wait
                        .until(ExpectedConditions.visibilityOfAllElementsLocatedBy(By.xpath("//div[@role='option']")));

                if (!othserviceOptions.isEmpty()) {
                    int randomIndex = new Random().nextInt(othserviceOptions.size());
                    WebElement selectedService = othserviceOptions.get(randomIndex);
                    selectedService.click();
                    logWriter.println("✔ Selected Service: " + selectedService.getText());
                } else {
                    logWriter.println("⚠️ No other service options found.");
                }
            } catch (Exception e) {
                logWriter.println("❌ Error selecting other service in Package form: " + e.getMessage());
            }
            try {
                WebElement countryDropdown = wait
                        .until(ExpectedConditions.elementToBeClickable(By.xpath("(//input[@role='combobox'])[3]")));
                countryDropdown.click();                 // Wait for dropdown to expand
                wait.until(ExpectedConditions.attributeToBe(countryDropdown, "aria-expanded", "true"));

                // Fetch all service options
                List<WebElement> countryOptions = wait
                        .until(ExpectedConditions.visibilityOfAllElementsLocatedBy(By.xpath("//div[@role='option']")));

                if (!countryOptions.isEmpty()) {
                    int randomIndex = new Random().nextInt(countryOptions.size());
                    WebElement selectedService = countryOptions.get(randomIndex);
                    selectedService.click();
                    logWriter.println("✔ Selected Country: " + selectedService.getText());
                } else {
                    logWriter.println("⚠️ No Country options found.");
                }
            } catch (Exception e) {
                logWriter.println("❌ Error selecting Country in Package form: " + e.getMessage());
            }
            Utils.submitForm(driver, logWriter, "What You Do");
        } catch (Exception e) {
            logWriter.println("❌ Error filling 'What You Do' section: " + e.getMessage());
        }    
    }
}
