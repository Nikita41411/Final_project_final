package tests;

import io.qameta.allure.junit4.DisplayName;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import pages.ForgotPasswordPage;
import pages.LoginPage;
import pages.MainPage;
import pages.RegisterPage;
import helpers.UserGenerator;

import static org.junit.Assert.assertTrue;

public class LoginTest extends BaseTest {
    private String email;
    private String password;
    private String name;

    @Before
    public void setUpUser() {
        // Создание пользователя через API перед тестами
        UserGenerator.UserData userData = UserGenerator.createUserViaAPI();
        email = userData.getEmail();
        password = userData.getPassword();
        name = userData.getName();
    }

    @Test
    @DisplayName("Вход по кнопке 'Войти в аккаунт' на главной")
    public void testLoginFromMainPage() {
        MainPage mainPage = new MainPage(driver);
        LoginPage loginPage = mainPage.open()
                .clickLoginButton();

        assertTrue("Должен отображаться заголовок 'Вход'",
                loginPage.isLoginHeaderVisible());

        mainPage = loginPage.login(email, password);

        assertTrue("После входа должна отображаться главная страница",
                mainPage.isMainHeaderVisible());
    }

    @Test
    @DisplayName("Вход через кнопку 'Личный кабинет'")
    public void testLoginFromPersonalAccountButton() {
        MainPage mainPage = new MainPage(driver);
        LoginPage loginPage = mainPage.open()
                .clickPersonalAccountButton();

        assertTrue("Должен отображаться заголовок 'Вход'",
                loginPage.isLoginHeaderVisible());

        mainPage = loginPage.login(email, password);

        assertTrue("После входа должна отображаться главная страница",
                mainPage.isMainHeaderVisible());
    }

    @Test
    @DisplayName("Вход через кнопку в форме регистрации")
    public void testLoginFromRegisterForm() {
        MainPage mainPage = new MainPage(driver);
        RegisterPage registerPage = mainPage.open()
                .clickLoginButton()
                .clickRegisterLink();

        LoginPage loginPage = registerPage.clickLoginLink();

        assertTrue("Должен отображаться заголовок 'Вход'",
                loginPage.isLoginHeaderVisible());

        mainPage = loginPage.login(email, password);

        assertTrue("После входа должна отображаться главная страница",
                mainPage.isMainHeaderVisible());
    }

    @Test
    @DisplayName("Вход через кнопку в форме восстановления пароля")
    public void testLoginFromForgotPasswordForm() {
        MainPage mainPage = new MainPage(driver);
        ForgotPasswordPage forgotPasswordPage = mainPage.open()
                .clickLoginButton()
                .clickForgotPasswordLink();

        LoginPage loginPage = forgotPasswordPage.clickLoginLink();

        assertTrue("Должен отображаться заголовок 'Вход'",
                loginPage.isLoginHeaderVisible());

        mainPage = loginPage.login(email, password);

        assertTrue("После входа должна отображаться главная страница",
                mainPage.isMainHeaderVisible());
    }

    @After
    public void tearDown() {
        // Удаление тестового пользователя через API
        if (email != null && password != null) {
            UserGenerator.deleteUser(email, password);
        }
    }
}