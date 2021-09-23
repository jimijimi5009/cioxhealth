/**
 * 
 */
package com.cioxhealth.Reporter;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import com.aventstack.extentreports.reporter.JsonFormatter;
import com.google.Common.BasePage;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;


public class ExtentReportTool extends BasePage {
	protected static ExtentReports report;
	private ExtentSparkReporter reporter;
	private JsonFormatter jformatter;

	public void startReport(String testCase) throws IOException {

		String dirPath = System.getProperty("user.dir") + "\\Reports";
		Path path = Paths.get(dirPath);
		Files.createDirectories(path);

		report = new ExtentReports();
		reporter = new ExtentSparkReporter(new File(dirPath + "\\HTML_Report\\" + testCase + ".html"));
		jformatter = new JsonFormatter(dirPath + "\\JSON_Report\\" + testCase + ".json");
		report.attachReporter(jformatter, reporter);
		test = report.createTest(testCase);
	}

	public void endReport() {
		report.flush();
		report.removeTest(test);
	}

	public void saveReport() {
		report.flush();
	}
}
