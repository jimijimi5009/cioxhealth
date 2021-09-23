/**
 * 
 */
package com.cioxhealth;


import com.cioxhealth.Common.BasePage;
import com.cioxhealth.pages.GoogleSearchPage;
import org.json.simple.parser.ParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.awt.*;
import java.io.IOException;
import java.lang.invoke.MethodHandles;


public class SampleTC extends BasePage {
	/** ##### Page Variable ##### */
	private final Logger log = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
	GoogleSearchPage googleSearchPage;

	@Test(enabled = true)
	public void TC01_Search_webapp() throws IOException, ParseException, AWTException, InterruptedException {
		googleSearchPage = new GoogleSearchPage(driver);
		log.info("user Sending Search input Data.");
		Assert.assertTrue(googleSearchPage.inputSearchData(), "Failed to input Data.. ");

		log.info("user Matching Ui Result With Data");
		Assert.assertTrue(googleSearchPage.ValidateSearchData(), "Failed to Match Data With Ui.. ");
	}

}
