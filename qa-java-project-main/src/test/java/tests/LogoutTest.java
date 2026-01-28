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

public class LogoutTest extends BaseTest {
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
    @DisplayName("Выход из аккаунта")
    public void testLogout() {
        MainPage mainPage = new MainPage(driver);

        // Логинимся
        mainPage.open()
                .clickLoginButton()
                .login(email, password);

        // Переходим в личный кабинет
        ProfilePage profilePage = new ProfilePage(driver);
        mainPage.clickPersonalAccountButton();

        // Выходим из аккаунта
        LoginPage loginPage = profilePage.clickLogoutButton();

        assertTrue("После выхода должен отображаться заголовок 'Вход'",
                loginPage.isLoginHeaderVisible());
    }

    @After
    public void tearDown() {
        if (email != null && password != null) {
            UserGenerator.deleteUser(email, password);
        }
    }
}