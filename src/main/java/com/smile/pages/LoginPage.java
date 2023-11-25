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
}
