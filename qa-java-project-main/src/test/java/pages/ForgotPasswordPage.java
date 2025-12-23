package pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;
import org.openqa.selenium.support.PageFactory;
import io.qameta.allure.Step;

public class ForgotPasswordPage {
    private WebDriver driver;

    public ForgotPasswordPage(WebDriver driver) {
        this.driver = driver;
        PageFactory.initElements(driver, this);
    }

    // Локаторы
    @FindBy(how = How.XPATH, using = ".//h2[text()='Восстановление пароля']")
    private WebElement forgotPasswordHeader;

    @FindBy(how = How.LINK_TEXT, using = "Войти")
    private WebElement loginLink;

    // Методы
    @Step("Проверить видимость заголовка 'Восстановление пароля'")
    public boolean isForgotPasswordHeaderVisible() {
        return forgotPasswordHeader.isDisplayed();
    }

    @Step("Кликнуть на ссылку 'Войти'")
    public LoginPage clickLoginLink() {
        loginLink.click();
        return new LoginPage(driver);
    }
}