package com.smile.testcases;

import com.simon.core.api.ApiResponse;
import com.simon.core.apidriver.ApiDriver;
import com.simon.core.apidriver.auth.SmileAuthentication;
import com.simon.core.asserts.SoftAssertions;
import com.simon.core.asserts.UIAssertions;
import com.simon.core.testng.BaseTest;
import com.smile.apiobjects.user.UserPayloadGenerator;
import com.smile.apiobjects.user.UsersApiObject;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.WebDriver;
import org.testng.annotations.BeforeClass;

import java.util.List;

import static com.smile.apiobjects.user.SmileUsers.ADMIN;
import static java.net.HttpURLConnection.HTTP_OK;

public class BaseUITest extends BaseTest {
    protected WebDriver webDriver;
    protected UIAssertions uiAssertions;
    protected SoftAssertions softAssertions = new SoftAssertions();
    protected String baseUrl;


    @BeforeClass(alwaysRun = true)
    public void beforeClass() {
        initTest();
        this.baseUrl = configurator.getFrontendUrl();
    }

    protected void initDriver() {
        webDriver = WebDriverManager.edgedriver().create();
        webDriver.manage().window().maximize();
        uiAssertions = new UIAssertions(webDriver);
    }

    protected void deleteUsers(List<String> usernames) {
        if (usernames.isEmpty()) return;

        ApiDriver apiDriver = new ApiDriver(new SmileAuthentication(configurator));
        ApiResponse response = apiDriver.login(ADMIN);
        assertion.assertEquals(response.statusCode(), HTTP_OK, "Verify login administrator successfully");

        UsersApiObject usersApiObject = new UsersApiObject(apiDriver);
        final String firstIdJsonPath = "data[0].id";
        for (String username : usernames) {
            reporter.logStep("CLEANUP - Delete user: " + username);
            response = usersApiObject.filterUser(UserPayloadGenerator.builder()
                    .username(username)
                    .build());
            assertion.assertEquals(response.statusCode(), HTTP_OK, "Verify query user information successfully");
            String id = response.getStringFromJsonPath(firstIdJsonPath);
            response = usersApiObject.deleteUser(id);
            assertion.assertEquals(response.statusCode(), HTTP_OK, "Verify delete user successfully");
        }
    }
}
