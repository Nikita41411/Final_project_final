package praktikum;

import org.junit.Before;
import org.junit.Test;
import java.util.List;
import static org.junit.Assert.*;

public class DatabaseTest {

    private Database database;

    @Before
    public void setUp() {
        database = new Database();
    }

    @Test
    public void testAvailableBuns() {
        List<Bun> buns = database.availableBuns();

        assertNotNull(buns);
        assertEquals(3, buns.size());

        assertEquals("black bun", buns.get(0).getName());
        assertEquals(100.0f, buns.get(0).getPrice(), 0.001);

        assertEquals("white bun", buns.get(1).getName());
        assertEquals(200.0f, buns.get(1).getPrice(), 0.001);

        assertEquals("red bun", buns.get(2).getName());
        assertEquals(300.0f, buns.get(2).getPrice(), 0.001);
    }

    @Test
    public void testAvailableIngredients() {
        List<Ingredient> ingredients = database.availableIngredients();

        assertNotNull(ingredients);
        assertEquals(6, ingredients.size());

        assertEquals(IngredientType.SAUCE, ingredients.get(0).getType());
        assertEquals("hot sauce", ingredients.get(0).getName());
        assertEquals(100.0f, ingredients.get(0).getPrice(), 0.001);

        assertEquals(IngredientType.SAUCE, ingredients.get(1).getType());
        assertEquals("sour cream", ingredients.get(1).getName());
        assertEquals(200.0f, ingredients.get(1).getPrice(), 0.001);

        assertEquals(IngredientType.SAUCE, ingredients.get(2).getType());
        assertEquals("chili sauce", ingredients.get(2).getName());
        assertEquals(300.0f, ingredients.get(2).getPrice(), 0.001);

        assertEquals(IngredientType.FILLING, ingredients.get(3).getType());
        assertEquals("cutlet", ingredients.get(3).getName());
        assertEquals(100.0f, ingredients.get(3).getPrice(), 0.001);

        assertEquals(IngredientType.FILLING, ingredients.get(4).getType());
        assertEquals("dinosaur", ingredients.get(4).getName());
        assertEquals(200.0f, ingredients.get(4).getPrice(), 0.001);

        assertEquals(IngredientType.FILLING, ingredients.get(5).getType());
        assertEquals("sausage", ingredients.get(5).getName());
        assertEquals(300.0f, ingredients.get(5).getPrice(), 0.001);
    }

    @Test
    public void testDatabaseConstructor() {
        assertNotNull(database);

        List<Bun> buns = database.availableBuns();
        List<Ingredient> ingredients = database.availableIngredients();

        assertFalse(buns.isEmpty());
        assertFalse(ingredients.isEmpty());
    }
}