package com.myairlines.airlinetest.extensions;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import org.apache.commons.io.FileUtils;
import org.junit.jupiter.api.extension.AfterAllCallback;
import org.junit.jupiter.api.extension.BeforeAllCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.TestWatcher;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.Optional;

public class MyReportingExtension implements TestWatcher, BeforeAllCallback, AfterAllCallback {

    private static Logger logger = LoggerFactory.getLogger(MyReportingExtension.class);

    private ExtentReports extentReports;


    @Override
    public void beforeAll(ExtensionContext context) throws Exception {

        logger.info("Entering {} of {}", "beforeAll", "MyReportingExtension");
        extentReports = getExtentReports(context);

        logger.info("Exiting {} of {}", "beforeAll", "MyReportingExtension");
    }

    private ExtentReports getExtentReports(ExtensionContext context) {
        //ExtentSparkReporter sparkReporter = new ExtentSparkReporter(System.getProperty("user.dir") + File.separator + "extentreport" + File.separator + "ExtentReportResults.html");
        ExtentSparkReporter sparkReporter = new ExtentSparkReporter("extentreport" + File.separator + "ExtentReportResults.html");
        sparkReporter.config().setReportName("My Project Automation Report");
        sparkReporter.config().setDocumentTitle("My Test Results");

        ExtentReports extentReports = new ExtentReports();
        extentReports.attachReporter(sparkReporter);
        String envName = getApplicationProperty(context, "env.name");
        extentReports.setSystemInfo("Environment", envName);
        extentReports.setSystemInfo("QA", "Shagun Dhingra");
        return extentReports;
    }

    private String getApplicationProperty(ExtensionContext context, String propertyName) {
        ApplicationContext applicationContext = SpringExtension.getApplicationContext(context);
        return applicationContext.getEnvironment().getProperty(propertyName);
    }

    @Override
    public void testDisabled(ExtensionContext context, Optional<String> reason) {

        String displayName = context.getDisplayName();

        ExtentTest test = extentReports.createTest(displayName);

        test.skip("Test is disabled");

        logger.info("Test is disabled: {}", displayName);
    }

    @Override
    public void testSuccessful(ExtensionContext context) {

        String displayName = context.getDisplayName();

        ExtentTest test = extentReports.createTest(displayName);

        test.pass("Test is passed");

        logger.info("Test is successful: {}", displayName);
    }

    @Override
    public void testAborted(ExtensionContext context, Throwable cause) {
        String displayName = context.getDisplayName();

        ExtentTest test = extentReports.createTest(displayName);

        test.skip("Test is aborted");

        logger.info("Test is aborted: {}", displayName);
    }

    @Override
    public void testFailed(ExtensionContext context, Throwable cause) {

        String displayName = context.getDisplayName();

        ExtentTest test = extentReports.createTest(displayName);
        test.fail(cause.getLocalizedMessage());

        try {
            String screenshotPath = takeScreenshot(context, displayName);
            test.addScreenCaptureFromPath(screenshotPath);
        } catch (Exception exp) {
            exp.printStackTrace();
        }


        logger.error("Test is failed: {}", displayName, cause);
    }

    private String takeScreenshot(ExtensionContext extensionContext, String screenshotName) throws Exception {

        Object test = extensionContext.getRequiredTestInstance();
        Field a = test.getClass().getDeclaredField("driver");
        a.setAccessible(true);
        WebDriver driver = (WebDriver) a.get(test);
        String outputFilePath = System.getProperty("user.dir") + File.separator + "extentreport" + File.separator + "screenshots" + File.separator + screenshotName + ".png";
        File screenshot = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
        FileUtils.copyFile(screenshot, new File(outputFilePath));

        return outputFilePath;
    }


    @Override
    public void afterAll(ExtensionContext context) throws Exception {
        logger.info("Entering {} of {}", "afterAll", "MyReportingExtension");

        extentReports.flush();
        logger.info("Exiting {} of {}", "afterAll", "MyReportingExtension");
    }

    public String takeScreenshot(ChromeDriver driver, String screenshotName) throws IOException {

        String outputFilePath = new File("").getAbsolutePath() + File.separator + "build" + File.separator + "screenshots" + File.separator + screenshotName + ".jpg";
        File screenshot = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
        FileUtils.copyFile(screenshot, new File(outputFilePath));
        return outputFilePath;
    }
}
