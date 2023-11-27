package com.smile.testcases;

import com.smile.pages.LoginPage;
import com.smile.pages.NavigatorPage;
import com.smile.pages.RegisterPage;
import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.List;

import static com.smile.apiobjects.user.IUser.DEFAULT_PASSWORD;

public class RegisterTest extends BaseUITest {
    private static final String USERNAME_IS_REQUIRED = "Username is required";
    private static final String LENGTH_SHOULD_BE_3_TO_16 = "Length should be 3 to 16";
    private static final String LENGTH_SHOULD_BE_0_TO_32 = "Length should be 0 to 32";
    private static final String EMAIL_IS_REQUIRED = "Email is required";
    private static final String PLEASE_INPUT_A_VALID_EMAIL = "Please input a valid email";
    private static final String PASSWORD_IS_REQUIRED = "Password is required";
    private static final String LENGTH_SHOULD_BE_8_TO_20 = "Length should be 8 to 20";
    private static final String PASSWORD_IS_NOT_MATCH = "The two passwords should be the same";
    private static final String PASSWORD_INCORRECT = "Password should contains: at least a lower character, at least a upper character, at least a number and no space";
    private final String uniqueNumber = getUniqueNumberStr(5);
    private final List<String> needDeleteUsers = new ArrayList<>();
    private NavigatorPage navigatorPage;
    private LoginPage loginPage;
    private RegisterPage registerPage;

    @Override
    @BeforeClass(alwaysRun = true)
    public void beforeClass() {
        super.beforeClass();
    }

    @AfterClass(alwaysRun = true)
    public void afterClass() {
        deleteUsers(needDeleteUsers);
    }

    @BeforeMethod(alwaysRun = true)
    public void beforeMethod() {
        initDriver();

        reporter.logStep("Step 1 - Open browser and navigate to home page");
        webDriver.get(this.baseUrl);
        navigatorPage = new NavigatorPage(webDriver);
        uiAssertions.assertTrue(navigatorPage.homeMenuItem.isDisplayed(), "Verify navbar is displayed");

        reporter.logStep("Step 2 - Click the login button to navigate to the login page");
        loginPage = navigatorPage.clickLoginNavigateTologinPage();
        uiAssertions.assertTrue(loginPage.loginBtn.isDisplayed(), "Verify login page is displayed");

        reporter.logStep("Step 3 - Click the register link to navigate to the register page");
        registerPage = loginPage.clickRegisterLinkNavigateToRegisterPage();
        uiAssertions.assertTrue(registerPage.registerBtn.isDisplayed(), "Verify register page is displayed");
    }


    @AfterMethod(alwaysRun = true)
    public void afterMethod() {
        webDriver.quit();
    }

    @Test(groups = {"P0"}, description = "TC0000: Verify register user success")
    public void test_TC0000_VerifyRegisterUserSuccess() {
        String username = "TC0000" + uniqueNumber;
        String nickname = username + " TC";
        String email = username + "@gmail.com";

        reporter.logStep("Step 4 - Enter valid data in all fields to register the user: " + username);
        registerPage.username.sendKeys(username);
        registerPage.nickname.sendKeys(nickname);
        registerPage.email.sendKeys(email);
        registerPage.password.sendKeys(DEFAULT_PASSWORD);
        registerPage.rePassword.sendKeys(DEFAULT_PASSWORD);
        uiAssertions.assertTrue(registerPage.registerBtn.isEnabled(),
                "Verify the register button should be clickable after fill all fields with valid data");

        registerPage.registerBtn.click();
        loginPage = new LoginPage(webDriver);
        loginPage.waitPageLoadReady();
        needDeleteUsers.add(username);
        uiAssertions.assertTrue(loginPage.loginBtn.isDisplayed(),
                "Verify register user successfully then the page will automate navigate to login page");

        reporter.logStep("Step 5 - Login with new user: " + username);
        loginPage.username.sendKeys(username);
        loginPage.password.sendKeys(DEFAULT_PASSWORD);
        uiAssertions.assertTrue(loginPage.loginBtn.isEnabled(),
                "Verify the login button should be clickable after fill all fields with valid data");

        loginPage.loginBtn.click();
        navigatorPage = new NavigatorPage(webDriver);
        navigatorPage.waitPageLoadReady();
        uiAssertions.assertTrue(navigatorPage.userMenuItem(nickname).isDisplayed(),
                "Verify nickname is displayed after login");
    }

    @Test(groups = {"Regression"}, description = "TC0001: Verify register user error when properties incorrect")
    public void test_TC0001_VerifyRegisterUserErrorWhenPropertiesIncorrect() {

        reporter.logStep("Step 4-1 - Enter empty string to username");
        registerPage.username.sendKeys("");
        registerPage.nickname.click();
        registerPage.waitPageLoadReady();
        uiAssertions.assertTrue(registerPage.hasErrorItem(USERNAME_IS_REQUIRED), "Verify error tip is displayed");

        reporter.logStep("Step 4-2 - Enter a too short username");
        registerPage.username.sendKeys("ab");
        registerPage.nickname.click();
        registerPage.waitPageLoadReady();
        uiAssertions.assertTrue(registerPage.hasErrorItem(LENGTH_SHOULD_BE_3_TO_16), "Verify error tip is displayed");

        reporter.logStep("Step 4-3 - Enter a too long username");
        registerPage.username.sendKeys("Abcde123456789012");
        registerPage.nickname.click();
        registerPage.waitPageLoadReady();
        uiAssertions.assertTrue(registerPage.hasErrorItem(LENGTH_SHOULD_BE_3_TO_16), "Verify error tip is displayed");

        reporter.logStep("Step 5-1 - Enter a too long nickname");
        registerPage.nickname.sendKeys("Ab123456789012345678901234567890E");
        registerPage.username.click();
        registerPage.waitPageLoadReady();
        uiAssertions.assertTrue(registerPage.hasErrorItem(LENGTH_SHOULD_BE_0_TO_32), "Verify error tip is displayed");

        reporter.logStep("Step 6-1 - Enter a empty string to email");
        registerPage.email.sendKeys("");
        registerPage.nickname.click();
        registerPage.waitPageLoadReady();
        uiAssertions.assertTrue(registerPage.hasErrorItem(EMAIL_IS_REQUIRED), "Verify error tip is displayed");

        reporter.logStep("Step 6-2 - Enter a invalid format email");
        registerPage.email.sendKeys("invalid_email");
        registerPage.nickname.click();
        registerPage.waitPageLoadReady();
        uiAssertions.assertTrue(registerPage.hasErrorItem(PLEASE_INPUT_A_VALID_EMAIL), "Verify error tip is displayed");

        reporter.logStep("Step 7-1 - Enter a empty string to password");
        registerPage.password.sendKeys("");
        registerPage.nickname.click();
        registerPage.waitPageLoadReady();
        uiAssertions.assertTrue(registerPage.hasErrorItem(PASSWORD_IS_REQUIRED), "Verify error tip is displayed");

        reporter.logStep("Step 7-2 - Enter a too short password");
        registerPage.password.sendKeys("PassW0r");
        registerPage.nickname.click();
        registerPage.waitPageLoadReady();
        uiAssertions.assertTrue(registerPage.hasErrorItem(LENGTH_SHOULD_BE_8_TO_20), "Verify error tip is displayed");

        reporter.logStep("Step 7-3 - Enter a too long password");
        registerPage.password.sendKeys("Ab1234567890123456789");
        registerPage.nickname.click();
        registerPage.waitPageLoadReady();
        uiAssertions.assertTrue(registerPage.hasErrorItem(LENGTH_SHOULD_BE_8_TO_20), "Verify error tip is displayed");

        reporter.logStep("Step 7-4 - Enter a invalid password that without lower case letter");
        registerPage.password.clear();
        registerPage.password.sendKeys("PASSW0RD");
        registerPage.nickname.click();
        registerPage.waitPageLoadReady();
        uiAssertions.assertTrue(registerPage.hasErrorItem(PASSWORD_INCORRECT), "Verify error tip is displayed");

        reporter.logStep("Step 7-5 - Enter a invalid password that without upper case letter");
        registerPage.password.clear();
        registerPage.password.sendKeys("passw0rd");
        registerPage.nickname.click();
        registerPage.waitPageLoadReady();
        uiAssertions.assertTrue(registerPage.hasErrorItem(PASSWORD_INCORRECT), "Verify error tip is displayed");

        reporter.logStep("Step 7-6 - Enter a invalid password that without number");
        registerPage.password.clear();
        registerPage.password.sendKeys("Password");
        registerPage.nickname.click();
        registerPage.waitPageLoadReady();
        uiAssertions.assertTrue(registerPage.hasErrorItem(PASSWORD_INCORRECT), "Verify error tip is displayed");

        reporter.logStep("Step 8-1 - Enter a non-match re-password that different with password");
        registerPage.password.clear();
        registerPage.password.sendKeys(DEFAULT_PASSWORD);
        registerPage.rePassword.sendKeys(DEFAULT_PASSWORD + "1");
        registerPage.nickname.click();
        registerPage.waitPageLoadReady();
        uiAssertions.assertTrue(registerPage.hasErrorItem(PASSWORD_IS_NOT_MATCH), "Verify error tip is displayed");
    }
}
