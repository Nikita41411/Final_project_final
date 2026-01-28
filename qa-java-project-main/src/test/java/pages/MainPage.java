package pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;
import org.openqa.selenium.support.PageFactory;
import io.qameta.allure.Step;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class MainPage {
    private WebDriver driver;

    // Конструктор
    public MainPage(WebDriver driver) {
        this.driver = driver;
        PageFactory.initElements(driver, this);
    }

    // Локаторы
    @FindBy(how = How.XPATH, using = ".//button[text()='Войти в аккаунт']")
    private WebElement loginButton;

    @FindBy(how = How.XPATH, using = ".//p[text()='Личный Кабинет']")
    private WebElement personalAccountButton;

    @FindBy(how = How.XPATH, using = ".//h1[text()='Соберите бургер']")
    private WebElement mainHeader;

    @FindBy(how = How.XPATH, using = ".//span[text()='Булки']/parent::div")
    private WebElement bunsTab;

    @FindBy(how = How.XPATH, using = ".//span[text()='Соусы']/parent::div")
    private WebElement saucesTab;

    @FindBy(how = How.XPATH, using = ".//span[text()='Начинки']/parent::div")
    private WebElement toppingsTab;

    @FindBy(how = How.CLASS_NAME, using = "tab_tab_type_current__2BEPc")
    private WebElement activeTab;

    @FindBy(how = How.CLASS_NAME, using = "AppHeader_header__logo__2D0X2")
    private WebElement logo;

    @FindBy(how = How.LINK_TEXT, using = "Конструктор")
    private WebElement constructorLink;

    // Методы
    @Step("Открыть главную страницу")
    public MainPage open() {
        driver.get("https://stellarburgers.education-services.ru");
        return this;
    }

    @Step("Кликнуть на кнопку 'Войти в аккаунт'")
    public LoginPage clickLoginButton() {
        loginButton.click();
        return new LoginPage(driver);
    }

    @Step("Кликнуть на кнопку 'Личный Кабинет'")
    public LoginPage clickPersonalAccountButton() {
        personalAccountButton.click();
        return new LoginPage(driver);
    }

    @Step("Проверить видимость главного заголовка")
    public boolean isMainHeaderVisible() {
        return mainHeader.isDisplayed();
    }

    @Step("Перейти на вкладку 'Булки'")
    public MainPage clickBunsTab() {
        new WebDriverWait(driver, Duration.ofSeconds(3))
                .until(ExpectedConditions.elementToBeClickable(bunsTab));
        bunsTab.click();
        return this;
    }

    @Step("Перейти на вкладку 'Соусы'")
    public MainPage clickSaucesTab() {
        saucesTab.click();
        return this;
    }

    @Step("Перейти на вкладку 'Начинки'")
    public MainPage clickToppingsTab() {
        toppingsTab.click();
        return this;
    }

    @Step("Получить текст активной вкладки")
    public String getActiveTabText() {
        return activeTab.getText();
    }

    @Step("Кликнуть на логотип")
    public MainPage clickLogo() {
        logo.click();
        return this;
    }

    @Step("Кликнуть на ссылку 'Конструктор'")
    public MainPage clickConstructorLink() {
        constructorLink.click();
        return this;
    }
}