package pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;
import org.openqa.selenium.support.PageFactory;
import io.qameta.allure.Step;

public class ProfilePage {
    private WebDriver driver;

    public ProfilePage(WebDriver driver) {
        this.driver = driver;
        PageFactory.initElements(driver, this);
    }

    // Локаторы
    @FindBy(how = How.XPATH, using = ".//a[text()='Профиль']")
    private WebElement profileLink;

    @FindBy(how = How.XPATH, using = ".//button[text()='Выход']")
    private WebElement logoutButton;

    @FindBy(how = How.XPATH, using = ".//label[text()='Имя']/following-sibling::input")
    private WebElement nameInput;

    @FindBy(how = How.XPATH, using = ".//label[text()='Логин']/following-sibling::input")
    private WebElement loginInput;

    // Методы
    @Step("Проверить видимость поля 'Имя'")
    public boolean isNameInputVisible() {
        return nameInput.isDisplayed();
    }

    @Step("Кликнуть на кнопку 'Выход'")
    public LoginPage clickLogoutButton() {
        logoutButton.click();
        return new LoginPage(driver);
    }

    @Step("Получить значение поля 'Имя'")
    public String getNameValue() {
        return nameInput.getAttribute("value");
    }

    @Step("Получить значение поля 'Логин'")
    public String getLoginValue() {
        return loginInput.getAttribute("value");
    }
}