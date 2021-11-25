package com.cioxhealth.pages;

import com.cioxhealth.Common.BasePage;
import com.cioxhealth.Common.SeleniumMethods;
import com.cioxhealth.Utilities.JsonTool;
import com.cioxhealth.Utilities.ReadPropertiesFile;
import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;
import org.openqa.selenium.support.PageFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.lang.invoke.MethodHandles;
import java.util.List;

public class ArchLoginPage extends BasePage {

    private static final Logger log = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    public ArchLoginPage(WebDriver driver) {
        PageFactory.initElements(driver, this);
    }

    @FindBy(xpath = "//*[@type='checkbox']")
    private WebElement checkBox;

    @FindBy(css = "input#rtbOktaUserEmail")
    private WebElement userEmail;

    @FindBy(xpath = "//*[@id='okta-signin-username']")
    private WebElement userEmailAgn;

    @FindBy(xpath = "//*[@type='password']")
    private WebElement EEUserPasswordTF;

    @FindBy(xpath = "//*[@name='answer']")
    private WebElement EEUsersecQues;

    @FindBy(css = "input#ibLogin")
    private WebElement submitBTN;

    @FindBy(css = "input#okta-signin-submit")
    private WebElement nextBTn;

    @FindBy(xpath = "//input[@value='Verify']")
    private WebElement verifyBTn;

    @FindBy(xpath = "//*[@class='rddlIcon']")
    private WebElement roleBTn;

    @FindBy(css = "input#ctl00_cphMain_rtbUserName")
    private WebElement searchUname;

    @FindBy(css = "input#ibLogin")
    private WebElement smtBTn;

    @FindBy(css = "a#ctl00_cphMain_lbSearch")
    private WebElement searchBTn;

    @FindBy(css = "a#ctl00_cphMain_ueMain_rcbStatus_Arrow")
    private WebElement statusBtn;

    @FindBy(css = "a#ctl00_cphMain_ueMain_rcbOwnershipCompany_Arrow")
    private WebElement ownshpBtn;

    @FindBy(xpath = "(//*[@class='rcbScroll rcbWidth'])[1]//li[1]")
    private WebElement activeBtn;

    @FindBy(css = "a#ctl00_cphMain_ueMain_lbSave")
    private WebElement saveBtn;

    @FindBy(how = How.XPATH, using = "//*[@class='rgMasterTable']//tbody//tr//td[1]")
    List<WebElement> userlistEle;

    @FindBy(how = How.XPATH, using = "//*[@class='rgMasterTable']//tbody//tr//td[11]")
    List<WebElement> editButton;

    @FindBy(how = How.XPATH, using = "(//ul[@class='rcbList'])[1]//li")
    List<WebElement> ownList;

    @FindBy(how = How.XPATH, using = "//*[@id='ctl00_cphMain_ueMain_chkblProject']//td//label")
    List<WebElement> projectNamelist;

    @FindBy(how = How.XPATH, using = "//*[@id='ctl00_cphMain_ueMain_chkblProject']//td//input")
    List<WebElement> projectNameSelectionBtn;

    @FindBy(how = How.XPATH, using = "//*[@checked='checked']")
    List<WebElement> checkedBtn;






    public boolean loginToEEAdmin() {
        boolean flag = false;
        try {

            this.setUserEmail();
            this.clickCheckBox();
            this.ClickSubmit();
            this.inputUserEmailAgain();
            this.clickNextBTn();
            this.setUserpassword();
            this.clickVerifyBTN();
            this.setSecurityQuestion();
            this.clickVerifyBTN();
            flag = true;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return flag;
    }

    public boolean selectRole() {
        boolean flag = false;
        try {

            SeleniumMethods.waitUntilElementIsVisible(roleBTn);
            SeleniumMethods.jsClick(roleBTn);
            prop = new ReadPropertiesFile();
            String userRole = prop.getPropertyValue("userRole");
            SeleniumMethods.waitFor(1000);  //intend
            SeleniumMethods.clickWithStrElement("//*[@class='rddlList']//*[contains(text(),'" + userRole + "')]");
            SeleniumMethods.jsClick(smtBTn);

            flag = true;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return flag;
    }

    public void UserManagement(String testcaseName) throws ParseException {

        String jsonFilePath = System.getProperty("user.dir") + "\\src\\test\\resources\\TestData\\CioxHealthTestCaseData\\" + testcaseName
                + ".json";

        try {
            JSONObject jsonObject = JsonTool.readJson(jsonFilePath);
            String userName = (String) jsonObject.get("UserName");
            String userID = (String) jsonObject.get("userID");


            SeleniumMethods.waitandclear(searchUname);
            SeleniumMethods.sendKeys(searchUname,userName);
            SeleniumMethods.jsClick(searchBTn);
            SeleniumMethods.waitFor(2000); //intened
            for (int i = 0; i < userlistEle.size() ; i++) {
                if (userlistEle.get(i).getText().contains(userID)) {
                    editButton.get(i).click();
                    break;
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    public void fillUpUserProf(String testcaseName) throws ParseException {

        String jsonFilePath = System.getProperty("user.dir") + "\\src\\test\\resources\\TestData\\CioxHealthTestCaseData\\" + testcaseName
                + ".json";

        try {
            JSONObject jsonObject = JsonTool.readJson(jsonFilePath);
            String ownerShip = (String) jsonObject.get("ownerShip");

            SeleniumMethods.waitAndClick(ownshpBtn);

            for (WebElement webElement : ownList) {
                if (webElement.getText().equalsIgnoreCase(ownerShip.toLowerCase())) {
                    webElement.click();
                    break;
                }
            }

            SeleniumMethods.jsClick(statusBtn);
            SeleniumMethods.jsClick(activeBtn);


        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    public void selectProject(String testcaseName) throws ParseException {

        String jsonFilePath = System.getProperty("user.dir") + "\\src\\test\\resources\\TestData\\CioxHealthTestCaseData\\" + testcaseName
                + ".json";

        try {
            JSONObject jsonObject = JsonTool.readJson(jsonFilePath);
            String projectName = (String) jsonObject.get("projectName");

            for (WebElement webElement : checkedBtn) {

                if (webElement.getAttribute("checked").contains("checked")) {
                    webElement.click();
                    break;
                }

            }
            for (int i = 0; i < projectNamelist.size() ; i++) {
                if (projectNamelist.get(i).getText().equalsIgnoreCase(projectName.toLowerCase())) {
                    projectNameSelectionBtn.get(i).click();
                    break;
                }
            }
            SeleniumMethods.jsClick(saveBtn);

        } catch (IOException e) {
            e.printStackTrace();
        }


    }
    /** ##### private Helper Methods ##### */


    private void setUserEmail() {

        prop = new ReadPropertiesFile();
        String userMail = prop.getPropertyValue("userEmail");
        SeleniumMethods.waitandclear(userEmail);
        SeleniumMethods.sendKeys(userEmail,userMail);

    }
    private void inputUserEmailAgain() {

        prop = new ReadPropertiesFile();
        String userMail = prop.getPropertyValue("userEmail");
        SeleniumMethods.waitandclear(userEmailAgn);
        SeleniumMethods.sendKeys(userEmailAgn,userMail);

    }
    private void setUserpassword() {

        prop = new ReadPropertiesFile();
        String getDecPassFormFile = prop.getPropertyValue("UserPassword");
        String UserPassword = SeleniumMethods.decryptedPassword(getDecPassFormFile);
        SeleniumMethods.waitandclear(EEUserPasswordTF);
        SeleniumMethods.sendKeys(EEUserPasswordTF,UserPassword);

    }
    private void setSecurityQuestion() {

        prop = new ReadPropertiesFile();
        String getDecPassFormFile = prop.getPropertyValue("SecurityQuestion");
        String UserPassword = SeleniumMethods.decryptedPassword(getDecPassFormFile);
        SeleniumMethods.waitandclear(EEUsersecQues);
        SeleniumMethods.sendKeys(EEUsersecQues,UserPassword);

    }
    private void ClickSubmit(){
        if (checkBox.isEnabled()) {
            SeleniumMethods.clickButton(submitBTN);
        }else {
            log.error("Please Input Email.....");
        }

    }
    private void clickNextBTn(){
        try {
            SeleniumMethods.waitUntilElementIsVisible(nextBTn);
            SeleniumMethods.clickButton(nextBTn);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void clickCheckBox(){
        try {
            SeleniumMethods.waitUntilElementIsVisible(checkBox);
            SeleniumMethods.clickButton(checkBox);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private void clickVerifyBTN(){

        try {
            SeleniumMethods.waitUntilElementIsVisible(verifyBTn);
            SeleniumMethods.clickButton(verifyBTn);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }



}
