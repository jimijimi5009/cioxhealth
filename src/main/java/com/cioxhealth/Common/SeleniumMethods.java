package com.cioxhealth.Common;

import com.google.Utilities.JsonTool;
import com.google.Utilities.ReadPropertiesFile;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.apache.commons.lang3.SystemUtils;
import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.ie.InternetExplorerOptions;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.*;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.invoke.MethodHandles;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.*;


public class SeleniumMethods extends BasePage {

	private static final Logger log = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

	public static String getEnvironmentD() {
		String env = null;
		try {
			env = System.getProperty("environment");
			if (env != null) {
				System.out.println("Test execution running in " + env + " environment.");
			} else {
				System.out.println("Environment parameter is null");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return env;
	}
	public static String getEnvironment() {
		String env = null;
		try {
			String jsonFilePath = System.getProperty("user.dir") +"/src/test/resources/TestData/TestData.json";
			JSONObject jsonObject = JsonTool.readJson(jsonFilePath);
			env = (String) jsonObject.get("Environment");
			if (env != null) {
				System.out.println("Test execution running in " + env + " environment.");
			} else {
				System.out.println("Environment parameter is null");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return env;
	}

	public static WebDriver setBrowser(String browserType) throws InterruptedException {
		String osName =getOperatingSystemSystemUtils();
		switch (browserType.toLowerCase()) {
		case "chrome":
			if (osName.contains("Win")){
				setChromeDriverForWin();
			}else if (osName.contains("Mac")){
				setChromeDriverForMac();
			}
			break;
		case "ie":
			setIEDriver();
			break;
		default:
			System.out.println(browserType + " is not a supported browser");
		}
		driver.manage().window().maximize();
		driver.findElement(By.tagName("html")).sendKeys(Keys.chord(Keys.CONTROL, "0"));
		return driver;
	}

	public static WebDriver setChromeDriverForWin() {

		String downloadFilepath = System.getProperty("user.dir") + "\\src\\resources\\browser_downloads";
		HashMap<String, Object> chromePrefs = new HashMap<String, Object>();
		chromePrefs.put("profile.default_content_settings.popups", 0);
		chromePrefs.put("download.default_directory", downloadFilepath);

		ChromeOptions options = new ChromeOptions();
		options.setExperimentalOption("prefs", chromePrefs);
		options.addArguments("--test-type");
		options.addArguments("--disable-extensions");
		options.setCapability(CapabilityType.ACCEPT_SSL_CERTS, true);
		WebDriverManager.chromedriver().setup();
		driver = new ChromeDriver(options);
		System.out.println("Starting Chrome browser");
		return driver;
	}

	public static WebDriver setChromeDriverForMac() {
		ChromeOptions options = new ChromeOptions();
		String downloadFilepath = System.getProperty("user.dir") + "\\src\\resources\\browser_downloads";
		HashMap<String, Object> chromePrefs = new HashMap<String, Object>();
		chromePrefs.put("profile.default_content_settings.popups", 0);
		chromePrefs.put("download.default_directory", downloadFilepath);
		options.setExperimentalOption("prefs", chromePrefs);
		options.addArguments("--test-type");
		options.addArguments("--disable-extensions");
		options.setCapability(CapabilityType.ACCEPT_SSL_CERTS, true);
		WebDriverManager.chromedriver().setup();
		driver = new ChromeDriver(options);
		return driver;
	}

	public static WebDriver setIEDriver() {

		InternetExplorerOptions options = new InternetExplorerOptions();
		options.ignoreZoomSettings().introduceFlakinessByIgnoringSecurityDomains().setCapability("requireWindowFocus",
				true);
		WebDriverManager.iedriver().setup();
		driver = new InternetExplorerDriver(options);
		System.out.println("Starting IE browser");
		return driver;
	}

	public static void startWebApp()
			throws AWTException, InterruptedException, IOException, ParseException {
		String jsonFilePath = System.getProperty("user.dir") +"/src/test/resources/TestData/TestData.json";
		JSONObject jsonObject = JsonTool.readJson(jsonFilePath);
		String env = getEnvironment();
		String browser = (String) jsonObject.get("Browser");
		String webApp = (String) jsonObject.get("Application");

		openBrowserAndNavigateToBasePage(webApp, browser, env);  // useing static now...
	}

	public static void openBrowser(String browser) throws AWTException, InterruptedException {
		prop = new ReadPropertiesFile();
		bhairav(browser);
		driver = setBrowser(browser);
	}

	public static void navigateToBasePage(String application, String environment)
			throws AWTException, InterruptedException {
		prop = new ReadPropertiesFile();
		String url = prop.getPropertyValue(environment.toUpperCase() + "_" + application.toUpperCase() + "_URL");
		driver.get(url);
	}

	public static void openBrowserAndNavigateToBasePage(String application, String browser, String environment)
			throws InterruptedException {
		prop = new ReadPropertiesFile();
		String url = prop.getPropertyValue(environment.toUpperCase() + "_" + application.toUpperCase() + "_URL");
		String osName =getOperatingSystemSystemUtils();
		if (osName.contains("Win")){
			bhairav(browser);
		}
		driver = setBrowser(browser);
		driver.get(url);
	}

	public static String getOperatingSystemSystemUtils() {
		String os = SystemUtils.OS_NAME;
		return os;
	}

	public static void bhairav(String appName) throws InterruptedException {
		String cmdString[] = null;

		switch (appName.toLowerCase()) {
		case "chrome":
			cmdString = new String[] { "chrome.exe", "chromedriver.exe" };
			break;
		case "ie":
			cmdString = new String[] { "iexplore.exe", "IEDriverServer.exe" };
			break;
		case "intake98":
		case "provider98":
		case "reportworx":
		case "eligible2000":
		case "smsportal":
		case "cignaportal":
			cmdString = new String[] { "mstsc.exe" };
			break;
		case "notepad":
			cmdString = new String[] { "notepad.exe", "Winium.Desktop.Driver.exe" };
			break;
		default:
			cmdString = new String[] { appName };
			break;
		}

		Process process = null;
		for (int i = 0; i < cmdString.length; i++) {
			String runCMD = "taskkill /f /im " + cmdString[i] + " /t";
			try {
				process = Runtime.getRuntime().exec(runCMD);
				process.waitFor();
				process.destroy();
				System.out.println("Closing " + cmdString[i]);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public static String encryptedPassword(String passWord){

		byte[] encodedString = new byte[0];
		try {
			encodedString = Base64.encodeBase64(passWord.getBytes());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return(new String(encodedString));
	}
	public static String decryptedPassword(String passWord){

		byte[] decodedString = new byte[0];
		try {
			 decodedString = Base64.decodeBase64(passWord);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return(new String(decodedString));
	}

	public static String currentDateTime(String currentDateTimePattern) {
		LocalDateTime datetime = LocalDateTime.now();
		DateTimeFormatter pattern = DateTimeFormatter.ofPattern(currentDateTimePattern);
		String formattedDate = datetime.format(pattern);
		return formattedDate;
	}

	public static String currentDatePlusDays(Integer daysToAdd) {
		LocalDateTime datetime = LocalDateTime.now().plusDays(daysToAdd);
		DateTimeFormatter pattern = DateTimeFormatter.ofPattern("MM/dd/yyyy");
		String formattedDate = datetime.format(pattern);
		return formattedDate;
	}

	public static String currentMonthStartDate() {
		LocalDateTime datetime = LocalDateTime.now();
		LocalDateTime start = datetime.withDayOfMonth(1);
		DateTimeFormatter pattern = DateTimeFormatter.ofPattern("MM/dd/yyyy");
		String formattedDate = start.format(pattern);
		return formattedDate;
	}

	public static void closeBrowser() throws InterruptedException {
		driver.close();
		Thread.sleep(1000); // intended
	}

	public static String getCurrentTime() {
    Calendar c = Calendar.getInstance();
    SimpleDateFormat dateformat = new SimpleDateFormat("hh:mm:ss");  
    String datetime = dateformat.format(c.getTime()); 
    return datetime;
	}
	
	public static boolean biggerThanTime(String time1,String time2){
	    String hhmmss1[] = time1.split(":");
	    String hhmmss2[] = time2.split(":");
	    for (int i = 0; i < hhmmss1.length; i++) {
	        if(Integer.parseInt(hhmmss1[i])>Integer.parseInt(hhmmss2[i]))
	            return true;
	    }
	    return false;
	}


	public static String setDate(String patternString) {
		LocalDateTime datetime = LocalDateTime.now();
		DateTimeFormatter pattern = DateTimeFormatter.ofPattern(patternString);
		String formattedDate = datetime.format(pattern);
		return formattedDate;
	}

	public static String setDate(Integer daysToAdd, String patternString) {
		LocalDateTime datetime = LocalDateTime.now().plusDays(daysToAdd);
		DateTimeFormatter pattern = DateTimeFormatter.ofPattern(patternString);
		String formattedDate = datetime.format(pattern);
		return formattedDate;
	}

	public static void waitUntilElementIsClickable(WebElement element) {
		new WebDriverWait(driver, 120).until(ExpectedConditions.elementToBeClickable(element));
	}

	public static void waitUntilClickable(WebElement element) {
		try {
			waitUntilElementIsClickable(element);
		} catch (Exception e) {
			waitUntilElementIsClickable(element);
		}
	}

	public static void waitAndClick(WebElement element) {
		waitUntilClickable(element);
		element.click();
	}

	public static void waitAndSendKeys(WebElement element, String testData) {
		waitUntilClickable(element);
		element.sendKeys(testData);
	}

	public static boolean isTextPresent(String testData) {
		boolean flag = false;
		try {
			driver.findElement(By.xpath(testData));
			flag = true;
		} catch (Exception e) {
			flag = false;
		}
		return flag;
	}

	public static void waitUntilTextIsVisible(WebElement element, String testData) {
		new WebDriverWait(driver, 180).until(ExpectedConditions.textToBePresentInElement(element, testData));
	}

	public static void waitUntilElementIsVisible(WebElement element) {
		new WebDriverWait(driver, 180).until(ExpectedConditions.visibilityOf(element));
	}

	public static void checkIfPageIsReady() {
		JavascriptExecutor js = (JavascriptExecutor) driver;
		String pageState = null;
		do {
			pageState = js.executeScript("return document.readyState").toString();
		} while (!pageState.equalsIgnoreCase("complete"));
	}

	public static void clickElementUsingAction(WebElement element, WebElement clickElem) {
		Actions action = new Actions(driver);
		action.moveToElement(element).build().perform();
		SeleniumMethods.waitAndClick(clickElem);

	}

	public static String convertToFullLengthGender(String gender, String patternType) {
		String fullGender = null;
		if (gender.equalsIgnoreCase("M") && patternType.contentEquals("1")) {
			fullGender = "Male";
		} else if (gender.equalsIgnoreCase("M") && patternType.contentEquals("2")) {
			fullGender = "M=Male";
		} else if (gender.equalsIgnoreCase("M") && patternType.contentEquals("3")) {
			fullGender = "MALE";
		} else if (gender.equalsIgnoreCase("F") && patternType.contentEquals("1")) {
			fullGender = "Female";
		} else if (gender.equalsIgnoreCase("F") && patternType.contentEquals("2")) {
			fullGender = "F=Female";
		} else if (gender.equalsIgnoreCase("F") && patternType.contentEquals("3")) {
			fullGender = "FEMALE";
		} else {
			fullGender = gender;
		}
		return fullGender;
	}

	public static boolean isFileClosed(String fileName) {
		File file = new File(fileName);
		return file.renameTo(file);
	}

	public static void selectFromDropDown(WebElement element, String testdata) {
		try {
			// highlightElementBackground(element, "Pass");
			waitUntilClickable(element);
			Select option = new Select(element);
			option.selectByVisibleText(testdata);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void selectFromDropDownByVal(WebElement element, String testdata) {
		try {
			// highlightElementBackground(element, "Pass");
			waitUntilClickable(element);
			Select option = new Select(element);
			option.selectByValue(testdata);
		} catch (NoSuchElementException e) {
			e.getMessage();
		}
	}

	public static String interpretDate(String testData) {
		String setDate = null;
		int tempDate = 0;
		testData = testData.toLowerCase();

		try {
			if ((testData.contains("today") || testData.contains("current")) && testData.contains("+")) {
				String[] splitText = testData.split("\\+");
				tempDate = Integer.parseInt(splitText[1].trim());
				setDate = currentDatePlusDays(tempDate);
			} else if ((testData.contains("today") || testData.contains("current")) && testData.contains("-")) {
				String[] splitText = testData.split("\\-");
				tempDate = Integer.parseInt(splitText[1].trim());
				tempDate *= -1;
				setDate = currentDatePlusDays(tempDate);
			} else if ((testData.contains("today") || testData.contains("current"))) {
				tempDate = 0;
				setDate = currentDatePlusDays(tempDate);
			} else if (testData.contains("future")) {
				tempDate = 3; // keeping default value of 3 days in future
				setDate = currentDatePlusDays(tempDate);
			} else if (testData.contains("/")) {
				setDate = testData;
			} else {
				System.err.println("Please check the date value.");
			}
		} catch (NumberFormatException e) {
			e.printStackTrace();
		}
		return setDate;
	}

	public static boolean isElementPresent(WebElement element) {
		return (element != null) ? true : false;
	}

	public static boolean isElePresent(By testData) {
		boolean flag = false;
		try {
			flag = driver.findElements(testData).size() > 0;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return flag;
	}

//	public static boolean isDimensionVisible(By testData) {
//		boolean flag = false;
//		try {
//			Dimension dim = driver.findElement(testData).getSize();
//			flag = (dim.getHeight() > 0 && dim.getWidth() > 0);
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//		return flag;
//	}

	public static boolean isCssVisible(By testData) {
		boolean flag = false;
		try {
			String elementStyle = driver.findElement(testData).getAttribute("style");
			flag = !(elementStyle.contentEquals("display: none;") || elementStyle.contentEquals("visibility: hidden"));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return flag;
	}

//	public static boolean isElementVisible(By testData) {
//		boolean flag = false;
//		if (isElePresent(testData) && isDimensionVisible(testData) && isCssVisible(testData)) {
//			flag = true;
//			System.out.println("test");
//		}
//		return flag;
//	}

	public static WebElement eligibilityOrAvality(By para1, By para2) {
		WebElement element = null;
		WebElement element1;
		WebElement element2;

		do {
			try {
				element1 = driver.findElement(para1);
			} catch (Exception e) {
				element1 = null;
			}
			try {
				element2 = driver.findElement(para2);
			} catch (Exception e) {
				element2 = null;
			}
			if (element1 != null) {
				element = element1;
			} else if (element2 != null) {
				element = element2;
			}
		} while (element == null);
		return element;
	}

	public static void jsClick(WebElement element) {

		try {
			JavascriptExecutor js = (JavascriptExecutor) driver;
			js.executeScript("arguments[0].click()", element);
		}catch (ElementClickInterceptedException e){
			log.error(e.getMessage());
		}


	}

	public static void handleAlert() {
		try {
			Thread.sleep(2000); // intended
			if (isAlertPresent()) {
				driver.switchTo().alert().accept();
			}
		} catch (Exception e) {
			// do nothing
		}
	}

	public static boolean isAlertPresent() {
		boolean flag = false;
		try {
			new WebDriverWait(driver, 3).until(ExpectedConditions.alertIsPresent());
			driver.switchTo().alert();
			flag = true;
		} catch (NoAlertPresentException e) {
			e.printStackTrace();
		}
		return flag;
	}

	public static List<WebElement> getDropdownListItems(By testData) {
		return new Select(driver.findElement(testData)).getOptions();
	}

	public static void navigateBackToPreviousPage() {
		driver.navigate().back();
	}

	public static WebElement getRootElement(WebElement element) {
		return (WebElement) ((JavascriptExecutor) driver).executeScript("return arguments[0].shadowRoot", element);
	}

	public static String initCap(String testData) {
		return testData.substring(0, testData.length() - (testData.length() - 1)).toUpperCase()
				+ testData.substring(1).toLowerCase();
	}


	public static void testSeparater(String suiteName, String testData) {
		try {
			String line = "";
			String lineChar = "-";
			int numberOfChar = 25;
			for (int i = 0; i < numberOfChar; i++) {
				line = line + lineChar;
			}
			System.out.println(line + " " + suiteName + " : " + testData.toUpperCase() + " " + line);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void setTextFieldValueIfNull(WebElement element, String testData)
			throws HeadlessException, UnsupportedFlavorException, IOException, InterruptedException {
		try {
			StringSelection stringSelection = new StringSelection("");
			Toolkit.getDefaultToolkit().getSystemClipboard().setContents(stringSelection, null);
			Thread.sleep(1000); // intended
			element.click();
			Thread.sleep(1000); // intended
			element.sendKeys(Keys.chord(Keys.CONTROL, "A"));
			element.sendKeys(Keys.chord(Keys.CONTROL, "C"));
			Thread.sleep(1000); // intended
			String clipData = (String) Toolkit.getDefaultToolkit().getSystemClipboard()
					.getData(DataFlavor.stringFlavor);
			clipData = clipData.trim();
			if (clipData == null || clipData.equals("")) {
				Thread.sleep(1000); // intended
				element.sendKeys(testData);
			}
		} catch (HeadlessException | InterruptedException | UnsupportedFlavorException | IOException e) {
			e.printStackTrace();
		}
	}

	public static void saveValue(String fileName, String testData) {
		String fileNamePath = System.getProperty("user.dir") + "\\src\\resources\\temp\\" + fileName + ".txt";
		try {
			FileWriter writer = new FileWriter(new File(fileNamePath), false);
			writer.write(testData);
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static String getSavedValue(String fileName) throws IOException {
		String testData = null;
		String fileNamePath = System.getProperty("user.dir") + "\\src\\resources\\temp\\" + fileName + ".txt";
		Path path = Paths.get(fileNamePath);
		try {
			testData = Files.readAllLines(path).get(0);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return testData;
	}

	public static LocalDate stringToDate(String stringDate) {
		String cleanDate = interpretDate(stringDate);
		DateTimeFormatter pattern = DateTimeFormatter.ofPattern("MM/dd/yyyy");
		LocalDate date = LocalDate.parse(cleanDate, pattern);
		return date;

	}

	public static Integer dateDiffToDays(String startDate, String endDate) {
		LocalDate sDate = stringToDate(startDate);
		LocalDate eDate = stringToDate(endDate);
		Integer numberOfDays = Math.toIntExact(ChronoUnit.DAYS.between(sDate, eDate));
		return numberOfDays;
	}

	public static String currentTime() {
		LocalDateTime datetime = LocalDateTime.now();
		DateTimeFormatter pattern = DateTimeFormatter.ofPattern("HH:mm:ss SSS");
		String formattedTime = datetime.format(pattern);
		return formattedTime;
	}


	public static void waitFor(int waitMilis) {
		try {
			Thread.sleep(waitMilis);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	public static void explicitlyWaitForElementisClickable(WebElement ele) {
		int explicitWaitTime = 15;
		try {
			WebDriverWait wait = new WebDriverWait(driver, explicitWaitTime);
			wait.until(ExpectedConditions.elementToBeClickable(ele));
		} catch (Exception e) {
			System.out.println("Failed to Get the elelement After waiting " + explicitWaitTime + "Second...");
			e.printStackTrace();
		}
	}

	public static void explicitlyWaitForElement(WebElement ele) {
		int explicitWaitTime = 50;
		try {
			WebDriverWait wait = new WebDriverWait(driver, explicitWaitTime);
			wait.until(ExpectedConditions.visibilityOf(ele));
		} catch (Exception e) {
			System.out.println("Failed to Get the elelement After waiting " + explicitWaitTime + "Second...");
			e.printStackTrace();
		}
	}

	public static boolean explicitlyWaitForElementText(WebElement ele, String text) {
		int explicitWaitTime = 20;
		try {
			WebDriverWait wait = new WebDriverWait(driver, explicitWaitTime);
			return wait.until(ExpectedConditions.textToBePresentInElement(ele, text));
		} catch (Exception e) {
			System.out.println("Failed to Get the Expected Text After waiting " + explicitWaitTime + "Second...");
			e.printStackTrace();
			return false;
		}
	}

	public static void highlightElementBackground(WebElement element, String flag) {
		JavascriptExecutor js = (JavascriptExecutor) driver;

		if (flag.equalsIgnoreCase("pass")) {
			// js.executeScript("arguments[0].style.border='1px groove green'", element);
			js.executeScript("arguments[0].style.backgroundColor = '" + "yellow" + "'", element);
		} else {
			// js.executeScript("arguments[0].style.border='4px solid red'", element);
			js.executeScript("arguments[0].style.backgroundColor = '" + "red" + "'", element);
		}

		waitFor(2000);
	}

	public static String extractNumberFromString(String number) {
		String num = number.replaceAll("[^0-9]+", " ");
		return num.replaceAll(" ", "");
	}

	public static void swithcFrameBasedOnId(String frameIDname) {
		log.debug("getting inside frame..........................>>>");
		driver.switchTo().frame(frameIDname);

	}

	public static void exitFromFrame() {
		log.debug("exiting from frame..........................>>>");
		driver.switchTo().defaultContent();

	}

	public static void ClickButtoninsideFrame(WebElement element, int frameInx) {
		// this.logger.debug("Navigating to My Providers");
		driver.switchTo().frame(frameInx);
		waitFor(1000);
		element.click();
		// driver.switchTo().defaultContent();

	}

	public static void ClickButtoninsideFrameWithExit(WebElement element, int frameInx) {
		// this.logger.debug("Navigating to My Providers");
		driver.switchTo().frame(frameInx);
		waitFor(1000);
		element.click();
		driver.switchTo().defaultContent();

	}

	public static void varifyText(String text, WebElement element) {

		if (element.getText().contains(text)) {
			element.isDisplayed();
		} else {
			throw new RuntimeException("Text Don't Match");
		}

	}

	public static String getText(WebElement element) {
		//highlightElementBackground(element, "pass");
		String getElement = element.getText();
		return getElement;
	}
	public static void clickWithStrElement(String xpath) {
		driver.findElement(By.xpath(xpath)).click();
	}

	public static List<String> getAllText(List<WebElement> relements) {
		List<String> all_elements_text=new ArrayList<>();

		for (int i = 0; i < relements.size(); i++) {
			all_elements_text.add(relements.get(i).getText());
		}

		return all_elements_text;

	}
	public static String getUrl(String environment,String application){

		try {
			prop = new ReadPropertiesFile();
			String url = prop.getPropertyValue(environment.toUpperCase() + "_" + application.toUpperCase() + "_URL");
			return url;
		}catch (Exception e){
			e.printStackTrace();
		}
	return  null;
	}

	public static void clickButton(WebElement element) {
		log.debug("Navigating to My Providers");
		//highlightElementBackground(element, "pass");
		element.click();
	}

	public static void sendKeys(WebElement element, String data) {
		 log.debug("Navigating to My Providers");
		 //highlightElementBackground(element, "pass");
		element.sendKeys(data);
		;
	}

	public static void waitandclear(WebElement element) {

		waitUntilElementIsVisible(element);
		element.clear();
	}

	public static void clickFromListOfElement(List<WebElement> elements){

		JavascriptExecutor js = (JavascriptExecutor) driver;
		elements.forEach(e -> {
			js.executeScript("arguments[0].click();", e);
		});
	}
	public static WebElement getElementBasedOnText(WebElement element,String text){
		return element.findElement(By.xpath("//span[contains(string(), "+ text +")]"));
	}


	public static String waitAndgetValue(WebElement element) {
		String value = null;
		try {
			waitUntilElementIsVisible(element);
			value = element.getAttribute("value");
			return  value;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return value;
	}

	public static void clickWithActionCls( WebElement element ) {

		Actions builder = new Actions(driver);
		builder.moveToElement( element ).click( element );
		builder.perform();
	}
	public static void clickWithActionCls( String  element) {

		WebElement el = driver.findElement(By.xpath(element));
		Actions builder = new Actions(driver);
		builder.moveToElement( el ).click( el );
		builder.perform();
	}

}
