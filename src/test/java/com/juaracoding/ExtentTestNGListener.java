package com.juaracoding;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;

public class ExtentTestNGListener implements ITestListener {

    private static ExtentReports extent;
    private static ExtentTest test;

    @Override
    public void onStart(ITestContext context) {
        if (extent == null) {
            ExtentSparkReporter spark = new ExtentSparkReporter("target/ExtentReports/index.html");
            spark.config().setReportName("HRIS API Test Suite");
            spark.config().setDocumentTitle("Test Execution Report");
            
            extent = new ExtentReports();
            extent.attachReporter(spark);
            extent.setSystemInfo("Environment", "QA");
            extent.setSystemInfo("User", "Tester");
            extent.setSystemInfo("OS", System.getProperty("os.name"));
            extent.setSystemInfo("Java Version", System.getProperty("java.version"));
        }
    }

    @Override
    public void onTestStart(ITestResult result) {
        String className = result.getTestClass().getRealClass().getSimpleName();
        String methodName = result.getMethod().getMethodName();
        test = extent.createTest(className + " - " + methodName, 
                                "Test from " + className + " suite");
    }

    @Override
    public void onTestSuccess(ITestResult result) {
        long executionTime = result.getEndMillis() - result.getStartMillis();
        test.log(Status.PASS, "Test passed successfully in " + executionTime + "ms");
    }

    @Override
    public void onTestFailure(ITestResult result) {
        long executionTime = result.getEndMillis() - result.getStartMillis();
        test.log(Status.FAIL, "Test failed after " + executionTime + "ms");
        test.log(Status.FAIL, "Error: " + result.getThrowable());
    }

    @Override
    public void onTestSkipped(ITestResult result) {
        test.log(Status.SKIP, "Test was skipped");
    }

    @Override
    public void onFinish(ITestContext context) {
        if (extent != null) {
            extent.flush();
        }
    }
}