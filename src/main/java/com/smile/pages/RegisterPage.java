package com.smile.pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

public class RegisterPage extends BasePage {
    @FindBy(css = "input[placeholder='Username']")
    public WebElement username;

    @FindBy(css = "input[placeholder='Nickname']")
    public WebElement nickname;

    @FindBy(xpath = "//label[normalize-space()='Email']/following-sibling::*//input")
    public WebElement email;

    @FindBy(xpath = "//label[normalize-space()='Password']/following-sibling::*//input")
    public WebElement password;

    @FindBy(xpath = "//label[normalize-space()='Re Password']/following-sibling::*//input")
    public WebElement rePassword;

    @FindBy(xpath = "//button[normalize-space()='Register']")
    public WebElement registerBtn;

    public RegisterPage(WebDriver driver) {
        super(driver);
    }

}
