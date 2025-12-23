package tests;

import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import pages.LoginPage;
import pages.MainPage;
import pages.RegisterPage;
import helpers.UserGenerator;

import static org.junit.Assert.*;

public class RegisterTest extends BaseTest {
    private String email;
    private String password;
    private String name;

    @Before
    public void generateTestData() {
        UserGenerator.UserData userData = UserGenerator.generateRandomUser();
        email = userData.getEmail();
        password = userData.getPassword();
        name = userData.getName();
    }

    @Test
    @DisplayName("Успешная регистрация")
    public void testSuccessfulRegistration() {
        MainPage mainPage = new MainPage(driver);
        RegisterPage registerPage = mainPage.open()
                .clickLoginButton()
                .clickRegisterLink();

        assertTrue("Заголовок 'Регистрация' должен быть виден",
                registerPage.isRegisterHeaderVisible());

        LoginPage loginPage = registerPage.register(name, email, password);

        assertTrue("После регистрации должен отображаться заголовок 'Вход'",
                loginPage.isLoginHeaderVisible());
    }

    @Test
    @DisplayName("Ошибка при регистрации с некорректным паролем")
    public void testRegistrationWithInvalidPassword() {
        MainPage mainPage = new MainPage(driver);
        RegisterPage registerPage = mainPage.open()
                .clickLoginButton()
                .clickRegisterLink();

        // Ввод пароля короче 6 символов
        registerPage.enterName(name)
                .enterEmail(email)
                .enterPassword("12345") // Пароль из 5 символов
                .clickRegisterButton();

        String errorText = registerPage.getErrorMessage();
        assertNotNull("Сообщение об ошибке должно существовать", errorText);
        assertFalse("Сообщение об ошибке не должно быть пустым", errorText.isEmpty());
    }

    @After
    public void cleanup() {
        // Удаление тестового пользователя через API
        if (email != null && password != null) {
            UserGenerator.deleteUser(email, password);
        }
    }
}