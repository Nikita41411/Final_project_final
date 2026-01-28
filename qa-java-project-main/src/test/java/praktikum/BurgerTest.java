package praktikum;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;
import java.util.Arrays;
import java.util.List;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class BurgerTest {

    @Mock
    private Bun bunMock;

    @Mock
    private Ingredient ingredient1Mock;

    @Mock
    private Ingredient ingredient2Mock;

    @Mock
    private Ingredient ingredient3Mock;

    private Burger burger;

    @Before
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        burger = new Burger();
    }

    @Test
    public void testSetBuns() {
        burger.setBuns(bunMock);
        assertEquals(bunMock, burger.bun);
    }

    @Test
    public void testAddIngredient() {
        burger.addIngredient(ingredient1Mock);
        assertEquals(1, burger.ingredients.size());
        assertEquals(ingredient1Mock, burger.ingredients.get(0));

        burger.addIngredient(ingredient2Mock);
        assertEquals(2, burger.ingredients.size());
    }

    @Test
    public void testRemoveIngredient() {
        burger.addIngredient(ingredient1Mock);
        burger.addIngredient(ingredient2Mock);
        burger.addIngredient(ingredient3Mock);

        assertEquals(3, burger.ingredients.size());

        burger.removeIngredient(1);
        assertEquals(2, burger.ingredients.size());
        assertEquals(ingredient1Mock, burger.ingredients.get(0));
        assertEquals(ingredient3Mock, burger.ingredients.get(1));
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testRemoveIngredientInvalidIndex() {
        burger.removeIngredient(0);
    }

    @Test
    public void testMoveIngredient() {
        burger.addIngredient(ingredient1Mock);
        burger.addIngredient(ingredient2Mock);
        burger.addIngredient(ingredient3Mock);

        burger.moveIngredient(0, 2);

        List<Ingredient> expectedOrder = Arrays.asList(ingredient2Mock, ingredient3Mock, ingredient1Mock);
        assertEquals(expectedOrder, burger.ingredients);
    }

    @Test
    public void testGetPrice() {
        when(bunMock.getPrice()).thenReturn(100.0f);
        when(ingredient1Mock.getPrice()).thenReturn(50.0f);
        when(ingredient2Mock.getPrice()).thenReturn(75.0f);

        burger.setBuns(bunMock);
        burger.addIngredient(ingredient1Mock);
        burger.addIngredient(ingredient2Mock);

        float expectedPrice = 100.0f * 2 + 50.0f + 75.0f;
        assertEquals(expectedPrice, burger.getPrice(), 0.001);

        verify(bunMock, times(1)).getPrice();
        verify(ingredient1Mock, times(1)).getPrice();
        verify(ingredient2Mock, times(1)).getPrice();
    }

    @Test
    public void testGetPriceWithNoIngredients() {
        when(bunMock.getPrice()).thenReturn(150.0f);

        burger.setBuns(bunMock);

        float expectedPrice = 150.0f * 2;
        assertEquals(expectedPrice, burger.getPrice(), 0.001);
    }

    @Test
    public void testGetReceipt() {
        when(bunMock.getName()).thenReturn("white bun");
        when(bunMock.getPrice()).thenReturn(200.0f);

        when(ingredient1Mock.getType()).thenReturn(IngredientType.SAUCE);
        when(ingredient1Mock.getName()).thenReturn("hot sauce");
        when(ingredient1Mock.getPrice()).thenReturn(100.0f);

        when(ingredient2Mock.getType()).thenReturn(IngredientType.FILLING);
        when(ingredient2Mock.getName()).thenReturn("cutlet");
        when(ingredient2Mock.getPrice()).thenReturn(100.0f);

        burger.setBuns(bunMock);
        burger.addIngredient(ingredient1Mock);
        burger.addIngredient(ingredient2Mock);

        String receipt = burger.getReceipt();

        assertTrue("Receipt should contain bun name",
                receipt.contains("white bun"));
        assertTrue("Receipt should contain first ingredient",
                receipt.contains("hot sauce"));
        assertTrue("Receipt should contain second ingredient",
                receipt.contains("cutlet"));
        assertTrue("Receipt should contain price",
                receipt.contains("Price"));
        assertTrue("Receipt should contain price value 600",
                receipt.contains("600"));

        verify(bunMock, atLeast(1)).getName();
        verify(bunMock, times(1)).getPrice();
        verify(ingredient1Mock, times(1)).getType();
        verify(ingredient1Mock, times(1)).getName();
        verify(ingredient1Mock, times(1)).getPrice();
        verify(ingredient2Mock, times(1)).getType();
        verify(ingredient2Mock, times(1)).getName();
        verify(ingredient2Mock, times(1)).getPrice();
    }

    @Test
    public void testGetReceiptWithNoIngredients() {
        when(bunMock.getName()).thenReturn("black bun");
        when(bunMock.getPrice()).thenReturn(100.0f);

        burger.setBuns(bunMock);

        String receipt = burger.getReceipt();

        assertTrue("Receipt should contain bun name",
                receipt.contains("black bun"));
        assertTrue("Receipt should contain price",
                receipt.contains("Price"));
        assertTrue("Receipt should contain price value 200",
                receipt.contains("200"));

        assertFalse("Receipt should not contain 'sauce' (no ingredients)",
                receipt.contains("sauce"));
        assertFalse("Receipt should not contain 'filling' (no ingredients)",
                receipt.contains("filling"));
    }

    @Test
    public void testGetReceiptStructure() {
        when(bunMock.getName()).thenReturn("test bun");
        when(bunMock.getPrice()).thenReturn(50.0f);

        burger.setBuns(bunMock);

        String receipt = burger.getReceipt();

        assertTrue(receipt.contains("test bun"));
        assertTrue(receipt.contains("Price"));
        assertTrue(receipt.contains("100"));

        int count = countOccurrences(receipt, "test bun");
        assertTrue("Bun name should appear at least once in receipt", count >= 1);
    }

    @Test
    public void testReceiptContainsCorrectPrice() {
        when(bunMock.getName()).thenReturn("test");
        when(bunMock.getPrice()).thenReturn(33.33f);

        burger.setBuns(bunMock);

        String receipt = burger.getReceipt();

        assertTrue("Receipt should contain 'Price:'",
                receipt.contains("Price:"));

        assertTrue("Receipt should contain price ~66",
                receipt.contains("66"));
    }

    private int countOccurrences(String text, String pattern) {
        int count = 0;
        int index = 0;
        while ((index = text.indexOf(pattern, index)) != -1) {
            count++;
            index += pattern.length();
        }
        return count;
    }
}