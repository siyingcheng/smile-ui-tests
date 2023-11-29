package com.smile.testcases;

import com.simon.core.api.ApiResponse;
import com.simon.core.apidriver.ApiDriver;
import com.simon.core.apidriver.auth.SmileAuthentication;
import com.simon.core.asserts.SoftAssertions;
import com.simon.core.asserts.UIAssertions;
import com.simon.core.testng.BaseTest;
import com.smile.apiobjects.user.SmileRole;
import com.smile.apiobjects.user.UserPayloadGenerator;
import com.smile.apiobjects.user.UsersApiObject;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.WebDriver;
import org.testng.annotations.BeforeClass;

import java.util.List;
import java.util.Objects;

import static com.smile.apiobjects.user.IUser.DEFAULT_PASSWORD;
import static com.smile.apiobjects.user.SmileUsers.ADMIN;
import static java.net.HttpURLConnection.HTTP_OK;

public class BaseUITest extends BaseTest {
    protected WebDriver webDriver;
    private ApiDriver apiDriver;
    protected UIAssertions uiAssertions;
    protected SoftAssertions softAssertions = new SoftAssertions();
    protected String baseUrl;


    @BeforeClass(alwaysRun = true)
    public void beforeClass() {
        initTest();
        this.baseUrl = configurator.getFrontendUrl();
        this.apiDriver = new ApiDriver(new SmileAuthentication(configurator));
    }

    protected void initDriver() {
        webDriver = WebDriverManager.edgedriver().create();
        webDriver.manage().window().maximize();
        uiAssertions = new UIAssertions(webDriver);
    }

    protected void deleteUsers(List<String> usernames) {
        if (usernames.isEmpty()) return;

        apiLoginAdministrator();

        UsersApiObject usersApiObject = new UsersApiObject(apiDriver);
        final String firstIdJsonPath = "data[0].id";
        for (String username : usernames) {
            reporter.logStep("CLEANUP - Delete user: " + username);
            ApiResponse response = usersApiObject.filterUser(UserPayloadGenerator.builder()
                    .username(username)
                    .build());
            assertion.assertEquals(response.statusCode(), HTTP_OK, "Verify query user information successfully");
            String id = response.getStringFromJsonPath(firstIdJsonPath);
            if (Objects.isNull(id)) continue;
            response = usersApiObject.deleteUser(id);
            int statusCode = response.statusCode();
            assertion.assertTrue(statusCode == HTTP_OK, "Verify delete user successfully");
        }
    }

    private void apiLoginAdministrator() {
        reporter.logStep("Step - Login with administrator: " + ADMIN.getUsername());
        ApiResponse response = apiDriver.login(ADMIN);
        assertion.assertEquals(response.statusCode(), HTTP_OK, "Verify login administrator successfully");
    }

    protected void apiCreateUser(UserPayloadGenerator userPayload) {
        apiLoginAdministrator();

        reporter.logStep("Step - Create a user with API, username = " + userPayload.getUsername());
        UsersApiObject usersApiObject = new UsersApiObject(apiDriver);
        ApiResponse response = usersApiObject.createUser(userPayload);
        softAssertions.assertEquals(response.statusCode(), HTTP_OK, "Verify create user successfully");
    }

    protected UserPayloadGenerator generateUser(String username) {
        return UserPayloadGenerator.builder()
                .username(username)
                .nickname(username + " TC")
                .email(username + "@gmail.com")
                .password(DEFAULT_PASSWORD)
                .roles(SmileRole.ROLE_USER)
                .build();
    }
}
