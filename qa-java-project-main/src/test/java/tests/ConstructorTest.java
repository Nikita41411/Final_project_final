package tests;

import io.qameta.allure.junit4.DisplayName;
import org.junit.Test;
import pages.MainPage;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class ConstructorTest extends BaseTest {

    @Test
    @DisplayName("Переход к разделу 'Булки'")
    public void testNavigateToBunsSection() {
        MainPage mainPage = new MainPage(driver);
        mainPage.open();

        // Сначала переключаемся на другую вкладку
        mainPage.clickSaucesTab();
        // Затем возвращаемся на Булки
        mainPage.clickBunsTab();

        String activeTab = mainPage.getActiveTabText();
        assertEquals("Активной должна быть вкладка 'Булки'", "Булки", activeTab);
    }

    @Test
    @DisplayName("Переход к разделу 'Соусы'")
    public void testNavigateToSaucesSection() {
        MainPage mainPage = new MainPage(driver);
        mainPage.open();

        mainPage.clickSaucesTab();

        String activeTab = mainPage.getActiveTabText();
        assertEquals("Активной должна быть вкладка 'Соусы'", "Соусы", activeTab);
    }

    @Test
    @DisplayName("Переход к разделу 'Начинки'")
    public void testNavigateToToppingsSection() {
        MainPage mainPage = new MainPage(driver);
        mainPage.open();

        mainPage.clickToppingsTab();

        String activeTab = mainPage.getActiveTabText();
        assertEquals("Активной должна быть вкладка 'Начинки'", "Начинки", activeTab);
    }
}