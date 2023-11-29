package com.smile.testcases;

import com.smile.apiobjects.user.SmileRole;
import com.smile.apiobjects.user.SmileUserDTO;
import com.smile.apiobjects.user.UserPayloadGenerator;
import com.smile.pages.LoginPage;
import com.smile.pages.NavigatorPage;
import com.smile.pages.usermanagement.CreateUserPage;
import com.smile.pages.usermanagement.EditUserPage;
import com.smile.pages.usermanagement.UserManagementPage;
import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.smile.apiobjects.user.SmileUsers.ADMIN;

public class UserManagementTest extends BaseUITest {
    private static final String HEADER_ID = "ID";
    private static final String HEADER_USERNAME = "Username";
    private static final String HEADER_NICKNAME = "Nickname";
    private static final String HEADER_EMAIL = "Email";
    private static final String HEADER_ROLES = "Roles";
    private static final String HEADER_ENABLED = "Enabled";
    private static final SmileUserDTO EMPTY_USER = new SmileUserDTO(null, null, null, null, SmileRole.ROLE_USER, false);
    private NavigatorPage navigatorPage;
    private LoginPage loginPage;
    private String uniqueNumber = getUniqueNumberStr(5);
    private UserManagementPage userManagementPage;
    private CreateUserPage createUserPage;
    private EditUserPage editUserPage;
    private List<String> needDeleteUsers = new ArrayList<>();

    @Override
    @BeforeClass(alwaysRun = true)
    public void beforeClass() {
        super.beforeClass();
    }

    @AfterClass(alwaysRun = true)
    public void afterClass() {
        deleteUsers(needDeleteUsers);
    }

    @BeforeMethod(alwaysRun = true)
    public void beforeMethod() {
        initDriver();

        reporter.logStep("Step 1 - Open browser and navigate to home page");
        webDriver.get(this.baseUrl);
        navigatorPage = new NavigatorPage(webDriver);
        uiAssertions.assertTrue(navigatorPage.homeMenuItem.isDisplayed(), "Verify navbar is displayed");

        reporter.logStep("Step 2 - Click the login button to navigate to the login page");
        loginPage = navigatorPage.clickLoginNavigateTologinPage();
        uiAssertions.assertTrue(loginPage.loginBtn.isDisplayed(), "Verify login page is displayed");

        reporter.logStep("Step 3 - Login administrator");
        loginPage.inputUsernameAndPassword(ADMIN.getUsername(), ADMIN.getPassword());
        navigatorPage = loginPage.clickLoginBtn();
        uiAssertions.assertTrue(navigatorPage.managementMenuItem.isDisplayed(),
                "Verify administrator login successfully");

        reporter.logStep("Step 4 - Navigate to user management page");
        userManagementPage = navigatorPage.navigateToUserManagementPage();
        uiAssertions.assertTrue(userManagementPage.addUserBtn.isDisplayed());
    }


    @AfterMethod(alwaysRun = true)
    public void afterMethod() {
        webDriver.quit();
    }

    @Test(groups = {"P0"}, description = "TC0005: Verify retrieve users success")
    public void test_TC0005_VerifyRetrieveUsersSuccess() {

        reporter.logStep("Step 4 - Retrieve users in list");
        List<SmileUserDTO> users = userManagementPage.userList();
        uiAssertions.assertFalse(users.isEmpty(),
                "Verify retrieve users success");

        reporter.logStep("Step 5 - View the header of the table");
        List<String> headerList = userManagementPage.userHeaderList();
        uiAssertions.assertEquals(headerList.get(0), HEADER_ID,
                "Verify the ID is displayed in the header of the table");
        uiAssertions.assertEquals(headerList.get(1), HEADER_USERNAME,
                "Verify the username is displayed in the header of the table");
        uiAssertions.assertEquals(headerList.get(2), HEADER_NICKNAME,
                "Verify the nickname is displayed in the header of the table");
        uiAssertions.assertEquals(headerList.get(3), HEADER_EMAIL,
                "Verify the email is displayed in the header of the table");
        uiAssertions.assertEquals(headerList.get(4), HEADER_ROLES,
                "Verify the roles is displayed in the header of the table");
        uiAssertions.assertEquals(headerList.get(5), HEADER_ENABLED,
                "Verify the enabled is displayed in the header of the table");

        reporter.logStep("Step 6 - View user is displayed in the table");
        Optional<SmileUserDTO> adminOptional = users.stream()
                .filter(user -> user.username().equals(ADMIN.getUsername()))
                .findFirst();
        uiAssertions.assertTrue(adminOptional.isPresent(), "Verify the admin user is displayed in the table");
        SmileUserDTO smileUserDTO = adminOptional.orElse(EMPTY_USER);
        uiAssertions.assertEquals(smileUserDTO.id(), ADMIN.getId(),
                "Verify the admin user's ID is displayed correctly");
        uiAssertions.assertEquals(smileUserDTO.username(), ADMIN.getUsername(),
                "Verify the admin user's username is displayed correctly");
        uiAssertions.assertEquals(smileUserDTO.nickname(), ADMIN.getNickname(),
                "Verify the admin user's nickname is displayed correctly");
        uiAssertions.assertEquals(smileUserDTO.email(), ADMIN.getEmail(),
                "Verify the admin user's email is displayed correctly");
        uiAssertions.assertEquals(smileUserDTO.roles(), ADMIN.getRole(),
                "Verify the admin user's role is displayed correctly");
    }

    @Test(groups = {"Regression"}, description = "TC0006: Verify create user success")
    public void test_TC0006_VerifyCreateUserSuccess() {
        String username = "TC0006" + uniqueNumber;
        UserPayloadGenerator userPayload = generateUser(username);

        reporter.logStep("Step 4 - Click Add User button open create new user form page");
        CreateUserPage createUserPage = userManagementPage.clickAddUserOpenCreateUserPage();
        uiAssertions.assertTrue(createUserPage.username.isDisplayed(), "Verify create user page is displayed");

        reporter.logStep("Step 5 - Enter valid data in the page");
        createUserPage.inputUsername(username)
                .inputNickname(userPayload.getNickname())
                .inputEmail(userPayload.getEmail())
                .inputPassword(userPayload.getPassword())
                .inputConfirmPassword(userPayload.getPassword())
                .selectRole(userPayload.getRoles());

        reporter.logStep("Step 5 - Click confirm button to create new user");
        createUserPage.clickConfirmBtn();
        needDeleteUsers.add(username);

        List<SmileUserDTO> userDTOList = userManagementPage.userList();
        Optional<SmileUserDTO> userDto = userDTOList.stream()
                .filter(user -> user.username().equals(username))
                .findFirst();
        uiAssertions.assertTrue(userDto.isPresent(), "Verify new user created successfully");
        SmileUserDTO user = userDto.get();
        uiAssertions.assertEquals(user.username(), username, "Verify username is correct");
        uiAssertions.assertEquals(user.nickname(), userPayload.getNickname(), "Verify nickname is correct");
        uiAssertions.assertEquals(user.email(), userPayload.getEmail(), "Verify email is correct");
        uiAssertions.assertEquals(user.roles(), userPayload.getRoles(), "Verify role is correct");
        uiAssertions.assertTrue(user.enabled(), "Verify enabled is correct");
    }

    @Test(groups = {"Regression"}, description = "TC0007: Verify delete user success")
    public void test_TC0007_VerifyDeleteUserSuccess() {
        String username = "TC0007" + uniqueNumber;
        UserPayloadGenerator userPayloadGenerator = generateUser(username);

        reporter.logStep("Step 4 - Create a test user for deletion validation");
        apiCreateUser(userPayloadGenerator);

        reporter.logStep("Step 5 - Delete user by click delete, username = " + username);
        userManagementPage = userManagementPage.refresh();
        userManagementPage.deleteUser(username);
        // verify the user has been deleted
        boolean isUserDeleted = userManagementPage.userList()
                .stream()
                .noneMatch(user -> user.username().equals(username));
        needDeleteUsers.add(username); // Add the user to the deletion list as a precaution in case the UI deletion fails.
        uiAssertions.assertTrue(isUserDeleted, "Verify user deleted successfully");
    }

    @Test(groups = {"Regression"}, description = "TC0008: Verify update user success")
    public void test_TC0008_VerifyUpdateUserSuccess() {
        String username = "TC0008" + uniqueNumber;
        UserPayloadGenerator userPayload = generateUser(username);

        reporter.logStep("Step 4 - Create a test user for update validation");
        apiCreateUser(userPayload);
        needDeleteUsers.add(username);

        reporter.logStep("Step 5 - Open the user's edit page, edit the user with new properties and confirm");
        userManagementPage = userManagementPage.refresh();
        EditUserPage editUserPage = userManagementPage.openEditUserPage(username);
        userPayload.setNickname(userPayload.getNickname() + "Update")
                .setEmail(userPayload.getEmail() + ".update")
                .setRoles(SmileRole.ROLE_ADMIN)
                .setEnabled(false);
        editUserPage.inputNickname(userPayload.getNickname())
                .inputEmail(userPayload.getEmail())
                .setRoleInput(userPayload.getRoles())
                .setDisabledRatio()
                .confirm();
        // verify user update success
        uiAssertions.assertFalse(editUserPage.getConfirmBtn().isDisplayed(),
                "Verify that the edit page is hidden after successfully updating the suer");
        SmileUserDTO updateUser = userManagementPage.userList()
                .stream()
                .filter(user -> user.username().equals(username))
                .findFirst()
                .orElse(EMPTY_USER);
        uiAssertions.assertEquals(updateUser.nickname(), userPayload.getNickname(),
                "Verify the user's nickname updated successfully");
        uiAssertions.assertEquals(updateUser.email(), userPayload.getEmail(),
                "Verify the user's email updated successfully");
        uiAssertions.assertEquals(updateUser.roles(), userPayload.getRoles(),
                "Verify the user's role updated successfully");
        uiAssertions.assertEquals(updateUser.enabled(), userPayload.getEnabled(),
                "Verify the user's enabled updated successfully");
    }
}
