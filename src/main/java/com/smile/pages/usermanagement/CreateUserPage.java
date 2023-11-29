package com.smile.pages.usermanagement;

import com.smile.apiobjects.user.SmileRole;
import com.smile.pages.BasePage;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;

public class CreateUserPage extends BasePage {

    @FindBy(xpath = "//label[normalize-space()='Username']/following-sibling::div//input")
    public WebElement username;

    @FindBy(xpath = "//label[normalize-space()='Nickname']/following-sibling::div//input")
    public WebElement nickname;

    @FindBy(xpath = "//label[normalize-space()='Email']/following-sibling::div//input")
    public WebElement email;

    @FindBy(xpath = "//label[normalize-space()='Password']/following-sibling::div//input")
    public WebElement password;

    @FindBy(xpath = "//label[normalize-space()='Confirm Password']/following-sibling::div//input")
    public WebElement confirmPassword;

    @FindBy(xpath = "//label[normalize-space()='Role']/following-sibling::div//input")
    public WebElement role;

    @FindBy(css = "footer button.el-button--primary")
    public WebElement confirmBtn;

    @FindBy(css = "footer > span > button")
    public WebElement cancelBtn;

    @FindBy(xpath = "//span[normalize-space()='Normal User']")
    public WebElement normalUser;

    @FindBy(xpath = "//span[normalize-space()='Admin User']")
    public WebElement roleAdmin;

    public CreateUserPage(WebDriver driver) {
        super(driver);
    }

    public CreateUserPage setUsername(String username) {
        this.username.clear();
        this.username.sendKeys(username);
        return this;
    }

    public CreateUserPage setNickname(String nickname) {
        this.nickname.clear();
        this.nickname.sendKeys(nickname);
        return this;
    }

    public CreateUserPage setEmail(String email) {
        this.email.clear();
        this.email.sendKeys(email);
        return this;
    }

    public CreateUserPage setPassword(String password) {
        this.password.clear();
        this.password.sendKeys(password);
        return this;
    }

    public CreateUserPage setConfirmPassword(String confirmPassword) {
        this.confirmPassword.clear();
        this.confirmPassword.sendKeys(confirmPassword);
        return this;
    }

    public CreateUserPage selectRole(SmileRole role) {
        actions.moveToElement(this.role).perform();
        this.role.click();
        wait.until(ExpectedConditions.visibilityOf(roleAdmin));
        if (role == SmileRole.ROLE_ADMIN) {
            roleAdmin.click();
        } else {
            normalUser.click();
        }
        return this;
    }

    public void clickConfirmBtn() {
        this.confirmBtn.click();
        waitPageLoadReady();
    }

    public CreateUserPage inputUsername(String username) {
        this.username.clear();
        this.username.sendKeys(username);
        return this;
    }

    public CreateUserPage inputNickname(String nickname) {
        this.nickname.clear();
        this.nickname.sendKeys(nickname);
        return this;
    }

    public CreateUserPage inputEmail(String email) {
        this.email.clear();
        this.email.sendKeys(email);
        return this;
    }

    public CreateUserPage inputPassword(String password) {
        this.password.clear();
        this.password.sendKeys(password);
        return this;
    }

    public CreateUserPage inputConfirmPassword(String confirmPassword) {
        this.confirmPassword.clear();
        this.confirmPassword.sendKeys(confirmPassword);
        return this;
    }
}
