package pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;
import org.openqa.selenium.support.PageFactory;
import io.qameta.allure.Step;

public class RegisterPage {
    private WebDriver driver;

    public RegisterPage(WebDriver driver) {
        this.driver = driver;
        PageFactory.initElements(driver, this);
    }

    // Локаторы
    @FindBy(how = How.XPATH, using = ".//h2[text()='Регистрация']")
    private WebElement registerHeader;

    @FindBy(how = How.XPATH, using = ".//label[text()='Имя']/following-sibling::input")
    private WebElement nameInput;

    @FindBy(how = How.XPATH, using = ".//label[text()='Email']/following-sibling::input")
    private WebElement emailInput;

    @FindBy(how = How.XPATH, using = ".//label[text()='Пароль']/following-sibling::input")
    private WebElement passwordInput;

    @FindBy(how = How.XPATH, using = ".//button[text()='Зарегистрироваться']")
    private WebElement registerButton;

    @FindBy(how = How.CLASS_NAME, using = "input__error")
    private WebElement errorMessage;

    @FindBy(how = How.LINK_TEXT, using = "Войти")
    private WebElement loginLink;

    // Методы
    @Step("Проверить видимость заголовка 'Регистрация'")
    public boolean isRegisterHeaderVisible() {
        return registerHeader.isDisplayed();
    }

    @Step("Ввести имя: {name}")
    public RegisterPage enterName(String name) {
        nameInput.sendKeys(name);
        return this;
    }

    @Step("Ввести email: {email}")
    public RegisterPage enterEmail(String email) {
        emailInput.sendKeys(email);
        return this;
    }

    @Step("Ввести пароль: {password}")
    public RegisterPage enterPassword(String password) {
        passwordInput.sendKeys(password);
        return this;
    }

    @Step("Кликнуть на кнопку 'Зарегистрироваться'")
    public LoginPage clickRegisterButton() {
        registerButton.click();
        return new LoginPage(driver);
    }

    @Step("Выполнить регистрацию")
    public LoginPage register(String name, String email, String password) {
        enterName(name);
        enterEmail(email);
        enterPassword(password);
        return clickRegisterButton();
    }

    @Step("Получить текст ошибки")
    public String getErrorMessage() {
        return errorMessage.getText();
    }

    @Step("Проверить видимость сообщения об ошибке")
    public boolean isErrorVisible() {
        return errorMessage.isDisplayed();
    }

    @Step("Кликнуть на ссылку 'Войти'")
    public LoginPage clickLoginLink() {
        loginLink.click();
        return new LoginPage(driver);
    }
}