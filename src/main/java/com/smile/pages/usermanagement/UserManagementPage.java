package com.smile.pages.usermanagement;

import com.aventstack.extentreports.Status;
import com.smile.apiobjects.user.SmileRole;
import com.smile.apiobjects.user.SmileUserDTO;
import com.smile.pages.BasePage;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.RemoteWebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;

import java.util.List;

public class UserManagementPage extends BasePage {

    @FindBy(xpath = "//button[normalize-space()='Add User']")
    public WebElement addUserBtn;

    @FindBy(css = "input[placeholder='Type to search']")
    public WebElement searchInput;

    @FindBy(xpath = "//button[contains(@class, 'el-button--primary') and normalize-space()='OK']")
    public WebElement popConfirmOK;

    public UserManagementPage(WebDriver driver) {
        super(driver);
    }

    public List<SmileUserDTO> userList() {
        return driver.findElements(By.xpath("//tbody/tr"))
                .stream()
                .map(tr -> {
                    List<WebElement> tdList = tr.findElements(By.tagName("td"));
                    boolean isEnable = tdList.get(5)
                            .findElement(By.tagName("i"))
                            .getAttribute("style")
                            .contains("green");
                    return new SmileUserDTO(Integer.valueOf(tdList.get(0).getText().trim()),
                            tdList.get(1).getText().trim(),
                            tdList.get(2).getText().trim(),
                            tdList.get(3).getText().trim(),
                            SmileRole.valueOf(tdList.get(4).getText().trim()),
                            isEnable);
                })
                .toList();
    }

    public List<String> userHeaderList() {
        return driver.findElements(By.xpath("//thead/tr/th"))
                .stream()
                .map(WebElement::getText)
                .toList();
    }

    public CreateUserPage clickAddUserOpenCreateUserPage() {
        addUserBtn.click();
        waitPageLoadReady();
        return new CreateUserPage(driver);
    }

    public void deleteUser(String username) {
        findTrByUsername(username)
                .findElements(By.tagName("td"))
                .get(6)
                .findElement(By.className("el-button--danger"))
                .click();
        wait.until(ExpectedConditions.elementToBeClickable(popConfirmOK));
        popConfirmOK.click();
        waitPageLoadReady();
    }

    public EditUserPage openEditUserPage(String username) {
        findTrByUsername(username)
                .findElements(By.tagName("td"))
                .get(6)
                .findElement(By.className("el-button--primary"))
                .click();
        waitPageLoadReady();
        return new EditUserPage(driver);
    }

    private WebElement findTrByUsername(String username) {
        var trOpt = driver.findElements(By.xpath("//tbody/tr")).stream()
                .filter(tr -> tr.findElements(By.tagName("td"))
                        .get(1)
                        .getText()
                        .trim()
                        .equals(username))
                .findFirst();
        if (trOpt.isEmpty()) {
            reporter.log(Status.WARNING, "Not found the item in the user list with username: " + username);
            return new RemoteWebElement();
        }
        return trOpt.get();
    }
}
