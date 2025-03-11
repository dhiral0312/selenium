package com.selemiumautomation;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.List;
import java.util.concurrent.TimeUnit;
import com.opencsv.CSVReader;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

public class Test {

    @SuppressWarnings("deprecation")
    public static void main(String[] args) {
        System.setProperty("webdriver.chrome.driver", "C:\\Program Files\\chromedriver-win64\\chromedriver.exe");

        ChromeOptions options = new ChromeOptions();
        options.addArguments("--remote-allow-origins=*");
        WebDriver driver = new ChromeDriver(options);
        driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
        driver.manage().window().maximize();
        try (CSVReader reader = new CSVReader(new FileReader("C:\\Users\\admin6\\Desktop\\testemail.csv"));
                PrintWriter logWriter = new PrintWriter(new FileWriter("test_results.txt", true))) {

            String[] credentials;

            // Skip the first line (header)
            reader.readNext();

            while ((credentials = reader.readNext()) != null) {
                if (credentials.length < 4) {
                    logWriter.println("❌ Skipping invalid line: " + String.join(",", credentials));
                    continue;
                }

                String username = credentials[2];
                String password = credentials[3];

                // Ensure credentials.csv contains at least 3 elements
                String email = (credentials.length > 2) ? credentials[2] : "default@example.com";
                String profileImagePath = "C:\\Users\\admin6\\Downloads\\web3.jpg";

                driver.get("https://dev.sidekik.io/sign-in/service-provider");
                logWriter.println("Logging in with username: " + username);

                WebElement usernameField = driver.findElement(By.name("email"));
                WebElement passwordField = driver.findElement(By.name("password"));
                WebElement loginButton = driver.findElement(By.xpath("//button[text()='Log in']"));

                usernameField.sendKeys(username);
                passwordField.sendKeys(password);
                loginButton.click();

                Thread.sleep(5000); // Allow time for login

                if (driver.getCurrentUrl().contains("dashboard")) {
                    logWriter.println("✅ Successfully logged in.");
                    logSiteErrors(driver, logWriter);

                    // ✅ Corrected FormFiller call with all required parameters
                   //FormFiller.fillForm(driver, logWriter, profileImagePath, email);
                    //logSiteErrors(driver, logWriter);

                    // ✅ Call CustomPackage after filling the form
                    CustomPackage.handleCustomPackage(driver, logWriter);
                    logSiteErrors(driver, logWriter);

                } else {
                    logWriter.println("❌ Login failed for user: " + username);
                    Utils.takeScreenshot(driver, "LoginFailure"); // Capture login failure screenshot

                }
            }
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
            Utils.takeScreenshot(driver, "TestFailure"); // Call the screenshot method from FormFiller.java
        } finally {
            driver.quit();
        }
    }

    private static void logSiteErrors(WebDriver driver, PrintWriter logWriter) {
        try {
            // Find all error messages on the page
            List<WebElement> errorMessages = driver.findElements(By.xpath(
                    "//*[contains(@class, 'text-[0.8rem] font-medium text-sk-red text-left pl-4') or " + // Case 1:Invalid Email, Password
                            "contains(@class, 'border-sk-red bg-sk-red text-background') or " + // Case 2: Toast Error Message
                            "contains(@class, 'text-sm font-semibold')]" // Case 3: Toast Inner Message
            ));

            // If no errors are found
            if (errorMessages.isEmpty()) {
                logWriter.println("✅ No error messages found.");
            } else {
                logWriter.println("⚠️ Error messages found:");
                Utils.takeScreenshot(driver, "TestFailure"); // Call the screenshot method from FormFiller.java
                for (WebElement error : errorMessages) {
                    // Log the text content of each error message
                    logWriter.println("  " + error.getText());
                }
            }
        } catch (Exception e) {
            logWriter.println("⚠️ Could not retrieve error messages: " + e.getMessage());
        }
    }

}
