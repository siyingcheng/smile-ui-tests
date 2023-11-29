package com.smile.pages;

import com.smile.pages.usermanagement.UserManagementPage;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;

public class NavigatorPage extends BasePage {
    @FindBy(xpath = "//li[normalize-space()='Home']")
    public WebElement homeMenuItem;

    @FindBy(xpath = "//li[@role='menuitem']//li[@role='menuitem'][normalize-space()='Login']")
    public WebElement loginNavigatorBtn;

    @FindBy(xpath = "//li[@role='menuitem' and normalize-space()='Management']")
    public WebElement managementMenuItem;

    @FindBy(xpath = "//li[@role='menuitem' and normalize-space()='User Management']")
    public WebElement userManagementMenuItem;

    public NavigatorPage(WebDriver driver) {
        super(driver);
    }

    public LoginPage clickLoginNavigateTologinPage() {
        this.loginNavigatorBtn.click();
        LoginPage loginPage = new LoginPage(driver);
        waitPageLoadReady();
        return loginPage;
    }

    public WebElement nickname(String nickname) {
        By by = By.xpath(String.format("//div[contains(@class, 'el-sub-menu__title') and normalize-space()='%s']", nickname));
        return driver.findElement(by);
    }

    public WebElement userMenuItem(String nickname) {
        return this.driver.findElement(
                By.xpath(String.format("//li[@role='menuitem']//li[@role='menuitem'][normalize-space()='%s']", nickname))
        );
    }

    public UserManagementPage navigateToUserManagementPage() {
        actions.moveToElement(managementMenuItem).build().perform();
        wait.until(ExpectedConditions.visibilityOf(userManagementMenuItem));
        userManagementMenuItem.click();
        waitPageLoadReady();
        return new UserManagementPage(driver);
    }
}
