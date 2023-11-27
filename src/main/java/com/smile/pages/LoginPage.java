package com.smile.pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

public class LoginPage extends BasePage {
    @FindBy(css = "input[placeholder='Username or Email']")
    public WebElement username;

    @FindBy(css = "input[placeholder='Password']")
    public WebElement password;

    @FindBy(xpath = "//button[normalize-space()='Login']")
    public WebElement loginBtn;

    @FindBy(linkText = "Create an account")
    public WebElement createAnAccountLink;

    public LoginPage(WebDriver driver) {
        super(driver);
    }

    public RegisterPage clickRegisterLinkNavigateToRegisterPage() {
        createAnAccountLink.click();
        RegisterPage registerPage = new RegisterPage(driver);
        waitPageLoadReady();
        return registerPage;
    }

    public void inputUsernameAndPassword(String username, String password) {
        this.username.clear();
        this.username.sendKeys(username);
        this.password.clear();
        this.password.sendKeys(password);
    }

    public NavigatorPage clickLoginBtn() {
        this.loginBtn.click();
        waitPageLoadReady();
        return new NavigatorPage(driver);
    }
}
