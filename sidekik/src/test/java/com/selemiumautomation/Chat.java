package com.selemiumautomation;

import java.io.PrintWriter;
import java.time.Duration;
import java.util.List;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class Chat {
    public static void chatHandler(WebDriver driver, PrintWriter logWriter) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(15));

        try {
            // Click on chat button
            WebElement chatButton = wait
                    .until(ExpectedConditions.elementToBeClickable(By.xpath("//a[contains(text(), 'Chat')]")));
            chatButton.click();
            logWriter.println("💬 Clicked on the chat button.");

            // Wait for chat list to be visible
            @SuppressWarnings("unused")
            WebElement chatList = wait.until(ExpectedConditions
                    .visibilityOfElementLocated(By.xpath("//div[contains(@class,'overflow-auto custom-scrollbar')]")));
            logWriter.println("✅ Chat list is visible.");

            // Select the first chat from the list
            List<WebElement> chatItems = driver
                    .findElements(By.xpath("//div[contains(@class,'overflow-auto custom-scrollbar')]/div"));
            if (!chatItems.isEmpty()) {
                chatItems.get(0).click();
                logWriter.println("📩 Opened the first chat in the list.");
            } else {
                logWriter.println("⚠️ No chats found in the list.");
                return;
            }

            // Wait for chat window to appear
            @SuppressWarnings("unused")
            WebElement chatWindow = wait.until(ExpectedConditions
                    .visibilityOfElementLocated(By.xpath("//div[contains(@class,'flex flex-col h-full w-4/5')]")));
            logWriter.println("✅ Chat window is open.");
            Thread.sleep(1000);

            // Locate message input box
            WebElement messageInput = wait.until(
                    ExpectedConditions.elementToBeClickable(By.xpath("//input[@placeholder='Type a message...']")));
            messageInput.sendKeys("Hello, this is a test message!");
            logWriter.println("✍️ Entered test message in chat.");
            Thread.sleep(1000);

            // Locate and click the send button
            WebElement sendButton = wait.until(ExpectedConditions.elementToBeClickable(
                    By.xpath(
                            "//button[contains(@class, 'inline-flex') and contains(@class, 'justify-center') and contains(@class, 'p-2')]")));
            sendButton.click();
            logWriter.println("📩 Clicked send button.");

            // Verify the message appears in the chat
            WebElement sentMessage = wait.until(ExpectedConditions
                    .visibilityOfElementLocated(By.xpath("//p[contains(text(),'Hello, this is a test message!')]")));
            if (sentMessage.isDisplayed()) {
                logWriter.println("✅ Message sent successfully.");
            } else {
                logWriter.println("⚠️ Message not found in chat history.");
            }

            try {
                WebElement messageState = driver.findElement(By.xpath(
                        "//p[contains(text(),'Hello, this is a test message!')]/following-sibling::span[contains(@class, 'read-receipt')]"));
                String messageStatus = messageState.getAttribute("class");

                if (messageStatus.contains("unread")) {
                    logWriter.println("🔵 Message is unread.");
                } else if (messageStatus.contains("read")) {
                    logWriter.println("✅ Message is read.");
                } else {
                    logWriter.println("⚠️ Message state unknown.");
                }
            } catch (Exception e) {
                logWriter.println("⚠️ Unable to find message read/unread state.");
            }

        } catch (Exception e) {
            logWriter.println("❌ Error in chat module: " + e.getMessage());
        }
    }
}
