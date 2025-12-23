package pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;
import org.openqa.selenium.support.PageFactory;
import io.qameta.allure.Step;

public class LoginPage {
    private WebDriver driver;

    public LoginPage(WebDriver driver) {
        this.driver = driver;
        PageFactory.initElements(driver, this);
    }

    // Локаторы
    @FindBy(how = How.XPATH, using = ".//h2[text()='Вход']")
    private WebElement loginHeader;

    @FindBy(how = How.XPATH, using = ".//label[text()='Email']/following-sibling::input")
    private WebElement emailInput;

    @FindBy(how = How.XPATH, using = ".//label[text()='Пароль']/following-sibling::input")
    private WebElement passwordInput;

    @FindBy(how = How.XPATH, using = ".//button[text()='Войти']")
    private WebElement loginButton;

    @FindBy(how = How.LINK_TEXT, using = "Зарегистрироваться")
    private WebElement registerLink;

    @FindBy(how = How.LINK_TEXT, using = "Восстановить пароль")
    private WebElement forgotPasswordLink;

    @FindBy(how = How.CLASS_NAME, using = "input__error")
    private WebElement errorMessage;

    // Методы
    @Step("Проверить видимость заголовка 'Вход'")
    public boolean isLoginHeaderVisible() {
        return loginHeader.isDisplayed();
    }

    @Step("Ввести email: {email}")
    public LoginPage enterEmail(String email) {
        emailInput.sendKeys(email);
        return this;
    }

    @Step("Ввести пароль: {password}")
    public LoginPage enterPassword(String password) {
        passwordInput.sendKeys(password);
        return this;
    }

    @Step("Кликнуть на кнопку 'Войти'")
    public MainPage clickLoginButton() {
        loginButton.click();
        return new MainPage(driver);
    }

    @Step("Выполнить вход")
    public MainPage login(String email, String password) {
        enterEmail(email);
        enterPassword(password);
        return clickLoginButton();
    }

    @Step("Кликнуть на ссылку 'Зарегистрироваться'")
    public RegisterPage clickRegisterLink() {
        registerLink.click();
        return new RegisterPage(driver);
    }

    @Step("Кликнуть на ссылку 'Восстановить пароль'")
    public ForgotPasswordPage clickForgotPasswordLink() {
        forgotPasswordLink.click();
        return new ForgotPasswordPage(driver);
    }

    @Step("Получить текст ошибки")
    public String getErrorMessage() {
        return errorMessage.getText();
    }

    @Step("Проверить наличие ошибки")
    public boolean isErrorVisible() {
        return errorMessage.isDisplayed();
    }
}