package tests;

import io.qameta.allure.junit4.DisplayName;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import pages.LoginPage;
import pages.MainPage;
import pages.ProfilePage;
import helpers.UserGenerator;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertEquals;

public class NavigationTest extends BaseTest {
    private String email;
    private String password;
    private String name;

    @Before
    public void setUpUser() {
        UserGenerator.UserData userData = UserGenerator.createUserViaAPI();
        email = userData.getEmail();
        password = userData.getPassword();
        name = userData.getName();
    }

    @Test
    @DisplayName("Переход в личный кабинет")
    public void testNavigateToPersonalAccount() {
        MainPage mainPage = new MainPage(driver);

        // Логинимся
        mainPage.open()
                .clickLoginButton()
                .login(email, password);

        // Переходим в личный кабинет
        ProfilePage profilePage = new ProfilePage(driver);
        mainPage.clickPersonalAccountButton();

        assertTrue("Должно отображаться поле 'Имя' в личном кабинете",
                profilePage.isNameInputVisible());

        // Проверяем данные пользователя
        assertEquals("Имя должно совпадать", name, profilePage.getNameValue());
        assertEquals("Email должен совпадать (без учета регистра)",
                email.toLowerCase(), profilePage.getLoginValue().toLowerCase());
    }

    @Test
    @DisplayName("Переход из личного кабинета в конструктор по клику на 'Конструктор'")
    public void testNavigateFromProfileToConstructorViaLink() {
        MainPage mainPage = new MainPage(driver);

        // Логинимся и переходим в личный кабинет
        mainPage.open()
                .clickLoginButton()
                .login(email, password)
                .clickPersonalAccountButton();

        // Возвращаемся в конструктор
        mainPage.clickConstructorLink();

        assertTrue("Должна отображаться главная страница конструктора",
                mainPage.isMainHeaderVisible());
    }

    @Test
    @DisplayName("Переход из личного кабинета в конструктор по клику на логотип")
    public void testNavigateFromProfileToConstructorViaLogo() {
        MainPage mainPage = new MainPage(driver);

        // Логинимся и переходим в личный кабинет
        mainPage.open()
                .clickLoginButton()
                .login(email, password)
                .clickPersonalAccountButton();

        // Возвращаемся в конструктор через логотип
        mainPage.clickLogo();

        assertTrue("Должна отображаться главная страница конструктора",
                mainPage.isMainHeaderVisible());
    }

    @After
    public void tearDown() {
        if (email != null && password != null) {
            UserGenerator.deleteUser(email, password);
        }
    }
}