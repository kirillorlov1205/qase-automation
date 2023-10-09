package tests;

import driver.UiDriverActions;
import io.qameta.allure.Description;
import model.Constants;
import model.User;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import service.LoginPageService;
import service.ProjectsPageService;

public class LoginPageTest extends BaseTest {

    private LoginPageService loginPageService;

    @BeforeClass
    public void setUp() {
        loginPageService = new LoginPageService();
    }

    @Test(description = "Verify successful login with valid credentials", priority = 1)
    @Description("Login with valid credentials")
    public void verifySuccessfulLoginWithValidCredentials() {
        ProjectsPageService projectsPageService = loginPageService.login(Constants.USER_WITH_VALID_CREDENTIALS);
        Assert.assertTrue(projectsPageService.isProjectsPageDisplayed(), "Login failed");
    }

    @Test(description = "Verify wrong email validation", priority = 2)
    @Description("Wrong email validation")
    public void verifyWrongEmailValidation() {
        loginPageService.login(Constants.USER_WITH_WRONG_EMAIL);
        String actualValidationMessage = loginPageService.getLoginValidationMessage();
        String expectedValidationMessage = Constants.INVALID_CREDENTIALS_VALIDATION_MESSAGE;
        Assert.assertEquals(actualValidationMessage, expectedValidationMessage, "Validation message doesn't " +
                "match expected");
    }

    @Test(description = "Verify wrong password validation", priority = 3)
    @Description("Wrong password validation")
    public void verifyWrongPasswordValidation() {
        loginPageService.login(Constants.USER_WITH_WRONG_PASSWORD);
        String actualValidationMessage = loginPageService.getLoginValidationMessage();
        String expectedValidationMessage = Constants.INVALID_CREDENTIALS_VALIDATION_MESSAGE;
        Assert.assertEquals(actualValidationMessage, expectedValidationMessage, "Validation message doesn't " +
                "match expected");
    }

    @Test(description = "Verify empty email validation", priority = 4)
    @Description("Empty email validation")
    public void verifyEmptyEmailValidation() {
        loginPageService.login(Constants.USER_WITH_EMPTY_EMAIL);
        Assert.assertTrue(loginPageService.isEmptyEmailValidationMessageDisplayed(), "Empty email " +
                "validation message hasn't been shown");
    }

    @Test(description = "Verify empty password validation", priority = 5)
    @Description("Empty password validation")
    public void verifyEmptyPasswordValidation() {
        loginPageService.login(Constants.USER_WITH_EMPTY_PASSWORD);
        Assert.assertTrue(loginPageService.isEmptyPasswordValidationMessageDisplayed(), "Empty password " +
                "validation message hasn't been shown");
    }

    @Test(description = "Verify wrong email format validation", priority = 6, dataProvider = "Wrong format emails")
    @Description("Wrong email format validation")
    public void verifyWrongEmailFormatValidation(String email) {
        User userWithWrongFormatEmail = new User(email);
        loginPageService.login(userWithWrongFormatEmail);
        String actualValidationMessage = loginPageService.getWrongEmailFormatValidationMessage();
        String expectedValidationMessage = String.format(Constants.INVALID_EMAIL_FORMAT_VALIDATION_MESSAGE,
                userWithWrongFormatEmail.getEmail());
        Assert.assertEquals(actualValidationMessage, expectedValidationMessage, "Validation message doesn't " +
                "match expected");
    }

    @Test(description = "Verify additional link transferring", priority = 7, dataProvider = "Additional links")
    @Description("Additional link transferring")
    public void verifyAdditionalLinkTransferring(String linkName, String expectedPageUrl) {
        loginPageService.openLoginPage()
                .clickOnAdditionalLinkByName(linkName);
        String actualPageUrl = UiDriverActions.getSecondOpenedTabUrl();
        Assert.assertTrue(actualPageUrl.contains(expectedPageUrl), "Page url doesn't match expected");
    }

    @DataProvider(name = "Wrong format emails")
    public Object[][] wrongFormatEmailsList() {
        return new Object[][]{
                {"abc.def@mail#archive.com"},
                {"abc..def@mail.com"},
                {".abc@mail.com"},
                {"abc.def@mail"},
                {"abc.def@mail..com"}
        };
    }

    @DataProvider(name = "Additional links")
    public Object[][] additionalLinksList() {
        return new Object[][]
                {
                        {"YouTube", "https://www.youtube.com/playlist?list=PLt75o-m3IfmzbfsuO6Ey-mZgvEtLkWJnD"},
                        {"blog", "https://qase.io/blog/"},
                        {"Twitter", "https://twitter.com/qase_io"},
                        {"LinkedIn", "https://www.linkedin.com/company/qaseio/"},
                };
    }
}
