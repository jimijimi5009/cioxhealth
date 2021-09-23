package com.cioxhealth.pages;

import com.google.Common.BasePage;
import com.google.Common.SeleniumMethods;
import com.google.Utilities.JsonTool;
import org.json.simple.JSONObject;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;
import org.openqa.selenium.support.PageFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.invoke.MethodHandles;
import java.util.ArrayList;
import java.util.List;


public class GoogleSearchPage extends BasePage {
	/** ##### Page Variable ##### */
	private WebDriver driver;
	private final Logger log = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

	/** ##### Page Constructor ##### */
	public GoogleSearchPage(WebDriver driver) {
		this.driver = driver;
		PageFactory.initElements(this.driver, this);
	}

	/** ##### Page Element ##### */
	@FindBy(xpath = "//input[@type='text']")
	private WebElement searchBox;

	@FindBy(xpath = "(//input[@name='btnK'])[2]")
	private WebElement searchBtn;

	@FindBy(xpath = "//*[@id='result-stats']")
	private WebElement resultStat;

	@FindBy(how = How.XPATH, using = "//div[@class=\"g\"]//h3")
	List<WebElement> searchResult;


	/** ##### Page Test Method ##### */
	public boolean inputSearchData() {

		boolean flag = false;
		try {

			SeleniumMethods.waitAndgetValue(searchBox);
			SeleniumMethods.waitAndSendKeys(searchBox,getTestData("SearchData"));
			SeleniumMethods.clickButton(searchBtn);
			flag= true;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return flag;
	}

	public boolean ValidateSearchData( ) {

		boolean flag = false;
		try {

			SeleniumMethods.waitUntilElementIsVisible(resultStat);
			ArrayList<String> SearchListNameFromUi = (ArrayList<String>) SeleniumMethods.getAllText(searchResult);
			if (SearchListNameFromUi.contains(getTestData("ExpectedResult"))) {
				flag=true;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return flag;
	}
	/** ##### Page Private Helper method ##### */


	private String getTestData(String data){
		try {
			String jsonFilePath = System.getProperty("user.dir") +"/src/test/resources/TestData/TestData.json";
			JSONObject jsonObject = JsonTool.readJson(jsonFilePath);
			return   (String) jsonObject.get(data);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
}
