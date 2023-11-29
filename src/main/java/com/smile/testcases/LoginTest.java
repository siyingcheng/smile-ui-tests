package com.smile.testcases;

import com.smile.pages.LoginPage;
import com.smile.pages.NavigatorPage;
import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.List;

import static com.smile.apiobjects.user.SmileUsers.ADMIN;
import static com.smile.apiobjects.user.SmileUsers.INVALID;
import static com.smile.apiobjects.user.SmileUsers.OWEN;

public class LoginTest extends BaseUITest {
    private final List<String> needDeleteUsers = new ArrayList<>();
    private NavigatorPage navigatorPage;
    private LoginPage loginPage;

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
    }


    @AfterMethod(alwaysRun = true)
    public void afterMethod() {
        webDriver.quit();
    }

    @Test(groups = {"P0"}, description = "TC0002: Verify login with administrator success")
    public void test_TC0002_VerifyLoginWithAdministratorSuccess() {

        reporter.logStep("Step 3 - Input username and password");
        loginPage.inputUsernameAndPassword(ADMIN.getUsername(), ADMIN.getPassword());

        reporter.logStep("Step 4 - Click login button");
        navigatorPage = loginPage.clickLoginBtn();

        reporter.logStep("Step 5 - Verify login success");
        uiAssertions.assertTrue(navigatorPage.userMenuItem(ADMIN.getNickname()).isDisplayed(),
                "Verify login success");
        uiAssertions.assertTrue(navigatorPage.managementMenuItem.isDisplayed(),
                "Verify administrator could able to view the Management menu item");
    }

    @Test(groups = {"P0"}, description = "TC0003: Verify login with standard user success")
    public void test_TC0003_VerifyLoginWithStandardUserSuccess() {

        reporter.logStep("Step 3 - Input username and password");
        loginPage.inputUsernameAndPassword(OWEN.getUsername(), OWEN.getPassword());

        reporter.logStep("Step 4 - Click login button");
        navigatorPage = loginPage.clickLoginBtn();

        reporter.logStep("Step 5 - Verify login success");
        uiAssertions.assertTrue(navigatorPage.userMenuItem(OWEN.getNickname()).isDisplayed(),
                "Verify login success");
    }

    @Test(groups = {"Regression"}, description = "TC0004: Verify login fail with disabled user")
    public void test_TC0004_VerifyLoginFailWithDisabledUser() {

        reporter.logStep("Step 3 - Input username and password");
        loginPage.inputUsernameAndPassword(INVALID.getUsername(), INVALID.getPassword());

        reporter.logStep("Step 4 - Click login button");
        loginPage.clickLoginBtn();

        reporter.logStep("Step 5 - Verify login fail");
        uiAssertions.assertTrue(loginPage.hasErrorMessage("Login failed: user account is abnormal"),
                "Verify login error");
    }
}
