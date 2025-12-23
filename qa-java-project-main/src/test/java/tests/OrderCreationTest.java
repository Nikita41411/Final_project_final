package tests;

import io.qameta.allure.Step;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

public class OrderCreationTest extends BaseTest {

    private String userEmail;
    private String userPassword = "password123";
    private String userName = "Test User";
    private String accessToken;
    private String bunId;
    private String mainId;
    private String sauceId;

    @Before
    public void setUpTest() {
        userEmail = generateUniqueEmail();

        Response response = createUser(userEmail, userPassword, userName);
        accessToken = response.then().extract().path("accessToken");

        Response ingredientsResponse = given()
                .spec(requestSpec)
                .get("/ingredients");

        bunId = ingredientsResponse.then().extract().path("data.find { it.type == 'bun' }._id");
        mainId = ingredientsResponse.then().extract().path("data.find { it.type == 'main' }._id");
        sauceId = ingredientsResponse.then().extract().path("data.find { it.type == 'sauce' }._id");
    }

    @Test
    @DisplayName("Создание заказа с авторизацией и ингредиентами")
    public void createOrderWithAuthAndIngredients() {
        List<String> selectedIngredients = Arrays.asList(bunId, mainId);

        createOrderWithAuth(accessToken, selectedIngredients)
                .then()
                .statusCode(200)
                .body("success", equalTo(true))
                .body("order.number", greaterThan(0))
                .body("order.ingredients", not(empty()));
    }

    @Test
    @DisplayName("Создание заказа только с булкой")
    public void createOrderWithOnlyBun() {
        List<String> selectedIngredients = Arrays.asList(bunId);

        createOrderWithAuth(accessToken, selectedIngredients)
                .then()
                .statusCode(200)
                .body("success", equalTo(true))
                .body("order.number", greaterThan(0));
    }

    @Test
    @DisplayName("Создание заказа без авторизации")
    public void createOrderWithoutAuth() {
        List<String> selectedIngredients = Arrays.asList(bunId, mainId);

        createOrderWithoutAuth(selectedIngredients)
                .then()
                .statusCode(200)
                .body("success", equalTo(true))
                .body("order.number", greaterThan(0));
    }

    @Test
    @DisplayName("Создание заказа без ингредиентов")
    public void createOrderWithoutIngredientsShouldFail() {
        List<String> selectedIngredients = Arrays.asList();

        createOrderWithAuth(accessToken, selectedIngredients)
                .then()
                .statusCode(400)
                .body("success", equalTo(false));
    }

    @Test
    @DisplayName("Создание заказа с неверным хешем ингредиентов")
    public void createOrderWithInvalidIngredientHash() {
        List<String> invalidIngredients = Arrays.asList("invalid_hash_1", "invalid_hash_2");

        createOrderWithAuth(accessToken, invalidIngredients)
                .then()
                .statusCode(500); // Изменено с проверки body на проверку только статуса
    }

    @Test
    @DisplayName("Создание заказа с авторизацией, но пустым списком ингредиентов")
    public void createOrderWithAuthButEmptyIngredients() {
        createOrderWithAuth(accessToken, Arrays.asList())
                .then()
                .statusCode(400)
                .body("success", equalTo(false));
    }

    @Test
    @DisplayName("Создание заказа с несколькими ингредиентами")
    public void createOrderWithMultipleIngredients() {
        List<String> selectedIngredients = Arrays.asList(bunId, mainId, sauceId);

        createOrderWithAuth(accessToken, selectedIngredients)
                .then()
                .statusCode(200)
                .body("success", equalTo(true))
                .body("order.ingredients", hasSize(3));
    }

    @Step("Создание заказа с авторизацией")
    private Response createOrderWithAuth(String token, List<String> ingredients) {
        return given()
                .spec(requestSpec)
                .header("Authorization", formatToken(token))
                .body(createOrderJson(ingredients))
                .post("/orders");
    }

    @Step("Создание заказа без авторизации")
    private Response createOrderWithoutAuth(List<String> ingredients) {
        return given()
                .spec(requestSpec)
                .body(createOrderJson(ingredients))
                .post("/orders");
    }

    @Step("Создание пользователя")
    private Response createUser(String email, String password, String name) {
        return given()
                .spec(requestSpec)
                .body(createUserJson(email, password, name))
                .post("/auth/register");
    }

    @After
    public void tearDown() {
        if (accessToken != null) {
            deleteUser(accessToken);
        }
    }

    @Step("Удаление пользователя")
    private void deleteUser(String token) {
        given()
                .spec(requestSpec)
                .header("Authorization", formatToken(token))
                .delete("/auth/user")
                .then()
                .statusCode(202);
    }
}