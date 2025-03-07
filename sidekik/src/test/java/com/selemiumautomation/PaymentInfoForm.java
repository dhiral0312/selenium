package com.selemiumautomation;

import org.openqa.selenium.*;


import java.io.PrintWriter;

public class PaymentInfoForm {

    public static void fill(WebDriver driver, PrintWriter logWriter) {
        logWriter.println("📝 Filling 'Payment Info' Form...");

        try {
            Utils.fillTextField(driver, "bankName", "XYZ BANK", logWriter);
            Utils.fillTextField(driver, "BSBNumber", "dshfh", logWriter);
            Utils.fillTextField(driver, "IBAN", "ASED", logWriter);
            Utils.fillTextField(driver, "name", "Willson Smith", logWriter);
            Utils.fillTextField(driver, "SWIFTCode", "AERDRS78", logWriter);
            Utils.fillTextField(driver, "bankAddress", "32, Test building, near test shop, test city, test state", logWriter);

            Utils.submitForm(driver, logWriter, "Payment Info");

        } catch (Exception e) {
            logWriter.println("❌ Error filling 'Payment Info' Form: " + e.getMessage());
        }
    }
}
