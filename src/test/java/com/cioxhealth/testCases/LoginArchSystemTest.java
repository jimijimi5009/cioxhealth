package com.cioxhealth.testCases;


import com.cioxhealth.Common.BasePage;
import com.cioxhealth.pages.ArchLoginPage;
import org.json.simple.parser.ParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.Test;

import java.lang.invoke.MethodHandles;


public class LoginArchSystemTest extends BasePage {
	/** ##### Page Variable ##### */
	private final Logger log = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
	ArchLoginPage archLoginPage ;

//	@Test(enabled = true)
//	public void TC01_Login_To_ARCH_SYSTEM() throws IOException, ParseException, AWTException, InterruptedException {
//
//		archLoginPage = new ArchLoginPage(driver);
//		log.info("I am login to Arch System");
//		Assert.assertTrue(archLoginPage.loginToEEAdmin(), "Failed to Login to Arch System.. ");
//		Assert.assertTrue(archLoginPage.selectRole(), "Failed to Login to Arch System.. ");
//	}

	@Test(priority = 2)
	public void TC02_ASSIGNING_PROJECT_ARCH_SYSTEM() throws ParseException {

		String testcaseName = Thread.currentThread().getStackTrace()[1].getMethodName();
		archLoginPage = new ArchLoginPage(driver);
		log.info("I am login to Arch System");
		archLoginPage.loginToEEAdmin();
		archLoginPage.selectRole();

		log.info("I am  Assigning project to user inArch System");
		archLoginPage.UserManagement(testcaseName);
		archLoginPage.fillUpUserProf(testcaseName);
		archLoginPage.selectProject(testcaseName);
	}

}
