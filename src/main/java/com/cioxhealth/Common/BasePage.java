/**
 * 
 */
package com.cioxhealth.Common;

import com.aventstack.extentreports.ExtentTest;
import com.google.Reporter.ExtentReportTool;
import com.google.Utilities.ReadPropertiesFile;
import org.json.simple.parser.ParseException;
import org.openqa.selenium.WebDriver;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeSuite;

import java.awt.*;
import java.io.IOException;
import java.lang.reflect.Method;


public class BasePage {

	protected static WebDriver driver;
	protected static ReadPropertiesFile prop;
	protected static ExtentTest test;
	ExtentReportTool extent;

	@BeforeSuite
	public void initTest() throws IOException {
		extent = new ExtentReportTool();

	}

	@AfterSuite
	public void endTest() {
		extent.endReport();
	}

	@BeforeMethod
	public void testcaseInit(Method method) throws IOException, ParseException, InterruptedException, AWTException {
		SeleniumMethods.startWebApp();
		String testcaseName = method.getName();
		extent.startReport(testcaseName);
	}

	@AfterMethod
	public void saveTest() throws InterruptedException {
		SeleniumMethods.closeBrowser();
		extent.saveReport();

	}

}
