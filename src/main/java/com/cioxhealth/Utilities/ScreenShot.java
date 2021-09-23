package com.cioxhealth.Utilities;

import org.openqa.selenium.OutputType;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Reporter;

import java.io.File;
import java.lang.invoke.MethodHandles;

public class ScreenShot {
	private static final Logger logger = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
	private static final String OUTPUT_DIR = "test-output";
	private static final String IMAGE_DIR = "images";

	public static void take(WebDriver driver, String imageName) {
		logger.debug("Taking screenshot...");
		File screenshot = (File) ((RemoteWebDriver) driver).getScreenshotAs(OutputType.FILE);
		String reportDir = ".";
		reportDir = reportDir + File.separatorChar + "test-output";
		File imagesDir = new File(reportDir + File.separatorChar + "images");
		if (!imagesDir.isDirectory() && !imagesDir.mkdirs()) {
			logger.error("Failed to create directory " + imagesDir);
		}

		String imageNameStr;
		if (!imageName.endsWith(".")) {
			imageNameStr = imageName + ".png";
		} else {
			imageNameStr = imageName + "png";
		}

		File image = new File(imagesDir.toString() + File.separatorChar + imageNameStr);
		if (image.exists()) {
			logger.debug("Old screenshot with same name exists, deleting: " + imageNameStr);
			image.delete();
		}

		screenshot.renameTo(image);
		String screenShotLocation = "Screenshot saved to: " + image.getAbsolutePath();
		logger.debug(screenShotLocation);
		logger.debug("RP_MESSAGE#FILE#{}#{}", image.getAbsolutePath(), imageName);
		Reporter.log(screenShotLocation);
	}
}