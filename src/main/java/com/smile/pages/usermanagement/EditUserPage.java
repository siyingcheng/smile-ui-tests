package com.smile.pages.usermanagement;

import com.smile.apiobjects.user.SmileRole;
import com.smile.pages.BasePage;
import lombok.Getter;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;

@Getter
public class EditUserPage extends BasePage {

    @FindBy(xpath = "//label[normalize-space()='Username']/following-sibling::div//input")
    protected WebElement usernameInput;

    @FindBy(xpath = "//label[normalize-space()='Nickname']/following-sibling::div//input")
    protected WebElement nicknameInput;

    @FindBy(xpath = "//label[normalize-space()='Email']/following-sibling::div//input")
    protected WebElement emailInput;

    @FindBy(xpath = "//label[normalize-space()='Enabled']//input")
    protected WebElement enabledRatio;

    @FindBy(xpath = "//label[normalize-space()='Disabled']//input")
    protected WebElement disabledRatio;

    @FindBy(xpath = "//label[normalize-space()='Role']/following-sibling::div//input")
    protected WebElement roleInput;

    @FindBy(css = "footer .el-button--primary")
    protected WebElement confirmBtn;

    @FindBy(css = "footer > span > button")
    protected WebElement cancelBtn;

    public EditUserPage(WebDriver driver) {
        super(driver);
    }

    public EditUserPage inputUsername(String username) {
        this.usernameInput.clear();
        this.usernameInput.sendKeys(username);
        return this;
    }

    public EditUserPage inputNickname(String nickname) {
        this.nicknameInput.clear();
        this.nicknameInput.sendKeys(nickname);
        return this;
    }

    public EditUserPage inputEmail(String email) {
        this.emailInput.clear();
        this.emailInput.sendKeys(email);
        return this;
    }

    public EditUserPage setEnabledRatio() {
        actions.moveToElement(this.enabledRatio)
                .click()
                .build()
                .perform();
        return this;
    }

    public EditUserPage setDisabledRatio() {
        actions.moveToElement(this.disabledRatio)
                .click()
                .build()
                .perform();
        ;
        return this;
    }

    public void confirm() {
        this.confirmBtn.click();
        waitPageLoadReady();
    }

    public void cancel() {
        this.cancelBtn.click();
        waitPageLoadReady();
    }

    public EditUserPage setRoleInput(SmileRole roles) {
        roleInput.click();
        if (roles == SmileRole.ROLE_ADMIN) {
            By admin = By.xpath("//span[normalize-space()='Admin User']");
            wait.until(ExpectedConditions.elementToBeClickable(admin));
            driver.findElement(admin).click();
        } else {
            By user = By.xpath("//span[normalize-space()='Normal User']");
            wait.until(ExpectedConditions.elementToBeClickable(user));
            driver.findElement(user).click();
        }
        return this;
    }
}
