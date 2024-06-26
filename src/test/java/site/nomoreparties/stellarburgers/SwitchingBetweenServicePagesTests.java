package site.nomoreparties.stellarburgers;

import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.response.ValidatableResponse;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.WebDriver;
import site.nomoreparties.stellarburgers.api.client.UserClient;
import site.nomoreparties.stellarburgers.api.generationtestdata.UserGenerator;
import site.nomoreparties.stellarburgers.api.pojo.User;
import site.nomoreparties.stellarburgers.drivers.WebDriverConstructor;
import site.nomoreparties.stellarburgers.pages.HomePage;

import static org.junit.Assert.assertTrue;

@DisplayName("Переходы между страницами сервиса")
public class SwitchingBetweenServicePagesTests {
    private WebDriver driver;
    private User user;
    private final UserClient userClient = new UserClient();
    private ValidatableResponse createResponse;
    private HomePage homePage;

    @Before
    public void setUp() {
        WebDriverConstructor webDriverConstructor = new WebDriverConstructor();
        driver = webDriverConstructor.getWebDriver();
        user = UserGenerator.randomUser();
        createResponse = userClient.createUser(user);
        homePage = new HomePage(driver)
                .open()
                .waitLoadHomePage();
    }

    @After
    public void tearDown() {
        driver.quit();
        String accessToken = userClient.getAccessToken(createResponse);
        userClient.deleteUser(accessToken);
    }
    @Test
    @DisplayName("Переход из личного кабинета в конструктор, по клику на кнопку <<Конструктор>>")
    public void clickConstructorButtonTest(){
        boolean constructorIsDisplayed = homePage
                .clickLoginButton()
                .waitLoadPageAuthorizationForm()
                .fillInTheEmailField(user.getEmail())
                .fillInThePasswordField(user.getPassword())
                .clickEnterButton()
                .waitLoadHomePage()
                .clickPersonalAccountButtonWithAuthorization()
                .waitLoadPagePersonalAccount()
                .clickConstructorButton()
                .waitLoadHomePage()
                .titleAssembleBurgerIsDisplayed();
        assertTrue(constructorIsDisplayed); // Убедились, что перешли на главную страницу к конструктору
    }
    @Test
    @DisplayName("Переход из личного кабинета в конструктор, по клику на логотип Stellar Burgers")
    public void clickLogoTest(){
        boolean constructorIsDisplayed = homePage
                .clickLoginButton()
                .waitLoadPageAuthorizationForm()
                .fillInTheEmailField(user.getEmail())
                .fillInThePasswordField(user.getPassword())
                .clickEnterButton()
                .waitLoadHomePage()
                .clickPersonalAccountButtonWithAuthorization()
                .waitLoadPagePersonalAccount()
                .clickLogo()
                .waitLoadHomePage()
                .titleAssembleBurgerIsDisplayed();
        assertTrue(constructorIsDisplayed); // Убедились, что перешли на главную страницу к конструктору
    }
    @Test
    @DisplayName("Переход в личный кабинет, по клику на кнопку <<Личный кабинет>> на главной")
    public void goToYourPersonalAccountTest(){
        boolean profileIsDisplayed = homePage
                .clickLoginButton()
                .waitLoadPageAuthorizationForm()
                .fillInTheEmailField(user.getEmail())
                .fillInThePasswordField(user.getPassword())
                .clickEnterButton()
                .waitLoadHomePage()
                .clickPersonalAccountButtonWithAuthorization()
                .waitLoadPagePersonalAccount()
                .userProfileIsDisplayed();
        assertTrue(profileIsDisplayed); // Убедились, что перешли в личный кабинет пользователя
    }
}
