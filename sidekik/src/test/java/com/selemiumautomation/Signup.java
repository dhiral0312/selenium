package com.selemiumautomation;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.Duration;

import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import com.opencsv.exceptions.CsvValidationException;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class Signup {
    public static void main(String[] args) throws IOException, InterruptedException {
        // Set the path to the ChromeDriver executable
        System.setProperty("webdriver.chrome.driver", "C:\\Program Files\\chromedriver-win64\\chromedriver.exe");

        WebDriver driver = new ChromeDriver();
        driver.manage().window().maximize();

        // CSV file path
        String csvFilePath = "C:\\Users\\admin6\\Desktop\\testemail.csv";
        // Report file path
        String reportFilePath = "C:\\Users\\admin6\\Desktop\\signup_report.txt";

        // Increase wait time to 20 seconds
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(20));

        // Open a PrintWriter for the report file
        PrintWriter reportWriter = new PrintWriter(new FileWriter(reportFilePath, true));

        try (CSVReader reader = new CSVReaderBuilder(new java.io.FileReader(csvFilePath))
                .withSkipLines(1)
                .build()) {

            String[] line;
            while ((line = reader.readNext()) != null) {
                String firstName = line[0];
                String lastName = line[1];
                String email = line[2];
                String password = line[3];

                // Navigate to the sign-in page
                driver.get("https://dev.sidekik.io/sign-in/service-provider");

                // Click the "Sign up" button
                WebElement signUpButton = wait.until(
                        ExpectedConditions.elementToBeClickable(By.xpath("//button[text()='Sign up']")));
                signUpButton.click();

                // Wait for the sign-up form to be visible (e.g., firstName field)
                WebElement firstNameField = wait.until(
                        ExpectedConditions.visibilityOfElementLocated(By.name("firstName")));

                // Locate the rest of the sign-up fields
                WebElement lastNameField = driver.findElement(By.name("lastName"));
                WebElement emailField = driver.findElement(By.name("email"));
                WebElement passwordField = driver.findElement(By.name("password"));

                // Enter the sign-up details
                firstNameField.sendKeys(firstName);
                lastNameField.sendKeys(lastName);
                emailField.sendKeys(email);
                passwordField.sendKeys(password);

                // Wait for the checkbox element to be clickable and click it.
                WebElement checkbox = wait.until(ExpectedConditions.elementToBeClickable(
                        By.xpath("//button[@role='checkbox' and @aria-checked='false']")));
                checkbox.click();
            

                // Locate and click the submission button
                WebElement submitButton = driver
                        .findElement(By.xpath("//button[text()='Sign up as a service provider']"));
                submitButton.click();
                // Optional: wait a few seconds for the toast to appear
                Thread.sleep(10000);

                // Attempt to capture the toast message using a try-catch
                String toastMessage = "No toast message";
                try {
                    WebElement toast = wait.until(ExpectedConditions.visibilityOfElementLocated(
                            By.xpath("//ol/li[@role='status']")));
                    toastMessage = toast.getText();
                } catch (Exception e) {
                    System.out.println("Toast message not found.");
                }

                // Capture the current URL after submission
                String actualUrl = driver.getCurrentUrl();
                String expectedUrl = "https://dev.sidekik.io/service-provider/dashboard";

                // Log the result based on URL and toast message
                if (actualUrl.equals(expectedUrl)) {
                    System.out.println("Sign-up successful for email: " + email);
                    reportWriter.println(
                            "SUCCESS: Sign-up successful for email: " + email + " | Toast message: " + toastMessage);
                } else {
                    System.out.println("Sign-up failed for email: " + email);
                    reportWriter.println(
                            "FAILURE: Sign-up failed for email: " + email + " | Toast message: " + toastMessage);
                }
                // Add a separator for clarity in the log file
                reportWriter.println("--------------------------------------------------");
                reportWriter.flush();
                Thread.sleep(2000);
            }

        } catch (CsvValidationException e) {
            e.printStackTrace();
        } finally {
            reportWriter.close();
            driver.quit();
        }
    }
}
