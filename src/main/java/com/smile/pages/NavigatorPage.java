package com.smile.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

public class NavigatorPage extends BasePage {
    @FindBy(xpath = "//li[normalize-space()='Home']")
    public WebElement homeMenuItem;

    @FindBy(xpath = "//li[@role='menuitem']//li[@role='menuitem'][normalize-space()='Login']")
    public WebElement loginNavigatorBtn;

    @FindBy(xpath = "//li[@role='menuitem' and normalize-space()='Management']")
    public WebElement managementMenuItem;

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
}
